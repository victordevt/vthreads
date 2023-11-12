package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        runClassicThreads();
        runVirtualThreads();
    }

    private static void runClassicThreads() {
        int i = 0;
        for (i = 0; i < 100_000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private static void runVirtualThreads() {

        String filePath = "./vthreads.txt";
        int nrThreads = 100_000;
        Thread[] virtualThreads = new Thread[nrThreads];
        for (int i = 0; i < nrThreads; i++) {
            int threadNumber = i;
            virtualThreads[i] = Thread.ofVirtual().start(() -> {
                try {
                    appendToFile(filePath, "Virtual Thread " + threadNumber + '\n');
                } catch (IOException e) {
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

//        int i = 0;
//        appendToFile("./vthreads.txt", "ok");
//        for (i = 0; i < 100_000; i++) {
//            Thread thread = Thread.ofVirtual().name("virtual thread - " + i).start(() -> {
//                try {
//                    Thread.sleep(1000);
//                    appendToFile("./vthreads.txt", Thread.currentThread().getName());
//                } catch (InterruptedException e) {
//                    System.out.println("Exc");
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//        i = 0;
    }

    public static void appendToFile(String filePath, String textToAppend) throws IOException {
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
}
