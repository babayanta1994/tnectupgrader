package ru.trueip.tnectupgrader.base.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.trueip.tnectupgrader.app.App;

/**
 * Created by ektitarev on 12.09.2018.
 */

public class BaseReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        App.getMainComponent().inject(this);
    }
}
