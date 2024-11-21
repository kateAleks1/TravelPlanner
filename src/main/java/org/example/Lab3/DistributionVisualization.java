package org.example.Lab3;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DistributionVisualization {

    public static void main(String[] args) {
        int nSamples = 10000;

        // Генерація даних для розподілів
        List<Double> uniformSamples = new ArrayList<>();
        List<Double> exponentialSamples = new ArrayList<>();
        List<Double> normalSamples = new ArrayList<>();

        for (int i = 0; i < nSamples; i++) {
            uniformSamples.add(uniform(0, 1));
            exponentialSamples.add(exponential());
            normalSamples.add(normal());
        }

        // Побудова гістограм
        JFreeChart uniformChart = createHistogram(uniformSamples, "Рівномірний розподіл [0, 1]");
        JFreeChart exponentialChart = createHistogram(exponentialSamples, "Експоненційний розподіл");
        JFreeChart normalChart = createHistogram(normalSamples, "Нормальний розподіл");

        // Побудова інтегральних графіків
        JFreeChart uniformCdfChart = createCumulativeChart(uniformSamples, "Інтегральний розподіл (Рівномірний)");
        JFreeChart exponentialCdfChart = createCumulativeChart(exponentialSamples, "Інтегральний розподіл (Експоненційний)");
        JFreeChart normalCdfChart = createCumulativeChart(normalSamples, "Інтегральний розподіл (Нормальний)");


        showChart(uniformChart, "Рівномірний розподіл");
        showChart(exponentialChart, "Експоненційний розподіл");
        showChart(normalChart, "Нормальний розподіл");

        showChart(uniformCdfChart, "Інтегральний розподіл (Рівномірний)");
        showChart(exponentialCdfChart, "Інтегральний розподіл (Експоненційний)");
        showChart(normalCdfChart, "Інтегральний розподіл (Нормальний)");
    }

    // Функція генерації рівномірного розподілу в діапазоні [a, b]
    public static double uniform(double a, double b) {
        Random random = new Random();
        return a + (b - a) * random.nextDouble();
    }

    // Функція генерації експоненційного розподілу
    public static double exponential() {
        Random random = new Random();
        return -Math.log(1 - random.nextDouble());
    }

    // Функція генерації нормального розподілу (метод Бокса-Мюллера)
    public static double normal() {
        Random random = new Random();
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();
        double z = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        return (z + 3) / 6; // Приведення до діапазону [0, 1]
    }

    // Функція створення гістограми
    public static JFreeChart createHistogram(List<Double> data, String title) {
        HistogramDataset dataset = new HistogramDataset();
        double[] dataArray = data.stream().mapToDouble(d -> d).toArray();
        dataset.addSeries("Data", dataArray, 50); // Кількість бінів = 50

        return ChartFactory.createHistogram(
                title,
                "Значення",
                "Частота",
                dataset
        );
    }

    // Функція створення інтегрального графіку (Cumulative Distribution Function)
    public static JFreeChart createCumulativeChart(List<Double> data, String title) {
        Collections.sort(data); // Сортуємо дані
        XYSeries series = new XYSeries("CDF");

        int n = data.size();
        for (int i = 0; i < n; i++) {
            double value = data.get(i);
            double cumulativeProbability = (i + 1) / (double) n;
            series.add(value, cumulativeProbability);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        return ChartFactory.createXYLineChart(
                title,
                "Значення",
                "Ймовірність",
                dataset
        );
    }

    // Функція відображення графіку
    public static void showChart(JFreeChart chart, String title) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}