package org.example.Calc;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public static void main(String[] args) {
        String filePath = "weather_data.csv";
        String[] data = {"Дата", "Температура (°C)", "Влажность (%)"};

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String line : data) {
                writer.append(line);
                writer.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
