package org.example.Calc;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class LinearRegressionAnalysis {

    public static void main(String[] args) {
        String filePath = "data.csv";

        generateDataset(filePath, 100);

        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                xValues.add(Double.parseDouble(values[0]));
                yValues.add(Double.parseDouble(values[1]));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        double[] xArray = xValues.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = yValues.stream().mapToDouble(Double::doubleValue).toArray();

        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }

        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
        double rSquared = regression.getRSquare();

        System.out.println("Regression Equation: Y = " + slope + " * X + " + intercept);
        System.out.println("Coefficient of Determination (R^2): " + rSquared);

        SwingUtilities.invokeLater(() -> createAndShowPlot(xArray, yArray, slope, intercept));

        System.out.println("Residual Analysis:");
        for (int i = 0; i < xArray.length; i++) {
            double predictedY = regression.predict(xArray[i]);
            double residual = yArray[i] - predictedY;
            System.out.println("X: " + xArray[i] + ", Observed Y: " + yArray[i] + ", Predicted Y: " + predictedY + ", Residual: " + residual);
        }

        System.out.println("Relationship between R^2 and correlation coefficient: R^2 = r^2");
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

    private static void generateDataset(String filePath, int size) {
        Random random = new Random();
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < size; i++) {
                double x = i + random.nextDouble();
                double y = 2.5 * x + 10 + random.nextGaussian() * 5;
                writer.write(x + "," + y + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error generating dataset: " + e.getMessage());
        }
    }
}
