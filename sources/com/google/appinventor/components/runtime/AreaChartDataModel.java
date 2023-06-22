package com.google.appinventor.components.runtime;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.List;

public class AreaChartDataModel extends LineChartBaseDataModel<AreaChartView> {
    public AreaChartDataModel(LineData data, AreaChartView view) {
        super(data, view);
    }

    public void setColor(int argb) {
        super.setColor(argb);
        if (this.dataset instanceof LineDataSet) {
            this.dataset.setFillColor(argb);
        }
    }

    public void setColors(List<Integer> colors) {
        super.setColors(colors);
        if (!colors.isEmpty() && (this.dataset instanceof LineDataSet)) {
            this.dataset.setFillColor(colors.get(0).intValue());
        }
    }

    /* access modifiers changed from: protected */
    public void setDefaultStylingProperties() {
        super.setDefaultStylingProperties();
        this.dataset.setDrawFilled(true);
        if (this.dataset instanceof LineDataSet) {
            this.dataset.setFillAlpha(100);
        }
    }
}
