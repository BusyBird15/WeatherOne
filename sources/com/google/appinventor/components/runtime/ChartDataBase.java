package com.google.appinventor.components.runtime;

import android.util.Log;
import android.view.MotionEvent;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.LineType;
import com.google.appinventor.components.common.PointStyle;
import com.google.appinventor.components.runtime.ChartDataModel;
import com.google.appinventor.components.runtime.util.CsvUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SimpleObject
public abstract class ChartDataBase implements Component, DataSourceChangeListener, OnChartGestureListener, OnChartValueSelectedListener {
    protected ChartDataModel<?, ?, ?, ?, ?> chartDataModel;
    private int color;
    private YailList colors;
    protected Chart container;
    protected List<String> dataFileColumns;
    /* access modifiers changed from: private */
    public DataSource<?, ?> dataSource;
    protected String dataSourceKey;
    private String elements;
    private boolean initialized = false;
    private String label;
    /* access modifiers changed from: private */
    public Object lastDataSourceValue;
    protected List<String> sheetsColumns;
    protected ExecutorService threadRunner;
    /* access modifiers changed from: private */
    public int tick = 0;
    protected boolean useSheetHeaders;
    protected List<String> webColumns;

    static /* synthetic */ int access$308(ChartDataBase x0) {
        int i = x0.tick;
        x0.tick = i + 1;
        return i;
    }

    protected ChartDataBase(Chart chartContainer) {
        this.container = chartContainer;
        chartContainer.addDataComponent(this);
        initChartData();
        DataSourceKey("");
        this.threadRunner = Executors.newSingleThreadExecutor();
    }

    public void setExecutorService(ExecutorService service) {
        this.threadRunner = service;
    }

