package org.example.Lab3;

import java.util.Random;

public class NormalDistributionComparison2 {

    // Генерація нормально розподілених величин за допомогою Ziggurat Algorithm
    public static double zigguratNormal(Random random) {
        // Константи для Ziggurat (зазвичай вибираються залежно від точності)
        final int R = 128; // кількість рівнів
        final double[] x = new double[R + 1];
        final double[] y = new double[R];
        final double area = 0.3989422804014327; // 1/sqrt(2*pi)

        // Ініціалізація рівнів Ziggurat
        x[R] = area;
        for (int i = R - 1; i >= 0; i--) {
            x[i] = Math.sqrt(-2.0 * Math.log(x[i + 1] / area));
        }
        for (int i = 0; i < R; i++) {
            y[i] = area * Math.exp(-0.5 * x[i] * x[i]);
        }

        while (true) {
            int level = random.nextInt(R); // випадковий рівень
            double u = random.nextDouble();
            double value = u * x[level];

            // Перевірка чи значення лежить у "трапеції"
            if (Math.abs(value) < x[level + 1]) {
                return random.nextBoolean() ? value : -value;
            }

            // Генеруємо нове значення, якщо перевірка не пройшла
            double yValue = y[level] + random.nextDouble() * (y[level + 1] - y[level]);
            if (yValue < Math.exp(-0.5 * value * value)) {
                return random.nextBoolean() ? value : -value;
            }
        }
    }

    // Генерація нормально розподілених величин за допомогою Box-Muller Algorithm
    public static double boxMullerNormal(Random random) {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();
        return Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
    }

    public static void main(String[] args) {
        Random random = new Random();
        int nSamples = 1_000_000; // Кількість величин для тесту

        // Час виконання Ziggurat Algorithm
        long startZiggurat = System.nanoTime();
        for (int i = 0; i < nSamples; i++) {
            zigguratNormal(random);
        }
        long endZiggurat = System.nanoTime();
        System.out.println("Ziggurat Algorithm Time: " + (endZiggurat - startZiggurat) / 1_000_000.0 + " ms");

        // Час виконання Box-Muller Algorithm
        long startBoxMuller = System.nanoTime();
        for (int i = 0; i < nSamples; i++) {
            boxMullerNormal(random);
        }
        long endBoxMuller = System.nanoTime();
        System.out.println("Box-Muller Algorithm Time: " + (endBoxMuller - startBoxMuller) / 1_000_000.0 + " ms");
    }
}
