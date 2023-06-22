package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.appinventor.components.runtime.ChartView;
import com.google.appinventor.components.runtime.util.ChartDataSourceUtil;
import com.google.appinventor.components.runtime.util.YailList;
import gnu.mapping.Symbol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ChartDataModel<E extends Entry, T extends IDataSet<E>, D extends ChartData<T>, C extends Chart<D>, V extends ChartView<E, T, D, C, V>> {
    protected D data;
    protected T dataset;
    protected List<E> entries;
    protected int maximumTimeEntries = 200;
    protected V view;

    public enum EntryCriterion {
        All,
        XValue,
        YValue
    }

    public abstract void addEntryFromTuple(YailList yailList);

    public abstract Entry getEntryFromTuple(YailList yailList);

    public abstract YailList getTupleFromEntry(Entry entry);

    /* access modifiers changed from: protected */
    public abstract int getTupleSize();

    protected ChartDataModel(D data2, V view2) {
        this.data = data2;
        this.view = view2;
        this.entries = new ArrayList();
    }

    public T getDataset() {
        return this.dataset;
    }

    public D getData() {
        return this.data;
    }

    public void setColor(int argb) {
        if (this.dataset instanceof DataSet) {
            ((DataSet) this.dataset).setColor(argb);
        }
    }

    public void setColors(List<Integer> colors) {
        if (this.dataset instanceof DataSet) {
            ((DataSet) this.dataset).setColors(colors);
        }
    }

    public void setLabel(String text) {
        getDataset().setLabel(text);
    }

    public void setElements(String elements) {
        int tupleSize = getTupleSize();
        String[] entries2 = elements.split(",");
        for (int i = tupleSize - 1; i < entries2.length; i += tupleSize) {
            List<String> tupleEntries = new ArrayList<>();
            for (int j = tupleSize - 1; j >= 0; j--) {
                tupleEntries.add(entries2[i - j]);
            }
            addEntryFromTuple(YailList.makeList((List) tupleEntries));
        }
    }

    public void importFromList(List<?> list) {
        for (Object next : list) {
            YailList tuple = null;
            if (next instanceof YailList) {
                tuple = (YailList) next;
            } else if (next instanceof List) {
                tuple = YailList.makeList((List) next);
            }
            if (tuple != null) {
                addEntryFromTuple(tuple);
            }
        }
    }

    public void removeValues(List<?> values) {
        for (Object next : values) {
            YailList tuple = null;
            if (next instanceof YailList) {
                tuple = (YailList) next;
            } else if (next instanceof List) {
                tuple = YailList.makeList((List) next);
            } else if (next instanceof Symbol) {
            }
            if (tuple != null) {
                removeEntryFromTuple(tuple);
            }
        }
    }

    public void importFromColumns(YailList columns, boolean hasHeaders) {
        importFromList(getTuplesFromColumns(columns, hasHeaders));
    }

    public YailList getTuplesFromColumns(YailList columns, boolean hasHeaders) {
        int rows = ChartDataSourceUtil.determineMaximumListSize(columns);
        List<YailList> tuples = new ArrayList<>();
        for (int i = hasHeaders ? 1 : 0; i < rows; i++) {
            ArrayList<String> tupleElements = new ArrayList<>();
            for (int j = 0; j < columns.size(); j++) {
                Object value = columns.getObject(j);
                if (!(value instanceof YailList)) {
                    tupleElements.add(getDefaultValue(i - 1));
                } else {
                    YailList column = (YailList) value;
                    if (column.size() > i) {
                        tupleElements.add(column.getString(i));
                    } else if (column.size() == 0) {
                        tupleElements.add(getDefaultValue(i - 1));
                    } else {
                        tupleElements.add("");
                    }
                }
            }
            tuples.add(YailList.makeList((List) tupleElements));
        }
        return YailList.makeList((List) tuples);
    }

    public void removeEntryFromTuple(YailList tuple) {
        Entry entry = getEntryFromTuple(tuple);
        if (entry != null) {
            removeEntry(findEntryIndex(entry));
        }
    }

    public void removeEntry(int index) {
        if (index >= 0) {
            this.entries.remove(index);
        }
    }

    public boolean doesEntryExist(YailList tuple) {
        return findEntryIndex(getEntryFromTuple(tuple)) >= 0;
    }

    public YailList findEntriesByCriterion(String value, EntryCriterion criterion) {
        List<YailList> entries2 = new ArrayList<>();
        for (E entry : this.entries) {
            if (isEntryCriterionSatisfied(entry, criterion, value)) {
                entries2.add(getTupleFromEntry(entry));
            }
        }
        return YailList.makeList((List) entries2);
    }

    public YailList getEntriesAsTuples() {
        return findEntriesByCriterion(Component.TYPEFACE_DEFAULT, EntryCriterion.All);
    }

    /* access modifiers changed from: protected */
    public boolean isEntryCriterionSatisfied(Entry entry, EntryCriterion criterion, String value) {
        switch (criterion) {
            case All:
                return true;
            case XValue:
                if (entry instanceof PieEntry) {
                    return ((PieEntry) entry).getLabel().equals(value);
                }
                try {
                    float xValue = Float.parseFloat(value);
                    float compareValue = entry.getX();
                    if (entry instanceof BarEntry) {
                        compareValue = (float) Math.floor((double) compareValue);
                    }
                    return compareValue == xValue;
                } catch (NumberFormatException e) {
                    return false;
                }
            case YValue:
                try {
                    return entry.getY() == Float.parseFloat(value);
                } catch (NumberFormatException e2) {
                    return false;
                }
            default:
                throw new IllegalArgumentException("Unknown criterion: " + criterion);
        }
    }

    /* access modifiers changed from: protected */
    public int findEntryIndex(Entry entry) {
        for (int i = 0; i < this.entries.size(); i++) {
            if (areEntriesEqual((Entry) this.entries.get(i), entry)) {
                return i;
            }
        }
        return -1;
    }

    public void clearEntries() {
        this.entries.clear();
    }

    public void addTimeEntry(YailList tuple) {
        if (this.entries.size() >= this.maximumTimeEntries) {
            this.entries.remove(0);
        }
        addEntryFromTuple(tuple);
    }

    public void setMaximumTimeEntries(int entries2) {
        this.maximumTimeEntries = entries2;
    }

    /* access modifiers changed from: protected */
    public void setDefaultStylingProperties() {
    }

    /* access modifiers changed from: protected */
    public String getDefaultValue(int index) {
        return index + "";
    }

    /* access modifiers changed from: protected */
    public boolean areEntriesEqual(Entry e1, Entry e2) {
        return e1.equalTo(e2);
    }

    public List<E> getEntries() {
        return Collections.unmodifiableList(this.entries);
    }
}
