package com.example.kesar.testapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by kesar on 11/1/2017.
 */

public class InstantDownloadingFiles {

    private static final String TAG = "Download Task";
    private Context context;
    private TextView mEnd;
    private TextView mStartSave;
    private TextView mEndSave;
    private String downloadUrl = "";
    private String downloadFileName = "";

    private String uploadUrl = "http://10.0.2.2/TestApp/updateinfo.php";

    public InstantDownloadingFiles(Context context, String downloadUrl, TextView mEnd, TextView mStartSave, TextView mEndSave) {

        this.context = context;
        this.downloadUrl = downloadUrl;
        this.mEnd = mEnd;
        this.mStartSave = mStartSave;
        this.mEndSave = mEndSave;

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

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                if (outputFile != null) {

                    Long secondTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStampSecond = getDateCurrentTimeZone(secondTimeStampLong);
                    mEnd.setText(timeStampSecond);

                    Toast.makeText(context, "Files Downloaded Successfully", Toast.LENGTH_LONG).show();

                    Long startTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStartStamp = getDateCurrentTimeZone(startTimeStampLong);
                    mStartSave.setText(timeStartStamp);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            uploadFile();

                        }
                    }, 1419);

                } else {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(context, "Downloading Failed", Toast.LENGTH_LONG).show();

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

                    }
                }, 3000);

                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }

            super.onPostExecute(aVoid);

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

        private void uploadFile() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String Response = jsonObject.getString("response");

                        Long endTimeStampLong = System.currentTimeMillis()/1000;
                        String timeStampSecond = getDateCurrentTimeZone(endTimeStampLong);
                        mEndSave.setText(timeStampSecond);

                        Toast.makeText(context, "Files Uploaded on Database", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, Response, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("name", downloadFileName.trim());
                    params.put("file", String.valueOf(outputFile));

                    return params;
                }
            };

            MySingleton.getInstance(context).addToRequestQue(stringRequest);
            Long endTimeStampLong = System.currentTimeMillis()/1000;
            String timeStampSecond = getDateCurrentTimeZone(endTimeStampLong);
            mEndSave.setText(timeStampSecond);

            Toast.makeText(context, "Files Uploaded on Database", Toast.LENGTH_LONG).show();

        }

        public String getDateCurrentTimeZone(long timestamp) {

            try{

                Calendar calendar = Calendar.getInstance();
                TimeZone timeZone = TimeZone.getDefault();
                calendar.setTimeInMillis(timestamp * 1000);
                calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.getTimeInMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date currenTimeZone = (Date) calendar.getTime();
                return sdf.format(currenTimeZone);

            }catch (Exception e) {

            }
            return "";

        }
    }

}

