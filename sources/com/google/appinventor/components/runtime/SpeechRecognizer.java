package com.google.appinventor.components.runtime;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.annotations.UsesQueries;
import com.google.appinventor.components.annotations.androidmanifest.ActionElement;
import com.google.appinventor.components.annotations.androidmanifest.IntentFilterElement;
import com.google.appinventor.components.common.ComponentCategory;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MEDIA, description = "Component for using Voice Recognition to convert from speech to text", iconName = "images/speechRecognizer.png", nonVisible = true, version = 3)
@UsesPermissions({"android.permission.RECORD_AUDIO", "android.permission.INTERNET"})
public class SpeechRecognizer extends AndroidNonvisibleComponent implements Component, OnClearListener, SpeechListener {
    private final ComponentContainer container;
    /* access modifiers changed from: private */
    public boolean havePermission = false;
    private String language = "";
    private Intent recognizerIntent;
    private String result;
    private SpeechRecognizerController speechRecognizerController;
    private boolean useLegacy = true;

    public SpeechRecognizer(ComponentContainer container2) {
        super(container2.$form());
        container2.$form().registerForOnClear(this);
        this.container = container2;
        this.result = "";
        this.recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        this.recognizerIntent.putExtra("android.speech.extra.PARTIAL_RESULTS", true);
        UseLegacy(this.useLegacy);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Language() {
        return this.language;
    }

    @SimpleProperty
    public void Language(String language2) {
        this.language = language2;
        if (TextUtils.isEmpty(language2)) {
            this.recognizerIntent.removeExtra("android.speech.extra.LANGUAGE");
        } else {
            this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE", language2);
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Result() {
        return this.result;
    }

    @SimpleFunction
    public void GetText() {
        if (!this.havePermission) {
            this.form.runOnUiThread(new Runnable() {
                public void run() {
                    SpeechRecognizer.this.form.askPermission("android.permission.RECORD_AUDIO", new PermissionResultHandler() {
                        public void HandlePermissionResponse(String permission, boolean granted) {
                            if (granted) {
                                boolean unused = this.havePermission = true;
                                this.GetText();
                                return;
                            }
                            SpeechRecognizer.this.form.dispatchPermissionDeniedEvent((Component) this, "GetText", "android.permission.RECORD_AUDIO");
                        }
                    });
                }
            });
            return;
        }
        BeforeGettingText();
        this.speechRecognizerController.addListener(this);
        this.speechRecognizerController.start();
    }

    @SimpleFunction
    public void Stop() {
        if (this.speechRecognizerController != null) {
            this.speechRecognizerController.stop();
        }
    }

    @SimpleEvent
    public void BeforeGettingText() {
        EventDispatcher.dispatchEvent(this, "BeforeGettingText", new Object[0]);
    }

    @SimpleEvent
    public void AfterGettingText(String result2, boolean partial) {
        EventDispatcher.dispatchEvent(this, "AfterGettingText", result2, Boolean.valueOf(partial));
    }

    public void onPartialResult(String text) {
        this.result = text;
        AfterGettingText(this.result, true);
    }

    public void onResult(String text) {
        this.result = text;
        AfterGettingText(this.result, false);
    }

    public void onError(int errorNumber) {
        this.form.dispatchErrorOccurredEvent(this, "GetText", errorNumber, new Object[0]);
    }

    public void onClear() {
        Stop();
        this.speechRecognizerController = null;
        this.recognizerIntent = null;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "If true, an app can retain their older behaviour.")
    public boolean UseLegacy() {
        return this.useLegacy;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @UsesQueries(intents = {@IntentFilterElement(actionElements = {@ActionElement(name = "android.speech.RecognitionService")})})
    @SimpleProperty(description = "If true, a separate dialog is used to recognize speech (the default). If false, speech is recognized in the background and partial results are also provided.")
    public void UseLegacy(boolean useLegacy2) {
        this.useLegacy = useLegacy2;
        Stop();
        if (useLegacy2 || Build.VERSION.SDK_INT < 8) {
            this.speechRecognizerController = new IntentBasedSpeechRecognizer(this.container, this.recognizerIntent);
        } else {
            this.speechRecognizerController = new ServiceBasedSpeechRecognizer(this.container, this.recognizerIntent);
        }
    }
}
