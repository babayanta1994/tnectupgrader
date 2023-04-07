package ru.trueip.tnectupgrader.app.main_screen;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import ru.trueip.tnectupgrader.R;
import ru.trueip.tnectupgrader.base.BaseActivity;
import ru.trueip.tnectupgrader.base.BaseRouter;
import ru.trueip.tnectupgrader.databinding.ActivityMainBinding;
import ru.trueip.tnectupgrader.utils.Logger;


public class MainActivity extends BaseActivity<MainContract, MainPresenter, ActivityMainBinding> implements MainContract {

    private final static int PERMISSIONS_REQUEST_RECORD_AUDIO = 1778;
    private final int SCROLL_MARGIN = 10;

    private Handler handler;

    @Override
    public ActivityMainBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setContext(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            presenter.setExtras(bundle);
        }


    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == 101)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                presenter.setAppsAdapter();
                return null;
            }

            @Override
            protected void onPreExecute() {
                // before executing background task. Like showing a progress dialog
            }


        }.execute(null, null, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public MainContract getContract() {
        return this;
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new MainRouter(this);
    }

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.error(TAG,">>>ON STOP");
        try{
            finish();
        }catch (Exception e){}
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.error(TAG,">>>ON PAUSE");
    }

    @Override
    public BaseRouter getRouter() {
        return router;
    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}