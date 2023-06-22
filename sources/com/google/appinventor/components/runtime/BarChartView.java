package com.google.appinventor.components.runtime;

import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.List;

public class BarChartView extends AxisChartView<BarEntry, IBarDataSet, BarData, BarChart, BarChartView> {
    private static final float GROUP_SPACE = 0.08f;
    private static final float START_X_VALUE = 0.0f;
    private float barSpace = 0.0f;
    private float barWidth = 0.3f;

    public BarChartView(Chart chartComponent) {
        super(chartComponent);
        this.chart = new BarChart(this.form);
        this.data = new BarData();
        this.chart.setData(this.data);
        initializeDefaultSettings();
    }

    public View getView() {
        return this.chart;
    }

    public ChartDataModel<BarEntry, IBarDataSet, BarData, BarChart, BarChartView> createChartModel() {
        BarChartDataModel model = new BarChartDataModel(this.data, this);
        recalculateBarSpaceAndWidth();
        return model;
    }

    private void recalculateBarSpaceAndWidth() {
        int dataSetCount = this.chart.getData().getDataSetCount();
        if (dataSetCount > 1) {
            float x = 0.92f / ((float) dataSetCount);
            this.barSpace = 0.1f * x;
            this.barWidth = 0.9f * x;
            this.chart.getData().setBarWidth(this.barWidth);
        }
        if (dataSetCount == 2) {
            this.chart.getXAxis().setCenterAxisLabels(true);
        }
    }

    /* access modifiers changed from: protected */
    public void initializeDefaultSettings() {
        super.initializeDefaultSettings();
        this.chart.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.chart.getXAxis().setGranularity(1.0f);
    }

    /* access modifiers changed from: protected */
    public void refresh(ChartDataModel<BarEntry, IBarDataSet, BarData, BarChart, BarChartView> model, List<BarEntry> entries) {
        IBarDataSet dataset = model.getDataset();
        if (dataset instanceof BarDataSet) {
            ((BarDataSet) dataset).setValues(entries);
        }
        regroupBars();
        this.chart.getData().notifyDataChanged();
        this.chart.notifyDataSetChanged();
        this.chart.invalidate();
    }

    private void regroupBars() {
        if (this.chart.getData().getDataSetCount() > 1) {
            this.chart.groupBars(0.0f, GROUP_SPACE, this.barSpace);
            int maxEntries = 0;
            for (IBarDataSet dataSet : this.chart.getData().getDataSets()) {
                maxEntries = Math.max(maxEntries, dataSet.getEntryCount());
            }
            this.chart.getXAxis().setAxisMinimum(0.0f);
            this.chart.getXAxis().setAxisMaximum((this.chart.getData().getGroupWidth(GROUP_SPACE, this.barSpace) * ((float) maxEntries)) + 0.0f);
        }
    }
}
