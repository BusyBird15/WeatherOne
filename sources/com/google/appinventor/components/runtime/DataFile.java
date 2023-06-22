package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ChartDataSourceUtil;
import com.google.appinventor.components.runtime.util.CsvUtil;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONException;

@SimpleObject
@DesignerComponent(category = ComponentCategory.STORAGE, description = "Component that allows reading CSV and JSON data. The DataFile contains functionality relevant to accessing CSV or JSON parsed data in the form of rows or columns. Can be used together with the ChartData2D component to import data directly from a file to the Chart. The component may also be dragged and dropped on a Chart after a file has been selected and parsed successfully to create ChartData components automatically from the file onto the Chart.", iconName = "images/dataFile.png", nonVisible = true, version = 1)
public class DataFile extends FileBase implements DataSource<YailList, Future<YailList>> {
    /* access modifiers changed from: private */
    public YailList columnNames = new YailList();
    /* access modifiers changed from: private */
    public YailList columns = new YailList();
    /* access modifiers changed from: private */
    public YailList rows = new YailList();
    private final ExecutorService threadRunner = Executors.newSingleThreadExecutor();

    public DataFile(ComponentContainer container) {
        super(container);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns a list of rows corresponding to the Data File's content.")
    public YailList Rows() {
        return getYailListPropertyHelper(new Callable<YailList>() {
            public YailList call() {
                return DataFile.this.rows;
            }
        });
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns a list of columns corresponding to the Data File's content.")
    public YailList Columns() {
        return getYailListPropertyHelper(new Callable<YailList>() {
            public YailList call() {
                return DataFile.this.columns;
            }
        });
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the elements of the first row of the Data File's contents.")
    public YailList ColumnNames() {
        return getYailListPropertyHelper(new Callable<YailList>() {
            public YailList call() {
                return DataFile.this.columnNames;
            }
        });
    }

    private YailList getYailListPropertyHelper(Callable<YailList> propertyCallable) {
        try {
            return (YailList) this.threadRunner.submit(propertyCallable).get();
        } catch (InterruptedException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (ExecutionException e2) {
            Log.e(getClass().getName(), e2.getMessage());
        }
        return new YailList();
    }

    @DesignerProperty(editorType = "asset")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void SourceFile(String source) {
        ReadFile("//" + source);
    }

    @SimpleFunction(description = "Indicates source file to load data from. The expected format of the contents of the file are either CSV or JSON.Prefix the filename with / to read from a specific file on the SD card. for instance /myFile.txt will read the file /sdcard/myFile.txt. To read assets packaged with an application (also works for the Companion) start the filename with // (two slashes). If a filename does not start with a slash, it will be read from the applications private storage (for packaged apps) and from /sdcard/AppInventor/data for the Companion.The results of the reading are stored in the Rows, Columns and ColumnNames properties of the component.")
    public void ReadFile(String fileName) {
        readFromFile(fileName);
    }

    public YailList getColumn(String column) {
        int index = this.columnNames.indexOf(column) - 1;
        if (index < 0) {
            return new YailList();
        }
        return (YailList) this.columns.getObject(index);
    }

    /* access modifiers changed from: protected */
    public void afterRead(String result) {
        try {
            if (result.charAt(0) == '{') {
                try {
                    this.columns = JsonUtil.getColumnsFromJson(result);
                    this.rows = ChartDataSourceUtil.getTranspose(this.columns);
                } catch (JSONException e) {
                    this.rows = CsvUtil.fromCsvTable(result);
                    this.columns = ChartDataSourceUtil.getTranspose(this.rows);
                }
            } else {
                this.rows = CsvUtil.fromCsvTable(result);
                this.columns = ChartDataSourceUtil.getTranspose(this.rows);
            }
            this.columnNames = this.rows.size() > 0 ? (YailList) this.rows.getObject(0) : new YailList();
        } catch (Exception e2) {
            Log.e(getClass().getName(), "Unable to parse DataFile", e2);
        }
    }

    public Future<YailList> getDataValue(final YailList columns2) {
        return this.threadRunner.submit(new Callable<YailList>() {
            public YailList call() {
                ArrayList<YailList> resultingColumns = new ArrayList<>();
                for (int i = 0; i < columns2.size(); i++) {
                    resultingColumns.add(DataFile.this.getColumn(columns2.getString(i)));
                }
                return YailList.makeList((List) resultingColumns);
            }
        });
    }
}
