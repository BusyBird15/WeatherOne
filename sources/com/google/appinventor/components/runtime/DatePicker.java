package com.google.appinventor.components.runtime;

import android.app.DatePickerDialog;
import android.os.Handler;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "<p>A button that, when clicked on, launches a popup dialog to allow the user to select a date.</p>", version = 4)
public class DatePicker extends ButtonBase {
    /* access modifiers changed from: private */
    public Handler androidUIHandler;
    private boolean customDate = false;
    /* access modifiers changed from: private */
    public DatePickerDialog date;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(android.widget.DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
            if (datePicker.isShown()) {
                int unused = DatePicker.this.year = selectedYear;
                int unused2 = DatePicker.this.javaMonth = selectedMonth;
                int unused3 = DatePicker.this.month = DatePicker.this.javaMonth + 1;
                int unused4 = DatePicker.this.day = selectedDay;
                DatePicker.this.date.updateDate(DatePicker.this.year, DatePicker.this.javaMonth, DatePicker.this.day);
                Calendar unused5 = DatePicker.this.instant = Dates.DateInstant(DatePicker.this.year, DatePicker.this.month, DatePicker.this.day);
                DatePicker.this.androidUIHandler.post(new Runnable() {
                    public void run() {
                        DatePicker.this.AfterDateSet();
                    }
                });
            }
        }
    };
    /* access modifiers changed from: private */
    public int day;
    private Form form;
    /* access modifiers changed from: private */
    public Calendar instant;
    /* access modifiers changed from: private */
    public int javaMonth;
    private String[] localizedMonths = new DateFormatSymbols().getMonths();
    /* access modifiers changed from: private */
    public int month;
    /* access modifiers changed from: private */
    public int year;

    public DatePicker(ComponentContainer container) {
        super(container);
        this.form = container.$form();
        Calendar c = Calendar.getInstance();
        this.year = c.get(1);
        this.javaMonth = c.get(2);
        this.month = this.javaMonth + 1;
        this.day = c.get(5);
        this.instant = Dates.DateInstant(this.year, this.month, this.day);
        this.date = new DatePickerDialog(this.container.$context(), this.datePickerListener, this.year, this.javaMonth, this.day);
        this.androidUIHandler = new Handler();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "the Year that was last picked using the DatePicker")
    public int Year() {
        return this.year;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "the number of the Month that was last picked using the DatePicker. Note that months start in 1 = January, 12 = December.")
    public int Month() {
        return this.month;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the name of the Month that was last picked using the DatePicker, in textual format.")
    public String MonthInText() {
        return this.localizedMonths[this.javaMonth];
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "the Day of the month that was last picked using the DatePicker.")
    public int Day() {
        return this.day;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "the instant of the date that was last picked using the DatePicker.")
    public Calendar Instant() {
        return this.instant;
    }

    @SimpleFunction(description = "Allows the user to set the date to be displayed when the date picker opens.\nValid values for the month field are 1-12 and 1-31 for the day field.\n")
    public void SetDateToDisplay(int year2, int month2, int day2) {
        int jMonth = month2 - 1;
        try {
            GregorianCalendar cal = new GregorianCalendar(year2, jMonth, day2);
            cal.setLenient(false);
            cal.getTime();
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "SetDateToDisplay", ErrorMessages.ERROR_ILLEGAL_DATE, new Object[0]);
        }
        this.date.updateDate(year2, jMonth, day2);
        this.instant = Dates.DateInstant(year2, month2, day2);
        this.customDate = true;
    }

    @SimpleFunction(description = "Allows the user to set the date from the instant to be displayed when the date picker opens.")
    public void SetDateToDisplayFromInstant(Calendar instant2) {
        int year2 = Dates.Year(instant2);
        int month2 = Dates.Month(instant2);
        int day2 = Dates.Day(instant2);
        this.date.updateDate(year2, month2, day2);
        Calendar instant3 = Dates.DateInstant(year2, month2, day2);
        this.customDate = true;
    }

    @SimpleFunction(description = "Launches the DatePicker dialog.")
    public void LaunchPicker() {
        click();
    }

    public void click() {
        if (!this.customDate) {
            Calendar c = Calendar.getInstance();
            int year2 = c.get(1);
            int jMonth = c.get(2);
            int day2 = c.get(5);
            this.date.updateDate(year2, jMonth, day2);
            this.instant = Dates.DateInstant(year2, jMonth + 1, day2);
        } else {
            this.customDate = false;
        }
        this.date.show();
    }

    @SimpleEvent(description = "Event that runs after the user chooses a Date in the dialog")
    public void AfterDateSet() {
        EventDispatcher.dispatchEvent(this, "AfterDateSet", new Object[0]);
    }
}
