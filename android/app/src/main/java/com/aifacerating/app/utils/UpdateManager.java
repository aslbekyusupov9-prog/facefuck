package com.aifacerating.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.aifacerating.app.network.ApiClient;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {

    private static final String TAG = "UpdateManager";
    // Remote Version Check JSON URL hosted on GitHub raw main
    private static final String VERSION_CHECK_URL = "https://raw.githubusercontent.com/aslbekyusupov9-prog/facefuck/main/version.json";

    public static void checkForUpdates(final Activity activity, final boolean showToastIfLatest) {
        new Thread(() -> {
            try {
                URL url = new URL(VERSION_CHECK_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";
                    in.close();

                    JSONObject json = new JSONObject(result);
                    int remoteVersionCode = json.optInt("versionCode", 1);
                    String remoteVersionName = json.optString("versionName", "1.0.0");
                    String apkUrl = json.optString("apkUrl", "");
                    String changelog = json.optString("changelog", "Yangi xususiyatlar va tuzatishlar.");

                    PackageManager pm = activity.getPackageManager();
                    PackageInfo pInfo = pm.getPackageInfo(activity.getPackageName(), 0);
                    int currentVersionCode = pInfo.versionCode;

                    Log.d(TAG, "Current VersionCode: " + currentVersionCode + ", Remote VersionCode: " + remoteVersionCode);

                    if (remoteVersionCode > currentVersionCode && !apkUrl.isEmpty()) {
                        new Handler(Looper.getMainLooper()).post(() -> 
                            showUpdateDialog(activity, remoteVersionName, apkUrl, changelog)
                        );
                    } else if (showToastIfLatest) {
                        new Handler(Looper.getMainLooper()).post(() -> 
                            Toast.makeText(activity, "Sizda eng so'nggi versiya o'rnatilgan!", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Update check failed", e);
            }
        }).start();
    }

    private static void showUpdateDialog(final Activity activity, String versionName, String apkUrl, String changelog) {
        if (activity.isFinishing()) return;

        new AlertDialog.Builder(activity)
            .setTitle("🚀 Yangi Versiya Mavjud! (v" + versionName + ")")
            .setMessage("AI Face Rating ilovasining yangi versiyasi tayyor.\n\nYangiliklar:\n" + changelog + "\n\nHozir to'g'ridan-to'g'ri yangilaysizmi?")
            .setPositiveButton("Yangilash (Update)", (dialog, which) -> downloadAndInstallApk(activity, apkUrl))
            .setNegativeButton("Keyinroq", null)
            .setCancelable(false)
            .show();
    }

    private static void downloadAndInstallApk(final Activity activity, String apkUrl) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Yuklab olinmoqda...");
        progressDialog.setMessage("Yangi APK versiyasi yuklanmoqda, iltimos kuting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.connect();

                int fileLength = conn.getContentLength();
                File apkFile = new File(activity.getExternalFilesDir(null), "app-update.apk");
                if (apkFile.exists()) apkFile.delete();

                InputStream input = conn.getInputStream();
                FileOutputStream output = new FileOutputStream(apkFile);

                byte[] data = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    if (fileLength > 0) {
                        int progress = (int) (total * 100 / fileLength);
                        new Handler(Looper.getMainLooper()).post(() -> progressDialog.setProgress(progress));
                    }
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                new Handler(Looper.getMainLooper()).post(() -> {
                    progressDialog.dismiss();
                    installApk(activity, apkFile);
                });

            } catch (Exception e) {
                Log.e(TAG, "APK download failed", e);
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "APK yuklashda xatolik: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private static void installApk(Context context, File apkFile) {
        if (!apkFile.exists()) return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(apkFile);
        }

        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
