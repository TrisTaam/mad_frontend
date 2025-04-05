package com.example.mobile6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private final ExecutorService diskIO = Executors.newSingleThreadExecutor();
    private final ExecutorService networkIO = Executors.newFixedThreadPool(THREAD_COUNT);
    private static volatile AppExecutors instance;

    public static AppExecutors getInstance() {
        if (instance != null) return instance;
        synchronized (AppExecutors.class) {
            if (instance == null) {
                instance = new AppExecutors();
            }
        }
        return instance;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }

    public ExecutorService networkIO() {
        return networkIO;
    }
}
