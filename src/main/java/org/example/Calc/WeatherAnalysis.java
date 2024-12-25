package org.example.Calc;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class WeatherAnalysis {

    public static void main(String[] args) {
        String apiKey = "75acf38b12fd4aee83e82158241812";  // Ваш API-ключ
        String city = "Kharkiv";  // Город
        String urlString = "http://api.weatherapi.com/v1/forecast.json?q=" + city + "&key=" + apiKey + "&days=7&aqi=no&alerts=no";  // Запрос данных

        // Списки для хранения данных
        List<Double> temperatures = new ArrayList<>();
        List<Double> humidities = new ArrayList<>();

        try {
            // Получение данных из API
            JSONObject weatherData = getWeatherData(urlString);
            // Извлекаем данные о температуре и влажности на каждый день
            for (int i = 0; i < weatherData.getJSONObject("forecast").getJSONArray("forecastday").length(); i++) {
                JSONObject dayData = weatherData.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(i);
                temperatures.add(dayData.getJSONObject("day").getDouble("avgtemp_c"));
                humidities.add(dayData.getJSONObject("day").getDouble("avghumidity"));
            }

            // Преобразование списков в массивы
            double[] tempArray = temperatures.stream().mapToDouble(Double::doubleValue).toArray();
            double[] humidityArray = humidities.stream().mapToDouble(Double::doubleValue).toArray();

            // Анализ корреляции
            PearsonsCorrelation correlation = new PearsonsCorrelation();
            double correlationCoefficient = correlation.correlation(tempArray, humidityArray);
            System.out.println("Correlation coefficient (Temperature vs Humidity): " + correlationCoefficient);

            // Линейная регрессия
            SimpleRegression regression = new SimpleRegression();
            for (int i = 0; i < tempArray.length; i++) {
                regression.addData(tempArray[i], humidityArray[i]);
            }

            double slope = regression.getSlope();
            double intercept = regression.getIntercept();
            double rSquared = regression.getRSquare();

            System.out.println("Regression Equation: Y = " + slope + " * X + " + intercept);
            System.out.println("R^2: " + rSquared);

            // Визуализация данных и регрессионной линии
            SwingUtilities.invokeLater(() -> createAndShowPlot(tempArray, humidityArray, slope, intercept));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject getWeatherData(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return new JSONObject(content.toString());
    }

    private static void createAndShowPlot(double[] x, double[] y, double slope, double intercept) {
        // Создаем серию данных для графика
        XYSeries series = new XYSeries("Temperature vs Humidity");
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }

        // Создаем график
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Temperature vs Humidity",  // Название графика
                "Temperature (°C)",  // Ось X
                "Humidity (%)",  // Ось Y
                dataset
        );

        // Рисуем регрессионную линию
        XYSeries regressionLine = new XYSeries("Regression Line");
        for (double i = 0; i < 40; i++) {
            regressionLine.add(i, slope * i + intercept);
        }
        dataset.addSeries(regressionLine);

        // Создаем панель для отображения графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        JFrame frame = new JFrame("Weather Data Analysis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
