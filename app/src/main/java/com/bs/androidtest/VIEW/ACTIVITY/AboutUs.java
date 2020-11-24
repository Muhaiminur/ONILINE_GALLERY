package com.bs.androidtest.VIEW.ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bs.androidtest.LIBRARY.Utility;
import com.bs.androidtest.R;
import com.bs.androidtest.databinding.ActivityAboutUsBinding;

public class AboutUs extends AppCompatActivity {

    Context context;
    Utility utility;
    ActivityAboutUsBinding aboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        aboutUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        try {
            context = AboutUs.this;
            utility = new Utility(context);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(context.getResources().getString(R.string.action_us));
            aboutUsBinding.meGit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_link(context.getResources().getString(R.string.github_string));
                }
            });
            aboutUsBinding.meStack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_link(context.getResources().getString(R.string.stackoverflow_string));
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    //open link to browser
    public void open_link(String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
            try {
                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } catch (Exception e2) {
                Log.d("Error Line Number", Log.getStackTraceString(e2));
                utility.showDialog(context.getResources().getString(R.string.no_browser_string));
            }
        }
    }
}