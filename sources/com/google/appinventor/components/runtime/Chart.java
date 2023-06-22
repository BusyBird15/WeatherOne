package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ChartType;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ElementsUtil;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SimpleObject
@DesignerComponent(category = ComponentCategory.CHARTS, description = "A component that allows visualizing data", version = 2)
@UsesLibraries(libraries = "mpandroidchart.jar")
public class Chart extends AndroidViewComponent implements ComponentContainer, OnInitializeListener {
    private int backgroundColor;
    private ChartView<?, ?, ?, ?, ?> chartView;
    private final ArrayList<ChartDataBase> dataComponents;
    private String description;
    private boolean gridEnabled;
    private YailList labels;
    private boolean legendEnabled;
    private int pieRadius;
    private int tick = 1;
    private ChartType type;
    private final RelativeLayout view;
    private boolean zeroX;
    private boolean zeroY;

    public Chart(ComponentContainer container) {
        super(container);
        this.view = new RelativeLayout(container.$context());
        container.$add(this);
        this.dataComponents = new ArrayList<>();
        Type(ChartType.Line);
        Width(176);
        Height(144);
        BackgroundColor(0);
        Description("");
        PieRadius(100);
        LegendEnabled(true);
        GridEnabled(true);
        Labels(new YailList());
        XFromZero(false);
        YFromZero(false);
        $form().registerForOnInitialize(this);
    }

    public View getView() {
        return this.view;
    }

    public Activity $context() {
        return this.container.$context();
    }

    public Form $form() {
        return this.container.$form();
    }

    public void $add(AndroidViewComponent component) {
        throw new UnsupportedOperationException("ChartBase.$add() called");
    }

    public void setChildWidth(AndroidViewComponent component, int width) {
        throw new UnsupportedOperationException("ChartBase.setChildWidth called");
    }

    public void setChildHeight(AndroidViewComponent component, int height) {
        throw new UnsupportedOperationException("ChartBase.setChildHeight called");
    }

