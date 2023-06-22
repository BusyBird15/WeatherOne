package com.google.appinventor.components.runtime.util;

import android.os.Handler;
import com.google.appinventor.components.runtime.AlarmHandler;

public final class TimerInternal implements Runnable {
    private AlarmHandler component;
    private boolean enabled;
    private Handler handler;
    private int interval;

    public TimerInternal(AlarmHandler component2, boolean enabled2, int interval2) {
        this(component2, enabled2, interval2, new Handler());
    }

    public TimerInternal(AlarmHandler component2, boolean enabled2, int interval2, Handler handler2) {
        this.handler = handler2;
        this.component = component2;
        this.enabled = enabled2;
        this.interval = interval2;
        if (enabled2) {
            handler2.postDelayed(this, (long) interval2);
        }
    }

    public int Interval() {
        return this.interval;
    }

    public void Interval(int interval2) {
        this.interval = interval2;
        if (this.enabled) {
            this.handler.removeCallbacks(this);
            this.handler.postDelayed(this, (long) interval2);
        }
    }

    public boolean Enabled() {
        return this.enabled;
    }

    public void Enabled(boolean enabled2) {
        if (this.enabled) {
            this.handler.removeCallbacks(this);
        }
        this.enabled = enabled2;
        if (enabled2) {
            this.handler.postDelayed(this, (long) this.interval);
        }
    }

    public void run() {
        if (this.enabled) {
            this.component.alarm();
            if (this.enabled) {
                this.handler.postDelayed(this, (long) this.interval);
            }
        }
    }
}
