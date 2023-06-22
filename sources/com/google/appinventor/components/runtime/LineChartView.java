package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class LineChartView extends LineChartViewBase<LineChartView> {
    public LineChartView(Chart chartComponent) {
        super(chartComponent);
    }

    public ChartDataModel<Entry, ILineDataSet, LineData, LineChart, LineChartView> createChartModel() {
        return new LineChartDataModel(this.data, this);
    }
}
