package com.google.appinventor.components.runtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.annotations.UsesQueries;
import com.google.appinventor.components.annotations.androidmanifest.ActionElement;
import com.google.appinventor.components.annotations.androidmanifest.IntentFilterElement;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.EndedStatus;
import com.google.appinventor.components.common.StartedStatus;
import com.google.appinventor.components.runtime.util.BulkPermissionRequest;
import com.google.appinventor.components.runtime.util.PhoneCallUtil;

@DesignerComponent(category = ComponentCategory.SOCIAL, description = "<p>A non-visible component that makes a phone call to the number specified in the <code>PhoneNumber</code> property, which can be set either in the Designer or Blocks Editor. The component has a <code>MakePhoneCall</code> method, enabling the program to launch a phone call.</p><p>Often, this component is used with the <code>ContactPicker</code> component, which lets the user select a contact from the ones stored on the phone and sets the <code>PhoneNumber</code> property to the contact's phone number.</p><p>To directly specify the phone number (e.g., 650-555-1212), set the <code>PhoneNumber</code> property to a Text with the specified digits (e.g., \"6505551212\").  Dashes, dots, and parentheses may be included (e.g., \"(650)-555-1212\") but will be ignored; spaces may not be included.</p>", iconName = "images/phoneCall.png", nonVisible = true, version = 3)
@UsesQueries(intents = {@IntentFilterElement(actionElements = {@ActionElement(name = "android.intent.action.DIAL")})})
@SimpleObject
public class PhoneCall extends AndroidNonvisibleComponent implements Component, OnDestroyListener, ActivityResultListener {
    private static final int PHONECALL_REQUEST_CODE = 1346916174;
    private final CallStateReceiver callStateReceiver;
    private final Context context;
    private boolean didRegisterReceiver = false;
    /* access modifiers changed from: private */
    public boolean havePermission = false;
    private String phoneNumber;

    private enum CallStatus {
        INCOMING_WAITING,
        INCOMING_ANSWERED,
        OUTGOING_WAITING
    }

    public PhoneCall(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.form.registerForOnDestroy(this);
        this.form.registerForActivityResult(this, PHONECALL_REQUEST_CODE);
        PhoneNumber("");
        this.callStateReceiver = new CallStateReceiver();
    }

