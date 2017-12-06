package edu.cmps121.app.utilities;

import android.os.Handler;
import android.os.Message;

import edu.cmps121.app.activities.MapsActivity;

public class ThreadHandler extends Handler {
    private MapsActivity activity;

    public ThreadHandler(MapsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        activity.processBundle(msg.getData());
    }
}
