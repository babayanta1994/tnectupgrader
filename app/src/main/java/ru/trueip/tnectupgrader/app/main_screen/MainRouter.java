package ru.trueip.tnectupgrader.app.main_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.trueip.tnectupgrader.base.BaseRouter;

/**
 * Created by user on 07-Sep-17.
 */

public class MainRouter extends BaseRouter {

    private Activity activity;

    public MainRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(BaseRouter.Destination dest, Bundle bundle) {
        switch (dest) {
            case MAIN_SCREEN:
                MainActivity.start(activity, bundle);
                activity.finish();
                break;
        }
    }
}