    public void initChartData() {
        this.chartDataModel = this.container.createChartModel();
        Color(-16777216);
        Label("");
        this.chartDataModel.view.chart.setOnChartGestureListener(this);
        this.chartDataModel.view.chart.setOnChartValueSelectedListener(this);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int Color() {
        return this.color;
    }

    @DesignerProperty(defaultValue = "&HFF000000", editorType = "color")
    @SimpleProperty
    public void Color(int argb) {
        this.color = argb;
        this.chartDataModel.setColor(this.color);
        refreshChart();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public YailList Colors() {
        return this.colors;
    }

    @SimpleProperty
    public void Colors(YailList colors2) {
        List<Integer> resultColors = new ArrayList<>();
        for (int i = 0; i < colors2.size(); i++) {
            String color2 = colors2.getString(i);
            try {
                long colorValue = Long.parseLong(color2);
                if (colorValue > 2147483647L) {
                    colorValue -= 4294967296L;
                }
                resultColors.add(Integer.valueOf((int) colorValue));
            } catch (NumberFormatException e) {
                this.container.$form().dispatchErrorOccurredEvent(this.container, "Colors", ErrorMessages.ERROR_INVALID_CHART_DATA_COLOR, color2);
            }
        }
        this.colors = YailList.makeList((List) resultColors);
        this.chartDataModel.setColors(resultColors);
        refreshChart();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public String Label() {
        return this.label;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty
    public void Label(String text) {
        this.label = text;
        this.chartDataModel.setLabel(text);
        refreshChart();
    }

    @DesignerProperty(defaultValue = "0", editorType = "chart_point_shape")
    @SimpleProperty(userVisible = false)
    public void PointShape(PointStyle shape) {
        if (this.chartDataModel instanceof ScatterChartDataModel) {
            ((ScatterChartDataModel) this.chartDataModel).setPointShape(shape);
        }
    }

    @DesignerProperty(defaultValue = "0", editorType = "chart_line_type")
    @SimpleProperty(userVisible = false)
    public void LineType(LineType type) {
        if (this.chartDataModel instanceof LineChartBaseDataModel) {
            ((LineChartBaseDataModel) this.chartDataModel).setLineType(type);
        }
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void ElementsFromPairs(final String elements2) {
        this.elements = elements2;
        if (elements2 != null && !elements2.equals("") && this.initialized) {
            this.threadRunner.execute(new Runnable() {
                public void run() {
                    ChartDataBase.this.chartDataModel.setElements(elements2);
                    ChartDataBase.this.refreshChart();
                }
            });
        }
    }

    @DesignerProperty(editorType = "boolean")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void SpreadsheetUseHeaders(boolean useHeaders) {
        this.useSheetHeaders = useHeaders;
    }

    @DesignerProperty(editorType = "data_file_column")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void DataFileXColumn(String column) {
        this.dataFileColumns.set(0, column);
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Sets the column to parse from the attached Web component for the x values. If a column is not specified, default values for the x values will be generated instead.", userVisible = false)
    public void WebXColumn(String column) {
        this.webColumns.set(0, column);
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void SpreadsheetXColumn(String column) {
        this.sheetsColumns.set(0, column);
    }

    @DesignerProperty(editorType = "data_file_column")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void DataFileYColumn(String column) {
        this.dataFileColumns.set(1, column);
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Sets the column to parse from the attached Web component for the y values. If a column is not specified, default values for the y values will be generated instead.", userVisible = false)
    public void WebYColumn(String column) {
        this.webColumns.set(1, column);
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void SpreadsheetYColumn(String column) {
        this.sheetsColumns.set(1, column);
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void DataSourceKey(String key) {
        this.dataSourceKey = key;
    }

    @DesignerProperty(editorType = "chart_data_source")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public <K, V> void Source(DataSource<K, V> dataSource2) {
        if (this.dataSource != dataSource2 && (this.dataSource instanceof ObservableDataSource)) {
            ((ObservableDataSource) this.dataSource).removeDataObserver(this);
        }
        this.dataSource = dataSource2;
        if (this.initialized) {
            if (dataSource2 instanceof ObservableDataSource) {
                ((ObservableDataSource) dataSource2).addDataObserver(this);
                if (this.dataSourceKey == null) {
                    return;
                }
            }
            if (dataSource2 instanceof DataFile) {
                importFromDataFileAsync((DataFile) dataSource2, YailList.makeList((List) this.dataFileColumns));
            } else if (dataSource2 instanceof TinyDB) {
                ImportFromTinyDB((TinyDB) dataSource2, this.dataSourceKey);
            } else if (dataSource2 instanceof CloudDB) {
                ImportFromCloudDB((CloudDB) dataSource2, this.dataSourceKey);
            } else if (dataSource2 instanceof Spreadsheet) {
                importFromSpreadsheetAsync((Spreadsheet) dataSource2, YailList.makeList((List) this.sheetsColumns), this.useSheetHeaders);
            } else if (dataSource2 instanceof Web) {
                importFromWebAsync((Web) dataSource2, YailList.makeList((List) this.webColumns));
            }
        }
    }

    @SimpleFunction
    public void ImportFromList(final YailList list) {
        this.threadRunner.execute(new Runnable() {
            public void run() {
                ChartDataBase.this.chartDataModel.importFromList(list);
                ChartDataBase.this.refreshChart();
            }
        });
    }

    @SimpleFunction(description = "Clears all of the data.")
    public void Clear() {
        this.threadRunner.execute(new Runnable() {
            public void run() {
                ChartDataBase.this.chartDataModel.clearEntries();
                ChartDataBase.this.refreshChart();
            }
        });
    }

    @SimpleFunction
    public <K, V> void ChangeDataSource(final DataSource<K, V> source, final String keyValue) {
        this.threadRunner.execute(new Runnable() {
            public void run() {
                List<String> columnsList;
                if ((source instanceof DataFile) || (source instanceof Web)) {
                    YailList keyValues = new YailList();
                    try {
                        keyValues = CsvUtil.fromCsvRow(keyValue);
                    } catch (Exception e) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                    if (source instanceof DataFile) {
                        columnsList = ChartDataBase.this.dataFileColumns;
                    } else if (source instanceof Spreadsheet) {
                        columnsList = ChartDataBase.this.sheetsColumns;
                    } else if (source instanceof Web) {
                        columnsList = ChartDataBase.this.webColumns;
                    } else {
                        throw new IllegalArgumentException(source + " is not an expected DataSource");
                    }
                    for (int i = 0; i < columnsList.size(); i++) {
                        String columnValue = "";
                        if (keyValues.size() > i) {
                            columnValue = keyValues.getString(i);
                        }
                        columnsList.set(i, columnValue);
                    }
                } else {
                    ChartDataBase.this.dataSourceKey = keyValue;
                }
                Object unused = ChartDataBase.this.lastDataSourceValue = null;
                ChartDataBase.this.Source(source);
            }
        });
    }

    @SimpleFunction(description = "Un-links the currently associated Data Source component from the Chart.")
    public void RemoveDataSource() {
        this.threadRunner.execute(new Runnable() {
            public void run() {
                ChartDataBase.this.Source((DataSource) null);
                ChartDataBase.this.dataSourceKey = "";
                Object unused = ChartDataBase.this.lastDataSourceValue = null;
                for (int i = 0; i < ChartDataBase.this.dataFileColumns.size(); i++) {
                    ChartDataBase.this.dataFileColumns.set(i, "");
                    ChartDataBase.this.sheetsColumns.set(i, "");
                    ChartDataBase.this.webColumns.set(i, "");
                }
            }
        });
    }

    @SimpleFunction(description = "Returns a List of entries with x values matching the specified x value. A single entry is represented as a List of values of the entry.")
    public YailList GetEntriesWithXValue(final String x) {
        try {
            return (YailList) this.threadRunner.submit(new Callable<YailList>() {
                public YailList call() {
                    return ChartDataBase.this.chartDataModel.findEntriesByCriterion(x, ChartDataModel.EntryCriterion.XValue);
                }
            }).get();
        } catch (InterruptedException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (ExecutionException e2) {
            Log.e(getClass().getName(), e2.getMessage());
        }
        return new YailList();
    }

    @SimpleFunction(description = "Returns a List of entries with y values matching the specified y value. A single entry is represented as a List of values of the entry.")
    public YailList GetEntriesWithYValue(final String y) {
        try {
            return (YailList) this.threadRunner.submit(new Callable<YailList>() {
                public YailList call() {
                    return ChartDataBase.this.chartDataModel.findEntriesByCriterion(y, ChartDataModel.EntryCriterion.YValue);
                }
            }).get();
        } catch (InterruptedException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (ExecutionException e2) {
            Log.e(getClass().getName(), e2.getMessage());
        }
        return new YailList();
    }

    @SimpleFunction(description = "Returns all the entries of the Data Series. A single entry is represented as a List of values of the entry.")
    public YailList GetAllEntries() {
        try {
            return (YailList) this.threadRunner.submit(new Callable<YailList>() {
                public YailList call() {
                    return ChartDataBase.this.chartDataModel.getEntriesAsTuples();
                }
            }).get();
        } catch (InterruptedException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (ExecutionException e2) {
            Log.e(getClass().getName(), e2.getMessage());
        }
        return new YailList();
    }

    @SimpleFunction
    public void ImportFromTinyDB(TinyDB tinyDB, String tag) {
        final List<?> list = tinyDB.getDataValue(tag);
        updateCurrentDataSourceValue(tinyDB, tag, list);
        this.threadRunner.execute(new Runnable() {
            public void run() {
                ChartDataBase.this.chartDataModel.importFromList(list);
                ChartDataBase.this.refreshChart();
            }
        });
    }

    @SimpleFunction
    public void ImportFromCloudDB(final CloudDB cloudDB, final String tag) {
        final Future<YailList> list = cloudDB.getDataValue(tag);
        this.threadRunner.execute(new Runnable() {
            public void run() {
                try {
                    YailList listValue = (YailList) list.get();
                    ChartDataBase.this.updateCurrentDataSourceValue(cloudDB, tag, listValue);
                    ChartDataBase.this.chartDataModel.importFromList(listValue);
                    ChartDataBase.this.refreshChart();
                } catch (InterruptedException e) {
                    Log.e(getClass().getName(), e.getMessage());
                } catch (ExecutionException e2) {
                    Log.e(getClass().getName(), e2.getMessage());
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void importFromDataFileAsync(DataFile dataFile, YailList columns) {
        final Future<YailList> dataFileColumns2 = dataFile.getDataValue(columns);
        this.threadRunner.execute(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: java.lang.Object} */
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.google.appinventor.components.runtime.util.YailList} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                    r1 = 0
                    java.util.concurrent.Future r3 = r0     // Catch:{ InterruptedException -> 0x0019, ExecutionException -> 0x002a }
                    java.lang.Object r3 = r3.get()     // Catch:{ InterruptedException -> 0x0019, ExecutionException -> 0x002a }
                    r0 = r3
                    com.google.appinventor.components.runtime.util.YailList r0 = (com.google.appinventor.components.runtime.util.YailList) r0     // Catch:{ InterruptedException -> 0x0019, ExecutionException -> 0x002a }
                    r1 = r0
                L_0x000b:
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.ChartDataModel<?, ?, ?, ?, ?> r3 = r3.chartDataModel
                    r4 = 1
                    r3.importFromColumns(r1, r4)
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    r3.refreshChart()
                    return
                L_0x0019:
                    r2 = move-exception
                    java.lang.Class r3 = r5.getClass()
                    java.lang.String r3 = r3.getName()
                    java.lang.String r4 = r2.getMessage()
                    android.util.Log.e(r3, r4)
                    goto L_0x000b
                L_0x002a:
                    r2 = move-exception
                    java.lang.Class r3 = r5.getClass()
                    java.lang.String r3 = r3.getName()
                    java.lang.String r4 = r2.getMessage()
                    android.util.Log.e(r3, r4)
                    goto L_0x000b
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.ChartDataBase.AnonymousClass11.run():void");
            }
        });
    }

    /* access modifiers changed from: protected */
    public void importFromSpreadsheetAsync(final Spreadsheet sheets, YailList columns, final boolean useHeaders) {
        final Future<YailList> sheetColumns = sheets.getDataValue(columns, useHeaders);
        this.threadRunner.execute(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: java.lang.Object} */
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.google.appinventor.components.runtime.util.YailList} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r6 = this;
                    r5 = 0
                    r1 = 0
                    java.util.concurrent.Future r3 = r0     // Catch:{ InterruptedException -> 0x0030, ExecutionException -> 0x0041 }
                    java.lang.Object r3 = r3.get()     // Catch:{ InterruptedException -> 0x0030, ExecutionException -> 0x0041 }
                    r0 = r3
                    com.google.appinventor.components.runtime.util.YailList r0 = (com.google.appinventor.components.runtime.util.YailList) r0     // Catch:{ InterruptedException -> 0x0030, ExecutionException -> 0x0041 }
                    r1 = r0
                L_0x000c:
                    com.google.appinventor.components.runtime.Spreadsheet r3 = r4
                    com.google.appinventor.components.runtime.ChartDataBase r4 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.DataSource r4 = r4.dataSource
                    if (r3 != r4) goto L_0x0021
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.ChartDataBase r4 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.DataSource r4 = r4.dataSource
                    r3.updateCurrentDataSourceValue(r4, r5, r5)
                L_0x0021:
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.ChartDataModel<?, ?, ?, ?, ?> r3 = r3.chartDataModel
                    boolean r4 = r6
                    r3.importFromColumns(r1, r4)
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    r3.refreshChart()
                    return
                L_0x0030:
                    r2 = move-exception
                    java.lang.Class r3 = r6.getClass()
                    java.lang.String r3 = r3.getName()
                    java.lang.String r4 = r2.getMessage()
                    android.util.Log.e(r3, r4)
                    goto L_0x000c
                L_0x0041:
                    r2 = move-exception
                    java.lang.Class r3 = r6.getClass()
                    java.lang.String r3 = r3.getName()
                    java.lang.String r4 = r2.getMessage()
                    android.util.Log.e(r3, r4)
                    goto L_0x000c
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.ChartDataBase.AnonymousClass12.run():void");
            }
        });
    }

    /* access modifiers changed from: protected */
    public void importFromWebAsync(final Web webComponent, YailList columns) {
        final Future<YailList> webColumns2 = webComponent.getDataValue(columns);
        this.threadRunner.execute(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: java.lang.Object} */
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.google.appinventor.components.runtime.util.YailList} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r6 = this;
                    r5 = 0
                    r1 = 0
                    java.util.concurrent.Future r3 = r0     // Catch:{ InterruptedException -> 0x002f, ExecutionException -> 0x0040 }
                    java.lang.Object r3 = r3.get()     // Catch:{ InterruptedException -> 0x002f, ExecutionException -> 0x0040 }
                    r0 = r3
                    com.google.appinventor.components.runtime.util.YailList r0 = (com.google.appinventor.components.runtime.util.YailList) r0     // Catch:{ InterruptedException -> 0x002f, ExecutionException -> 0x0040 }
                    r1 = r0
                L_0x000c:
                    com.google.appinventor.components.runtime.Web r3 = r4
                    com.google.appinventor.components.runtime.ChartDataBase r4 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.DataSource r4 = r4.dataSource
                    if (r3 != r4) goto L_0x0021
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.ChartDataBase r4 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.DataSource r4 = r4.dataSource
                    r3.updateCurrentDataSourceValue(r4, r5, r5)
                L_0x0021:
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    com.google.appinventor.components.runtime.ChartDataModel<?, ?, ?, ?, ?> r3 = r3.chartDataModel
                    r4 = 1
                    r3.importFromColumns(r1, r4)
                    com.google.appinventor.components.runtime.ChartDataBase r3 = com.google.appinventor.components.runtime.ChartDataBase.this
                    r3.refreshChart()
                    return
                L_0x002f:
                    r2 = move-exception
                    java.lang.Class r3 = r6.getClass()
                    java.lang.String r3 = r3.getName()
                    java.lang.String r4 = r2.getMessage()
                    android.util.Log.e(r3, r4)
                    goto L_0x000c
                L_0x0040:
                    r2 = move-exception
                    java.lang.Class r3 = r6.getClass()
                    java.lang.String r3 = r3.getName()
                    java.lang.String r4 = r2.getMessage()
                    android.util.Log.e(r3, r4)
                    goto L_0x000c
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.ChartDataBase.AnonymousClass13.run():void");
            }
        });
    }

    /* access modifiers changed from: protected */
    public void refreshChart() {
        this.container.getChartView().refresh(this.chartDataModel);
    }

    public HandlesEventDispatching getDispatchDelegate() {
        return this.container.getDispatchDelegate();
    }

    public void Initialize() {
        this.initialized = true;
        if (this.dataSource != null) {
            Source(this.dataSource);
        } else {
            ElementsFromPairs(this.elements);
        }
    }

    public void onDataSourceValueChange(final DataSource<?, ?> component, final String key, final Object newValue) {
        if (component == this.dataSource && isKeyValid(key)) {
            this.threadRunner.execute(new Runnable() {
                public void run() {
                    if (ChartDataBase.this.lastDataSourceValue instanceof List) {
                        ChartDataBase.this.chartDataModel.removeValues((List) ChartDataBase.this.lastDataSourceValue);
                    }
                    ChartDataBase.this.updateCurrentDataSourceValue(component, key, newValue);
                    if (ChartDataBase.this.lastDataSourceValue instanceof List) {
                        ChartDataBase.this.chartDataModel.importFromList((List) ChartDataBase.this.lastDataSourceValue);
                    }
                    ChartDataBase.this.refreshChart();
                }
            });
        }
    }

    public void onReceiveValue(RealTimeDataSource<?, ?> component, String key, Object value) {
        boolean importData;
        final Object finalValue;
        if (component == this.dataSource) {
            if (component instanceof BluetoothClient) {
                String valueString = (String) value;
                importData = valueString.startsWith(this.dataSourceKey);
                if (importData) {
                    value = valueString.substring(this.dataSourceKey.length());
                }
                finalValue = value;
            } else {
                importData = isKeyValid(key);
                finalValue = value;
            }
            if (importData) {
                this.container.$context().runOnUiThread(new Runnable() {
                    public void run() {
                        int unused = ChartDataBase.this.tick = ChartDataBase.this.container.getSyncedTValue(ChartDataBase.this.tick);
                        ChartDataBase.this.chartDataModel.addTimeEntry(YailList.makeList(Arrays.asList(new Object[]{Integer.valueOf(ChartDataBase.this.tick), finalValue})));
                        ChartDataBase.this.refreshChart();
                        ChartDataBase.access$308(ChartDataBase.this);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCurrentDataSourceValue(DataSource<?, ?> source, String key, Object newValue) {
        if (source == this.dataSource && isKeyValid(key)) {
            if (source instanceof Web) {
                this.lastDataSourceValue = this.chartDataModel.getTuplesFromColumns(((Web) source).getColumns(YailList.makeList((List) this.webColumns)), true);
            } else if (source instanceof Spreadsheet) {
                this.lastDataSourceValue = this.chartDataModel.getTuplesFromColumns(((Spreadsheet) source).getColumns(YailList.makeList((List) this.sheetsColumns), this.useSheetHeaders), this.useSheetHeaders);
            } else {
                this.lastDataSourceValue = newValue;
            }
        }
    }

    private boolean isKeyValid(String key) {
        return key == null || key.equals(this.dataSourceKey);
    }

    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
    }

    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
    }

    public void onChartLongPressed(MotionEvent motionEvent) {
    }

    public void onChartDoubleTapped(MotionEvent motionEvent) {
    }

    public void onChartSingleTapped(MotionEvent motionEvent) {
    }

    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
    }

    public void onChartScale(MotionEvent motionEvent, float v, float v1) {
    }

    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {
    }

    public void onValueSelected(final Entry entry, Highlight highlight) {
        this.container.$form().runOnUiThread(new Runnable() {
            public void run() {
                if (entry instanceof PieEntry) {
                    ChartDataBase.this.EntryClick(entry.getLabel(), (double) entry.getValue());
                } else {
                    ChartDataBase.this.EntryClick(Float.valueOf(entry.getX()), (double) entry.getY());
                }
            }
        });
    }

    @SimpleEvent
    public void EntryClick(Object x, double y) {
        EventDispatcher.dispatchEvent(this, "EntryClick", x, Double.valueOf(y));
        this.container.EntryClick(this, x, y);
    }

    public void onNothingSelected() {
    }
}
