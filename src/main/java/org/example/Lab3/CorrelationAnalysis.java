package org.example.Lab3;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.Arrays;

public class CorrelationAnalysis {
    public static void main(String[] args) {
        // Дані для аналізу
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 4, 6, 8, 10};
        // Обчислення коефіцієнта кореляції
        double correlationCoefficient = calculateCorrelation(x, y);

        // Визначення типу зв'язку
        String connectionType = getConnectionType(correlationCoefficient);

        // Виведення результатів
        System.out.printf("Коефіцієнт кореляції: %.2f%n", correlationCoefficient);
        System.out.println("Тип зв'язку: " + connectionType);

        // Побудова графіка
        createScatterPlot(x, y);
    }

    // Обчислення коефіцієнта кореляції
    // Обчислення коефіцієнта кореляції Пірсона
    private static double calculateCorrelation(double[] x, double[] y) {
        int n = x.length;

        // Обчислення середніх значень
        double meanX = Arrays.stream(x).average().orElse(0);
        double meanY = Arrays.stream(y).average().orElse(0);

        double numerator = 0; // Чисельник
        double sumX2 = 0;     // Сума квадратів відхилень X
        double sumY2 = 0;     // Сума квадратів відхилень Y

        for (int i = 0; i < n; i++) {
            double diffX = x[i] - meanX;
            double diffY = y[i] - meanY;

            numerator += diffX * diffY;
            sumX2 += diffX * diffX;
            sumY2 += diffY * diffY;
        }

        double denominator = Math.sqrt(sumX2 * sumY2);
        return numerator / denominator;
    }


    // Визначення типу та щільності зв'язку
    private static String getConnectionType(double r) {
        if (r > 0.7) return "Сильний прямий зв'язок";
        if (r > 0.3) return "Помірний прямий зв'язок";
        if (r > 0) return "Слабкий прямий зв'язок";
        if (r < -0.7) return "Сильний зворотний зв'язок";
        if (r < -0.3) return "Помірний зворотний зв'язок";
        if (r < 0) return "Слабкий зворотний зв'язок";
        return "Зв'язок відсутній";
    }

    // Побудова графіка
    private static void createScatterPlot(double[] x, double[] y) {
        XYSeries series1 = new XYSeries("Прямий зв'язок");
        XYSeries series2 = new XYSeries("Зворотний зв'язок");

        for (int i = 0; i < 5; i++) {
            series1.add(x[i], y[i]);
        }
        for (int i = 5; i < x.length; i++) {
            series2.add(x[i], y[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Графік кореляції",
                "X", "Y",
                dataset
        );

        JFrame frame = new JFrame("Візуалізація");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}
