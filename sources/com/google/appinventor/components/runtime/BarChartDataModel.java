package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarChartDataModel extends Chart2DDataModel<BarEntry, IBarDataSet, BarData, BarChart, BarChartView> {
    protected BarChartDataModel(BarData data, BarChartView view) {
        super(data, view);
        this.dataset = new BarDataSet(new ArrayList(), "");
        this.data.addDataSet(this.dataset);
        setDefaultStylingProperties();
    }

    public void addEntryFromTuple(YailList tuple) {
        int x;
        BarEntry entry = getEntryFromTuple(tuple);
        if (entry != null && (x = (int) entry.getX()) >= 0) {
            if (x < this.entries.size()) {
                this.entries.set(x, entry);
                return;
            }
            while (this.entries.size() < x) {
                this.entries.add(new BarEntry((float) this.entries.size(), 0.0f));
            }
            this.entries.add(entry);
        }
    }

    public Entry getEntryFromTuple(YailList tuple) {
        try {
            String rawX = tuple.getString(0);
            String rawY = tuple.getString(1);
            try {
                return new BarEntry((float) ((int) Math.floor((double) Float.parseFloat(rawX))), Float.parseFloat(rawY));
            } catch (NumberFormatException e) {
                ((BarChartView) this.view).getForm().dispatchErrorOccurredEvent(((BarChartView) this.view).chartComponent, "GetEntryFromTuple", ErrorMessages.ERROR_INVALID_CHART_ENTRY_VALUES, rawX, rawY);
                return null;
            }
        } catch (NullPointerException e2) {
            ((BarChartView) this.view).getForm().dispatchErrorOccurredEvent(((BarChartView) this.view).chartComponent, "GetEntryFromTuple", ErrorMessages.ERROR_NULL_CHART_ENTRY_VALUES, new Object[0]);
        } catch (IndexOutOfBoundsException e3) {
            ((BarChartView) this.view).getForm().dispatchErrorOccurredEvent(((BarChartView) this.view).chartComponent, "GetEntryFromTuple", ErrorMessages.ERROR_INSUFFICIENT_CHART_ENTRY_VALUES, Integer.valueOf(getTupleSize()), Integer.valueOf(tuple.size()));
        }
    }

    public void removeEntry(int index) {
        if (index < 0) {
            return;
        }
        if (index == this.entries.size() - 1) {
            this.entries.remove(index);
        } else {
            ((BarEntry) this.entries.get(index)).setY(0.0f);
        }
    }

    public void addTimeEntry(YailList tuple) {
        if (this.entries.size() >= this.maximumTimeEntries) {
            this.entries.remove(0);
        }
        this.entries.add(getEntryFromTuple(tuple));
    }

    /* access modifiers changed from: protected */
    public boolean areEntriesEqual(Entry e1, Entry e2) {
        if (!(e1 instanceof BarEntry) || !(e2 instanceof BarEntry) || Math.floor((double) e1.getX()) != Math.floor((double) e2.getX()) || e1.getY() != e2.getY()) {
            return false;
        }
        return true;
    }

    public YailList getTupleFromEntry(Entry entry) {
        return YailList.makeList((List) Arrays.asList(new Float[]{Float.valueOf((float) Math.floor((double) entry.getX())), Float.valueOf(entry.getY())}));
    }
}
