package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.YailList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PieChartDataModel extends Chart2DDataModel<PieEntry, IPieDataSet, PieData, PieChart, PieChartView> {
    private final List<LegendEntry> legendEntries = new ArrayList();

    public PieChartDataModel(PieData data, PieChartView view, PieChart chart) {
        super(data, view);
        this.dataset = new PieDataSet(new ArrayList(), "");
        this.data.addDataSet(this.dataset);
        chart.setData(data);
        setDefaultStylingProperties();
        this.view = view;
    }

    public void addEntryFromTuple(YailList tuple) {
        PieEntry entry = getEntryFromTuple(tuple);
        if (entry != null) {
            this.entries.add(entry);
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.label = tuple.getString(0);
            int entriesCount = this.entries.size();
            List<Integer> colors = getDataset().getColors();
            legendEntry.formColor = colors.get((entriesCount - 1) % colors.size()).intValue();
            this.legendEntries.add(legendEntry);
            ((PieChartView) this.view).addLegendEntry(legendEntry);
        }
    }

    public void removeEntry(int index) {
        if (index >= 0) {
            this.entries.remove(index);
            ((PieChartView) this.view).removeLegendEntry(this.legendEntries.remove(index));
            updateLegendColors();
        }
    }

    public void clearEntries() {
        super.clearEntries();
        ((PieChartView) this.view).removeLegendEntries(this.legendEntries);
        this.legendEntries.clear();
    }

    public Entry getEntryFromTuple(YailList tuple) {
        try {
            String xValue = tuple.getString(0);
            String yValue = tuple.getString(1);
            try {
                return new PieEntry(Float.parseFloat(yValue), xValue);
            } catch (NumberFormatException e) {
                ((PieChartView) this.view).getForm().dispatchErrorOccurredEvent(((PieChartView) this.view).chartComponent, "GetEntryFromTuple", ErrorMessages.ERROR_INVALID_CHART_ENTRY_VALUES, xValue, yValue);
                return null;
            }
        } catch (NullPointerException e2) {
            ((PieChartView) this.view).getForm().dispatchErrorOccurredEvent(((PieChartView) this.view).chartComponent, "GetEntryFromTuple", ErrorMessages.ERROR_NULL_CHART_ENTRY_VALUES, new Object[0]);
        } catch (IndexOutOfBoundsException e3) {
            ((PieChartView) this.view).getForm().dispatchErrorOccurredEvent(((PieChartView) this.view).chartComponent, "GetEntryFromTuple", ErrorMessages.ERROR_INSUFFICIENT_CHART_ENTRY_VALUES, Integer.valueOf(getTupleSize()), Integer.valueOf(tuple.size()));
        }
    }

    public YailList getTupleFromEntry(Entry entry) {
        PieEntry pieEntry = (PieEntry) entry;
        return YailList.makeList((List) Arrays.asList(new Serializable[]{pieEntry.getLabel(), Float.valueOf(pieEntry.getY())}));
    }

    /* access modifiers changed from: protected */
    public void setDefaultStylingProperties() {
        if (this.dataset instanceof PieDataSet) {
            this.dataset.setSliceSpace(3.0f);
        }
    }

    public void setColors(List<Integer> colors) {
        super.setColors(colors);
        updateLegendColors();
    }

    public void setColor(int argb) {
        setColors(Collections.singletonList(Integer.valueOf(argb)));
    }

    /* access modifiers changed from: protected */
    public boolean areEntriesEqual(Entry e1, Entry e2) {
        if (!(e1 instanceof PieEntry) || !(e2 instanceof PieEntry)) {
            return false;
        }
        PieEntry p1 = (PieEntry) e1;
        PieEntry p2 = (PieEntry) e2;
        if (!p1.getLabel().equals(p2.getLabel()) || p1.getY() != p2.getY()) {
            return false;
        }
        return true;
    }

    private void updateLegendColors() {
        for (int i = 0; i < this.legendEntries.size(); i++) {
            this.legendEntries.get(i).formColor = ((Integer) getDataset().getColors().get(i % getDataset().getColors().size())).intValue();
        }
    }
}
