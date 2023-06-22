package com.jdl.NotificationStyle;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesAssets;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.OnDestroyListener;
import com.google.appinventor.components.runtime.util.YailList;
import gnu.expr.Declaration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SimpleObject(external = true)
@UsesAssets(fileNames = "favorite_border.png,favorite.png,next.png,pause.png,play.png,previous.png,reply.png")
@DesignerComponent(category = ComponentCategory.EXTENSION, description = "Notification Style <br> Developed by Jarlisson", helpUrl = "https://github.com/jarlisson2/NotificationStyleAIX", iconName = "aiwebres/notification.png", nonVisible = true, version = 1)
public class NotificationStyle extends AndroidNonvisibleComponent implements OnDestroyListener {
    static List<Message> MESSAGES = new ArrayList();
    private static final String channelDefault = "ChannelA";
    private static final String iconNotificationDefault = "";
    private static final int importanceChannelDefault = 2;
    private static final int priorityNotificationDefault = 2;
    public Activity activity;
    public Notification.Builder builder;
    private String channel = channelDefault;
    private int colorNoti = -16777216;
    public ComponentContainer container;
    public Context context;
    /* access modifiers changed from: private */
    public boolean favorite;
    /* access modifiers changed from: private */
    public String group;
    private String iconNotification = "";
    private int importanceChannel = 2;
    /* access modifiers changed from: private */
    public String largeIcon;
    private final MediaSession mediaSession;
    /* access modifiers changed from: private */
    public String message;
    BroadcastReceiver messageBroad = new BroadcastReceiver() {
        public synchronized void onReceive(Context context, Intent intent) {
            Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
            if (resultsFromIntent != null) {
                CharSequence charSequence = resultsFromIntent.getCharSequence("key_text_reply");
                long currentTimeMillis = System.currentTimeMillis();
                Message message = new Message(charSequence, (CharSequence) null, currentTimeMillis);
                NotificationStyle.MESSAGES.add(message);
                NotificationStyle.this.CallbackMessage(charSequence.toString(), currentTimeMillis);
                String unused = NotificationStyle.this.group = intent.getStringExtra("group");
                String unused2 = NotificationStyle.this.message = message.toString();
                String unused3 = NotificationStyle.this.sender = "";
                long unused4 = NotificationStyle.this.timestamp = currentTimeMillis;
            }
        }
    };
    public NotificationManager notifManager;
    /* access modifiers changed from: private */
    public boolean pause;
    private int priorityNotification = 2;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        public synchronized void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("MUSIC_PLAY")) {
                NotificationStyle.this.CallbackMusicPlayer("Play");
            } else if (intent.getAction().equals("MUSIC_PAUSE")) {
                NotificationStyle.this.CallbackMusicPlayer("Pause");
            } else if (intent.getAction().equals("MUSIC_PREVIOUS")) {
                NotificationStyle.this.CallbackMusicPlayer("Previous");
            } else if (intent.getAction().equals("MUSIC_NEXT")) {
                NotificationStyle.this.CallbackMusicPlayer("Next");
            } else if (intent.getAction().equals("MUSIC_FAVORITE")) {
                boolean unused = NotificationStyle.this.favorite = !NotificationStyle.this.favorite;
                NotificationStyle.this.CallbackMusicPlayer(NotificationStyle.this.favorite ? "Favorite" : "Unfavorite");
                NotificationStyle.this.musicNotification(NotificationStyle.this.title, NotificationStyle.this.subtitle, NotificationStyle.this.largeIcon, NotificationStyle.this.pause, NotificationStyle.this.favorite);
            }
        }
    };
    /* access modifiers changed from: private */
    public String sender;
    /* access modifiers changed from: private */
    public String subtitle;
    /* access modifiers changed from: private */
    public long timestamp;
    /* access modifiers changed from: private */
    public String title;

    public NotificationStyle(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.container = componentContainer;
        this.context = componentContainer.$context();
        this.activity = (Activity) this.context;
        Channel(channelDefault);
        ImportanceChannel(2);
        PriorityNotification(2);
        IconNotification("");
        this.mediaSession = new MediaSession(this.context, "tag");
        cancelAllNotification();
        this.activity.registerReceiver(this.receiver, new IntentFilter("MUSIC_FAVORITE"));
        this.activity.registerReceiver(this.receiver, new IntentFilter("MUSIC_PAUSE"));
        this.activity.registerReceiver(this.receiver, new IntentFilter("MUSIC_PLAY"));
        this.activity.registerReceiver(this.receiver, new IntentFilter("MUSIC_PREVIOUS"));
        this.activity.registerReceiver(this.receiver, new IntentFilter("MUSIC_NEXT"));
        this.activity.registerReceiver(this.messageBroad, new IntentFilter("MESSAGE_REPLY"));
        this.form.registerForOnDestroy(this);
    }

    private int SetPriority(int i, boolean z) {
        int i2 = 2;
        int i3 = 0;
        switch (i) {
            case 0:
                return !z ? -2 : 1;
            case 1:
                return z ? 2 : -1;
            case 2:
                return z ? 3 : 0;
            case 3:
                return z ? 4 : 1;
            case 4:
                if (z) {
                    i2 = 5;
                }
                return i2;
            default:
                if (!z) {
                    i3 = 3;
                }
                return i3;
        }
    }

    private void StartApp(String str) {
        int currentTimeMillis = (int) System.currentTimeMillis();
        Intent intent = new Intent();
        String packageName = this.context.getPackageName();
        String localClassName = this.activity.getLocalClassName();
        String simpleName = this.activity.getClass().getSimpleName();
        if (localClassName.equals(simpleName)) {
            localClassName = packageName + "." + simpleName;
        }
        intent.setClassName(packageName, localClassName);
        intent.setFlags(603979776);
        intent.putExtra("APP_INVENTOR_START", str);
        this.builder.setContentIntent(PendingIntent.getActivity(this.context, currentTimeMillis, intent, Declaration.PACKAGE_ACCESS));
    }

    private void cancelAllNotification() {
        ((NotificationManager) this.context.getSystemService("notification")).cancelAll();
    }

    private void cancelNotification(int i) {
        ((NotificationManager) this.context.getSystemService("notification")).cancel(i);
    }

    /* access modifiers changed from: private */
    public Bitmap getBitmap(String str, Boolean bool) {
        try {
            if (bool.booleanValue()) {
                return BitmapFactory.decodeStream(this.form.openAssetForExtension(this, str));
            }
            if (str == "") {
                return null;
            }
            if (str.contains("://")) {
                return BitmapFactory.decodeStream((InputStream) new URL(str).getContent());
            }
            return BitmapFactory.decodeStream(str.contains("/") ? this.context.getContentResolver().openInputStream(Uri.fromFile(new File(str))) : this.container.$form().openAsset(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initChannelNotification(int i, String str) {
        if (this.notifManager == null) {
            this.notifManager = (NotificationManager) this.context.getSystemService("notification");
        }
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = this.notifManager.getNotificationChannel(str);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(str, this.channel, i);
                this.notifManager.createNotificationChannel(notificationChannel);
            }
            notificationChannel.setImportance(i);
            this.builder = new Notification.Builder(this.context, str);
            return;
        }
        this.builder = new Notification.Builder(this.context);
    }

    /* access modifiers changed from: private */
    public void musicNotification(String str, String str2, final String str3, boolean z, boolean z2) {
        initChannelNotification(2, "NotifMusic");
        Bitmap bitmap = getBitmap(this.iconNotification, false);
        if (bitmap != null) {
            this.builder.setSmallIcon(Icon.createWithBitmap(bitmap));
        } else {
            this.builder.setSmallIcon(17301569);
        }
        this.builder.setShowWhen(false);
        this.builder.setContentTitle(str);
        this.builder.setContentText(str2);
        if (Build.VERSION.SDK_INT < 26) {
            this.builder.setPriority(0);
        }
        this.builder.setAutoCancel(z);
        this.builder.setOngoing(!z);
        StartApp("Music");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.activity, 0, new Intent("MUSIC_FAVORITE"), Declaration.PACKAGE_ACCESS);
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this.activity, 0, new Intent("MUSIC_PAUSE"), Declaration.PACKAGE_ACCESS);
        PendingIntent broadcast3 = PendingIntent.getBroadcast(this.activity, 0, new Intent("MUSIC_PLAY"), Declaration.PACKAGE_ACCESS);
        PendingIntent broadcast4 = PendingIntent.getBroadcast(this.activity, 0, new Intent("MUSIC_PREVIOUS"), Declaration.PACKAGE_ACCESS);
        PendingIntent broadcast5 = PendingIntent.getBroadcast(this.activity, 0, new Intent("MUSIC_NEXT"), Declaration.PACKAGE_ACCESS);
        this.builder.setStyle(new Notification.MediaStyle().setShowActionsInCompactView(new int[]{1, 2, 3}).setMediaSession(this.mediaSession.getSessionToken()));
        Bitmap bitmap2 = getBitmap(z2 ? "favorite.png" : "favorite_border.png", true);
        Bitmap bitmap3 = getBitmap("previous.png", true);
        Bitmap bitmap4 = getBitmap(z ? "play.png" : "pause.png", true);
        Bitmap bitmap5 = getBitmap("next.png", true);
        Notification.Action build = new Notification.Action.Builder(Icon.createWithBitmap(bitmap2), "Favorite", broadcast).build();
        Notification.Action build2 = new Notification.Action.Builder(Icon.createWithBitmap(bitmap3), "Previous", broadcast4).build();
        Notification.Action build3 = new Notification.Action.Builder(Icon.createWithBitmap(bitmap4), "Pause", z ? broadcast3 : broadcast2).build();
        Notification.Action build4 = new Notification.Action.Builder(Icon.createWithBitmap(bitmap5), "Next", broadcast5).build();
        this.builder.addAction(build);
        this.builder.addAction(build2);
        this.builder.addAction(build3);
        this.builder.addAction(build4);
        this.builder.setSubText(str2);
        this.builder.setDefaults(-1);
        new Thread(new Runnable() {
            public void run() {
                Bitmap access$1000 = NotificationStyle.this.getBitmap(str3, false);
                if (access$1000 != null) {
                    NotificationStyle.this.builder.setLargeIcon(access$1000);
                }
                NotificationStyle.this.notifManager.notify(33333, NotificationStyle.this.builder.build());
            }
        }).start();
    }

    private void notificationMessage(String str, String str2, String str3, long j) {
        initChannelNotification(4, "NotifMesseg");
        StartApp("Message");
        RemoteInput build = new RemoteInput.Builder("key_text_reply").setLabel("Your answer...").build();
        Intent intent = new Intent("MESSAGE_REPLY");
        intent.putExtra("group", str);
        Notification.Action build2 = new Notification.Action.Builder(Icon.createWithBitmap(getBitmap("reply.png", true)), "Reply", PendingIntent.getBroadcast(this.context, 0, intent, 0)).addRemoteInput(build).build();
        Notification.MessagingStyle messagingStyle = new Notification.MessagingStyle("Me");
        messagingStyle.setConversationTitle(str);
        for (Message next : MESSAGES) {
            messagingStyle.addMessage(new Notification.MessagingStyle.Message(next.getText(), next.getTimestamp(), next.getSender()));
        }
        Bitmap bitmap = getBitmap(this.iconNotification, false);
        if (bitmap != null) {
            this.builder.setSmallIcon(Icon.createWithBitmap(bitmap));
        } else {
            this.builder.setSmallIcon(17301569);
        }
        this.builder.setStyle(messagingStyle);
        this.builder.addAction(build2);
        this.builder.setColor(this.colorNoti);
        if (Build.VERSION.SDK_INT < 26) {
            this.builder.setPriority(1);
        }
        this.builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        this.builder.setAutoCancel(true);
        this.builder.setOnlyAlertOnce(true);
        this.notifManager.notify(44444, this.builder.build());
    }

    private void sendNotification(String str, String str2, boolean z, final String str3, final String str4, String[] strArr, String str5, int i) {
        String str6;
        initChannelNotification(SetPriority(this.importanceChannel, true), "Notif");
        Bitmap bitmap = getBitmap(this.iconNotification, false);
        if (bitmap != null) {
            this.builder.setSmallIcon(Icon.createWithBitmap(bitmap));
        } else {
            this.builder.setSmallIcon(17301569);
        }
        this.builder.setContentTitle(str);
        this.builder.setContentText(str2);
        if (z) {
            this.builder.setStyle(new Notification.BigTextStyle().bigText(str2));
        }
        this.builder.setAutoCancel(true);
        this.builder.setDefaults(-1);
        if (Build.VERSION.SDK_INT < 26) {
            this.builder.setPriority(SetPriority(this.priorityNotification, false));
        }
        this.builder.setColor(this.colorNoti);
        StartApp(str5);
        AnonymousClass3 r5 = new BroadcastReceiver() {
            public synchronized void onReceive(Context context, Intent intent) {
                int intExtra = intent.getIntExtra("notificationId", 0);
                if (intExtra > 0) {
                    String action = intent.getAction();
                    String stringExtra = intent.getStringExtra("url");
                    if (NotificationStyle.this.notifManager != null) {
                        NotificationStyle.this.notifManager.cancel(intExtra);
                    }
                    if (stringExtra.contains("://")) {
                        NotificationStyle.this.container.$context().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringExtra)));
                    }
                    NotificationStyle.this.CallbackButtonAction(action);
                    NotificationStyle.this.activity.unregisterReceiver(this);
                }
            }
        };
        for (String str7 : strArr) {
            String str8 = "";
            if (str7.contains("|")) {
                str6 = str7.substring(0, str7.indexOf("|"));
                str8 = str7.substring(str7.indexOf("|") + 1);
            } else {
                str6 = str7;
            }
            Intent intent = new Intent(str6);
            intent.putExtra("notificationId", i);
            intent.putExtra("url", str8);
            this.builder.addAction(0, str6, PendingIntent.getBroadcast(this.activity, 0, intent, Declaration.PACKAGE_ACCESS));
            this.activity.registerReceiver(r5, new IntentFilter(str6));
        }
        final int i2 = i;
        new Thread(new Runnable() {
            public void run() {
                Bitmap access$1000 = NotificationStyle.this.getBitmap(str4, false);
                if (access$1000 != null) {
                    NotificationStyle.this.builder.setLargeIcon(access$1000);
                }
                Bitmap access$10002 = NotificationStyle.this.getBitmap(str3, false);
                if (access$10002 != null) {
                    NotificationStyle.this.builder.setStyle(new Notification.BigPictureStyle().bigPicture(access$10002));
                }
                NotificationStyle.this.notifManager.notify(i2, NotificationStyle.this.builder.build());
            }
        }).start();
    }

    @SimpleFunction(description = "With this block it is possible to create up to three action buttons in the notification and in addition, it is possible to add as a \"hyperlink\".")
    public void ActionNotification(String str, String str2, boolean z, String str3, String str4, YailList yailList, String str5, int i) {
        sendNotification(str, str2, z, str3, str4, yailList.toStringArray(), str5, i);
    }

    @SimpleFunction(description = "Creates a notification with a big picture and in addition it is possible to add title, subtitle and large icon.")
    public void BigPictureNotification(String str, String str2, String str3, String str4, String str5, int i) {
        sendNotification(str, str2, false, str3, str4, new String[0], str5, i);
    }

    @SimpleEvent(description = "When clicking on any action button, the name of the respective button will be returned by that block.")
    public void CallbackButtonAction(String str) {
        EventDispatcher.dispatchEvent(this, "CallbackButtonAction", str);
    }

    @SimpleEvent(description = "Return of the message typed in the notification.")
    public void CallbackMessage(String str, long j) {
        EventDispatcher.dispatchEvent(this, "CallbackMessage", str, Long.valueOf(j));
    }

    @SimpleEvent(description = "When clicking on any button of the media style notification, the name of the Action is returned in this block.")
    public void CallbackMusicPlayer(String str) {
        EventDispatcher.dispatchEvent(this, "CallbackMusicPlayer", str);
    }

    @SimpleFunction(description = "Cancels all notifications.")
    public void CancelAllNotification() {
        cancelAllNotification();
    }

    @SimpleFunction(description = "Delete MediaStyle notification.")
    public void CancelMusicNotification() {
        cancelNotification(33333);
    }

    @SimpleFunction(description = "Cancels a specific message.")
    public void CancelNotification(int i) {
        cancelNotification(i);
    }

    @SimpleProperty(description = "Get channnel name.")
    public String Channel() {
        return this.channel;
    }

    @DesignerProperty(defaultValue = "ChannelA", editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Starting in Android 8.0 (API level 26), all notifications must be assigned to a channel.")
    public void Channel(String str) {
        this.channel = str;
    }

    @SimpleFunction(description = "When calling the MessageReceiverNotification method, it stores the added messages and to remove, use this method.")
    public void ClearAllMessage() {
        MESSAGES = new ArrayList();
    }

    @SimpleProperty(description = "Get color notification.")
    public int ColorNotification() {
        return this.colorNoti;
    }

    @DesignerProperty(defaultValue = "&HFF000000", editorType = "color")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set priority notification.")
    public void ColorNotification(int i) {
        this.colorNoti = i;
    }

    @SimpleFunction(description = "If the user responds to the message in the notification, it will show a loading icon until that block is used.")
    public void ConfirmSendingMessage() {
        notificationMessage(this.group, this.message, this.sender, this.timestamp);
    }

    @SimpleFunction(description = "Gets whether the song is marked as a favorite.")
    public boolean GetFavorite() {
        return this.favorite;
    }

    @SimpleProperty(description = "Get path Icon assets.")
    public String IconNotification() {
        return this.iconNotification;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set Icon notification.")
    public void IconNotification(String str) {
        this.iconNotification = str;
    }

    @SimpleProperty(description = "Get priority channel.")
    public int ImportanceChannel() {
        return this.importanceChannel;
    }

    @DesignerProperty(defaultValue = "2", editorType = "integer")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set priority channel.")
    public void ImportanceChannel(int i) {
        this.importanceChannel = i;
    }

    @SimpleFunction(description = "Displays a large icon in the notification with title and subtitle.")
    public void LargeIconNotification(String str, String str2, boolean z, String str3, String str4, int i) {
        sendNotification(str, str2, z, "", str3, new String[0], str4, i);
    }

    @SimpleFunction(description = "Displays notification of MediaStyle with play button.")
    public void PauseMusicNotification() {
        this.pause = true;
        musicNotification(this.title, this.subtitle, this.largeIcon, this.pause, this.favorite);
    }

    @SimpleFunction(description = "Displays notification of MediaStyle with pause button.")
    public void PlayMusicNotification() {
        this.pause = false;
        musicNotification(this.title, this.subtitle, this.largeIcon, this.pause, this.favorite);
    }

    @SimpleProperty(description = "Get priority notification.")
    public int PriorityNotification() {
        return this.priorityNotification;
    }

    @DesignerProperty(defaultValue = "2", editorType = "integer")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set priority notification.")
    public void PriorityNotification(int i) {
        this.priorityNotification = i;
    }

    @SimpleFunction(description = "Through this block, you can show a notification of a received message with the ability to reply in the bar itself.")
    public void ReceiverMessageNotification(String str, String str2, String str3, long j) {
        MESSAGES.add(new Message(str2, str3 == "" ? null : str3, j));
        notificationMessage(str, str2, str3, j);
    }

    @SimpleFunction(description = "Starts the MediaStyle notification variables.")
    public void SetupMusicNotification(String str, String str2, String str3, boolean z) {
        this.title = str;
        this.subtitle = str2;
        this.largeIcon = str3;
        this.favorite = z;
    }

    @SimpleFunction(description = "Creates a simple notification with title and subtitle, capable of displaying large texts in the subtitle.")
    public void SimpleNotification(String str, String str2, boolean z, String str3, int i) {
        sendNotification(str, str2, z, "", "", new String[0], str3, i);
    }

    public void onDestroy() {
        if (this.notifManager == null) {
            this.notifManager.cancel(33333);
        }
    }
}
