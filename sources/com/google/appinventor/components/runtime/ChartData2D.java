package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@SimpleObject
@DesignerComponent(category = ComponentCategory.CHARTS, description = "A component that holds (x, y)-coordinate based data", iconName = "images/web.png", version = 1)
public final class ChartData2D extends ChartDataBase {
    public ChartData2D(Chart chartContainer) {
        super(chartContainer);
        this.dataFileColumns = Arrays.asList(new String[]{"", ""});
        this.sheetsColumns = Arrays.asList(new String[]{"", ""});
        this.webColumns = Arrays.asList(new String[]{"", ""});
    }

    @SimpleFunction
    public void AddEntry(final String x, final String y) {
        this.threadRunner.execute(new Runnable() {
            public void run() {
                ChartData2D.this.chartDataModel.addEntryFromTuple(YailList.makeList(Arrays.asList(new String[]{x, y})));
                ChartData2D.this.refreshChart();
            }
        });
    }

    @SimpleFunction
    public void RemoveEntry(final String x, final String y) {
        this.threadRunner.execute(new Runnable() {
            public void run() {
                ChartData2D.this.chartDataModel.removeEntryFromTuple(YailList.makeList(Arrays.asList(new String[]{x, y})));
                ChartData2D.this.refreshChart();
            }
        });
    }

    @SimpleFunction(description = "Checks whether an (x, y) entry exists in the Coordinate Data.Returns true if the Entry exists, and false otherwise.")
    public boolean DoesEntryExist(final String x, final String y) {
        try {
            return ((Boolean) this.threadRunner.submit(new Callable<Boolean>() {
                public Boolean call() {
                    return Boolean.valueOf(ChartData2D.this.chartDataModel.doesEntryExist(YailList.makeList(Arrays.asList(new String[]{x, y}))));
                }
            }).get()).booleanValue();
        } catch (InterruptedException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (ExecutionException e2) {
            Log.e(getClass().getName(), e2.getMessage());
        }
        return false;
    }

    @SimpleFunction
    public void ImportFromDataFile(DataFile dataFile, String xValueColumn, String yValueColumn) {
        importFromDataFileAsync(dataFile, YailList.makeList(Arrays.asList(new String[]{xValueColumn, yValueColumn})));
    }

    @SimpleFunction
    public void ImportFromSpreadsheet(Spreadsheet spreadsheet, String xColumn, String yColumn, boolean useHeaders) {
        importFromSpreadsheetAsync(spreadsheet, YailList.makeList(Arrays.asList(new String[]{xColumn, yColumn})), useHeaders);
    }

    @SimpleFunction(description = "Imports data from the specified Web component, given the names of the X and Y value columns. Empty columns are filled with default values (1, 2, 3, ... for Entry 1, 2, ...). In order for the data importing to be successful, to load the data, and then this block should be used on that Web component. The usage of the gotValue event in the Web component is unnecessary.")
    public void ImportFromWeb(Web web, String xValueColumn, String yValueColumn) {
        importFromWebAsync(web, YailList.makeList(Arrays.asList(new String[]{xValueColumn, yValueColumn})));
    }
}
