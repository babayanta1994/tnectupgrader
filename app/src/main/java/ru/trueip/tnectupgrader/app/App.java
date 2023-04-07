package ru.trueip.tnectupgrader.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;

import ru.trueip.tnectupgrader.di.DaggerMainComponent;
import ru.trueip.tnectupgrader.di.MainComponent;
import ru.trueip.tnectupgrader.di.modules.ContextModule;


public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static MainComponent mainComponent;
    private static Application application;
    private static Context context;
    private static boolean localeChanged;
    private static int activityNumber;

    public static MainComponent getMainComponent() {
        return mainComponent;
    }

    public static boolean isLocaleChanged() {
        return localeChanged;
    }

    public static void setLocaleChanged(boolean localeChanged) {
        App.localeChanged = localeChanged;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        mainComponent = DaggerMainComponent.builder()
                .contextModule(new ContextModule(application = this))
                .build();

    }

    public static String getStringRes(@StringRes int stringId) {
        return application.getString(stringId);
    }

    public static Context getContext() { return context; }


    public static void incrementStartedActivityNumber() {
        activityNumber++;
    }

    public static void decrementStartedActivityNumber() {
        activityNumber--;
    }

    public static int getStartedActivityNumber() {
        return activityNumber;
    }
}
