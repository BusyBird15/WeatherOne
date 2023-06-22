package com.google.appinventor.components.runtime;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.appinventor.components.runtime.ChartView;
import java.util.ArrayList;
import java.util.List;

public abstract class ChartView<E extends Entry, T extends IDataSet<E>, D extends ChartData<T>, C extends Chart<D>, V extends ChartView<E, T, D, C, V>> {
    protected C chart;
    protected Chart chartComponent;
    protected D data;
    protected Form form;
    protected Handler uiHandler = new Handler(Looper.myLooper());

    public abstract ChartDataModel<E, T, D, C, V> createChartModel();

    public abstract View getView();

    protected ChartView(Chart chartComponent2) {
        this.chartComponent = chartComponent2;
        this.form = chartComponent2.$form();
    }

    public Form getForm() {
        return this.form;
    }

    public void setBackgroundColor(int argb) {
        this.chart.setBackgroundColor(argb);
    }

    public void setDescription(String text) {
        this.chart.getDescription().setText(text);
    }

    public void setLegendEnabled(boolean enabled) {
        this.chart.getLegend().setEnabled(enabled);
    }

    /* access modifiers changed from: protected */
    public void initializeDefaultSettings() {
        this.chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        this.chart.getLegend().setWordWrapEnabled(true);
    }

    public void refresh() {
        this.chart.invalidate();
    }

    public void refresh(ChartDataModel<E, T, D, C, V> model) {
        new RefreshTask(model.getEntries()).execute(new ChartDataModel[]{model});
    }

    @SuppressLint({"StaticFieldLeak"})
    private class RefreshTask extends AsyncTask<ChartDataModel<E, T, D, C, V>, Void, ChartDataModel<E, T, D, C, V>> {
        private final List<E> entries;

        public RefreshTask(List<E> entries2) {
            this.entries = new ArrayList(entries2);
        }

        /* access modifiers changed from: protected */
        @SafeVarargs
        public final ChartDataModel<E, T, D, C, V> doInBackground(ChartDataModel<E, T, D, C, V>... chartDataModels) {
            return chartDataModels[0];
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ChartDataModel<E, T, D, C, V> result) {
            ChartView.this.refresh(result, this.entries);
        }
    }

    /* access modifiers changed from: protected */
    public void refresh(ChartDataModel<E, T, D, C, V> model, List<E> entries) {
        T dataset = model.getDataset();
        if (dataset instanceof DataSet) {
            ((DataSet) dataset).setValues(entries);
        }
        this.chart.getData().notifyDataChanged();
        this.chart.notifyDataSetChanged();
        this.chart.invalidate();
    }
}
