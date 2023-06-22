package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterChartView extends PointChartView<Entry, IScatterDataSet, ScatterData, ScatterChart, ScatterChartView> {
    public ScatterChartView(Chart chartComponent) {
        super(chartComponent);
        this.chart = new ScatterChart(this.form);
        this.data = new ScatterData();
        this.chart.setData(this.data);
        initializeDefaultSettings();
    }

    public ChartDataModel<Entry, IScatterDataSet, ScatterData, ScatterChart, ScatterChartView> createChartModel() {
        return new ScatterChartDataModel(this.data, this);
    }
}
