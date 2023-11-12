package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


// -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=recordingClassic.jfr
// https://dzone.com/articles/request-handling-approaches-threadpool-webflux-cor

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        runClassicThreads();
//        runVirtualThreads();
    }

    private static void runClassicThreads() throws IOException {
        String filePath = "./vthreads.txt";
        int nrThreads = 3000;
        Thread[] threads = new Thread[nrThreads];

        initFile(filePath);

        for (int i = 0; i < nrThreads; i++) {
            int thdIdx = i;
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    appendToFile(filePath, "Platform Thread " + thdIdx + "\n");
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    private static void runVirtualThreads() throws IOException {

        String filePath = "./vthreads.txt";
        int nrThreads = 3000;
        Thread[] virtualThreads = new Thread[nrThreads];

        initFile(filePath);

        for (int i = 0; i < nrThreads; i++) {
            int thdIdx = i;
            virtualThreads[i] = Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(1000);
                    appendToFile(filePath, "Virtual Thread " + thdIdx + '\n');
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (Thread virtualThread : virtualThreads) {
            try {
                virtualThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return;
    }

    public static void appendToFile(String filePath, String textToAppend) throws IOException {
        Path path = Path.of(filePath);


        try {
            Thread.sleep(1000); // 1000 milliseconds = 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Handle exception appropriately, e.g., log it or throw it
        }

        try {
            Files.write(path, textToAppend.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                System.out.println("File created: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            FileWriter fileWriter = new FileWriter(filePath, false);

            // Close the FileWriter to save changes
            fileWriter.close();
        }
    }
}
