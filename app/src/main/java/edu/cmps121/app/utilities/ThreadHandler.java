package edu.cmps121.app.utilities;

import android.os.Handler;
import android.os.Message;

import edu.cmps121.app.activities.MapsOverlayActivity;

public class ThreadHandler extends Handler {
    MapsOverlayActivity activity;

    public ThreadHandler(MapsOverlayActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        activity.processBundle(msg.getData());
    }
}
