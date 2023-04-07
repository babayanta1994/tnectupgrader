package ru.trueip.tnectupgrader.app.main_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.trueip.tnectupgrader.R;
import ru.trueip.tnectupgrader.api.HLMApi;
import ru.trueip.tnectupgrader.base.BasePresenter;
import ru.trueip.tnectupgrader.models.Apps;
import ru.trueip.tnectupgrader.models.responses.UpdatesModel;
import ru.trueip.tnectupgrader.repository.UnsafeOkHttpClient;
import ru.trueip.tnectupgrader.utils.Logger;

import static ru.trueip.tnectupgrader.utils.Constants.BUNDLE_UPGRADE_API;
import static ru.trueip.tnectupgrader.utils.Constants.BUNDLE_UPGRADE_OBJECTS;
import static ru.trueip.tnectupgrader.utils.Constants.BUNDLE_UPGRADE_SERVER_URL;
import static ru.trueip.tnectupgrader.utils.Constants.BUNDLE_UPGRADE_TOKEN;

public class MainPresenter extends BasePresenter<MainContract> {

    public static final String TAG = MainPresenter.class.getSimpleName();
    public ObservableField<AppsAdapter> appsAdapter = new ObservableField<>();

    public ObservableInt progressB = new ObservableInt();
    public ObservableBoolean isUpdate = new ObservableBoolean(false);
    public ObservableField<String> progressText = new ObservableField<>();

    ArrayList<UpdatesModel> updatesModels = new ArrayList<>();

    private Context context;
    String nameApk="update.apk";
    String token="";
    String serverUrl= "";
    String upgradeApi= "";
    public void setExtras(Bundle bundle){
        String jsonUpdate = bundle.getString(BUNDLE_UPGRADE_OBJECTS);
        token = bundle.getString(BUNDLE_UPGRADE_TOKEN);
        serverUrl = bundle.getString(BUNDLE_UPGRADE_SERVER_URL);
        upgradeApi = bundle.getString(BUNDLE_UPGRADE_API);

        Logger.error(TAG,">>>>>>JSON "+jsonUpdate);
        Logger.error(TAG,">>>>>>token "+token);
        Logger.error(TAG,">>>>>>serverUrl "+serverUrl);
        Logger.error(TAG,">>>>>>upgradeApi "+upgradeApi);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UpdatesModel>>(){}.getType();
        updatesModels = gson.fromJson(jsonUpdate, type);

        Logger.error(TAG,">>"+updatesModels.size());
    }




    public void setContext(Context context) {
        this.context = context;
    }


    public void onTopBtnClick(View view) {
        //view.getContext().getApplicationContext().finish();
        if(!isUpdate.get()){
            ((MainActivity)(context)).finish();
        }
    }


    private String createFullUrl(String baseUrl, String endpoint) {
        if (baseUrl != null && !baseUrl.isEmpty()) {
            return StringUtils.stripEnd(baseUrl, "/") + "/" + StringUtils.stripStart(endpoint, "/");
        }

        return StringUtils.stripEnd("https://trueip2.smartru.com/", "/") + "/" + StringUtils.stripStart(endpoint, "/");
    }

    DownloadZipFileTask downloadZipFileTask=null;
    private void downloadFile(String token, String baseurl,String fileurl,String name){
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        HLMApi hlmApi = retrofit.create(HLMApi.class);
        String burl=createFullUrl(baseurl,fileurl);
        Call<ResponseBody> call = hlmApi.downloadFileWithDynamicUrlSync(token,burl+"?name="+name);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Logger.error(TAG,">>>>>>>>>>success ");
                    downloadZipFileTask = new DownloadZipFileTask();
                    downloadZipFileTask.execute(response.body());

                    Logger.error(TAG,">>>>>>>>>>success ");
                   // Log.d("File download was a success? ", String.valueOf(writtenToDisk));
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.error(TAG,">>>>>>>>>>fail");
            }
        });
    }




    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], nameApk);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {



            if (progress[0].first == 100){

                Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_SHORT).show();
            }



            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                progressB.set(currentProgress);
                progressText.set(currentProgress + "%");

            }

            if (progress[0].first == -1) {
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.setEditMode(false);
            isUpdate.set(false);
        }
    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    private void saveToDisk(ResponseBody body, String filename) {
        try {

            String destination = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath()+"/"+filename;
           // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename
            File destinationFile = new File(destination);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                   // Log.d(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(Uri.parse("file://" + destination),getMimeType(destination));
                context.startActivity(install);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }



    AppsAdapter adapter;

    @SuppressLint("CheckResult")
    public void setAppsAdapter() {
        List<Apps> apps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        for(int i=0;i<updatesModels.size();i++){
            String version="-";
            try {
                version = packageManager.getPackageInfo(updatesModels.get(i).getBundleid(), 0).versionName;
            } catch (Exception e) { }

            apps.add(new Apps(updatesModels.get(i).getName(),"",updatesModels.get(i).getBundleid(),version,updatesModels.get(i).getVersion(),updatesModels.get(i).getUpdate_url(),updatesModels.get(i).getDescription()));
          //  apps.add(new Apps("GsLabs","default","com.gslabs.ru","1.0.11sd2","1.0.112","gslabs.apk"));
        }


        adapter = new AppsAdapter(R.layout.app_view,apps);
        adapter.addOnItemClickListener((position, item) -> {

            adapter.setEditMode(true);
            isUpdate.set(true);

            new AsyncTask<Void, Void, List<String>>() {
                @Override
                protected List<String> doInBackground(Void... params) {
                    downloadFile(token,serverUrl,upgradeApi,item.getUpdateUrl());
                    return null;
                }

            }.execute();
        });
        appsAdapter.set(adapter);
    }



}
