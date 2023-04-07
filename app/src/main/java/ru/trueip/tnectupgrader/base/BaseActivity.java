package ru.trueip.tnectupgrader.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import ru.trueip.tnectupgrader.BR;
import ru.trueip.tnectupgrader.app.App;
import ru.trueip.tnectupgrader.app.main_screen.MainActivity;
import ru.trueip.tnectupgrader.utils.Constants;

/**
 * Created by user on 07-Sep-17.
 *
 */

public abstract class BaseActivity<C extends BaseContract, P extends BasePresenter, B extends ViewDataBinding> extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    public P presenter = null;
    public B binding = null;
    protected BaseRouter router = null;
    private BroadcastReceiver broadcastReceiver = null;
    private BroadcastReceiver localeChangedReceiver = null;
    protected boolean processHomeButtonClick = false;

    private int currentApiVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }



        this.router = createRouter();
        this.binding = initBinding();
        this.presenter = createPresenter();
        this.binding.setVariable(BR.presenter, this.presenter);
        setContentView(binding.getRoot());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processBroadCast();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.FINISH_ACTIVITY);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public abstract B initBinding();

    public abstract C getContract();

    public abstract P createPresenter();

    public abstract BaseRouter createRouter();

    public void setTitle(String title) {     }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachToView(getContract());
        if (!(this instanceof MainActivity)) {
            if (App.getStartedActivityNumber() == 0) {
               // presenter.checkServerStatus(this);
            }
            App.incrementStartedActivityNumber();

            if (App.isLocaleChanged()) {
               // presenter.setLocale(BaseActivity.this);
                App.setLocaleChanged(false);
            }
            registerLocaleChangedReceiver();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
        if (!(this instanceof MainActivity)) {
            unregisterLocaleChangedReceiver();
            App.decrementStartedActivityNumber();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter = null;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    public void onBackPressed() {
        router.moveBackward();
    }

    protected void processBroadCast() {
        if ( processHomeButtonClick )
            finish();
    }

    protected void homeButtonClicked() {
        Intent intent = new Intent();
        intent.setAction(Constants.FINISH_ACTIVITY);
        sendBroadcast(intent);
    }

    private void registerLocaleChangedReceiver() {
        localeChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action != null && action.equals(Constants.ACTION_NOTIFY_LOCALE_CHANGED)) {
                    //presenter.setLocale(BaseActivity.this);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_NOTIFY_LOCALE_CHANGED);
        registerReceiver(localeChangedReceiver, intentFilter);
    }

    private void unregisterLocaleChangedReceiver() {
        if (localeChangedReceiver != null) {
            unregisterReceiver(localeChangedReceiver);
        }
    }

}
