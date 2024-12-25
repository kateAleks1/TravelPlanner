package org.example.Calc;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class DataGeneration {

    public static void main(String[] args) {
        // Кількість точок
        int n = 1000;

        // Ініціалізація генератора випадкових чисел
        Random rand = new Random();

        // Масиви для X та Y
        double[] X = new double[n];
        double[] Y = new double[n];

        // Генерація даних
        for (int i = 0; i < n; i++) {
            X[i] = rand.nextDouble() * 100; // Генерація випадкового числа від 0 до 100 для X
            Y[i] = 2 * X[i] + 10 + rand.nextGaussian() * 5; // Лінійна залежність Y = 2*X + 10 + шум
        }

        // Виведення результатів в консоль
        System.out.println("X, Y");
        for (int i = 0; i < n; i++) {
            System.out.println(X[i] + ", " + Y[i]);
        }

        // Запис в CSV файл (для використання в аналізі або візуалізації)
        try (FileWriter writer = new FileWriter("data.csv")) {
            writer.append("X,Y\n");
            for (int i = 0; i < n; i++) {
                writer.append(X[i] + "," + Y[i] + "\n");
            }
            System.out.println("Дані записано в файл data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
