package org.example.Calc;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;

public class MovieRegression {

    public static void main(String[] args) throws IOException {
        String datasetPath = "C:\\Users\\Екатерина\\Downloads\\Travels\\src\\main\\java\\org\\example\\Calc\\tmdb_5000_credits.csv";  // Заміни на шлях до твого файлу

        // Загружаємо фільми
        List<Movie> movies = loadMovies(datasetPath);

        // Для розрахунків беремо кількість акторів та кількість членів команди (crew)
        List<Integer> actorsCount = new ArrayList<>();
        List<Integer> crewCount = new ArrayList<>();

        for (Movie movie : movies) {
            actorsCount.add(movie.getActorCount());
            crewCount.add(movie.getCrewCount());
        }

        // Розрахунок коефіцієнта кореляції
        double correlation = calculatePearsonCorrelation(actorsCount, crewCount);
        System.out.println("Коефіцієнт кореляції: " + correlation);

        // Розрахунок лінійної регресії
        double[] x = actorsCount.stream().mapToDouble(Integer::doubleValue).toArray();
        double[] y = crewCount.stream().mapToDouble(Integer::doubleValue).toArray();

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[][] xMatrix = new double[x.length][1]; // 1 стовпець для x
        for (int i = 0; i < x.length; i++) {
            xMatrix[i][0] = x[i];
        }
        regression.newSampleData(y, xMatrix); // Подаємо дані в регресію

        // Отримання коефіцієнтів
        double[] coefficients = regression.estimateRegressionParameters();
        double intercept = coefficients[0];  // Вільний коефіцієнт
        double slope = coefficients[1];
        System.out.println("Рівняння лінійної регресії: y = " + intercept + " + " + slope + " * x");

        // Обчислення коефіцієнта детермінації R^2
        double rSquared = regression.calculateRSquared();
        System.out.println("Коефіцієнт детермінації R^2: " + rSquared);

        // Аналіз залишків
        double[] residuals = regression.estimateResiduals();
        double sumSquaredResiduals = 0;
        for (double residual : residuals) {
            sumSquaredResiduals += residual * residual;
        }
        double meanSquaredResiduals = sumSquaredResiduals / residuals.length;
        System.out.println("Середньоквадратична помилка залишків: " + Math.sqrt(meanSquaredResiduals));

        // Побудова графіка
        plotRegression(x, y, intercept, slope);

        // Зв'язок між коефіцієнтом детермінації та коефіцієнтом кореляції
        System.out.println("R^2 = (Коефіцієнт кореляції)^2: " + (correlation * correlation));
    }

    // Клас Movie для зберігання інформації про фільм
    static class Movie {
        String title;
        String castJson;
        String crewJson;

        Movie(String title, String castJson, String crewJson) {
            this.title = title;
            this.castJson = castJson;
            this.crewJson = crewJson;
        }

        // Количество акторів
        public int getActorCount() {
            return parseJsonArray(castJson).length();
        }

        // Количество членів команди
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

    // Метод для завантаження даних з CSV
    public static List<Movie> loadMovies(String datasetPath) throws IOException {
        List<Movie> movies = new ArrayList<>();
        Reader reader = Files.newBufferedReader(Paths.get(datasetPath));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

        for (CSVRecord record : records) {
            String title = record.get("title");
            String castJson = record.get("cast");
            String crewJson = record.get("crew");

            movies.add(new Movie(title, castJson, crewJson));
        }

        return movies;
    }

    // Метод для обчислення коефіцієнта кореляції Пірсона
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

    // Метод для побудови графіка
    public static void plotRegression(double[] x, double[] y, double intercept, double slope) {
        XYSeries series = new XYSeries("Data");
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }

        XYSeries regressionLine = new XYSeries("Regression Line");
        for (double xi : x) {
            regressionLine.add(xi, intercept + slope * xi);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(regressionLine);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Лінійна регресія: actorsCount vs crewCount",
                "actorsCount",
                "crewCount",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        JFrame frame = new JFrame("Лінійна регресія");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
