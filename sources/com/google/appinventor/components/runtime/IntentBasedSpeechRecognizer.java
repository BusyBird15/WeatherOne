package com.google.appinventor.components.runtime;

import android.content.Intent;

public class IntentBasedSpeechRecognizer extends SpeechRecognizerController implements ActivityResultListener {
    private ComponentContainer container;
    private Intent recognizerIntent;
    private int requestCode;
    private String result;

    public IntentBasedSpeechRecognizer(ComponentContainer container2, Intent recognizerIntent2) {
        this.container = container2;
        this.recognizerIntent = recognizerIntent2;
    }

    public void start() {
        if (this.requestCode == 0) {
            this.requestCode = this.container.$form().registerForActivityResult(this);
        }
        this.container.$context().startActivityForResult(this.recognizerIntent, this.requestCode);
    }

    public void stop() {
    }

    public void resultReturned(int requestCode2, int resultCode, Intent data) {
        if (requestCode2 == this.requestCode && resultCode == -1) {
            if (data.hasExtra("android.speech.extra.RESULTS")) {
                this.result = data.getExtras().getStringArrayList("android.speech.extra.RESULTS").get(0);
            } else {
                this.result = "";
            }
            this.speechListener.onResult(this.result);
        }
    }
}
