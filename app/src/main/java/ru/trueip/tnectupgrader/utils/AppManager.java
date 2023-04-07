package ru.trueip.tnectupgrader.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

import ru.trueip.tnectupgrader.models.AppInfoModel;

//            com.akuvox.file_explorer
//            com.akuvox.browser
//            com.android.browser
//            com.android.camera2
//            com.android.gallery3d
//            com.android.settings
//            com.android.deskclock
//            com.android.wallpaper
//            com.android.wallpaper.livepicker
public class AppManager {
    private final PackageManager packageManager;
    public AppManager(Context context){
        packageManager = context.getPackageManager();
    }

    public List<AppInfoModel> getInstallPackages(){
        List<AppInfoModel> appInfoModels = new ArrayList<>();
        List<PackageInfo> installPackages = packageManager.getInstalledPackages(0);


        for (PackageInfo installPackage:installPackages) {
            if((installPackage.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0||
                installPackage.packageName.contains("com.akuvox.file_explorer")||
                installPackage.packageName.contains("com.android.browser")||
                installPackage.packageName.contains("com.android.camera2")||
                installPackage.packageName.contains("com.android.gallery3d")||
                installPackage.packageName.contains("com.android.deskclock")||
                installPackage.packageName.contains("com.android.settings"))
            {
                AppInfoModel appInfoModel =
                        new AppInfoModel(
                                installPackage.packageName,
                                installPackage.versionCode,
                                installPackage.versionName,
                                installPackage.applicationInfo.loadLabel(packageManager).toString(),
                                installPackage.applicationInfo.loadIcon(packageManager)
                        );
                appInfoModels.add(appInfoModel);
            }
        }
        return appInfoModels;
    }
    public List<AppInfoModel> getInstallActivities(){
        List<AppInfoModel> apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){

            AppInfoModel app = new AppInfoModel(
                    ri.activityInfo.packageName,
                    1,
                    (String)ri.loadLabel(packageManager),
                    (String)ri.loadLabel(packageManager),
                    ri.activityInfo.loadIcon(packageManager)


            );
            apps.add(app);
        }

        return apps;
    }

}
