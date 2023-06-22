package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.google.appinventor.components.runtime.CloudDB;
import java.util.List;
import org.json.JSONException;
import redis.clients.jedis.JedisPubSub;

public class CloudDBJedisListener extends JedisPubSub {
    private static final boolean DEBUG = false;
    private static String LOG_TAG = "CloudDB";
    public CloudDB cloudDB;
    private Thread myThread = Thread.currentThread();

    public CloudDBJedisListener(CloudDB thisCloudDB) {
        this.cloudDB = thisCloudDB;
    }

    public void onSubscribe(String channel, int subscribedChannels) {
    }

    public void onMessage(String channel, String message) {
        try {
            List list = (List) JsonUtil.getObjectFromJson(message, false);
            String tag = (String) list.get(0);
            for (Object value : (List) list.get(1)) {
                String retValue = JsonUtil.getJsonRepresentationIfValueFileName(this.cloudDB.getForm(), value);
                if (retValue == null) {
                    this.cloudDB.DataChanged(tag, value);
                } else {
                    this.cloudDB.DataChanged(tag, retValue);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "onMessage: JSONException", e);
            this.cloudDB.CloudDBError("System Error: " + e.getMessage());
        }
    }

    public void terminate() {
        this.myThread.interrupt();
    }
}
