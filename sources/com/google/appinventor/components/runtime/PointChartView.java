package com.google.appinventor.components.runtime;

import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.google.appinventor.components.runtime.PointChartView;

public abstract class PointChartView<E extends Entry, T extends IBarLineScatterCandleBubbleDataSet<E>, D extends BarLineScatterCandleBubbleData<T>, C extends BarLineChartBase<D>, V extends PointChartView<E, T, D, C, V>> extends AxisChartView<E, T, D, C, V> {
    protected PointChartView(Chart chartComponent) {
        super(chartComponent);
    }

    /* access modifiers changed from: protected */
    public void initializeDefaultSettings() {
        super.initializeDefaultSettings();
        this.chart.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    }

    public View getView() {
        return this.chart;
    }
}
