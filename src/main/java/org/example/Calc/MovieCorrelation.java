package org.example.Calc;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class MovieCorrelation {

    public static void main(String[] args) throws IOException {
        String datasetPath = "C:\\Users\\Екатерина\\Downloads\\Travels\\src\\main\\java\\org\\example\\Calc\\tmdb_5000_credits.csv";

        // Загружаем фильмы
        List<Movie> movies = loadMovies(datasetPath);

        // Для расчетов возьмем количество актеров и количество членов команды (crew)
        List<Integer> actorsCount = new ArrayList<>();
        List<Integer> crewCount = new ArrayList<>();

        for (Movie movie : movies) {
            actorsCount.add(movie.getActorCount());
            crewCount.add(movie.getCrewCount());
        }

        // Расчет коэффициента корреляции
        double correlation = calculatePearsonCorrelation(actorsCount, crewCount);
        System.out.println("Коэффициент корреляции: " + correlation);

        // Визуализация данных и корреляции
        plotCorrelation(actorsCount, crewCount, correlation);
    }

    static class Movie {
        String title;
        String castJson;
        String crewJson;

        Movie(String title, String castJson, String crewJson) {
            this.title = title;
            this.castJson = castJson;
            this.crewJson = crewJson;
        }

        // Количество актеров
        public int getActorCount() {
            return parseJsonArray(castJson).length();
        }

        // Количество членов команды
        public int getCrewCount() {
            return parseJsonArray(crewJson).length();
        }

        private JSONArray parseJsonArray(String jsonString) {
            try {
                return new JSONArray(jsonString);
            } catch (Exception e) {
                return new JSONArray();
            }
        }
    }

    // Метод загрузки данных из CSV
    public static List<Movie> loadMovies(String datasetPath) throws IOException {
        List<Movie> movies = new ArrayList<>();

        Reader reader = Files.newBufferedReader(Paths.get(datasetPath));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        for (CSVRecord record : records) {
            String title = record.get("title");
            String castJson = record.get("cast"); // Извлекаем данные о cast в виде строки JSON
            String crewJson = record.get("crew"); // Извлекаем данные о crew в виде строки JSON

            movies.add(new Movie(title, castJson, crewJson));
        }

        return movies;
    }

    // Метод для вычисления коэффициента корреляции Пирсона
    public static double calculatePearsonCorrelation(List<Integer> x, List<Integer> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Lists must have the same size");
        }

        int n = x.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumX2 += x.get(i) * x.get(i);
            sumY2 += y.get(i) * y.get(i);
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        return numerator / denominator;
    }

    // Метод для построения графика корреляции
    public static void plotCorrelation(List<Integer> x, List<Integer> y, double correlation) {
        XYSeries series = new XYSeries("Data");
        for (int i = 0; i < x.size(); i++) {
            series.add(x.get(i), y.get(i));
        }

        // Создание коллекции данных для графика
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // Создание графика
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Корреляция: Количество актеров vs Количество членов команды",
                "Количество актеров",
                "Количество членов команды",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Добавление аннотации для коэффициента корреляции
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("Корреляция: " + correlation, x.get(x.size() - 1), y.get(y.size() - 1)));

        // Отображение графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        JFrame frame = new JFrame("Корреляция");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}