package com.google.appinventor.components.runtime;

import android.view.View;
import android.widget.RelativeLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class PieChartView extends ChartView<PieEntry, IPieDataSet, PieData, PieChart, PieChartView> {
    private float bottomOffset = 0.0f;
    /* access modifiers changed from: private */
    public final List<LegendEntry> legendEntries = new ArrayList();
    private final List<PieChart> pieCharts = new ArrayList();
    private int pieHoleRadius = 0;
    private final RelativeLayout rootView = new RelativeLayout(this.form);

    public PieChartView(Chart chartComponent) {
        super(chartComponent);
        this.chart = new PieChart(this.form);
        initializeDefaultSettings();
    }

    /* access modifiers changed from: protected */
    public void initializeDefaultSettings() {
        super.initializeDefaultSettings();
        this.chart.getLegend().setDrawInside(true);
        this.chart.getLegend().setCustom(this.legendEntries);
    }

    public View getView() {
        return this.rootView;
    }

    public ChartDataModel<PieEntry, IPieDataSet, PieData, PieChart, PieChartView> createChartModel() {
        return new PieChartDataModel(new PieData(), this, createPieChartRing());
    }

    private PieChart createPieChartRing() {
        PieChart pieChart;
        if (this.pieCharts.isEmpty()) {
            pieChart = (PieChart) this.chart;
        } else {
            pieChart = new PieChart(this.form);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
        }
        setPieChartProperties(pieChart);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        params.addRule(13, -1);
        pieChart.setLayoutParams(params);
        this.pieCharts.add(pieChart);
        this.rootView.addView(pieChart);
        return pieChart;
    }

    private void setPieChartProperties(PieChart chart) {
        chart.setDrawEntryLabels(false);
    }

    /* access modifiers changed from: protected */
    public void refresh(ChartDataModel<PieEntry, IPieDataSet, PieData, PieChart, PieChartView> model, List<PieEntry> entries) {
        IPieDataSet dataset = model.getDataset();
        if (dataset instanceof PieDataSet) {
            ((PieDataSet) dataset).setValues(entries);
        }
        this.chart.getLegend().setCustom(this.legendEntries);
        for (PieChart pieChart : this.pieCharts) {
            if (pieChart == this.chart || pieChart.getData().getDataSet().equals(model.getDataset())) {
                pieChart.getData().notifyDataChanged();
                pieChart.notifyDataSetChanged();
            }
            updatePieChartRingOffset(pieChart);
            pieChart.invalidate();
        }
    }

    public void resizePieRings() {
        int lastWidth = 0;
        int lastHeight = 0;
        float newHoleRadius = 100.0f - ((100.0f - ((float) this.pieHoleRadius)) * ((0.75f + (((float) this.pieHoleRadius) / 100.0f)) / ((float) this.pieCharts.size())));
        int i = 0;
        while (i < this.pieCharts.size()) {
            PieChart pieChart = this.pieCharts.get(i);
            changePieChartRadius(pieChart, newHoleRadius, i == this.pieCharts.size() + -1);
            if (i != 0) {
                float scalingFactor = newHoleRadius / 100.0f;
                lastWidth = (int) (((float) lastWidth) * scalingFactor);
                lastHeight = (int) (((float) lastHeight) * scalingFactor);
                changePieChartSize(pieChart, lastWidth, lastHeight);
            } else {
                lastHeight = pieChart.getHeight();
                lastWidth = pieChart.getWidth();
            }
            pieChart.invalidate();
            i++;
        }
    }

    private void changePieChartRadius(PieChart pieChart, float newHoleRadius, boolean lastChart) {
        if (!lastChart) {
            pieChart.setTransparentCircleRadius(newHoleRadius);
            pieChart.setHoleRadius(newHoleRadius);
            pieChart.setDrawHoleEnabled(true);
        } else if (this.pieHoleRadius == 0) {
            pieChart.setDrawHoleEnabled(false);
        } else {
            float setRadius = ((float) this.pieHoleRadius) * (1.0f + ((newHoleRadius - ((float) this.pieHoleRadius)) / 100.0f));
            pieChart.setTransparentCircleRadius(setRadius);
            pieChart.setHoleRadius(setRadius);
        }
    }

    private void changePieChartSize(PieChart pieChart, int width, int height) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieChart.getLayoutParams();
        params.width = width;
        params.height = height;
        pieChart.setLayoutParams(params);
    }

    private void updatePieChartRingOffset(PieChart pieChart) {
        if (this.chart == pieChart) {
            this.bottomOffset = Utils.convertPixelsToDp(this.chart.getLegend().mNeededHeight) / 2.5f;
            this.bottomOffset = Math.min(25.0f, this.bottomOffset);
        }
        pieChart.setExtraBottomOffset(this.bottomOffset);
        pieChart.calculateOffsets();
    }

    public void addLegendEntry(final LegendEntry entry) {
        this.uiHandler.post(new Runnable() {
            public void run() {
                PieChartView.this.legendEntries.add(entry);
            }
        });
    }

    public void removeLegendEntry(final LegendEntry entry) {
        this.uiHandler.post(new Runnable() {
            public void run() {
                PieChartView.this.legendEntries.remove(entry);
            }
        });
    }

    public void removeLegendEntries(List<LegendEntry> entries) {
        final List<LegendEntry> entriesCopy = new ArrayList<>(entries);
        this.uiHandler.post(new Runnable() {
            public void run() {
                PieChartView.this.legendEntries.removeAll(entriesCopy);
            }
        });
    }

    public void setPieRadius(int percent) {
        if (percent > 100) {
            percent = 100;
        } else if (percent < 0) {
            percent = 0;
        }
        this.pieHoleRadius = 100 - percent;
        resizePieRings();
    }

    public List<LegendEntry> getLegendEntries() {
        return this.legendEntries;
    }
}
