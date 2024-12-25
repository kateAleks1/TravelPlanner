package org.example.Calc;


import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

public class WeatherLinearRegressionAnalysis {

    public static void main(String[] args) {
        String apiKey = "75acf38b12fd4aee83e82158241812"; // Ваш API ключ
        String city = "Kharkiv"; // Город на английском
        String filePath = "weather_data.csv"; // Путь к файлу для сохранения данных

        List<Double> temperatures = new ArrayList<>();
        List<Double> humidities = new ArrayList<>();

        // Получаем данные из API и записываем в файл
        fetchWeatherData(apiKey, city, temperatures, humidities, 100); // 100 запросов для примера

        // Считываем данные из файла
        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                xValues.add(Double.parseDouble(values[0])); // Температура
                yValues.add(Double.parseDouble(values[1])); // Влажность
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Преобразуем списки в массивы для линейной регрессии
        double[] xArray = xValues.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = yValues.stream().mapToDouble(Double::doubleValue).toArray();

        // Создаем модель линейной регрессии
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }

        // Получаем параметры регрессии
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
        double rSquared = regression.getRSquare();

        System.out.println("Regression Equation: Y = " + slope + " * X + " + intercept);
        System.out.println("Coefficient of Determination (R^2): " + rSquared);

        // Визуализация данных
        SwingUtilities.invokeLater(() -> createAndShowPlot(xArray, yArray, slope, intercept));

        // Анализ остатков
        System.out.println("Residual Analysis:");
        for (int i = 0; i < xArray.length; i++) {
            double predictedY = regression.predict(xArray[i]);
            double residual = yArray[i] - predictedY;
            System.out.println("X: " + xArray[i] + ", Observed Y: " + yArray[i] + ", Predicted Y: " + predictedY + ", Residual: " + residual);
        }
    }

    private static void fetchWeatherData(String apiKey, String city, List<Double> temperatures, List<Double> humidities, int count) {
        String urlString = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + city;

        try (FileWriter writer = new FileWriter("weather_data.csv")) {
            for (int i = 0; i < count; i++) {
                // Получаем данные о погоде для каждого запроса
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Обрабатываем ответ JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                double temperature = jsonResponse.getJSONObject("current").getDouble("temp_c");
                int humidity = jsonResponse.getJSONObject("current").getInt("humidity");

                temperatures.add(temperature);
                humidities.add((double) humidity);

                // Записываем данные в файл
                writer.write(temperature + "," + humidity + "\n");
                Thread.sleep(1000); // Задержка между запросами
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }

    private static void createAndShowPlot(double[] x, double[] y, double slope, double intercept) {
        JFrame frame = new JFrame("Linear Regression Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                double xMin = Double.MAX_VALUE;
                double xMax = Double.MIN_VALUE;
                double yMin = Double.MAX_VALUE;
                double yMax = Double.MIN_VALUE;

                for (double v : x) {
                    if (v < xMin) xMin = v;
                    if (v > xMax) xMax = v;
                }

                for (double v : y) {
                    if (v < yMin) yMin = v;
                    if (v > yMax) yMax = v;
                }

                g2d.drawLine(padding, height - padding, width - padding, height - padding);
                g2d.drawLine(padding, padding, padding, height - padding);

                for (int i = 0; i < x.length; i++) {
                    int xPos = (int) ((x[i] - xMin) / (xMax - xMin) * (width - 2 * padding) + padding);
                    int yPos = (int) ((yMax - y[i]) / (yMax - yMin) * (height - 2 * padding) + padding);
                    g2d.fillOval(xPos - 3, yPos - 3, 6, 6);
                }

                int xStart = padding;
                int xEnd = width - padding;
                int yStart = (int) ((yMax - (slope * xMin + intercept)) / (yMax - yMin) * (height - 2 * padding) + padding);
                int yEnd = (int) ((yMax - (slope * xMax + intercept)) / (yMax - yMin) * (height - 2 * padding) + padding);
                g2d.drawLine(xStart, yStart, xEnd, yEnd);
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }
}