    public List<Component> getChildren() {
        return new ArrayList(this.dataComponents);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public ChartType Type() {
        return this.type;
    }

    @DesignerProperty(defaultValue = "0", editorType = "chart_type")
    @SimpleProperty(description = "Specifies the chart's type (area, bar, pie, scatter), which determines how to visualize the data.", userVisible = false)
    public void Type(ChartType type2) {
        boolean chartViewExists;
        if (this.chartView != null) {
            chartViewExists = true;
        } else {
            chartViewExists = false;
        }
        ChartView<?, ?, ?, ?, ?> newChartView = createChartViewFromType(type2);
        if (chartViewExists) {
            this.view.removeView(this.chartView.getView());
        }
        this.type = type2;
        this.chartView = newChartView;
        this.view.addView(this.chartView.getView(), 0);
        if (chartViewExists) {
            reinitializeChart();
        }
    }

    private ChartView<?, ?, ?, ?, ?> createChartViewFromType(ChartType type2) {
        switch (type2) {
            case Line:
                return new LineChartView(this);
            case Scatter:
                return new ScatterChartView(this);
            case Area:
                return new AreaChartView(this);
            case Bar:
                return new BarChartView(this);
            case Pie:
                return new PieChartView(this);
            default:
                throw new IllegalArgumentException("Invalid Chart type specified: " + type2);
        }
    }

    private void reinitializeChart() {
        Iterator<ChartDataBase> it = this.dataComponents.iterator();
        while (it.hasNext()) {
            it.next().initChartData();
        }
        Description(this.description);
        BackgroundColor(this.backgroundColor);
        LegendEnabled(this.legendEnabled);
        GridEnabled(this.gridEnabled);
        Labels(this.labels);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public String Description() {
        return this.description;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty
    public void Description(String text) {
        this.description = text;
        this.chartView.setDescription(this.description);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "color")
    @SimpleProperty
    public void BackgroundColor(int argb) {
        this.backgroundColor = argb;
        this.chartView.setBackgroundColor(argb);
    }

    @DesignerProperty(defaultValue = "100", editorType = "chart_pie_radius")
    @SimpleProperty(description = "Sets the Pie Radius of a Pie Chart from 0% to 100%, where the percentage indicates the percentage of the hole fill. 100% means that a full Pie Chart is drawn, while values closer to 0% correspond to hollow Pie Charts.", userVisible = false)
    public void PieRadius(int percent) {
        this.pieRadius = percent;
        if (this.chartView instanceof PieChartView) {
            ((PieChartView) this.chartView).setPieRadius(percent);
        }
    }

    @SimpleProperty
    public boolean LegendEnabled() {
        return this.legendEnabled;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void LegendEnabled(boolean enabled) {
        this.legendEnabled = enabled;
        this.chartView.setLegendEnabled(enabled);
    }

    @SimpleProperty
    public boolean GridEnabled() {
        return this.gridEnabled;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void GridEnabled(boolean enabled) {
        this.gridEnabled = enabled;
        if (this.chartView instanceof AxisChartView) {
            ((AxisChartView) this.chartView).setGridEnabled(enabled);
        }
    }

    @SimpleProperty
    public YailList Labels() {
        return this.labels;
    }

    @SimpleProperty(description = "Changes the Chart's X axis labels to the specified List of Strings,  provided that the Chart Type is set to a Chart with an Axis (applies to Area, Bar, Line, Scatter Charts). The labels are applied in order, starting from the smallest x value on the Chart, and continuing in order. If a label is not specified for an x value, a default value is used (the x value of the axis tick at that location).")
    public void Labels(YailList labels2) {
        this.labels = labels2;
        if (this.chartView instanceof AxisChartView) {
            List<String> stringLabels = new ArrayList<>();
            for (int i = 0; i < labels2.size(); i++) {
                stringLabels.add(labels2.getString(i));
            }
            ((AxisChartView) this.chartView).setLabels(stringLabels);
        }
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(userVisible = false)
    public void LabelsFromString(String labels2) {
        Labels(ElementsUtil.elementsFromString(labels2));
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void XFromZero(boolean zero) {
        this.zeroX = zero;
        if (this.chartView instanceof AxisChartView) {
            ((AxisChartView) this.chartView).setXMinimum(zero);
        }
    }

    @SimpleProperty
    public boolean XFromZero() {
        return this.zeroX;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void YFromZero(boolean zero) {
        this.zeroY = zero;
        if (this.chartView instanceof AxisChartView) {
            ((AxisChartView) this.chartView).setYMinimum(zero);
        }
    }

    @SimpleProperty
    public boolean YFromZero() {
        return this.zeroY;
    }

    @SimpleFunction
    public void SetDomain(double minimum, double maximum) {
        this.zeroX = minimum == 0.0d;
        if (this.chartView instanceof AxisChartView) {
            ((AxisChartView) this.chartView).setXBounds(minimum, maximum);
        }
    }

    @SimpleFunction
    public void SetRange(double minimum, double maximum) {
        this.zeroY = minimum == 0.0d;
        if (this.chartView instanceof AxisChartView) {
            ((AxisChartView) this.chartView).setYBounds(minimum, maximum);
        }
    }

    @SimpleEvent
    public void EntryClick(Component series, Object x, double y) {
        EventDispatcher.dispatchEvent(this, "EntryClick", series, x, Double.valueOf(y));
    }

    public ChartDataModel<?, ?, ?, ?, ?> createChartModel() {
        return this.chartView.createChartModel();
    }

    public void refresh() {
        this.chartView.refresh();
    }

    public ChartView<?, ?, ?, ?, ?> getChartView() {
        return this.chartView;
    }

    public void addDataComponent(ChartDataBase dataComponent) {
        this.dataComponents.add(dataComponent);
    }

    public void onInitialize() {
        if (this.chartView instanceof PieChartView) {
            ((PieChartView) this.chartView).setPieRadius(this.pieRadius);
            this.chartView.refresh();
        }
    }

    public int getSyncedTValue(int dataSeriesT) {
        int returnValue;
        if (this.tick - dataSeriesT > 1) {
            returnValue = this.tick;
        } else {
            returnValue = dataSeriesT;
        }
        this.tick = returnValue + 1;
        return returnValue;
    }
}
