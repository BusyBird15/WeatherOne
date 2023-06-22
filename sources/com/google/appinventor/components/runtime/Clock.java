package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.TimerInternal;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SimpleObject
@DesignerComponent(category = ComponentCategory.SENSORS, description = "<p>Non-visible component that provides the instant in time using the internal clock on the phone. It can fire a timer at regularly set intervals and perform time calculations, manipulations, and conversions.</p> <p>Methods to convert an instant to text are also available. Acceptable patterns are empty string, MM/DD/YYYY HH:mm:ss a, or MMM d, yyyyHH:mm. The empty string will provide the default format, which is \"MMM d, yyyy HH:mm:ss a\" for FormatDateTime \"MMM d, yyyy\" for FormatDate. To see all possible format, please see <a href=\"https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html\" _target=\"_blank\">here</a>. </p> ", iconName = "images/clock.png", nonVisible = true, version = 4)
public class Clock extends AndroidNonvisibleComponent implements Component, AlarmHandler, OnStopListener, OnResumeListener, OnDestroyListener, Deleteable {
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_INTERVAL = 1000;
    private boolean onScreen;
    private boolean timerAlwaysFires;
    private TimerInternal timerInternal;

    public Clock(ComponentContainer container) {
        super(container.$form());
        this.timerAlwaysFires = true;
        this.onScreen = false;
        this.timerInternal = new TimerInternal(this, true, 1000);
        this.form.registerForOnResume(this);
        this.form.registerForOnStop(this);
        this.form.registerForOnDestroy(this);
        if (this.form instanceof ReplForm) {
            this.onScreen = true;
        }
    }

    public Clock() {
        super((Form) null);
        this.timerAlwaysFires = true;
        this.onScreen = false;
    }

