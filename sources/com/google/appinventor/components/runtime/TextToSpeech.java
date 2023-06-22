package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.collect.Maps;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.ExternalTextToSpeech;
import com.google.appinventor.components.runtime.util.ITextToSpeech;
import com.google.appinventor.components.runtime.util.InternalTextToSpeech;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MEDIA, description = "The TextToSpeech component speaks a given text aloud.  You can set the pitch and the rate of speech. <p>You can also set a language by supplying a language code.  This changes the pronunciation of words, not the actual language spoken.  For example, setting the language to French and speaking English text will sound like someone speaking English (en) with a French accent.</p> <p>You can also specify a country by supplying a country code. This can affect the pronunciation.  For example, British English (GBR) will sound different from US English (USA).  Not every country code will affect every language.</p> <p>The languages and countries available depend on the particular device, and can be listed with the AvailableLanguages and AvailableCountries properties.</p>", iconName = "images/textToSpeech.png", nonVisible = true, version = 5)
public class TextToSpeech extends AndroidNonvisibleComponent implements Component, OnStopListener, OnResumeListener, OnDestroyListener {
    private static final String LOG_TAG = "TextToSpeech";
    private static final Map<String, Locale> iso3CountryToLocaleMap = Maps.newHashMap();
    private static final Map<String, Locale> iso3LanguageToLocaleMap = Maps.newHashMap();
    private YailList allCountries;
    private YailList allLanguages;
    private String country;
    private ArrayList<String> countryList;
    private boolean isTtsPrepared;
    private String iso2Country;
    private String iso2Language;
    private String language;
    private ArrayList<String> languageList;
    private float pitch = 1.0f;
    /* access modifiers changed from: private */
    public boolean result = false;
    private float speechRate = 1.0f;
    private final ITextToSpeech tts;

    static {
        initLocaleMaps();
    }

