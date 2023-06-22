package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class AreaChartView extends LineChartViewBase<AreaChartView> {
    public AreaChartView(Chart chartComponent) {
        super(chartComponent);
        this.chart.setHardwareAccelerationEnabled(false);
    }

    public ChartDataModel<Entry, ILineDataSet, LineData, LineChart, AreaChartView> createChartModel() {
        return new AreaChartDataModel(this.data, this);
    }
}
