package com.example.kesar.testapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;

    private Button mFirstUrl;
    private Button mSecondUrl;
    private Button mThirdUrl;
    private Button mFourthUrl;

    private TextView mStart, mStartSecond, mStartThird, mStartFourth;
    private TextView mEnd, mEndSecond, mEndThird, mEndFourth;
    private TextView mStartSave, mStartSaveSecond, mStartSaveThird ,mStartSaveFourth;
    private TextView mEndSave, mEndSaveSecond, mEndSaveThird ,mEndSaveFourth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Test App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mFirstUrl = (Button) findViewById(R.id.main_first_url);
        mSecondUrl = (Button) findViewById(R.id.main_second_url);
        mThirdUrl = (Button) findViewById(R.id.main_third_url);
        mFourthUrl = (Button) findViewById(R.id.main_fourth_url);

        mStart = (TextView) findViewById(R.id.card_start);
        mEnd = (TextView) findViewById(R.id.card_end);
        mStartSave = (TextView) findViewById(R.id.card_start_save);
        mEndSave = (TextView) findViewById(R.id.card_end_save);

        mStartSecond = (TextView) findViewById(R.id.card_start_second);
        mEndSecond = (TextView) findViewById(R.id.card_end_second);
        mStartSaveSecond = (TextView) findViewById(R.id.card_start_save_second);
        mEndSaveSecond = (TextView) findViewById(R.id.card_end_save_second);

        mStartThird = (TextView) findViewById(R.id.card_start_third);
        mEndThird = (TextView) findViewById(R.id.card_end_third);
        mStartSaveThird = (TextView) findViewById(R.id.card_start_save_third);
        mEndSaveThird = (TextView) findViewById(R.id.card_end_save_third);

        mStartFourth = (TextView) findViewById(R.id.card_start_fourth);
        mEndFourth = (TextView) findViewById(R.id.card_end_fourth);
        mStartSaveFourth = (TextView) findViewById(R.id.card_start_save_fourth);
        mEndSaveFourth = (TextView) findViewById(R.id.card_end_save_fourth);

        setListeners();

    }

    private void setListeners() {

        mFirstUrl.setOnClickListener(this);
        mSecondUrl.setOnClickListener(this);
        mThirdUrl.setOnClickListener(this);
        mFourthUrl.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.main_first_url:
                if (isInternetAvailable()) {

                    Long firstTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStampFirst = getDateCurrentTimeZone(firstTimeStampLong);
                    mStart.setText(timeStampFirst);
                    new DownloadFiles(MainActivity.this, mFirstUrl, Utils.firstURL, mEnd, mStartSave, mEndSave);

                } else {

                    Toast.makeText(MainActivity.this, "There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.main_second_url:
                if (isInternetAvailable()) {

                    Long secondTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStampSecond = getDateCurrentTimeZone(secondTimeStampLong);
                    mStartSecond.setText(timeStampSecond);
                    new DownloadFiles(MainActivity.this, mSecondUrl, Utils.secondURL, mEndSecond, mStartSaveSecond, mEndSaveSecond);

                } else {

                    Toast.makeText(MainActivity.this, "There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.main_third_url:
                if (isInternetAvailable()) {

                    Long thirdTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStampThird = getDateCurrentTimeZone(thirdTimeStampLong);
                    mStartThird.setText(timeStampThird);
                    new DownloadFiles(MainActivity.this, mThirdUrl, Utils.thirdURL, mEndThird, mStartSaveThird, mEndSaveThird);

                } else {

                    Toast.makeText(MainActivity.this, "There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.main_fourth_url:
                if (isInternetAvailable()) {

                    Long fourthTimeStampLong = System.currentTimeMillis()/1000;
                    String timeStampFourth = getDateCurrentTimeZone(fourthTimeStampLong);
                    mStartFourth.setText(timeStampFourth);
                    new DownloadFiles(MainActivity.this, mFourthUrl, Utils.fourthURL, mEndFourth, mStartSaveFourth, mEndSaveFourth);

                } else {

                    Toast.makeText(MainActivity.this, "There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

                }
                break;

        }

    }

    //Check for Internet Connectivity
    private boolean isInternetAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            return true;

        } else {

            return false;

        }
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

}
