package com.example.imagesaver.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.imagesaver.R;
import com.example.imagesaver.Util.ImageSaverPreferences;

public class HomePageActivity extends AppCompatActivity {


    private static final String TAG = HomePageActivity.class.getSimpleName();

    private String mName, mLocation;
    private EditText mETName, mETLocation;
    private Toolbar mToolbar;
    private TextInputLayout nameLayout;
    private TextInputLayout locationLayout;
    private TextView termsAndCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setupToolbar();
        mToolbar = findViewById(R.id.toolbar);
        initViews();
    }

    private void initViews() {

        mETName = findViewById(R.id.enter_name);
        mETLocation = findViewById(R.id.enter_location);
        nameLayout = findViewById(R.id.layout_enter_name);
        locationLayout = findViewById(R.id.layout_enter_location);
        termsAndCondition = findViewById(R.id.termsCondition);

        TextView submit = findViewById(R.id.submit);
        initCredentials();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidCredentials(mETName.getText().toString().trim(), mETLocation.getText().toString().trim())) {
                    ImageSaverPreferences.setPreferencesNameLocation(HomePageActivity.this, mETName.getText().toString().trim(), mETLocation.getText().toString().trim());
                    startActivity(new Intent(HomePageActivity.this, SupplierActivity.class));
                }
            }
        });

        termsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectUsingCustomTab("https://github.com/wasim1201");
            }
        });

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Welcome to Trade Show");
        }
    }


    private void redirectUsingCustomTab(String url) {
        Uri uri = Uri.parse(url);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(this, uri);
    }


    private void initCredentials() {
        getPrefNameLocation();

        if (!TextUtils.isEmpty(mName) && mName != null && !TextUtils.isEmpty(mLocation) && mLocation != null) {
            mETName.setText(mName);
            mETLocation.setText(mLocation);
        }
    }

    private void getPrefNameLocation() {
        mName = ImageSaverPreferences.getPreferencesName(this);
        mLocation = ImageSaverPreferences.getPreferenceLocation(this);
        Log.d(TAG, "getPrefNameLocation: " + mName + " - " + mLocation);
    }

    private boolean isValidCredentials(String name, String location) {
        if (TextUtils.isEmpty(name)) {
            nameLayout.setErrorEnabled(true);
            nameLayout.setError("Enter the name");
        } else {
            nameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(location)) {
            locationLayout.setErrorEnabled(true);
            locationLayout.setError("Enter the location");
        } else {
            locationLayout.setErrorEnabled(false);
        }

        return !nameLayout.isErrorEnabled() && !locationLayout.isErrorEnabled();

    }

}