    private static void initLocaleMaps() {
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                String iso3Country = locale.getISO3Country();
                if (iso3Country.length() > 0) {
                    iso3CountryToLocaleMap.put(iso3Country, locale);
                }
            } catch (MissingResourceException e) {
            }
            try {
                String iso3Language = locale.getISO3Language();
                if (iso3Language.length() > 0) {
                    iso3LanguageToLocaleMap.put(iso3Language, locale);
                }
            } catch (MissingResourceException e2) {
            }
        }
    }

    public TextToSpeech(ComponentContainer container) {
        super(container.$form());
        boolean useExternalLibrary;
        ITextToSpeech internalTextToSpeech;
        Language("");
        Country("");
        if (SdkLevel.getLevel() < 4) {
            useExternalLibrary = true;
        } else {
            useExternalLibrary = false;
        }
        Log.v(LOG_TAG, "Using " + (useExternalLibrary ? "external" : "internal") + " TTS library.");
        ITextToSpeech.TextToSpeechCallback callback = new ITextToSpeech.TextToSpeechCallback() {
            public void onSuccess() {
                boolean unused = TextToSpeech.this.result = true;
                TextToSpeech.this.AfterSpeaking(true);
            }

            public void onFailure() {
                boolean unused = TextToSpeech.this.result = false;
                TextToSpeech.this.AfterSpeaking(false);
            }
        };
        if (useExternalLibrary) {
            internalTextToSpeech = new ExternalTextToSpeech(container, callback);
        } else {
            internalTextToSpeech = new InternalTextToSpeech(container.$context(), callback);
        }
        this.tts = internalTextToSpeech;
        this.form.registerForOnStop(this);
        this.form.registerForOnResume(this);
        this.form.registerForOnDestroy(this);
        this.form.setVolumeControlStream(3);
        this.isTtsPrepared = false;
        this.languageList = new ArrayList<>();
        this.countryList = new ArrayList<>();
        this.allLanguages = YailList.makeList((List) this.languageList);
        this.allCountries = YailList.makeList((List) this.countryList);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean Result() {
        return this.result;
    }

    @DesignerProperty(defaultValue = "", editorType = "languages")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Sets the language for TextToSpeech. This changes the way that words are pronounced, not the actual language that is spoken.  For example setting the language to and speaking English text with sound like someone speaking English with a French accent.")
    public void Language(String language2) {
        Locale locale;
        switch (language2.length()) {
            case 2:
                locale = new Locale(language2);
                this.language = locale.getLanguage();
                break;
            case 3:
                locale = iso3LanguageToLocale(language2);
                this.language = locale.getISO3Language();
                break;
            default:
                locale = Locale.getDefault();
                this.language = locale.getLanguage();
                break;
        }
        this.iso2Language = locale.getLanguage();
    }

    private static Locale iso3LanguageToLocale(String iso3Language) {
        Locale mappedLocale = iso3LanguageToLocaleMap.get(iso3Language);
        if (mappedLocale == null) {
            mappedLocale = iso3LanguageToLocaleMap.get(iso3Language.toLowerCase(Locale.ENGLISH));
        }
        return mappedLocale == null ? Locale.getDefault() : mappedLocale;
    }

    @DesignerProperty(defaultValue = "1.0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Sets the Pitch for TextToSpeech The values should be between 0 and 2 where lower values lower the tone of synthesized voice and greater values raise it.")
    public void Pitch(float pitch2) {
        if (pitch2 < 0.0f || pitch2 > 2.0f) {
            Log.i(LOG_TAG, "Pitch value should be between 0 and 2, but user specified: " + pitch2);
            return;
        }
        this.pitch = pitch2;
        ITextToSpeech iTextToSpeech = this.tts;
        if (pitch2 == 0.0f) {
            pitch2 = 0.1f;
        }
        iTextToSpeech.setPitch(pitch2);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns current value of Pitch")
    public float Pitch() {
        return this.pitch;
    }

    @DesignerProperty(defaultValue = "1.0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Sets the SpeechRate for TextToSpeech. The values should be between 0 and 2 where lower values slow down the pitch and greater values accelerate it.")
    public void SpeechRate(float speechRate2) {
        if (speechRate2 < 0.0f || speechRate2 > 2.0f) {
            Log.i(LOG_TAG, "speechRate value should be between 0 and 2, but user specified: " + speechRate2);
            return;
        }
        this.speechRate = speechRate2;
        ITextToSpeech iTextToSpeech = this.tts;
        if (speechRate2 == 0.0f) {
            speechRate2 = 0.1f;
        }
        iTextToSpeech.setSpeechRate(speechRate2);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns current value of SpeechRate")
    public float SpeechRate() {
        return this.speechRate;
    }

    @SimpleProperty
    public String Language() {
        return this.language;
    }

    @DesignerProperty(defaultValue = "", editorType = "countries")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Country code to use for speech generation.  This can affect the pronounciation.  For example, British English (GBR) will sound different from US English (USA).  Not every country code will affect every language.")
    public void Country(String country2) {
        Locale locale;
        switch (country2.length()) {
            case 2:
                locale = new Locale(country2);
                this.country = locale.getCountry();
                break;
            case 3:
                locale = iso3CountryToLocale(country2);
                this.country = locale.getISO3Country();
                break;
            default:
                locale = Locale.getDefault();
                this.country = locale.getCountry();
                break;
        }
        this.iso2Country = locale.getCountry();
    }

    private static Locale iso3CountryToLocale(String iso3Country) {
        Locale mappedLocale = iso3CountryToLocaleMap.get(iso3Country);
        if (mappedLocale == null) {
            mappedLocale = iso3CountryToLocaleMap.get(iso3Country.toUpperCase(Locale.ENGLISH));
        }
        return mappedLocale == null ? Locale.getDefault() : mappedLocale;
    }

    @SimpleProperty
    public String Country() {
        return this.country;
    }

    @SimpleProperty(description = "List of the languages available on this device for use with TextToSpeech.  Check the Android developer documentation under supported languages to find the meanings of these abbreviations.")
    public YailList AvailableLanguages() {
        prepareLanguageAndCountryProperties();
        return this.allLanguages;
    }

    @SimpleProperty(description = "List of the country codes available on this device for use with TextToSpeech.  Check the Android developer documentation under supported languages to find the meanings of these abbreviations.")
    public YailList AvailableCountries() {
        prepareLanguageAndCountryProperties();
        return this.allCountries;
    }

    public void prepareLanguageAndCountryProperties() {
        if (this.isTtsPrepared) {
            return;
        }
        if (!this.tts.isInitialized()) {
            this.form.dispatchErrorOccurredEvent(this, LOG_TAG, ErrorMessages.ERROR_TTS_NOT_READY, new Object[0]);
            Speak("");
            return;
        }
        getLanguageAndCountryLists();
        this.isTtsPrepared = true;
    }

    private void getLanguageAndCountryLists() {
        if (SdkLevel.getLevel() >= 4) {
            for (Locale locale : Locale.getAvailableLocales()) {
                if (this.tts.isLanguageAvailable(locale) != -2) {
                    String tempLang = locale.getLanguage();
                    String tempCountry = locale.getISO3Country();
                    if (!tempLang.equals("") && !this.languageList.contains(tempLang)) {
                        this.languageList.add(tempLang);
                    }
                    if (!tempCountry.equals("") && !this.countryList.contains(tempCountry)) {
                        this.countryList.add(tempCountry);
                    }
                }
            }
            Collections.sort(this.languageList);
            Collections.sort(this.countryList);
            this.allLanguages = YailList.makeList((List) this.languageList);
            this.allCountries = YailList.makeList((List) this.countryList);
        }
    }

    @SimpleFunction
    public void Speak(String message) {
        BeforeSpeaking();
        this.tts.speak(message, new Locale(this.iso2Language, this.iso2Country));
    }

    @SimpleEvent
    public void BeforeSpeaking() {
        EventDispatcher.dispatchEvent(this, "BeforeSpeaking", new Object[0]);
    }

    @SimpleEvent(description = "Event to raise after the message is spoken. The result will be true if the message is spoken successfully, otherwise it will be false.")
    public void AfterSpeaking(boolean result2) {
        EventDispatcher.dispatchEvent(this, "AfterSpeaking", Boolean.valueOf(result2));
    }

    public void onStop() {
        this.tts.onStop();
    }

    public void onResume() {
        this.tts.onResume();
    }

    public void onDestroy() {
        this.tts.onDestroy();
    }
}