    @SimpleEvent(description = "The Timer event runs when the timer has gone off.")
    public void Timer() {
        if (this.timerAlwaysFires || this.onScreen) {
            EventDispatcher.dispatchEvent(this, "Timer", new Object[0]);
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Interval between timer events in ms")
    public int TimerInterval() {
        return this.timerInternal.Interval();
    }

    @DesignerProperty(defaultValue = "1000", editorType = "non_negative_integer")
    @SimpleProperty
    public void TimerInterval(int interval) {
        this.timerInternal.Interval(interval);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Fires timer if true")
    public boolean TimerEnabled() {
        return this.timerInternal.Enabled();
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void TimerEnabled(boolean enabled) {
        this.timerInternal.Enabled(enabled);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Will fire even when application is not showing on the screen if true")
    public boolean TimerAlwaysFires() {
        return this.timerAlwaysFires;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void TimerAlwaysFires(boolean always) {
        this.timerAlwaysFires = always;
    }

    public void alarm() {
        Timer();
    }

    @SimpleFunction(description = "Returns the phone's internal time.")
    public static long SystemTime() {
        return Dates.Timer();
    }

    @SimpleFunction(description = "Returns the current instant in time read from phone's clock.")
    public static Calendar Now() {
        return Dates.Now();
    }

    @SimpleFunction(description = "Returns an instant in time specified by MM/dd/YYYY hh:mm:ss or MM/dd/YYYY or hh:mm.")
    public static Calendar MakeInstant(String from) {
        try {
            return Dates.DateValue(from);
        } catch (IllegalArgumentException e) {
            throw new YailRuntimeError("Argument to MakeInstant should have form MM/dd/YYYY hh:mm:ss, or MM/dd/YYYY or hh:mm", "Sorry to be so picky.");
        }
    }

    @SimpleFunction(description = "Returns an instant in time specified by year, month, date in UTC.\nValid values for the month field are 1-12 and 1-31 for the day field.")
    public Calendar MakeDate(int year, int month, int day) {
        try {
            GregorianCalendar cal = new GregorianCalendar(year, month - 1, day);
            cal.setLenient(false);
            cal.getTime();
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "MakeDate", ErrorMessages.ERROR_ILLEGAL_DATE, new Object[0]);
        }
        return Dates.DateInstant(year, month, day);
    }

    @SimpleFunction(description = "Returns an instant in time specified by hour, minute, second in UTC.")
    public Calendar MakeTime(int hour, int minute, int second) {
        Calendar instant = new GregorianCalendar();
        try {
            instant.set(11, hour);
            instant.set(12, minute);
            instant.set(13, second);
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "MakeTime", ErrorMessages.ERROR_ILLEGAL_DATE, new Object[0]);
        }
        return instant;
    }

    @SimpleFunction(description = "Returns an instant in time specified by year, month, date, hour, minute, second in UTC.")
    public Calendar MakeInstantFromParts(int year, int month, int day, int hour, int minute, int second) {
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, day);
            try {
                gregorianCalendar.setLenient(false);
                gregorianCalendar.getTime();
                GregorianCalendar gregorianCalendar2 = gregorianCalendar;
            } catch (IllegalArgumentException e) {
                GregorianCalendar gregorianCalendar3 = gregorianCalendar;
                this.form.dispatchErrorOccurredEvent(this, "MakeInstantFromParts", ErrorMessages.ERROR_ILLEGAL_DATE, new Object[0]);
                Calendar instant = Dates.DateInstant(year, month, day);
                instant.set(11, hour);
                instant.set(12, minute);
                instant.set(13, second);
                return instant;
            }
        } catch (IllegalArgumentException e2) {
            this.form.dispatchErrorOccurredEvent(this, "MakeInstantFromParts", ErrorMessages.ERROR_ILLEGAL_DATE, new Object[0]);
            Calendar instant2 = Dates.DateInstant(year, month, day);
            instant2.set(11, hour);
            instant2.set(12, minute);
            instant2.set(13, second);
            return instant2;
        }
        Calendar instant22 = Dates.DateInstant(year, month, day);
        try {
            instant22.set(11, hour);
            instant22.set(12, minute);
            instant22.set(13, second);
        } catch (IllegalArgumentException e3) {
            this.form.dispatchErrorOccurredEvent(this, "MakeInstantFromParts", ErrorMessages.ERROR_ILLEGAL_DATE, new Object[0]);
        }
        return instant22;
    }

    @SimpleFunction(description = "Returns an instant in time specified by the milliseconds since 1970 in UTC.")
    public static Calendar MakeInstantFromMillis(long millis) {
        Calendar instant = Dates.Now();
        instant.setTimeInMillis(millis);
        return instant;
    }

    @SimpleFunction(description = "Returns the instant in time measured as milliseconds since 1970.")
    public static long GetMillis(Calendar instant) {
        return instant.getTimeInMillis();
    }

    @SimpleFunction(description = "Returns an instant in time some duration after the argument")
    public static Calendar AddDuration(Calendar instant, long quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAddInMillis(newInstant, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some seconds after the given instant.")
    public static Calendar AddSeconds(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 13, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some minutes after the given instant.")
    public static Calendar AddMinutes(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 12, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some hours after the given instant.")
    public static Calendar AddHours(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 11, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some days after the given instant.")
    public static Calendar AddDays(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 5, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns An instant in time some weeks after the given instant.")
    public static Calendar AddWeeks(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 3, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some months after the given instant.")
    public static Calendar AddMonths(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 2, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns an instant in time some years after the given instant.")
    public static Calendar AddYears(Calendar instant, int quantity) {
        Calendar newInstant = (Calendar) instant.clone();
        Dates.DateAdd(newInstant, 1, quantity);
        return newInstant;
    }

    @SimpleFunction(description = "Returns duration, which is milliseconds elapsed between instants.")
    public static long Duration(Calendar start, Calendar end) {
        return end.getTimeInMillis() - start.getTimeInMillis();
    }

    @SimpleFunction(description = "Converts the duration to the number of seconds.")
    public static long DurationToSeconds(long duration) {
        return Dates.ConvertDuration(duration, 13);
    }

    @SimpleFunction(description = "Converts the duration to the number of minutes.")
    public static long DurationToMinutes(long duration) {
        return Dates.ConvertDuration(duration, 12);
    }

    @SimpleFunction(description = "Converts the duration to the number of hours.")
    public static long DurationToHours(long duration) {
        return Dates.ConvertDuration(duration, 11);
    }

    @SimpleFunction(description = "Converts the duration to the number of days.")
    public static long DurationToDays(long duration) {
        return Dates.ConvertDuration(duration, 5);
    }

    @SimpleFunction(description = "Converts the duration to the number of weeks.")
    public static long DurationToWeeks(long duration) {
        return Dates.ConvertDuration(duration, 3);
    }

    @SimpleFunction(description = "Returns the second of the minute (0-59) from the instant.")
    public static int Second(Calendar instant) {
        return Dates.Second(instant);
    }

    @SimpleFunction(description = "Returns the minute of the hour (0-59) from the instant.")
    public static int Minute(Calendar instant) {
        return Dates.Minute(instant);
    }

    @SimpleFunction(description = "Returns the hour of the day (0-23) from the instant.")
    public static int Hour(Calendar instant) {
        return Dates.Hour(instant);
    }

    @SimpleFunction(description = "Returns the day of the month (1-31) from the instant.")
    public static int DayOfMonth(Calendar instant) {
        return Dates.Day(instant);
    }

    @SimpleFunction(description = "Returns the day of the week represented as a number from 1 (Sunday) to 7 (Saturday).")
    public static int Weekday(Calendar instant) {
        return Dates.Weekday(instant);
    }

    @SimpleFunction(description = "Returns the name of the day of the week from the instant.")
    public static String WeekdayName(Calendar instant) {
        return Dates.WeekdayName(instant);
    }

    @SimpleFunction(description = "Returns the month of the year represented as a number from 1 to 12).")
    public static int Month(Calendar instant) {
        return Dates.Month(instant) + 1;
    }

    @SimpleFunction(description = "Returns the name of the month from the instant, e.g., January, February, March...")
    public static String MonthName(Calendar instant) {
        return Dates.MonthName(instant);
    }

    @SimpleFunction(description = "The year")
    public static int Year(Calendar instant) {
        return Dates.Year(instant);
    }

    @SimpleFunction(description = "Returns text representing the date and time of an instant in the specified pattern")
    public static String FormatDateTime(Calendar instant, String pattern) {
        try {
            return Dates.FormatDateTime(instant, pattern);
        } catch (IllegalArgumentException e) {
            throw new YailRuntimeError("Illegal argument for pattern in Clock.FormatDateTime. Acceptable values are empty string, MM/dd/YYYY hh:mm:ss a, MMM d, yyyy HH:mm For all possible patterns, see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html", "Sorry to be so picky.");
        }
    }

    @SimpleFunction(description = "Text representing the date of an instant in the specified pattern")
    public static String FormatDate(Calendar instant, String pattern) {
        try {
            return Dates.FormatDate(instant, pattern);
        } catch (IllegalArgumentException e) {
            throw new YailRuntimeError("Illegal argument for pattern in Clock.FormatDate. Acceptable values are empty string, MM/dd/YYYY, or MMM d, yyyy. For all possible patterns, see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html", "Sorry to be so picky.");
        }
    }

    @SimpleFunction(description = "Text representing the time of an instant")
    public static String FormatTime(Calendar instant) {
        return Dates.FormatTime(instant);
    }

    public void onStop() {
        this.onScreen = false;
    }

    public void onResume() {
        this.onScreen = true;
    }

    public void onDestroy() {
        this.timerInternal.Enabled(false);
    }

    public void onDelete() {
        this.timerInternal.Enabled(false);
    }
}
