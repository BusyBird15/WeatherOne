package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.util.Log;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesResponse;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesActivities;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.annotations.androidmanifest.ActionElement;
import com.google.appinventor.components.annotations.androidmanifest.ActivityElement;
import com.google.appinventor.components.annotations.androidmanifest.IntentFilterElement;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ChartDataSourceUtil;
import com.google.appinventor.components.runtime.util.CsvUtil;
import com.google.appinventor.components.runtime.util.IOUtils;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.YailList;
import gnu.lists.LList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

@DesignerComponent(category = ComponentCategory.STORAGE, description = "Spreadsheet is a non-visible component for storing and receiving data from a Spreadsheet document using the Google Sheets API. <p>In order to utilize this component, one must first have a Google Developer Account. Then, one must create a new project under that Google Developer Account, enable the Google Sheets API on that project, and finally create a Service Account for the Sheets API.</p><p>Instructions on how to create the Service Account, as well as where to find other relevant information for using the Spreadsheet Component, can be found <a href='/reference/other/googlesheets-api-setup.html'>here</a>.</p>", iconName = "images/spreadsheet.png", nonVisible = true, version = 2)
@UsesLibraries({"googlesheets.jar", "jackson-core.jar", "google-api-client.jar", "google-api-client-jackson2.jar", "google-http-client.jar", "google-http-client-jackson2.jar", "google-oauth-client.jar", "google-oauth-client-jetty.jar", "grpc-context.jar", "opencensus.jar", "opencensus-contrib-http-util.jar", "guava.jar", "jetty.jar", "jetty-util.jar"})
@SimpleObject
@UsesActivities(activities = {@ActivityElement(configChanges = "orientation|keyboardHidden", intentFilters = {@IntentFilterElement(actionElements = {@ActionElement(name = "android.intent.action.MAIN")})}, name = "com.google.appinventor.components.runtime.WebViewActivity", screenOrientation = "behind")})
@UsesPermissions({"android.permission.INTERNET", "android.permission.ACCOUNT_MANAGER", "android.permission.GET_ACCOUNTS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"})
public class Spreadsheet extends AndroidNonvisibleComponent implements Component, ObservableDataSource<YailList, Future<YailList>> {
    private static final Pattern INTEGER = Pattern.compile("^[0-9]+$");
    private static final String LOG_TAG = "SPREADSHEET";
    private static final String WEBVIEW_ACTIVITY_CLASS = WebViewActivity.class.getName();
    private String accessToken = null;
    /* access modifiers changed from: private */
    public final Activity activity;
    private String apiKey;
    private String applicationName = FusiontablesControl.APP_NAME;
    private File cachedCredentialsFile = null;
    private YailList columns = new YailList();
    private final ComponentContainer container;
    /* access modifiers changed from: private */
    public String credentialsPath;
    private FutureTask<Void> lastTask = null;
    /* access modifiers changed from: private */
    public final Set<DataSourceChangeListener> observers = new HashSet();
    private int requestCode;
    private final Map<String, Integer> sheetIdMap = new HashMap();
    private Sheets sheetsService = null;
    /* access modifiers changed from: private */
    public String spreadsheetID = "";

    public Spreadsheet(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.container = componentContainer;
        this.activity = componentContainer.$context();
    }

    /* access modifiers changed from: private */
    public int getSheetID(Sheets sheetsSvcParam, String sheetName) {
        if (this.sheetIdMap.containsKey(sheetName)) {
            return this.sheetIdMap.get(sheetName).intValue();
        }
        try {
            Sheets.Spreadsheets.Get getSheetRequest = sheetsSvcParam.spreadsheets().get(this.spreadsheetID);
            List<String> ranges = new ArrayList<>();
            ranges.add(sheetName);
            getSheetRequest.setRanges(ranges);
            getSheetRequest.setIncludeGridData(false);
            com.google.api.services.sheets.v4.model.Spreadsheet testSheet = (com.google.api.services.sheets.v4.model.Spreadsheet) getSheetRequest.execute();
            if (testSheet.size() == 0) {
                return -1;
            }
            int sheetID = ((Sheet) testSheet.getSheets().get(0)).getProperties().getSheetId().intValue();
            this.sheetIdMap.put(sheetName, Integer.valueOf(sheetID));
            return sheetID;
        } catch (IOException e) {
            ErrorOccurred("getSheetID: IOException - " + e.getMessage());
            return -1;
        } catch (Exception e2) {
            ErrorOccurred("getSheetID: Unknown Exception - " + e2.getMessage());
            return -1;
        }
    }

