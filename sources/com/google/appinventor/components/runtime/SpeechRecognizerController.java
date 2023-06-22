package com.google.appinventor.components.runtime;

public abstract class SpeechRecognizerController {
    SpeechListener speechListener;

    /* access modifiers changed from: package-private */
    public void addListener(SpeechListener speechListener2) {
        this.speechListener = speechListener2;
    }

    /* access modifiers changed from: package-private */
    public void start() {
    }

    /* access modifiers changed from: package-private */
    public void stop() {
    }
}
