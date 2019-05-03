package com.example.imagesaver.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagesaver.Dialog.ListDialogFragment;
import com.example.imagesaver.R;
import com.example.imagesaver.Util.ImageSaverLab;
import com.example.imagesaver.Util.ImageSaverPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.imagesaver.Util.Constants.TYPE_SUPPLIER;

public class SupplierActivity extends AppCompatActivity implements ListDialogFragment.SelectedItem {

    private static final String TAG = SupplierActivity.class.getSimpleName();
    private Toolbar mToolbar;

    private TextInputLayout mSupplierLayout;
    private EditText mSupplierName;
    private TextView mNext, mSearchSupplier;
    private ArrayList<String> mSupplierList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        initViews();
        setupToolbar();
        fetchSupplierNameFromPref();
        fetchSupplierListFromPref();
        setupClickListeners();

    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        mSupplierLayout = findViewById(R.id.layout_supplier_name);
        mSupplierName = findViewById(R.id.supplier_name);
        mNext = findViewById(R.id.next);
        mSearchSupplier = findViewById(R.id.search_supplier);

    }

    private void setupClickListeners() {
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidSupplierName(mSupplierName.getText().toString().trim())){
                    ImageSaverPreferences.setPrefSupplierName(SupplierActivity.this, mSupplierName.getText().toString().trim());
                    updateSupplierList(mSupplierName.getText().toString().trim());
                    startActivity(new Intent(SupplierActivity.this, SupplierDetailsActivity.class));
                }
            }
        });

        mSearchSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSupplierList != null && mSupplierList.size() > 0){
                    DialogFragment newFragment = ListDialogFragment.newInstance(TYPE_SUPPLIER, mSupplierList);
                    newFragment.show(getSupportFragmentManager(), "suppliers");
                }
                else {
                    Toast.makeText(SupplierActivity.this, "No records found!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isValidSupplierName(String name) {
        if(TextUtils.isEmpty(name)){
            mSupplierLayout.setErrorEnabled(true);
            mSupplierLayout.setError("Enter the name");
        }else{
            mSupplierLayout.setErrorEnabled(false);
        }

        return !mSupplierLayout.isErrorEnabled();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Supplier Name");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void fetchSupplierNameFromPref(){
        String name= ImageSaverPreferences.getPrefSupplierName(this);
        Log.d(TAG, "fetchSupplierNameFromPref: " + name);

        if(! TextUtils.isEmpty(name) && name != null){
            mSupplierName.setText(name);
        }
    }

    private void fetchSupplierListFromPref() {
        String name = ImageSaverPreferences.getPrefSupplierList(this);
        Gson gson = new Gson();

        if(!TextUtils.isEmpty(name)) {
            mSupplierList = gson.fromJson(name, ArrayList.class);
        }

        Log.d(TAG, "fetchSupplierListFromPref: " + name);

    }


    private void updateSupplierList(String name) {
        if(!mSupplierList.contains(name)){
            mSupplierList.add(name);
            Log.d(TAG, "SupplierList: " + ImageSaverLab.getSupplierList().toString());

            Collections.sort(mSupplierList);

            updateSupplierListPref(mSupplierList);
        }

    }

    private void updateSupplierListPref(ArrayList<String> supplierList) {
        String listJson = new Gson().toJson(supplierList);
        ImageSaverPreferences.setPrefSupplierList(this, listJson);
    }

    @Override
    public void onSelected(int select) {
        mSupplierName.setText(mSupplierList.get(select));
    }
}