    @SimpleProperty
    public String CredentialsJson() {
        return this.credentialsPath;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty(description = "The JSON File with credentials for the Service Account")
    public void CredentialsJson(String credentialsPath2) {
        this.credentialsPath = credentialsPath2;
    }

    @SimpleProperty
    public String SpreadsheetID() {
        return this.spreadsheetID;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty(description = "The ID for the Google Sheets file you want to edit. You can find the spreadsheetID in the URL of the Google Sheets file.")
    public void SpreadsheetID(String spreadsheetID2) {
        if (spreadsheetID2.startsWith("https:")) {
            spreadsheetID2 = spreadsheetID2.substring(8).split("/")[3];
        }
        this.spreadsheetID = spreadsheetID2;
    }

    @SimpleProperty(userVisible = false)
    public String ApplicationName() {
        return this.applicationName;
    }

    @DesignerProperty(defaultValue = "App Inventor", editorType = "string")
    @SimpleProperty(description = "The name of your application, used when making API calls.")
    public void ApplicationName(String applicationName2) {
        this.applicationName = applicationName2;
    }

    private GoogleCredential authorize() throws IOException {
        if (this.cachedCredentialsFile == null) {
            this.cachedCredentialsFile = MediaUtil.copyMediaToTempFile(this.container.$form(), this.credentialsPath);
        }
        InputStream in = null;
        try {
            InputStream in2 = new FileInputStream(this.cachedCredentialsFile);
            try {
                GoogleCredential credential = GoogleCredential.fromStream(in2).createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
                credential.refreshToken();
                this.accessToken = credential.getAccessToken();
                IOUtils.closeQuietly(LOG_TAG, in2);
                Log.d(LOG_TAG, "Credential after refresh token: " + this.accessToken);
                return credential;
            } catch (Throwable th) {
                th = th;
                in = in2;
                IOUtils.closeQuietly(LOG_TAG, in);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            IOUtils.closeQuietly(LOG_TAG, in);
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (this.sheetsService == null) {
            this.sheetsService = new Sheets.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), authorize()).setApplicationName(this.applicationName).build();
        }
        return this.sheetsService;
    }

    /* access modifiers changed from: private */
    public String getColString(int colNumber) {
        if (colNumber == 0) {
            return "";
        }
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder colReference = new StringBuilder();
        while (colNumber > 0) {
            colReference.insert(0, alphabet[(colNumber - 1) % 26]);
            colNumber = (colNumber - 1) / 26;
        }
        return colReference.toString();
    }

    private int getColNum(String columnRef) {
        if (columnRef == null || columnRef.isEmpty()) {
            return -1;
        }
        int number = 0;
        char[] charArray = columnRef.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            number = (number * 26) + (charArray[i] - 'A') + 1;
        }
        return number;
    }

    @SimpleEvent(description = "Triggered whenever an API call encounters an error. Details about the error are in `errorMessage`.")
    public void ErrorOccurred(final String errorMessage) {
        Log.d(LOG_TAG, errorMessage);
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorMessage);
            }
        });
    }

    @SimpleFunction(description = "Converts the integer representation of rows and columns to A1-Notation used in Google Sheets for a single cell.")
    public String GetCellReference(int row, int column) {
        return getColString(column) + row;
    }

    @SimpleFunction(description = "Converts the integer representation of rows and columns for the corners of the range to A1-Notation used in Google Sheets.")
    public String GetRangeReference(int row1, int column1, int row2, int column2) {
        return GetCellReference(row1, column1) + ":" + GetCellReference(row2, column2);
    }

    @SimpleEvent(description = "The callback event for the ReadWithExactQuery or ReadWithPartialQuery block. The `response` is a list of rows numbers and a list of rows containing cell data.", userVisible = false)
    public void GotFilterResult(List<Integer> returnRows, List<List<String>> returnData) {
        Log.d(LOG_TAG, "GotFilterResult got: " + returnRows);
        EventDispatcher.dispatchEvent(this, "GotFilterResult", returnRows, returnData);
    }

    /* access modifiers changed from: private */
    public static String getResponseContent(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        InputStreamReader reader = null;
        try {
            InputStreamReader reader2 = new InputStreamReader(getConnectionStream(connection), encoding);
            try {
                int contentLength = connection.getContentLength();
                StringBuilder sb = contentLength != -1 ? new StringBuilder(contentLength) : new StringBuilder();
                char[] buf = new char[1024];
                while (true) {
                    int read = reader2.read(buf);
                    if (read != -1) {
                        sb.append(buf, 0, read);
                    } else {
                        String sb2 = sb.toString();
                        IOUtils.closeQuietly(LOG_TAG, reader2);
                        return sb2;
                    }
                }
            } catch (Throwable th) {
                th = th;
                reader = reader2;
                IOUtils.closeQuietly(LOG_TAG, reader);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            IOUtils.closeQuietly(LOG_TAG, reader);
            throw th;
        }
    }

    private static InputStream getConnectionStream(HttpURLConnection connection) throws SocketTimeoutException {
        try {
            return connection.getInputStream();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e2) {
            return connection.getErrorStream();
        }
    }

    @SimpleFunction(description = "Filters a Google Sheet for rows where the given column number matches the provided value.")
    public void ReadWithExactFilter(String sheetName, int colID, String value) {
        Log.d(LOG_TAG, "ReadRowsWithFilter colID " + colID + ", value " + value);
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("ReadWithExactFilter: SpreadsheetID is empty.");
        } else {
            AsynchUtil.runAsynchronously(RetrieveSheet(sheetName, colID, value, true, true));
        }
    }

    @SimpleFunction(description = "Filters a Google Sheet for rows where the given column number contains the provided value string.")
    public void ReadWithPartialFilter(String sheetName, int colID, String value) {
        Log.d(LOG_TAG, "ReadWithPartialFilter colID " + colID + ", value " + value);
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("ReadWithPartialFilter: SpreadsheetID is empty.");
        } else {
            AsynchUtil.runAsynchronously(RetrieveSheet(sheetName, colID, value, false, true));
        }
    }

    @SimpleFunction(description = "On the page with the provided sheetName, this method will read the row at the given rowNumber and trigger the GotRowData callback event.")
    public void ReadRow(String sheetName, int rowNumber) {
        if (this.spreadsheetID == "" || this.spreadsheetID == null) {
            ErrorOccurred("ReadRow: SpreadsheetID is empty.");
            return;
        }
        Log.d(LOG_TAG, "Read Row number: " + rowNumber);
        final String rangeReference = sheetName + "!" + rowNumber + ":" + rowNumber;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    if (Spreadsheet.this.credentialsPath == null) {
                        try {
                            String getUrl = String.format("https://docs.google.com/spreadsheets/d/%s/export?format=csv&range=%s", new Object[]{Spreadsheet.this.spreadsheetID, URLEncoder.encode(rangeReference, "UTF-8")});
                            Log.d(Spreadsheet.LOG_TAG, "ReadRow url: " + getUrl);
                            HttpURLConnection connection = (HttpURLConnection) new URL(getUrl).openConnection();
                            connection.setRequestMethod("GET");
                            if (connection.getResponseCode() == 400) {
                                Spreadsheet.this.ErrorOccurred("ReadRow: Bad HTTP Request. Please check the address and try again. " + getUrl);
                                return;
                            }
                            Iterator it = ((LList) CsvUtil.fromCsvTable(Spreadsheet.getResponseContent(connection)).getCdr()).iterator();
                            while (it.hasNext()) {
                                Object elem = it.next();
                                if (elem instanceof YailList) {
                                    final YailList row = (YailList) elem;
                                    Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Spreadsheet.this.GotRowData(row);
                                        }
                                    });
                                    return;
                                }
                            }
                            Spreadsheet.this.ErrorOccurred("ReadRow: Could not find a row from the HTTP Request.");
                        } catch (UnsupportedEncodingException e) {
                            Spreadsheet.this.ErrorOccurred("ReadRow: Error occurred encoding the query. UTF-8 is unsupported?");
                        }
                    } else {
                        List<List<Object>> values = ((ValueRange) Spreadsheet.this.getSheetsService().spreadsheets().values().get(Spreadsheet.this.spreadsheetID, rangeReference).execute()).getValues();
                        if (values == null || values.isEmpty()) {
                            Spreadsheet.this.ErrorOccurred("ReadRow: No data found");
                            return;
                        }
                        final List<String> ret = new ArrayList<>();
                        for (Object obj : values.get(0)) {
                            Object[] objArr = new Object[1];
                            if (obj == null) {
                                obj = "";
                            }
                            objArr[0] = obj;
                            ret.add(String.format("%s", objArr));
                        }
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.GotRowData(ret);
                            }
                        });
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Spreadsheet.this.ErrorOccurred("ReadRow: " + e2.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "The callback event for the ReadRow block. The `rowDataList` is a list of cell values in order of increasing column number.")
    public void GotRowData(List<String> rowDataList) {
        EventDispatcher.dispatchEvent(this, "GotRowData", rowDataList);
    }

    @SimpleFunction(description = "Given a list of values as `data`, writes the values to the row of the sheet with the given row number.")
    public void WriteRow(String sheetName, int rowNumber, YailList data) {
        if (this.spreadsheetID == "" || this.spreadsheetID == null) {
            ErrorOccurred("WriteRow: SpreadsheetID is empty.");
        } else if (this.credentialsPath == "" || this.credentialsPath == null) {
            ErrorOccurred("WriteRow: Credentials JSON file is required.");
        } else {
            final String rangeRef = String.format("%s!A%d", new Object[]{sheetName, Integer.valueOf(rowNumber)});
            List<List<Object>> values = new ArrayList<>();
            values.add(new ArrayList<>((LList) data.getCdr()));
            final ValueRange body = new ValueRange().setValues(values);
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        Spreadsheet.this.getSheetsService().spreadsheets().values().update(Spreadsheet.this.spreadsheetID, rangeRef, body).setValueInputOption("USER_ENTERED").execute();
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedWriteRow();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("WriteRow: " + e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the WriteRow block, called after the values on the table have finished updating")
    public void FinishedWriteRow() {
        EventDispatcher.dispatchEvent(this, "FinishedWriteRow", new Object[0]);
    }

    @SimpleFunction(description = "Given a list of values as `data`, appends the values to the next empty row of the sheet. Additionally, this returns the row number for the new row.")
    public void AddRow(final String sheetName, YailList data) {
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("AddRow: SpreadsheetID is empty.");
        } else if (this.credentialsPath == null || this.credentialsPath.isEmpty()) {
            ErrorOccurred("AddRow: Credentials JSON is required.");
        } else {
            List<List<Object>> values = new ArrayList<>();
            values.add(new ArrayList<>((LList) data.getCdr()));
            final ValueRange body = new ValueRange().setValues(values).setRange(sheetName);
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    int maxRow = 1;
                    try {
                        Sheets sheetsService = Spreadsheet.this.getSheetsService();
                        List<List<Object>> values = ((ValueRange) sheetsService.spreadsheets().values().get(Spreadsheet.this.spreadsheetID, sheetName).execute()).getValues();
                        if (values != null) {
                            maxRow = values.size() + 1;
                        }
                        final int rowNumber = Integer.parseInt(((AppendValuesResponse) sheetsService.spreadsheets().values().append(Spreadsheet.this.spreadsheetID, sheetName + "!A" + maxRow, body.setRange(sheetName + "!A" + maxRow)).setValueInputOption("USER_ENTERED").setInsertDataOption("INSERT_ROWS").execute()).getUpdates().getUpdatedRange().split("!")[1].split(":")[0].replaceAll("[^\\d.]", ""));
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedAddRow(rowNumber);
                            }
                        });
                        if (Spreadsheet.this.observers.size() > 0) {
                            Spreadsheet.this.RetrieveSheet(sheetName, -1, (String) null, false, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("AddRow: " + e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the AddRow block, called once the values on the table have been updated.")
    public void FinishedAddRow(int rowNumber) {
        EventDispatcher.dispatchEvent(this, "FinishedAddRow", Integer.valueOf(rowNumber));
    }

    @SimpleFunction(description = "Deletes the row with the given row number from the table.This does not clear the row, but removes it entirely.")
    public void RemoveRow(final String sheetName, final int rowNumber) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    Sheets sheetsService = Spreadsheet.this.getSheetsService();
                    int gridId = Spreadsheet.this.getSheetID(sheetsService, sheetName);
                    if (gridId == -1) {
                        Spreadsheet.this.ErrorOccurred("RemoveCol: sheetName not found");
                        return;
                    }
                    DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest().setRange(new DimensionRange().setSheetId(Integer.valueOf(gridId)).setDimension("ROWS").setStartIndex(Integer.valueOf(rowNumber - 1)).setEndIndex(Integer.valueOf(rowNumber)));
                    List<Request> requests = new ArrayList<>();
                    requests.add(new Request().setDeleteDimension(deleteRequest));
                    sheetsService.spreadsheets().batchUpdate(Spreadsheet.this.spreadsheetID, new BatchUpdateSpreadsheetRequest().setRequests(requests)).execute();
                    Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Spreadsheet.this.FinishedRemoveRow();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Spreadsheet.this.ErrorOccurred("RemoveRow: " + e.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "The callback event for the RemoveRow block, called once thevalues on the table have been updated.")
    public void FinishedRemoveRow() {
        EventDispatcher.dispatchEvent(this, "FinishedRemoveRow", new Object[0]);
    }

    @SimpleFunction(description = "On the page with the provided sheetName, reads the column at the given index and triggers the GotColumnData callback event.")
    public void ReadColumn(String sheetName, String column) {
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("ReadColumn: SpreadsheetID is empty.");
            return;
        }
        String colReference = column;
        if (Pattern.compile("^[0-9]+$").matcher(column).find()) {
            colReference = getColString(Integer.parseInt(column));
        }
        final String rangeRef = sheetName + "!" + colReference + ":" + colReference;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    if (Spreadsheet.this.credentialsPath == null) {
                        try {
                            String getUrl = String.format("https://docs.google.com/spreadsheets/d/%s/export?format=csv&range=%s", new Object[]{Spreadsheet.this.spreadsheetID, URLEncoder.encode(rangeRef, "UTF-8")});
                            Log.d(Spreadsheet.LOG_TAG, "ReadColumn url: " + getUrl);
                            HttpURLConnection connection = (HttpURLConnection) new URL(getUrl).openConnection();
                            connection.setRequestMethod("GET");
                            if (connection.getResponseCode() == 400) {
                                Spreadsheet.this.ErrorOccurred("ReadColumn: Bad HTTP Request. Please check the address and try again. " + getUrl);
                                return;
                            }
                            YailList parsedCsv = CsvUtil.fromCsvTable(Spreadsheet.getResponseContent(connection));
                            final List<String> col = new ArrayList<>();
                            Iterator it = ((LList) parsedCsv.getCdr()).iterator();
                            while (it.hasNext()) {
                                Object elem = it.next();
                                if (elem instanceof YailList) {
                                    YailList row = (YailList) elem;
                                    Object[] objArr = new Object[1];
                                    objArr[0] = row.isEmpty() ? "" : row.get(1);
                                    col.add(String.format("%s", objArr));
                                }
                            }
                            Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Spreadsheet.this.GotColumnData(col);
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            Spreadsheet.this.ErrorOccurred("ReadColumn: Error occurred encoding the query. UTF-8 is unsupported?");
                        }
                    } else {
                        List<List<Object>> values = ((ValueRange) Spreadsheet.this.getSheetsService().spreadsheets().values().get(Spreadsheet.this.spreadsheetID, rangeRef).execute()).getValues();
                        if (values == null || values.isEmpty()) {
                            Spreadsheet.this.ErrorOccurred("ReadColumn: No data found.");
                            return;
                        }
                        final List<String> ret = new ArrayList<>();
                        for (List<Object> row2 : values) {
                            ret.add(row2.isEmpty() ? "" : row2.get(0).toString());
                        }
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.GotColumnData(ret);
                            }
                        });
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Spreadsheet.this.ErrorOccurred("ReadColumn: " + e2.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "After calling the ReadColumn method, the data in the column will be stored as a list of text values in `columnData`.")
    public void GotColumnData(List<String> columnData) {
        Log.d(LOG_TAG, "GotColumnData got: " + columnData);
        EventDispatcher.dispatchEvent(this, "GotColumnData", columnData);
    }

    @SimpleFunction(description = "Given a list of values as `data`, this method will write the values to the column of the sheet and calls the FinishedWriteColumn event once complete.")
    public void WriteColumn(String sheetName, String column, YailList data) {
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("WriteColumn: SpreadsheetID is empty.");
        } else if (this.credentialsPath == null || this.credentialsPath.isEmpty()) {
            ErrorOccurred("WriteColumn: Credentials JSON is required.");
        } else {
            String colReference = column;
            if (INTEGER.matcher(column).matches()) {
                colReference = getColString(Integer.parseInt(column));
            }
            final String rangeRef = sheetName + "!" + colReference + ":" + colReference;
            List<List<Object>> values = new ArrayList<>();
            Iterator it = ((LList) data.getCdr()).iterator();
            while (it.hasNext()) {
                values.add(new ArrayList<>(Collections.singletonList(it.next())));
            }
            final ValueRange body = new ValueRange().setValues(values);
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        Spreadsheet.this.getSheetsService().spreadsheets().values().update(Spreadsheet.this.spreadsheetID, rangeRef, body).setValueInputOption("USER_ENTERED").execute();
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedWriteColumn();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("WriteColumn IOException: " + e.getMessage());
                    } catch (GeneralSecurityException e2) {
                        e2.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("WriteColumn GeneralSecurityException: " + e2.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the WriteColumn block, called once thevalues on the table have been updated.")
    public void FinishedWriteColumn() {
        EventDispatcher.dispatchEvent(this, "FinishedWriteColumn", new Object[0]);
    }

    @SimpleFunction(description = "Given a list of values as `data`, appends the values to the next empty column of the sheet.")
    public void AddColumn(final String sheetName, YailList data) {
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("AddColumn: SpreadsheetID is empty.");
        } else if (this.credentialsPath == null || this.credentialsPath.isEmpty()) {
            ErrorOccurred("AddColumn: Credentials JSON is required.");
        } else {
            List<List<Object>> values = new ArrayList<>();
            Iterator it = ((LList) data.getCdr()).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                List<Object> r = new ArrayList<>();
                r.add(o);
                values.add(r);
            }
            final ValueRange body = new ValueRange().setValues(values);
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        Sheets sheetsService = Spreadsheet.this.getSheetsService();
                        List<List<Object>> values = ((ValueRange) sheetsService.spreadsheets().values().get(Spreadsheet.this.spreadsheetID, sheetName).execute()).getValues();
                        if (values == null || values.isEmpty()) {
                            Spreadsheet.this.ErrorOccurred("AddColumn: No data found");
                            return;
                        }
                        int maxCol = 0;
                        for (List<Object> list : values) {
                            maxCol = Math.max(maxCol, list.size());
                        }
                        int nextCol = maxCol + 1;
                        final int addedColumn = nextCol;
                        String[] strArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                        UpdateValuesResponse updateValuesResponse = (UpdateValuesResponse) sheetsService.spreadsheets().values().update(Spreadsheet.this.spreadsheetID, sheetName + "!" + Spreadsheet.this.getColString(nextCol) + Component.TYPEFACE_SANSSERIF, body).setValueInputOption("USER_ENTERED").execute();
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedAddColumn(addedColumn);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("AddColumn IOException: " + e.getMessage());
                    } catch (GeneralSecurityException e2) {
                        e2.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("AddColumn GeneralSecurityException: " + e2.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "This event will be triggered once the AddColumn method has finished executing and the values on the spreadsheet have been updated. Additionally, this returns the column number for the new column.")
    public void FinishedAddColumn(int columnNumber) {
        EventDispatcher.dispatchEvent(this, "FinishedAddColumn", Integer.valueOf(columnNumber));
    }

    @SimpleFunction(description = "Deletes the column with the given column number from the table.This does not clear the column, but removes it entirely.")
    public void RemoveColumn(final String sheetName, String column) {
        final int columnNumber;
        if (Pattern.compile("^[0-9]+$").matcher(column).find()) {
            columnNumber = Integer.parseInt(column);
        } else {
            columnNumber = getColNum(column);
        }
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    Sheets sheetsService = Spreadsheet.this.getSheetsService();
                    int gridId = Spreadsheet.this.getSheetID(sheetsService, sheetName);
                    if (gridId == -1) {
                        Spreadsheet.this.ErrorOccurred("RemoveColumn: sheetName not found");
                        return;
                    }
                    DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest().setRange(new DimensionRange().setSheetId(Integer.valueOf(gridId)).setDimension("COLUMNS").setStartIndex(Integer.valueOf(columnNumber - 1)).setEndIndex(Integer.valueOf(columnNumber)));
                    List<Request> requests = new ArrayList<>();
                    requests.add(new Request().setDeleteDimension(deleteRequest));
                    sheetsService.spreadsheets().batchUpdate(Spreadsheet.this.spreadsheetID, new BatchUpdateSpreadsheetRequest().setRequests(requests)).execute();
                    Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Spreadsheet.this.FinishedRemoveColumn();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Spreadsheet.this.ErrorOccurred("RemoveColumn: " + e.getMessage());
                }
            }
        });
    }

    @SimpleEvent(description = "The callback event for the RemoveColumn block, called once the values on the table have been updated.")
    public void FinishedRemoveColumn() {
        EventDispatcher.dispatchEvent(this, "FinishedRemoveColumn", new Object[0]);
    }

    @SimpleFunction(description = "On the page with the provided sheetName, reads the cell at the given cellReference and triggers the GotCellData callback event.")
    public void ReadCell(final String sheetName, final String cellReference) {
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("ReadCell: SpreadsheetID is empty.");
        } else if (!cellReference.matches("[a-zA-Z]+[0-9]+")) {
            ErrorOccurred("ReadCell: Invalid Cell Reference");
        } else {
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    Object obj;
                    Log.d(Spreadsheet.LOG_TAG, "Reading Cell: " + cellReference);
                    try {
                        if (Spreadsheet.this.credentialsPath == null) {
                            try {
                                String getUrl = String.format("https://docs.google.com/spreadsheets/d/%s/export?format=csv&range=%s", new Object[]{Spreadsheet.this.spreadsheetID, URLEncoder.encode(cellReference, "UTF-8")});
                                Log.d(Spreadsheet.LOG_TAG, "ReadCell url: " + getUrl);
                                HttpURLConnection connection = (HttpURLConnection) new URL(getUrl).openConnection();
                                connection.setRequestMethod("GET");
                                if (connection.getResponseCode() == 400) {
                                    Spreadsheet.this.ErrorOccurred("ReadCell: Bad HTTP Request. Please check the address and try again. " + getUrl);
                                    return;
                                }
                                Iterator it = ((LList) CsvUtil.fromCsvTable(Spreadsheet.getResponseContent(connection)).getCdr()).iterator();
                                while (it.hasNext()) {
                                    Object elem = it.next();
                                    if (elem instanceof YailList) {
                                        YailList row = (YailList) elem;
                                        Object[] objArr = new Object[1];
                                        if (row.isEmpty()) {
                                            obj = "";
                                        } else {
                                            obj = row.get(1);
                                        }
                                        objArr[0] = obj;
                                        final String cellData = String.format("%s", objArr);
                                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Spreadsheet.this.GotCellData(cellData);
                                            }
                                        });
                                        return;
                                    }
                                }
                                Spreadsheet.this.ErrorOccurred("ReadCell: Error reading cell data from HTTP Request");
                            } catch (UnsupportedEncodingException e) {
                                Spreadsheet.this.ErrorOccurred("ReadCell: Error occurred encoding the query. UTF-8 is unsupported?");
                            }
                        } else {
                            List<List<Object>> values = ((ValueRange) Spreadsheet.this.getSheetsService().spreadsheets().values().get(Spreadsheet.this.spreadsheetID, sheetName + "!" + cellReference).execute()).getValues();
                            if (values == null || values.isEmpty()) {
                                Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Spreadsheet.this.GotCellData("");
                                    }
                                });
                                return;
                            }
                            Object[] objArr2 = new Object[1];
                            objArr2[0] = values.get(0).isEmpty() ? "" : values.get(0).get(0);
                            final String result = String.format("%s", objArr2);
                            Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Spreadsheet.this.GotCellData(result);
                                }
                            });
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("ReadCell: " + e2.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the ReadCell block. The `cellData` is the text value in the cell (and not the underlying formula).")
    public void GotCellData(String cellData) {
        Log.d(LOG_TAG, "GotCellData got: " + cellData);
        EventDispatcher.dispatchEvent(this, "GotCellData", cellData);
    }

    @SimpleFunction(description = "Given text or a number as `data`, writes the value into the cell. Once complete, it triggers the FinishedWriteCell callback event")
    public void WriteCell(String sheetName, String cellReference, Object data) {
        if (this.spreadsheetID == "") {
            ErrorOccurred("WriteCell: SpreadsheetID is empty.");
        } else if (this.credentialsPath == null) {
            ErrorOccurred("WriteCell: Credentials JSON is required.");
        } else {
            final String rangeRef = sheetName + "!" + cellReference;
            final ValueRange body = new ValueRange().setValues(Arrays.asList(new List[]{Arrays.asList(new Object[]{data})}));
            Log.d(LOG_TAG, "Writing Cell: " + rangeRef);
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        Spreadsheet.this.getSheetsService().spreadsheets().values().update(Spreadsheet.this.spreadsheetID, rangeRef, body).setValueInputOption("USER_ENTERED").execute();
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedWriteCell();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("WriteCell: " + e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the WriteCell block.")
    public void FinishedWriteCell() {
        EventDispatcher.dispatchEvent(this, "FinishedWriteCell", new Object[0]);
    }

    @SimpleFunction(description = "On the page with the provided sheetName, reads the cells at the given range. Triggers the getRangeReference once complete.")
    public void ReadRange(final String sheetName, final String rangeReference) {
        if (this.spreadsheetID == "" || this.spreadsheetID == null) {
            ErrorOccurred("ReadRange: SpreadsheetID is empty.");
        } else {
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    Log.d(Spreadsheet.LOG_TAG, "Reading Range: " + rangeReference);
                    try {
                        if (Spreadsheet.this.credentialsPath == null) {
                            try {
                                String getUrl = String.format("https://docs.google.com/spreadsheets/d/%s/export?format=csv&range=%s", new Object[]{Spreadsheet.this.spreadsheetID, URLEncoder.encode(rangeReference, "UTF-8")});
                                Log.d(Spreadsheet.LOG_TAG, "ReadRange url: " + getUrl);
                                HttpURLConnection connection = (HttpURLConnection) new URL(getUrl).openConnection();
                                connection.setRequestMethod("GET");
                                if (connection.getResponseCode() == 400) {
                                    Spreadsheet.this.ErrorOccurred("ReadRange: Bad HTTP Request. Please check the address and try again. " + getUrl);
                                    return;
                                }
                                final YailList parsedCsv = CsvUtil.fromCsvTable(Spreadsheet.getResponseContent(connection));
                                Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Spreadsheet.this.GotRangeData(parsedCsv);
                                    }
                                });
                            } catch (UnsupportedEncodingException e) {
                                Spreadsheet.this.ErrorOccurred("ReadRange: Error occurred encoding the query. UTF-8 is unsupported?");
                            }
                        } else {
                            List<List<Object>> values = ((ValueRange) Spreadsheet.this.getSheetsService().spreadsheets().values().get(Spreadsheet.this.spreadsheetID, sheetName + "!" + rangeReference).execute()).getValues();
                            if (values == null || values.isEmpty()) {
                                Spreadsheet.this.ErrorOccurred("ReadRange: No data found.");
                                return;
                            }
                            final List<List<String>> ret = new ArrayList<>();
                            for (List<Object> row : values) {
                                List<String> cellRow = new ArrayList<>();
                                for (Object cellValue : row) {
                                    Object[] objArr = new Object[1];
                                    if (cellValue == null) {
                                        cellValue = "";
                                    }
                                    objArr[0] = cellValue;
                                    cellRow.add(String.format("%s", objArr));
                                }
                                ret.add(cellRow);
                            }
                            Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Spreadsheet.this.GotRangeData(ret);
                                }
                            });
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("ReadRange: " + e2.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the ReadRange block. The `rangeData` is a list of rows with the requested dimensions.")
    public void GotRangeData(List<List<String>> rangeData) {
        Log.d(LOG_TAG, "GotRangeData got: " + rangeData);
        EventDispatcher.dispatchEvent(this, "GotRangeData", rangeData);
    }

    @SimpleFunction(description = "Given list of lists as `data`, writes the values into the range. The number of rows and columns in the range reference must match the dimensions of the data.")
    public void WriteRange(String sheetName, String rangeReference, YailList data) {
        if (this.spreadsheetID == "" || this.spreadsheetID == null) {
            ErrorOccurred("WriteRange: SpreadsheetID is empty.");
        } else if (this.credentialsPath == null) {
            ErrorOccurred("WriteRange: Credentials JSON is required.");
        } else {
            final String rangeRef = sheetName + "!" + rangeReference;
            Log.d(LOG_TAG, "Writing Range: " + rangeRef);
            List<List<Object>> values = new ArrayList<>();
            int cols = -1;
            Iterator it = ((LList) data.getCdr()).iterator();
            while (it.hasNext()) {
                Object elem = it.next();
                if (elem instanceof YailList) {
                    List<Object> r = new ArrayList<>();
                    Iterator it2 = ((LList) ((YailList) elem).getCdr()).iterator();
                    while (it2.hasNext()) {
                        r.add(it2.next());
                    }
                    values.add(r);
                    if (cols == -1) {
                        cols = r.size();
                    }
                    if (r.size() != cols) {
                        ErrorOccurred("WriteRange: Rows must have the same length");
                        return;
                    }
                }
            }
            if (values.size() == 0) {
                ErrorOccurred("WriteRange: Data must be a list of lists.");
                return;
            }
            final ValueRange body = new ValueRange().setValues(values);
            Log.d(LOG_TAG, "Body's Range in A1: " + body.getRange());
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        Spreadsheet.this.getSheetsService().spreadsheets().values().update(Spreadsheet.this.spreadsheetID, rangeRef, body).setValueInputOption("USER_ENTERED").execute();
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedWriteRange();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("WriteRange: " + e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the WriteRange block.")
    public void FinishedWriteRange() {
        EventDispatcher.dispatchEvent(this, "FinishedWriteRange", new Object[0]);
    }

    @SimpleFunction(description = "Empties the cells in the given range. Once complete, this block triggers the FinishedClearRange callback event.")
    public void ClearRange(String sheetName, String rangeReference) {
        if (this.spreadsheetID == "" || this.spreadsheetID == null) {
            ErrorOccurred("ClearRange: SpreadsheetID is empty.");
        } else if (this.credentialsPath == null) {
            ErrorOccurred("ClearRange: Credential JSON is required.");
        } else {
            final String rangeRef = sheetName + "!" + rangeReference;
            Log.d(LOG_TAG, "Clearing Range: " + rangeRef);
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        ClearValuesResponse clearValuesResponse = (ClearValuesResponse) Spreadsheet.this.getSheetsService().spreadsheets().values().clear(Spreadsheet.this.spreadsheetID, rangeRef, new ClearValuesRequest()).execute();
                        Spreadsheet.this.form.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.FinishedClearRange();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Spreadsheet.this.ErrorOccurred("ClearRange: " + e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleEvent(description = "The callback event for the ClearRange block.")
    public void FinishedClearRange() {
        EventDispatcher.dispatchEvent(this, "FinishedClearRange", new Object[0]);
    }

    @SimpleFunction(description = "Reads the *entire* Google Sheet document and triggers the GotSheetData callback event.")
    public void ReadSheet(String sheetName) {
        if (this.spreadsheetID == null || this.spreadsheetID.isEmpty()) {
            ErrorOccurred("ReadSheet: SpreadsheetID is empty.");
        } else {
            AsynchUtil.runAsynchronously(RetrieveSheet(sheetName, -1, (String) null, false, true));
        }
    }

    /* access modifiers changed from: package-private */
    public Runnable RetrieveSheet(String sheetName, int colID, String value, boolean exact, boolean fireEvent) {
        final String str = sheetName;
        final boolean z = fireEvent;
        final int i = colID;
        final boolean z2 = exact;
        final String str2 = value;
        return new Runnable() {
            public void run() {
                if (Spreadsheet.this.spreadsheetID == null || Spreadsheet.this.spreadsheetID.isEmpty()) {
                    Spreadsheet.this.ErrorOccurred("ReadSheet: SpreadsheetID is empty.");
                    return;
                }
                Log.d(Spreadsheet.LOG_TAG, "Reading Sheet: " + str);
                try {
                    if (Spreadsheet.this.credentialsPath == null) {
                        Log.d(Spreadsheet.LOG_TAG, "Reading Sheet: No credentials");
                        try {
                            String getUrl = String.format("https://docs.google.com/spreadsheets/d/%s/export?format=csv&sheet=%s", new Object[]{Spreadsheet.this.spreadsheetID, URLEncoder.encode(str + "!", "UTF-8")});
                            HttpURLConnection connection = (HttpURLConnection) new URL(getUrl).openConnection();
                            connection.setRequestMethod("GET");
                            if (connection.getResponseCode() == 400) {
                                Spreadsheet.this.ErrorOccurred("ReadSheet: Bad HTTP Request. Please check the address and try again. " + getUrl);
                                return;
                            }
                            final YailList parsedCsv = CsvUtil.fromCsvTable(Spreadsheet.getResponseContent(connection));
                            Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Spreadsheet.this.updateColumns(parsedCsv);
                                    Spreadsheet.this.notifyDataObservers((YailList) null, (Object) null);
                                    if (!z) {
                                        return;
                                    }
                                    if (i >= 0) {
                                        try {
                                            List<Integer> return_rows = new YailList();
                                            List<List<String>> return_data = new YailList();
                                            int rowNum = 0;
                                            while (rowNum < parsedCsv.size()) {
                                                YailList sheet_row = CsvUtil.fromCsvRow(parsedCsv.get(rowNum).toString());
                                                if (sheet_row.size() >= i) {
                                                    if ((z2 && sheet_row.get(i - 1).equals(str2)) || (!z2 && sheet_row.get(i - 1).toString().contains(str2))) {
                                                        return_rows.add(Integer.valueOf(rowNum));
                                                        return_data.add(sheet_row);
                                                    }
                                                    rowNum++;
                                                }
                                            }
                                            Spreadsheet.this.GotFilterResult(return_rows, return_data);
                                        } catch (Exception e) {
                                            Log.d(Spreadsheet.LOG_TAG, "ReadWithFilter (no creds) Error: " + e.getMessage());
                                            Spreadsheet.this.ErrorOccurred(e.getMessage());
                                        }
                                    } else {
                                        Spreadsheet.this.GotSheetData(parsedCsv);
                                    }
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            Spreadsheet.this.ErrorOccurred("ReadRange: Error occurred encoding the query. UTF-8 is unsupported?");
                        }
                    } else {
                        Log.d(Spreadsheet.LOG_TAG, "Reading Sheet: Credentials located.");
                        Sheets sheetsService = Spreadsheet.this.getSheetsService();
                        Log.d(Spreadsheet.LOG_TAG, "Reading Sheet: Got sheet service");
                        Log.d(Spreadsheet.LOG_TAG, "Got read result");
                        List<List<Object>> values = ((ValueRange) sheetsService.spreadsheets().values().get(Spreadsheet.this.spreadsheetID, str).execute()).getValues();
                        Log.d(Spreadsheet.LOG_TAG, "Reading Sheet: values count " + values.size());
                        if (values == null || values.isEmpty()) {
                            Spreadsheet.this.ErrorOccurred("ReadSheet: No data found.");
                            return;
                        }
                        final List<List<String>> ret = new ArrayList<>();
                        Log.d(Spreadsheet.LOG_TAG, "RetriveSheet data: " + values);
                        for (List<Object> row : values) {
                            List<String> cellRow = new ArrayList<>();
                            for (Object cellValue : row) {
                                Object[] objArr = new Object[1];
                                if (cellValue == null) {
                                    cellValue = "";
                                }
                                objArr[0] = cellValue;
                                cellRow.add(String.format("%s", objArr));
                            }
                            ret.add(cellRow);
                        }
                        Log.d(Spreadsheet.LOG_TAG, "RetriveSheet return rowcount: " + ret.size());
                        Spreadsheet.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Spreadsheet.this.updateColumns(YailList.makeList(ret));
                                Spreadsheet.this.notifyDataObservers((YailList) null, (Object) null);
                                Log.d(Spreadsheet.LOG_TAG, "RetriveSheet UIThread ");
                                if (i >= 0) {
                                    Log.d(Spreadsheet.LOG_TAG, "RetriveWithFilter: colID " + i);
                                    try {
                                        List<Integer> return_rows = new ArrayList<>();
                                        List<List<String>> return_data = new ArrayList<>();
                                        for (int rowNum = 0; rowNum < ret.size(); rowNum++) {
                                            Log.d(Spreadsheet.LOG_TAG, "Reading row row: " + rowNum);
                                            List<String> sheet_row = (List) ret.get(rowNum);
                                            Log.d(Spreadsheet.LOG_TAG, "Read with Filter row: " + sheet_row);
                                            if (sheet_row.size() >= i) {
                                                Log.d(Spreadsheet.LOG_TAG, "Checking field : |" + sheet_row.get(i - 1) + "|");
                                                if ((z2 && sheet_row.get(i - 1).equals(str2)) || (!z2 && sheet_row.get(i - 1).contains(str2))) {
                                                    Log.d(Spreadsheet.LOG_TAG, "Read with Filter check col: " + rowNum);
                                                    return_rows.add(Integer.valueOf(rowNum + 1));
                                                    return_data.add(sheet_row);
                                                }
                                            }
                                        }
                                        Spreadsheet.this.GotFilterResult(return_rows, return_data);
                                    } catch (Exception e) {
                                        Log.d(Spreadsheet.LOG_TAG, "Read with Filter Error: " + e.getClass().getName() + e.getMessage());
                                        Spreadsheet.this.ErrorOccurred(e.getMessage());
                                    }
                                } else {
                                    Spreadsheet.this.GotSheetData(ret);
                                }
                            }
                        });
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Spreadsheet.this.ErrorOccurred("RetrieveSheet Error: " + e2.getMessage());
                }
            }
        };
    }

    @SimpleEvent(description = "The callback event for the ReadSheet block. The `sheetData` is a list of rows.")
    public void GotSheetData(List<List<String>> sheetData) {
        Log.d(LOG_TAG, "GotSheetData got: " + sheetData);
        EventDispatcher.dispatchEvent(this, "GotSheetData", sheetData);
    }

    public Future<YailList> getDataValue(YailList key) {
        return getDataValue(key, false);
    }

    public Future<YailList> getDataValue(final YailList key, final boolean useHeaders) {
        final FutureTask<Void> currentTask = this.lastTask;
        FutureTask<YailList> getDataValueTask = new FutureTask<>(new Callable<YailList>() {
            public YailList call() throws Exception {
                if (currentTask != null && !currentTask.isDone() && !currentTask.isCancelled()) {
                    try {
                        currentTask.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return Spreadsheet.this.getColumns(key, useHeaders);
            }
        });
        AsynchUtil.runAsynchronously(getDataValueTask);
        return getDataValueTask;
    }

    public void addDataObserver(DataSourceChangeListener dataComponent) {
        this.observers.add(dataComponent);
    }

    public void removeDataObserver(DataSourceChangeListener dataComponent) {
        this.observers.remove(dataComponent);
    }

    public void notifyDataObservers(YailList key, Object newValue) {
        for (DataSourceChangeListener dataComponent : this.observers) {
            dataComponent.onDataSourceValueChange(this, (String) null, this.columns);
        }
    }

    /* access modifiers changed from: private */
    public void updateColumns(YailList parsedCsv) {
        try {
            this.columns = ChartDataSourceUtil.getTranspose(parsedCsv);
        } catch (Exception e) {
            this.columns = new YailList();
        }
    }

    private YailList getColumn(String column) {
        YailList result = new YailList();
        for (int i = 0; i < this.columns.size(); i++) {
            YailList list = (YailList) this.columns.getObject(i);
            if (!list.isEmpty() && list.getString(0).equals(column)) {
                return list;
            }
        }
        return result;
    }

    private YailList getColumn(int column) {
        YailList result = new YailList();
        if (column < this.columns.size()) {
            return (YailList) this.columns.getObject(column);
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public YailList getColumns(YailList keyColumns, boolean useHeaders) {
        YailList column;
        List<YailList> resultingColumns = new ArrayList<>();
        for (int i = 0; i < keyColumns.size(); i++) {
            String columnName = keyColumns.getString(i);
            if (useHeaders) {
                column = getColumn(columnName);
            } else {
                int colIndex = 0;
                char[] charArray = columnName.toCharArray();
                int length = charArray.length;
                for (int i2 = 0; i2 < length; i2++) {
                    colIndex = (colIndex * 26) + (charArray[i2] - 'A');
                }
                column = getColumn(colIndex);
            }
            resultingColumns.add(column);
        }
        return YailList.makeList((List) resultingColumns);
    }
}
