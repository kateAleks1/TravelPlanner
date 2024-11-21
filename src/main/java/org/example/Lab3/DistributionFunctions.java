package org.example.Lab3;

import java.util.Random;

public class DistributionFunctions {
    private static final Random random = new Random();
    // Спрощена реалізація алгоритму зиккурата
    public static double ziggurat() {
        Random random = new Random();
        double R = Math.sqrt(-2 * Math.log(random.nextDouble()));
        double V = 2 * Math.PI * random.nextDouble();
        return R * Math.cos(V);  // Повертаємо нормально розподілене значення
    }

    public static double exponential() {
        return -Math.log(1 - random.nextDouble());
    }

    // Генерація нормально розподіленої величини в діапазоні [0, 1] (метод Box-Muller)
    public static double normal() {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();
        double z0 = Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
        return z0 * 0.5 + 0.5; // нормалізація до [0,1]
    }

    public static double uniform(double a, double b) {
        return a + (b - a) * random.nextDouble();
    }
}
