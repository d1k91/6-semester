package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @TempDir
    Path tempDir;

    private Path createTempFile(String content) throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "test", ".txt");
        Files.write(tempFile, content.getBytes());
        return tempFile;
    }

    private String readFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    @Test
    void testBasicDataSeparation() throws IOException {
        String content = "42\n3.14\nHello\n-17\n2.718\nWorld\n";
        Path inputFile = createTempFile(content);

        Main.main(new String[]{
                "-o", tempDir.toString(),
                inputFile.toString()
        });

        Path integerFile = tempDir.resolve("numbers.txt");
        Path floatFile = tempDir.resolve("decimals.txt");
        Path stringFile = tempDir.resolve("texts.txt");

        assertTrue(Files.exists(integerFile));
        assertTrue(Files.exists(floatFile));
        assertTrue(Files.exists(stringFile));

        assertEquals("42\n-17\n", readFile(integerFile).replace("\r\n", "\n"));
        assertEquals("3.14\n2.718\n", readFile(floatFile).replace("\r\n", "\n"));
        assertEquals("Hello\nWorld\n", readFile(stringFile).replace("\r\n", "\n"));
    }

    @Test
    void testPrefixOption() throws IOException {
        String content = "42\n3.14\nHello";
        Path inputFile = createTempFile(content);

        Main.main(new String[]{
                "-o", tempDir.toString(),
                "-p", "test_",
                inputFile.toString()
        });

        assertTrue(Files.exists(tempDir.resolve("test_numbers.txt")));
        assertTrue(Files.exists(tempDir.resolve("test_decimals.txt")));
        assertTrue(Files.exists(tempDir.resolve("test_texts.txt")));
    }

    @Test
    void testAppendOption() throws IOException {
        String content1 = "42\n3.14\nHello";
        Path inputFile1 = createTempFile(content1);

        Main.main(new String[]{
                "-o", tempDir.toString(),
                inputFile1.toString()
        });

        String content2 = "17\n2.718\nWorld";
        Path inputFile2 = createTempFile(content2);

        Main.main(new String[]{
                "-o", tempDir.toString(),
                "-a",
                inputFile2.toString()
        });

        Path integerFile = tempDir.resolve("numbers.txt");
        assertEquals("42\n17\n", readFile(integerFile).replace("\r\n", "\n"));
    }

    @Test
    void testShortStats() throws IOException {
        String content = "12\n3.1415\nHello\nWorld\n-30\n6.5\n";
        Path inputFile = createTempFile(content);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{
                "-o", tempDir.toString(),
                "-s",
                inputFile.toString()
        });

        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("Total integers: 2"));
        assertTrue(output.contains("Total floating point numbers: 2"));
        assertTrue(output.contains("Total strings: 2"));
    }
    @Test
    void testFullStats() throws IOException {
        String content = "12\n3.1415\nHello\nWorld\n-30\n6.5\n";
        Path inputFile = createTempFile(content);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{
                "-o", tempDir.toString(),
                "-f",
                inputFile.toString()
        });

        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("Smallest integer: -30"));
        assertTrue(output.contains("Largest integer: 12"));
        assertTrue(output.contains("Sum of integers: -18"));
        assertTrue(output.contains("Average of integers: -9"));

        assertTrue(output.contains("Smallest floating point number: 3.1415"));
        assertTrue(output.contains("Largest floating point number: 6.5"));
        assertTrue(output.contains("Sum of floating point numbers: 9.6415"));
        assertTrue(output.contains("Average of floating point numbers: 4.82075"));

        assertTrue(output.contains("Shortest string length: 5"));
        assertTrue(output.contains("Longest string length: 5"));

    }

    @Test
    void testEmptyFileRemoval() throws IOException {
        String content = "Hello\nWorld";
        Path inputFile = createTempFile(content);

        Main.main(new String[]{
                "-o", tempDir.toString(),
                inputFile.toString()
        });

        assertFalse(Files.exists(tempDir.resolve("numbers.txt")));
        assertFalse(Files.exists(tempDir.resolve("decimals.txt")));
        assertTrue(Files.exists(tempDir.resolve("texts.txt")));
    }

    @Test
    void testMultipleInputFiles() throws IOException {
        Path inputFile1 = createTempFile("42\n3.14");
        Path inputFile2 = createTempFile("17\nWorld");

        Main.main(new String[]{
                "-o", tempDir.toString(),
                inputFile1.toString(),
                inputFile2.toString()
        });

        Path integerFile = tempDir.resolve("numbers.txt");
        assertTrue(Files.exists(integerFile));
        assertEquals("42\n17\n", readFile(integerFile).replace("\r\n", "\n"));
    }

    @Test
    void testInvalidInput() throws IOException {
        String content = "abc\n12.34.56\n123abc";
        Path inputFile = createTempFile(content);

        Main.main(new String[]{
                "-o", tempDir.toString(),
                inputFile.toString()
        });

        Path stringFile = tempDir.resolve("texts.txt");
        assertTrue(Files.exists(stringFile));
        String result = readFile(stringFile).replace("\r\n", "\n");
        assertTrue(result.contains("abc\n"));
        assertTrue(result.contains("12.34.56\n"));
        assertTrue(result.contains("123abc\n"));
    }
}
