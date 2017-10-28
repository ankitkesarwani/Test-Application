package com.example.kesar.testapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kesar on 10/28/2017.
 */

public class DownloadFiles {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private TextView mEnd;
    private String downloadUrl = "";
    private String downloadFileName = "";

    public DownloadFiles(Context context, Button buttonText, String downloadUrl, TextView mEnd) {

        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;
        this.mEnd = mEnd;

        downloadFileName = downloadUrl.replace(Utils.mainUrl, "");
        Log.e(TAG, downloadFileName);

        new DownloadingTask().execute();

    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            buttonText.setEnabled(false);
            buttonText.setText(R.string.downloadStarted);

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                if (outputFile != null) {

                    buttonText.setEnabled(true);
                    buttonText.setText(R.string.downloadCompleted);

                    Long secondTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStampSecond = getDateCurrentTimeZone(secondTimeStampLong);
                    mEnd.setText(timeStampSecond);

                } else {

                    buttonText.setText(R.string.downloadFailed);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            buttonText.setEnabled(true);
                            buttonText.setText(R.string.downloadAgain);

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {

                e.printStackTrace();

                //buttonText.setText("First Url");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        buttonText.setEnabled(true);
                        buttonText.setText(R.string.downloadAgain);

                    }
                }, 3000);

                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }

            super.onPostExecute(aVoid);

        }

        public String getDateCurrentTimeZone(long timestamp) {

            try{

                Calendar calendar = Calendar.getInstance();
                TimeZone timeZone = TimeZone.getDefault();
                calendar.setTimeInMillis(timestamp * 1000);
                calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.getTimeInMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currenTimeZone = (Date) calendar.getTime();
                return sdf.format(currenTimeZone);

            }catch (Exception e) {

            }
            return "";

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL(downloadUrl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();

                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {

                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());

                }

                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + Utils.downloadDirectory);

                } else {

                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                }

                if (!apkStorage.exists()) {

                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");

                }

                outputFile = new File(apkStorage, downloadFileName);

                if (!outputFile.exists()) {

                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");

                }

                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;

                while ((len1 = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, len1);

                }

                fos.close();
                is.close();

            } catch (Exception e) {

                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());

            }

            return null;
        }
    }
}
