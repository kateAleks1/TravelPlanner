package org.example.Lab3;
public class PerformanceTest {
    public static void main(String[] args) {
        int n = 1000000;

        // Вимірювання часу виконання для алгоритму Бокса-Мюллера
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            DistributionFunctions.normal();
        }
        long durationBoxMuller = System.nanoTime() - start;

        // Вимірювання часу виконання для алгоритму Зиккурат
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            DistributionFunctions.ziggurat();
        }
        long durationZiggurat = System.nanoTime() - start;

        // Виведення результатів
        System.out.println("Box-Muller time: " + durationBoxMuller + " ns");
        System.out.println("Ziggurat time: " + durationZiggurat + " ns");
    }
}
