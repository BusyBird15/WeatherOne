package com.google.appinventor.components.runtime.util;

public class Synchronizer<T> {
    private Throwable error;
    private String errorMessage;
    private volatile boolean finished = false;
    private T result;

    public synchronized void waitfor() {
        while (!this.finished) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void wakeup(T result2) {
        this.finished = true;
        this.result = result2;
        notifyAll();
    }

    public synchronized void error(String error2) {
        this.finished = true;
        this.errorMessage = error2;
        notifyAll();
    }

    public synchronized void caught(Throwable error2) {
        this.finished = true;
        this.error = error2;
        notifyAll();
    }

    public T getResult() {
        return this.result;
    }

    public String getError() {
        return this.errorMessage;
    }

    public Throwable getThrowable() {
        return this.error;
    }

    public String toString() {
        return "Synchronizer(" + this.result + ", " + this.error + ", " + this.errorMessage + ")";
    }
}
