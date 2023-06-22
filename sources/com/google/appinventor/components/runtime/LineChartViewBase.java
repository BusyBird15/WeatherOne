package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.appinventor.components.runtime.LineChartViewBase;

public abstract class LineChartViewBase<V extends LineChartViewBase<V>> extends PointChartView<Entry, ILineDataSet, LineData, LineChart, V> {
    protected LineChartViewBase(Chart chartComponent) {
        super(chartComponent);
        this.chart = new LineChart(this.form);
        this.data = new LineData();
        this.chart.setData(this.data);
        initializeDefaultSettings();
    }
}