    public void Initialize() {
        if (this.form.doesAppDeclarePermission("android.permission.READ_CALL_LOG")) {
            this.form.askPermission(new BulkPermissionRequest(this, "Initialize", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_PHONE_STATE", "android.permission.READ_CALL_LOG") {
                public void onGranted() {
                    PhoneCall.this.registerCallStateMonitor();
                }
            });
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String PhoneNumber() {
        return this.phoneNumber;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty
    public void PhoneNumber(String phoneNumber2) {
        this.phoneNumber = phoneNumber2;
    }

    @SimpleFunction(description = "Launches the default dialer app set to start a phone call usingthe number in the PhoneNumber property.")
    public void MakePhoneCall() {
        Intent i = new Intent("android.intent.action.DIAL", Uri.fromParts("tel", this.phoneNumber, (String) null));
        if (i.resolveActivity(this.form.getPackageManager()) != null) {
            this.form.startActivityForResult(i, PHONECALL_REQUEST_CODE);
        }
    }

    @UsesPermissions({"android.permission.CALL_PHONE"})
    @SimpleFunction(description = "Directly initiates a phone call using the number in the PhoneNumber property.")
    public void MakePhoneCallDirect() {
        if (!this.havePermission) {
            this.form.askPermission("android.permission.CALL_PHONE", new PermissionResultHandler() {
                public void HandlePermissionResponse(String permission, boolean granted) {
                    if (granted) {
                        boolean unused = PhoneCall.this.havePermission = true;
                        PhoneCall.this.MakePhoneCallDirect();
                        return;
                    }
                    PhoneCall.this.form.dispatchPermissionDeniedEvent((Component) PhoneCall.this, "MakePhoneCall", "android.permission.CALL_PHONE");
                }
            });
        } else {
            PhoneCallUtil.makePhoneCall(this.context, this.phoneNumber);
        }
    }

    @SimpleEvent(description = "Event indicating that a phonecall has started. If status is 1, incoming call is ringing; if status is 2, outgoing call is dialled. phoneNumber is the incoming/outgoing phone number.")
    @UsesPermissions({"android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_CALL_LOG", "android.permission.READ_PHONE_STATE"})
    public void PhoneCallStarted(@Options(StartedStatus.class) int status, String phoneNumber2) {
        StartedStatus startedStatus = StartedStatus.fromUnderlyingValue(Integer.valueOf(status));
        if (startedStatus != null) {
            PhoneCallStartedAbstract(startedStatus, phoneNumber2);
        }
    }

    public void PhoneCallStartedAbstract(StartedStatus status, String phoneNumber2) {
        EventDispatcher.dispatchEvent(this, "PhoneCallStarted", status.toUnderlyingValue(), phoneNumber2);
    }

    @SimpleEvent(description = "Event indicating that a phone call has ended. If status is 1, incoming call is missed or rejected; if status is 2, incoming call is answered before hanging up; if status is 3, outgoing call is hung up. phoneNumber is the ended call phone number.")
    @UsesPermissions({"android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_CALL_LOG", "android.permission.READ_PHONE_STATE"})
    public void PhoneCallEnded(@Options(EndedStatus.class) int status, String phoneNumber2) {
        EndedStatus endedStatus = EndedStatus.fromUnderlyingValue(Integer.valueOf(status));
        if (endedStatus != null) {
            PhoneCallEndedAbstract(endedStatus, phoneNumber2);
        }
    }

    public void PhoneCallEndedAbstract(EndedStatus status, String phoneNumber2) {
        EventDispatcher.dispatchEvent(this, "PhoneCallEnded", status.toUnderlyingValue(), phoneNumber2);
    }

    @SimpleEvent(description = "Event indicating that an incoming phone call is answered. phoneNumber is the incoming call phone number.")
    @UsesPermissions({"android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_CALL_LOG", "android.permission.READ_PHONE_STATE"})
    public void IncomingCallAnswered(String phoneNumber2) {
        EventDispatcher.dispatchEvent(this, "IncomingCallAnswered", phoneNumber2);
    }

    public void resultReturned(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHONECALL_REQUEST_CODE) {
            PhoneCallStartedAbstract(StartedStatus.Outgoing, "");
        }
    }

    private class CallStateReceiver extends BroadcastReceiver {
        private String number = "";
        private CallStatus status = null;

        public CallStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.PHONE_STATE".equals(action)) {
                String state = intent.getStringExtra("state");
                if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    this.status = CallStatus.INCOMING_WAITING;
                    this.number = intent.getStringExtra("incoming_number");
                    if (this.number != null) {
                        PhoneCall.this.PhoneCallStartedAbstract(StartedStatus.Incoming, this.number);
                    }
                } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                    if (this.status == CallStatus.INCOMING_WAITING) {
                        this.status = CallStatus.INCOMING_ANSWERED;
                        PhoneCall.this.IncomingCallAnswered(this.number);
                    }
                } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                    if (this.status == CallStatus.INCOMING_WAITING) {
                        PhoneCall.this.PhoneCallEndedAbstract(EndedStatus.IncomingRejected, this.number);
                    } else if (this.status == CallStatus.INCOMING_ANSWERED) {
                        PhoneCall.this.PhoneCallEndedAbstract(EndedStatus.IncomingEnded, this.number);
                    } else if (this.status == CallStatus.OUTGOING_WAITING) {
                        PhoneCall.this.PhoneCallEndedAbstract(EndedStatus.OutgoingEnded, this.number);
                    }
                    this.status = null;
                    this.number = "";
                }
            } else if ("android.intent.action.NEW_OUTGOING_CALL".equals(action)) {
                this.status = CallStatus.OUTGOING_WAITING;
                this.number = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
                PhoneCall.this.PhoneCallStartedAbstract(StartedStatus.Outgoing, this.number);
            }
        }
    }

    /* access modifiers changed from: private */
    public void registerCallStateMonitor() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        this.context.registerReceiver(this.callStateReceiver, intentFilter);
        this.didRegisterReceiver = true;
    }

    private void unregisterCallStateMonitor() {
        if (this.didRegisterReceiver) {
            this.context.unregisterReceiver(this.callStateReceiver);
            this.didRegisterReceiver = false;
        }
    }

    public void onDestroy() {
        unregisterCallStateMonitor();
    }
}
