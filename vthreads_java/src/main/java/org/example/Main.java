package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        stackOverFlowErrorExample();
    }

    private static void stackOverFlowErrorExample() {
        for (int i = 0; i < 100_000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}