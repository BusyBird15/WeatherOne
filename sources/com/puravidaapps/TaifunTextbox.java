package com.puravidaapps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.ReplForm;
import com.google.appinventor.components.runtime.TextBox;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@SimpleObject(external = true)
@DesignerComponent(category = ComponentCategory.EXTENSION, description = "Textbox extension. Version 6 as of 2019-07-30.", helpUrl = "https://puravidaapps.com/textbox.php", iconName = "https://puravidaapps.com/images/taifun16.png", nonVisible = true, version = 6)
@UsesPermissions(permissionNames = "android.permission.READ_EXTERNAL_STORAGE")
public class TaifunTextbox extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "TaifunTextbox";
    public static final int VERSION = 6;
    private static Context context;
    /* access modifiers changed from: private */
    public final Activity activity;
    private ComponentContainer container;
    private boolean isRepl = false;
    private boolean suppressWarnings;
    private HashMap<EditText, TextWatcher> textWatcherMap = new HashMap<>();

    public TaifunTextbox(ComponentContainer container2) {
        super(container2.$form());
        if (this.form instanceof ReplForm) {
            this.isRepl = true;
        }
        this.container = container2;
        context = container2.$context();
        this.activity = container2.$context();
        Log.d(LOG_TAG, "TaifunTextbox Created");
    }

    @SimpleFunction(description = "Return length of textbox.")
    public int Length(TextBox textBox) {
        return ((EditText) textBox.getView()).getText().length();
    }

    @SimpleFunction(description = "Return current position of cursor in textbox.")
    public int CurrentPosition(TextBox textBox) {
        return ((EditText) textBox.getView()).getSelectionStart();
    }

    @SimpleFunction(description = "Set cursor at a specific position in textbox.")
    public void SetCursorAt(TextBox textBox, int position) {
        EditText editText = (EditText) textBox.getView();
        int position2 = position - 1;
        if (Length(textBox) >= position2) {
            editText.setSelection(position2);
        } else {
            editText.setSelection(editText.getText().length());
        }
    }

    @SimpleFunction(description = "Set cursor at end position in textbox.")
    public void SetCursorAtEnd(TextBox textBox) {
        EditText editText = (EditText) textBox.getView();
        editText.setSelection(editText.getText().length());
    }

    @SimpleFunction(description = "Highlight text.")
    public void HighlightText(TextBox textBox, int fromPosition, int toPosition) {
        int fromPosition2 = fromPosition - 1;
        int toPosition2 = toPosition - 1;
        if (fromPosition2 > toPosition2) {
            int temp = fromPosition2;
            fromPosition2 = toPosition2;
            toPosition2 = temp;
        }
        if (Length(textBox) < fromPosition2) {
            fromPosition2 = Length(textBox);
        }
        if (Length(textBox) < toPosition2) {
            toPosition2 = Length(textBox);
        }
        ((EditText) textBox.getView()).setSelection(fromPosition2, toPosition2);
    }

    @SimpleEvent(description = "Event indicating that text has been changed.")
    public void AfterTextChanged(Object component) {
        Log.d(LOG_TAG, "AfterTextChanged");
        EventDispatcher.dispatchEvent(this, "AfterTextChanged", component);
    }

    @SimpleFunction(description = "Set background image.You can use a relative path or a complete path to the image file. To use a relative path, just prefix the imageFileName with /. For instance /myFile.jpg will set the file /mnt/sdcard/myFile.jpg. If a fileName starts with file:/// you can specify a complete path to the file. To use an image file from the assets, prefix the imageFileName with //. ")
    public void SetBackgroundImage(TextBox textBox, String imageFileName) {
        Bitmap bitmap;
        String completeFileName = completeFileName(imageFileName);
        Log.d(LOG_TAG, "SetBackgroundImage, completeFileName: " + completeFileName);
        File file = new File(completeFileName);
        if (file.isDirectory()) {
            Log.d(LOG_TAG, "Sorry, imageFileName is not a filename.");
            if (!this.suppressWarnings) {
                Toast.makeText(context, "Sorry, imageFileName is not a filename.", 0).show();
            }
        } else if (!imageFileName.startsWith("//") && !file.exists()) {
            Log.d(LOG_TAG, "Sorry, file does not exist.");
            if (!this.suppressWarnings) {
                Toast.makeText(context, "Sorry, file does not exist.", 0).show();
            }
        } else if (isJpg(imageFileName) || isPng(imageFileName)) {
            EditText editText = (EditText) textBox.getView();
            if (!imageFileName.startsWith("//") || this.isRepl) {
                bitmap = BitmapFactory.decodeFile(completeFileName, new BitmapFactory.Options());
            } else {
                bitmap = getBitmapFromAsset(imageFileName.substring(2));
            }
            editText.setBackgroundDrawable(new BitmapDrawable(bitmap));
        } else {
            Log.d(LOG_TAG, "Sorry, file must be in jpg or png format.");
            if (!this.suppressWarnings) {
                Toast.makeText(context, "Sorry, file must be in jpg or png format.", 0).show();
            }
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "whether Warnings should be suppressed")
    public boolean SuppressWarnings() {
        return this.suppressWarnings;
    }

    @DesignerProperty(defaultValue = "false", editorType = "boolean")
    @SimpleProperty
    public void SuppressWarnings(boolean suppressWarnings2) {
        this.suppressWarnings = suppressWarnings2;
    }

    private String completeFileName(String fileName) {
        File sd = Environment.getExternalStorageDirectory();
        String completeFileName = fileName;
        if (fileName.startsWith("file:///")) {
            completeFileName = fileName.substring(7);
        } else if (fileName.startsWith("//")) {
            String fileName2 = fileName.substring(2);
            if (this.isRepl) {
                completeFileName = Environment.getExternalStorageDirectory().getPath() + "/AppInventor/assets/" + fileName2;
            } else {
                completeFileName = fileName2;
            }
        } else if (!fileName.startsWith("/")) {
            completeFileName = sd + File.separator + fileName;
        } else if (!fileName.startsWith(sd.toString())) {
            completeFileName = sd + fileName;
        }
        Log.d(LOG_TAG, "completeFileName= " + completeFileName);
        return completeFileName;
    }

    private boolean isPng(String imageFileName) {
        return fileExtension(imageFileName).equalsIgnoreCase("png");
    }

    private boolean isJpg(String imageFileName) {
        if (fileExtension(imageFileName).equalsIgnoreCase("jpg") || fileExtension(imageFileName).equalsIgnoreCase("jpeg")) {
            return true;
        }
        return false;
    }

    private static String fileExtension(String name) {
        int idx = name.lastIndexOf(46);
        if (idx == -1 || idx == name.length() - 1) {
            return "";
        }
        return name.substring(idx + 1, name.length());
    }

    private Bitmap getBitmapFromAsset(String filename) {
        Bitmap bitmap;
        Log.d(LOG_TAG, "getBitmapFromAsset: " + filename);
        InputStream is = null;
        try {
            is = context.getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(is);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e2) {
            Log.e(LOG_TAG, e2.getMessage());
            if (!this.suppressWarnings) {
                Toast.makeText(context, e2.getMessage(), 0).show();
            }
            bitmap = null;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
        return bitmap;
    }

    @SimpleFunction(description = "Get Highlight color.")
    public int GetHighlightColor(TextBox textBox) {
        return ((EditText) textBox.getView()).getHighlightColor();
    }

    @SimpleFunction(description = "Set Highlight color.")
    public void SetHighlightColor(TextBox textBox, int color) {
        ((EditText) textBox.getView()).setHighlightColor(color);
    }

    @SimpleFunction(description = "Start Enter Pressed Listener for a textbox.")
    public void StartEnterPressedListener(final TextBox textBox) {
        Log.d(LOG_TAG, "StartEnterPressedListener");
        ((EditText) textBox.getView()).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TaifunTextbox.LOG_TAG, "onEditorAction, actionId: " + actionId);
                if (actionId != 6 && actionId != 2 && actionId != 5 && actionId != 7 && actionId != 3 && actionId != 4) {
                    return false;
                }
                TaifunTextbox.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        TaifunTextbox.this.EnterPressed(textBox);
                    }
                });
                return true;
            }
        });
    }

    @SimpleEvent(description = "Event indicating that enter in textbox has been pressed.")
    public void EnterPressed(Object component) {
        Log.d(LOG_TAG, "EnterPressed");
        EventDispatcher.dispatchEvent(this, "EnterPressed", component);
    }

    @SimpleFunction(description = "Set Icon for Enter button on soft keyboard for a textbox. Possible values for icon: DONE, GO, NEXT, PREVIOUS, SEARCH, SEND")
    public void SetSoftKeyboardIconEnter(TextBox textBox, String icon) {
        Log.d(LOG_TAG, "SetSoftKeyboardIconEnter: " + icon);
        EditText editText = (EditText) textBox.getView();
        if (icon.equals("DONE")) {
            editText.setImeOptions(6);
        } else if (icon.equals("GO")) {
            editText.setImeOptions(2);
        } else if (icon.equals("NEXT")) {
            editText.setImeOptions(5);
        } else if (icon.equals("PREVIOUS")) {
            editText.setImeOptions(7);
        } else if (icon.equals("SEARCH")) {
            editText.setImeOptions(3);
        } else if (icon.equals("SEND")) {
            editText.setImeOptions(4);
        }
    }

    @SimpleFunction(description = "Start Text Changed listener.")
    public void StartTextChangedListener(final TextBox textBox) {
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable text) {
                TaifunTextbox.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        TaifunTextbox.this.AfterTextChanged(textBox);
                    }
                });
            }
        };
        EditText editText = (EditText) textBox.getView();
        editText.addTextChangedListener(textWatcher);
        this.textWatcherMap.put(editText, textWatcher);
    }

    @SimpleFunction(description = "Stop Text Changed listener.")
    public void StopTextChangedListener(TextBox textBox) {
        EditText editText = (EditText) textBox.getView();
        if (this.textWatcherMap.get(editText) != null) {
            editText.removeTextChangedListener(this.textWatcherMap.get(editText));
            this.textWatcherMap.put(editText, (Object) null);
        }
    }
}
