package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class Main {
    static class NumberData {
        int totalNumbers = 0;
        int smallestNumber = Integer.MAX_VALUE;
        int largestNumber = Integer.MIN_VALUE;
        long totalSum = 0;
    }
    static class DecimalData {
        int totalDecimals = 0;
        double smallestDecimal = Double.POSITIVE_INFINITY;
        double largestDecimal = Double.NEGATIVE_INFINITY;
        double totalDecimalSum = 0;
    }
    static class TextData {
        int totalTexts = 0;
        int shortestText = Integer.MAX_VALUE;
        int longestText = 0;
    }
    public static void main(String[] args) {
        List<String> filesToProcess = new ArrayList<>();
        String outputDirectory = "";
        String filePrefix = "";
        boolean addToExisting = false;
        boolean showShortStats = false;
        boolean showDetailedStats = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) outputDirectory = args[++i];
                    break;
                case "-p":
                    if (i + 1 < args.length) filePrefix = args[++i];
                    break;
                case "-a":
                    addToExisting = true;
                    break;
                case "-s":
                    showShortStats = true;
                    break;
                case "-f":
                    showDetailedStats = true;
                    break;
                default:
                    filesToProcess.add(args[i]);
                    break;
            }
        }

        if (filesToProcess.isEmpty()) {
            System.err.println("No input files provided.");
            return;
        }
        if (!outputDirectory.isEmpty()) {
            Path outputPath = Paths.get(outputDirectory);
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                System.err.println("Failed to create output directory: " + outputPath);
                e.printStackTrace();
                return;
            }
        }

        Path integerOutput = Paths.get(outputDirectory, filePrefix + "numbers.txt");
        Path decimalOutput = Paths.get(outputDirectory, filePrefix + "decimals.txt");
        Path textOutput = Paths.get(outputDirectory, filePrefix + "texts.txt");

        boolean[] dataInfo = {false, false, false};
        NumberData numberData = new NumberData();
        DecimalData decimalData = new DecimalData();
        TextData textData = new TextData();

        try (
                BufferedWriter numberWriter = Files.newBufferedWriter(integerOutput, addToExisting ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                BufferedWriter decimalWriter = Files.newBufferedWriter(decimalOutput, addToExisting ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                BufferedWriter textWriter = Files.newBufferedWriter(textOutput, addToExisting ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)
        ) {
            for (String file : filesToProcess) {
                analyzeFile(file, numberWriter, decimalWriter, textWriter, dataInfo,
                        numberData, decimalData, textData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteIfEmpty(integerOutput, dataInfo[0]);
        deleteIfEmpty(decimalOutput, dataInfo[1]);
        deleteIfEmpty(textOutput, dataInfo[2]);

        if (showShortStats || showDetailedStats) {
            printStats(numberData, decimalData, textData, showDetailedStats, dataInfo);
        }
    }

    public static void analyzeFile(String file, BufferedWriter numberWriter, BufferedWriter decimalWriter,
                                   BufferedWriter textWriter, boolean[] dataInfo,
                                   NumberData numberData, DecimalData decimalData, TextData textData) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.matches("^-?\\d+$")) {
                    numberWriter.write(line);
                    numberWriter.newLine();
                    dataInfo[0] = true;
                    numberData.totalNumbers++;
                    int value = Integer.parseInt(line);
                    numberData.smallestNumber = Math.min(numberData.smallestNumber, value);
                    numberData.largestNumber = Math.max(numberData.largestNumber, value);
                    numberData.totalSum += value;
                } else if (line.matches("^-?\\d*\\.\\d+([eE][+-]?\\d+)?$") || line.matches("^-?\\d+([eE][+-]?\\d+)$")) {
                    decimalWriter.write(line);
                    decimalWriter.newLine();
                    dataInfo[1] = true;
                    decimalData.totalDecimals++;
                    double value = Double.parseDouble(line);
                    decimalData.smallestDecimal = Math.min(decimalData.smallestDecimal, value);
                    decimalData.largestDecimal = Math.max(decimalData.largestDecimal, value);
                    decimalData.totalDecimalSum += value;
                } else {
                    textWriter.write(line);
                    textWriter.newLine();
                    dataInfo[2] = true;
                    textData.totalTexts++;
                    int length = line.length();
                    textData.shortestText = Math.min(textData.shortestText, length);
                    textData.longestText = Math.max(textData.longestText, length);
                }
            }
        } catch (IOException e) {
            System.err.println("Error analyzing file: " + file);
            e.printStackTrace();
        }
    }

    private static void deleteIfEmpty(Path file, boolean hasData) {
        if (!hasData) {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                System.err.println("Error removing empty file: " + file);
                e.printStackTrace();
            }
        }
    }

    private static void printStats(NumberData numberData, DecimalData decimalData, TextData textData,
                                   boolean showDetailedStats, boolean[] dataInfo) {
        System.out.println("Data Analysis:");

        if (dataInfo[0]) {
            System.out.println("Total integers: " + numberData.totalNumbers);
            if (showDetailedStats) {
                System.out.println("Smallest integer: " + (numberData.totalNumbers > 0 ? numberData.smallestNumber : "N/A"));
                System.out.println("Largest integer: " + (numberData.totalNumbers > 0 ? numberData.largestNumber : "N/A"));
                System.out.println("Sum of integers: " + numberData.totalSum);
                System.out.println("Average of integers: " + (numberData.totalNumbers > 0 ? (double) numberData.totalSum / numberData.totalNumbers : "N/A"));
            }
        }

        if (dataInfo[1]) {
            System.out.println("\nTotal floating point numbers: " + decimalData.totalDecimals);
            if (showDetailedStats) {
                System.out.println("Smallest floating point number: " + (decimalData.totalDecimals > 0 ? decimalData.smallestDecimal : "N/A"));
                System.out.println("Largest floating point number: " + (decimalData.totalDecimals > 0 ? decimalData.largestDecimal : "N/A"));
                System.out.println("Sum of floating point numbers: " + String.format("%.3f", decimalData.totalDecimalSum));
                System.out.println("Average of floating point numbers: " + (decimalData.totalDecimals > 0 ? String.format("%.3f", (double) decimalData.totalDecimalSum / decimalData.totalDecimals) : "N/A"));
            }
        }

        if (dataInfo[2]) {
            System.out.println("\nTotal strings: " + textData.totalTexts);
            if (showDetailedStats) {
                System.out.println("Shortest string length: " + (textData.totalTexts > 0 ? textData.shortestText : "N/A"));
                System.out.println("Longest string length: " + (textData.totalTexts > 0 ? textData.longestText : "N/A"));
            }
        }
    }

}