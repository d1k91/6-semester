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

    }

}