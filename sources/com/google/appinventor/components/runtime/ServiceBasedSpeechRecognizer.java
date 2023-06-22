package com.google.appinventor.components.runtime;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import com.google.appinventor.components.runtime.util.ErrorMessages;

public class ServiceBasedSpeechRecognizer extends SpeechRecognizerController implements RecognitionListener {
    private ComponentContainer container;
    private Intent recognizerIntent;
    private String result;
    private SpeechRecognizer speech = null;

    public ServiceBasedSpeechRecognizer(ComponentContainer container2, Intent recognizerIntent2) {
        this.container = container2;
        this.recognizerIntent = recognizerIntent2;
    }

    public void start() {
        this.speech = SpeechRecognizer.createSpeechRecognizer(this.container.$context());
        this.speech.setRecognitionListener(this);
        this.speech.startListening(this.recognizerIntent);
    }

    public void stop() {
        if (this.speech != null) {
            this.speech.stopListening();
        }
    }

    public void onReadyForSpeech(Bundle bundle) {
    }

    public void onBeginningOfSpeech() {
    }

    public void onRmsChanged(float v) {
    }

    public void onBufferReceived(byte[] bytes) {
    }

    public void onEndOfSpeech() {
    }

    public void onError(int i) {
        this.speechListener.onError(getErrorMessage(i));
    }

    public void onResults(Bundle bundle) {
        if (bundle.isEmpty()) {
            this.result = "";
        } else {
            this.result = bundle.getStringArrayList("results_recognition").get(0);
        }
        this.speechListener.onResult(this.result);
    }

    public void onPartialResults(Bundle bundle) {
        if (bundle.isEmpty()) {
            this.result = "";
        } else {
            this.result = bundle.getStringArrayList("results_recognition").get(0);
        }
        this.speechListener.onPartialResult(this.result);
    }

    public void onEvent(int i, Bundle bundle) {
    }

    private int getErrorMessage(int errorCode) {
        switch (errorCode) {
            case 1:
                return ErrorMessages.ERROR_NETWORK_TIMEOUT;
            case 2:
                return ErrorMessages.ERROR_NETWORK;
            case 3:
                return ErrorMessages.ERROR_AUDIO;
            case 4:
                return ErrorMessages.ERROR_SERVER;
            case 5:
                return ErrorMessages.ERROR_CLIENT;
            case 6:
                return ErrorMessages.ERROR_SPEECH_TIMEOUT;
            case 7:
                return ErrorMessages.ERROR_NO_MATCH;
            case 8:
                return ErrorMessages.ERROR_RECOGNIZER_BUSY;
            case 9:
                return ErrorMessages.ERROR_INSUFFICIENT_PERMISSIONS;
            default:
                return 0;
        }
    }
}
