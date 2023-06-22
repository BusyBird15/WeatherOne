package com.google.appinventor.components.runtime.util;

import android.app.Activity;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.google.appinventor.components.runtime.util.ITextToSpeech;
import java.util.HashMap;
import java.util.Locale;

public class InternalTextToSpeech implements ITextToSpeech {
    private static final String LOG_TAG = "InternalTTS";
    /* access modifiers changed from: private */
    public final Activity activity;
    /* access modifiers changed from: private */
    public final ITextToSpeech.TextToSpeechCallback callback;
    /* access modifiers changed from: private */
    public volatile boolean isTtsInitialized;
    private Handler mHandler = new Handler();
    private int nextUtteranceId = 1;
    private TextToSpeech tts;
    private int ttsMaxRetries = 20;
    private int ttsRetryDelay = 500;

    public InternalTextToSpeech(Activity activity2, ITextToSpeech.TextToSpeechCallback callback2) {
        this.activity = activity2;
        this.callback = callback2;
        initializeTts();
    }

    private void initializeTts() {
        if (this.tts == null) {
            Log.d(LOG_TAG, "INTERNAL TTS is reinitializing");
            this.tts = new TextToSpeech(this.activity, new TextToSpeech.OnInitListener() {
                public void onInit(int status) {
                    if (status == 0) {
                        boolean unused = InternalTextToSpeech.this.isTtsInitialized = true;
                    }
                }
            });
        }
    }

    public void speak(String message, Locale loc) {
        Log.d(LOG_TAG, "Internal TTS got speak");
        speak(message, loc, 0);
    }

    public boolean isInitialized() {
        return this.isTtsInitialized;
    }

    /* access modifiers changed from: private */
    public void speak(final String message, final Locale loc, final int retries) {
        Log.d(LOG_TAG, "InternalTTS speak called, message = " + message);
        if (retries > this.ttsMaxRetries) {
            Log.d(LOG_TAG, "max number of speak retries exceeded: speak will fail");
            this.callback.onFailure();
        }
        if (this.isTtsInitialized) {
            Log.d(LOG_TAG, "TTS initialized after " + retries + " retries.");
            this.tts.setLanguage(loc);
            this.tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                public void onUtteranceCompleted(String utteranceId) {
                    InternalTextToSpeech.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            InternalTextToSpeech.this.callback.onSuccess();
                        }
                    });
                }
            });
            HashMap<String, String> params = new HashMap<>();
            int i = this.nextUtteranceId;
            this.nextUtteranceId = i + 1;
            params.put("utteranceId", Integer.toString(i));
            TextToSpeech textToSpeech = this.tts;
            TextToSpeech textToSpeech2 = this.tts;
            if (textToSpeech.speak(message, 0, params) == -1) {
                Log.d(LOG_TAG, "speak called and tts.speak result was an error");
                this.callback.onFailure();
                return;
            }
            return;
        }
        Log.d(LOG_TAG, "speak called when TTS not initialized");
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Log.d(InternalTextToSpeech.LOG_TAG, "delaying call to speak.  Retries is: " + retries + " Message is: " + message);
                InternalTextToSpeech.this.speak(message, loc, retries + 1);
            }
        }, (long) this.ttsRetryDelay);
    }

    public void onStop() {
        Log.d(LOG_TAG, "Internal TTS got onStop");
    }

    public void onDestroy() {
        Log.d(LOG_TAG, "Internal TTS got onDestroy");
        if (this.tts != null) {
            this.tts.shutdown();
            this.isTtsInitialized = false;
            this.tts = null;
        }
    }

    public void onResume() {
        Log.d(LOG_TAG, "Internal TTS got onResume");
        initializeTts();
    }

    public void setPitch(float pitch) {
        this.tts.setPitch(pitch);
    }

    public void setSpeechRate(float speechRate) {
        this.tts.setSpeechRate(speechRate);
    }

    public int isLanguageAvailable(Locale loc) {
        return this.tts.isLanguageAvailable(loc);
    }
}
