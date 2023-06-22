package com.google.appinventor.components.runtime.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.appinventor.components.runtime.ReplForm;
import com.google.appinventor.components.runtime.Texting;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 8647;
    public static final String TAG = "SmsBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        String phone = getPhoneNumber(intent);
        String msg = getMessage(intent);
        Log.i(TAG, "Received " + phone + " : " + msg);
        int receivingEnabled = Texting.isReceivingEnabled(context);
        if (receivingEnabled == 1) {
            Log.i(TAG, context.getApplicationInfo().packageName + " Receiving is not enabled, ignoring message.");
        } else if ((receivingEnabled == 2 || isRepl(context)) && !Texting.isRunning()) {
            Log.i(TAG, context.getApplicationInfo().packageName + " Texting isn't running, and either receivingEnabled is FOREGROUND or we are the repl.");
        } else {
            Texting.handledReceivedMessage(context, phone, msg);
            if (Texting.isRunning()) {
                Log.i(TAG, context.getApplicationInfo().packageName + " App in Foreground, delivering message.");
                return;
            }
            Log.i(TAG, context.getApplicationInfo().packageName + " Texting isn't running, but receivingEnabled == 2, sending notification.");
            sendNotification(context, phone, msg);
        }
    }

    private String getPhoneNumber(Intent intent) {
        String phone = "";
        try {
            if (intent.getAction().equals("com.google.android.apps.googlevoice.SMS_RECEIVED")) {
                return PhoneNumberUtils.formatNumber(intent.getExtras().getString(Texting.PHONE_NUMBER_TAG));
            }
            if (SdkLevel.getLevel() >= 19) {
                for (SmsMessage smsMsg : KitkatUtil.getMessagesFromIntent(intent)) {
                    if (smsMsg != null) {
                        String phone2 = smsMsg.getOriginatingAddress();
                        if (SdkLevel.getLevel() >= 21) {
                            phone = LollipopUtil.formatNumber(phone2);
                        } else {
                            phone = PhoneNumberUtils.formatNumber(phone2);
                        }
                    }
                }
                return phone;
            }
            for (Object pdu : (Object[]) intent.getExtras().get("pdus")) {
                phone = PhoneNumberUtils.formatNumber(SmsMessage.createFromPdu((byte[]) pdu).getOriginatingAddress());
            }
            return phone;
        } catch (NullPointerException e) {
            Log.w(TAG, "Unable to retrieve originating address from SmsMessage", e);
            return phone;
        }
    }

    private String getMessage(Intent intent) {
        try {
            if (intent.getAction().equals("com.google.android.apps.googlevoice.SMS_RECEIVED")) {
                return intent.getExtras().getString(Texting.MESSAGE_TAG);
            }
            if (SdkLevel.getLevel() >= 19) {
                StringBuilder sb = new StringBuilder();
                for (SmsMessage smsMsg : KitkatUtil.getMessagesFromIntent(intent)) {
                    if (smsMsg != null) {
                        sb.append(smsMsg.getMessageBody());
                    }
                }
                return sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            for (Object pdu : (Object[]) intent.getExtras().get("pdus")) {
                sb2.append(SmsMessage.createFromPdu((byte[]) pdu).getMessageBody());
            }
            return sb2.toString();
        } catch (NullPointerException e) {
            Log.w(TAG, "Unable to retrieve message body from SmsMessage", e);
            return "";
        }
    }

    private void sendNotification(Context context, String phone, String msg) {
        Log.i(TAG, "sendingNotification " + phone + ":" + msg);
        String packageName = context.getPackageName();
        Log.i(TAG, "Package name : " + packageName);
        try {
            String classname = packageName + ".Screen1";
            Intent newIntent = new Intent(context, Class.forName(classname));
            try {
                newIntent.setAction("android.intent.action.MAIN");
                newIntent.addCategory("android.intent.category.LAUNCHER");
                newIntent.addFlags(805306368);
                ((NotificationManager) context.getSystemService("notification")).notify((String) null, NOTIFICATION_ID, new NotificationCompat.Builder(context).setSmallIcon(17301648).setTicker(phone + " : " + msg).setWhen(System.currentTimeMillis()).setAutoCancel(true).setDefaults(1).setContentTitle("Sms from " + phone).setContentText(msg).setContentIntent(PendingIntent.getActivity(context, 0, newIntent, 201326592)).setNumber(Texting.getCachedMsgCount()).build());
                Log.i(TAG, "Notification sent, classname: " + classname);
                Intent intent = newIntent;
            } catch (ClassNotFoundException e) {
                e = e;
                Intent intent2 = newIntent;
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e2) {
            e = e2;
            e.printStackTrace();
        }
    }

    private boolean isRepl(Context context) {
        try {
            if (Class.forName(context.getPackageName() + ".Screen1").getSuperclass().equals(ReplForm.class)) {
                return true;
            }
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
