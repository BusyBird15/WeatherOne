package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.appinventor.components.runtime.ChartView;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.Arrays;
import java.util.List;

public abstract class Chart2DDataModel<E extends Entry, T extends IDataSet<E>, D extends ChartData<T>, C extends Chart<D>, V extends ChartView<E, T, D, C, V>> extends ChartDataModel<E, T, D, C, V> {
    protected Chart2DDataModel(D data, V view) {
        super(data, view);
    }

    /* access modifiers changed from: protected */
    public int getTupleSize() {
        return 2;
    }

    public YailList getTupleFromEntry(Entry entry) {
        return YailList.makeList((List) Arrays.asList(new Float[]{Float.valueOf(entry.getX()), Float.valueOf(entry.getY())}));
    }
}
