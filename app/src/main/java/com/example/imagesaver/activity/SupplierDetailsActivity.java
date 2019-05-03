package com.example.imagesaver.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagesaver.Dialog.CategoryDialogFragment;
import com.example.imagesaver.Dialog.FamilyDialogFragment;
import com.example.imagesaver.Dialog.SubFamilyDialogFragment;
import com.example.imagesaver.R;
import com.example.imagesaver.Util.Constants;
import com.example.imagesaver.Util.ImageSaverLab;
import com.example.imagesaver.Util.ImageSaverPreferences;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.FileProvider.getUriForFile;

public class SupplierDetailsActivity extends AppCompatActivity implements CategoryDialogFragment.SelectedItem, FamilyDialogFragment.SelectedItemFamily, SubFamilyDialogFragment.SelectedItemSubFamily {

    private static final String TAG = SupplierDetailsActivity.class.getSimpleName();

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_TAKE_VIDEO = 2;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 200;

    private String currentPhotoPath;
    private String currentVidPath;
    private RadioButton mLc, mOnSite, mOther;

    private CheckBox mTheme, mCustomization, mThreeSixFive, mSale;
    private TextView mCategory, mFamily, mSubFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_details);

        setupToolbar();
        initViews();
        fetchShippingMethodFromPref();
        fetchShippingTypesFromPref();
        fetchCategoryFamilySubFamilyFromPref();
    }

    private void fetchShippingTypesFromPref() {
        String shippingTypesFromPref = ImageSaverPreferences.getShippingType(this);

        if (shippingTypesFromPref.contains("theme")) {
            mTheme.setChecked(true);
        }
        if (shippingTypesFromPref.contains("customization")) {
            mCustomization.setChecked(true);
        }
        if (shippingTypesFromPref.contains("365")) {
            mThreeSixFive.setChecked(true);
        }
        if (shippingTypesFromPref.contains("sale")) {
            mSale.setChecked(true);
        }
    }

    private void fetchShippingMethodFromPref() {
        String shippingMethod = ImageSaverPreferences.getShippingMethod(this);

        switch (shippingMethod) {
            case "LC":
                mLc.setChecked(true);
                break;
            case "On Site":
                mOnSite.setChecked(true);
                break;
            case "Other":
                mOther.setChecked(true);
                break;
            default:
                //mOther.setChecked(true);
                break;
        }

    }

    private void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Capture Media");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setTitleTextColor(Color.BLACK);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {

        ImageView takePhoto = findViewById(R.id.take_photo);
        ImageView takeVideo = findViewById(R.id.take_video);
        RadioGroup radioGroup = findViewById(R.id.radio_shipping_method);
        mCategory = findViewById(R.id.category);
        mFamily = findViewById(R.id.family);
        mSubFamily = findViewById(R.id.sub_family);

        mLc = findViewById(R.id.lc);
        mOnSite = findViewById(R.id.on_site);
        mOther = findViewById(R.id.other);


        mTheme = findViewById(R.id.theme);
        mCustomization = findViewById(R.id.customization);
        mThreeSixFive = findViewById(R.id.three_six_five);
        mSale = findViewById(R.id.sale);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = findViewById(checkedId);
                ImageSaverPreferences.setShippingMethod(SupplierDetailsActivity.this, checkedButton.getText().toString().trim());
                Log.d(TAG, "onCheckedChanged: " + checkedButton.getText());
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    askForCameraPermission();
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        takeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    askForCameraPermission();

                } else {
                    try {
                        dispatchTakeVideoIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        mTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: mTheme -" + isChecked);

                if (mTheme.isChecked()) {
                    ImageSaverLab.addShippingTypes("theme");
                } else {
                    ImageSaverLab.removeShippingTypes("theme");
                }

                updateShippingTypePref();
            }
        });

        mCustomization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: mCustomization -" + isChecked);
                if (mCustomization.isChecked()) {
                    ImageSaverLab.addShippingTypes("customization");
                } else {
                    ImageSaverLab.removeShippingTypes("customization");
                }

                updateShippingTypePref();
            }
        });

        mThreeSixFive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: mThreeSixFive -" + isChecked);
                if (mThreeSixFive.isChecked()) {
                    ImageSaverLab.addShippingTypes("365");
                } else {
                    ImageSaverLab.removeShippingTypes("365");
                }

                updateShippingTypePref();
            }
        });

        mSale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: mSale  -" + isChecked);
                if (mSale.isChecked()) {
                    ImageSaverLab.addShippingTypes("sale");
                } else {
                    ImageSaverLab.removeShippingTypes("sale");
                }

                updateShippingTypePref();
            }
        });

        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new CategoryDialogFragment();
                newFragment.show(getSupportFragmentManager(), "categories");
            }
        });

        mFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FamilyDialogFragment();
                newFragment.show(getSupportFragmentManager(), "families");
            }
        });

        mSubFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogFragment newFragment = new SubFamilyDialogFragment();
//                newFragment.show(getSupportFragmentManager(), "sub_families");


                startActivity(new Intent(SupplierDetailsActivity.this, CameraActivity.class ));
            }
        });

    }

    private void updateShippingTypePref() {
        String shippingTypes = new Gson().toJson(ImageSaverLab.getShippingTypes());
        ImageSaverPreferences.setShippingType(this, shippingTypes);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                solveIssuesWithKitKat(photoURI, takePictureIntent);


            }
        }
    }

    private void solveIssuesWithKitKat(Uri photoURI, Intent takePictureIntent) {
        /** Camera has been stopped in android kit-kat
         * version 4.4.2
         * ref from "https://medium.com/@a1cooke/using-v4-support-library-fileprovider-and-camera-intent-a45f76879d61"
         */

        List<ResolveInfo> resolvedIntentActivities = getBaseContext().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;
            getBaseContext().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private void dispatchTakeVideoIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri videoUri;
                //Solving issues with kit-kat 19

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    videoUri = Uri.fromFile(photoFile);
                } else {
                    videoUri = getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_VIDEO);
                solveIssuesWithKitKat(videoUri, takePictureIntent);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            //galleryAddPic();
            dispatchTakePictureIntent(); //Again allow to take next media
            Toast.makeText(SupplierDetailsActivity.this, "Image Saved to internal memory", Toast.LENGTH_SHORT).show();

        } else if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {

            //galleryAddVid();
            try {
                dispatchTakeVideoIntent(); // Again allow to take next media
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(SupplierDetailsActivity.this, "Video Saved to internal memory", Toast.LENGTH_SHORT).show();
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = buildMediaName(Constants.TYPE_FILE) + timeStamp + "_";


        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File storageDir = getPrivateAlbumStorageDirForPhoto(SupplierDetailsActivity.this, buildMediaName(Constants.TYPE_FOLDER));

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        // Create a video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String VideoFileName = buildMediaName(Constants.TYPE_FILE) + timeStamp + "_";


        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File storageDir = getPrivateAlbumStorageDirForVideo(SupplierDetailsActivity.this, buildMediaName(Constants.TYPE_FOLDER));

        File video = File.createTempFile(
                VideoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );


        currentVidPath = video.getAbsolutePath();
        return video;
    }


    public File getPrivateAlbumStorageDirForPhoto(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(" NO PIC DIR", "Directory not created");
        }
        return file;
    }

    public File getPrivateAlbumStorageDirForVideo(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("NO VID DIR", "Directory not created");
        }
        return file;
    }

    private boolean checkCameraPermission() {
        if ((ContextCompat.checkSelfPermission(SupplierDetailsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)) {
            return false;
        } else {
            return true;
        }

    }

    private void askForCameraPermission() {
        ActivityCompat.requestPermissions(SupplierDetailsActivity.this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }

//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    private void galleryAddVid() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentVidPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                finish();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onSelected(int select) {
        mCategory.setText(getResources().getStringArray(R.array.planet)[select]);
        ImageSaverPreferences.setCategory(this, getResources().getStringArray(R.array.planet)[select]);
    }

    @Override
    public void onSelectedFamily(int select) {
        mFamily.setText(getResources().getStringArray(R.array.planet)[select]);
        ImageSaverPreferences.setFamily(this, getResources().getStringArray(R.array.planet)[select]);
    }

    @Override
    public void onSelectedSubFamily(int select) {
        mSubFamily.setText(getResources().getStringArray(R.array.planet)[select]);
        ImageSaverPreferences.setSubFamily(this, getResources().getStringArray(R.array.planet)[select]);
    }

    private void fetchCategoryFamilySubFamilyFromPref() {

        String category = ImageSaverPreferences.getCategory(this);
        String family = ImageSaverPreferences.getFamily(this);
        String subFamily = ImageSaverPreferences.getSubFamily(this);

        if (TextUtils.isEmpty(category)) {
            mCategory.setText(R.string.select_category_name);
        } else {
            mCategory.setText(category);
        }

        if (TextUtils.isEmpty(family)) {
            mFamily.setText(R.string.select_family_name);
        } else {
            mFamily.setText(family);
        }

        if (TextUtils.isEmpty(subFamily)) {
            mSubFamily.setText(R.string.select_sub_family_name);
        } else {
            mSubFamily.setText(subFamily);
        }
    }

    private String buildMediaName(int type) {
        String name, location, supplierName, shippingMethod, shippingType, category, family, subFamily;
        name = ImageSaverPreferences.getPreferencesName(this);
        location = ImageSaverPreferences.getPreferenceLocation(this);
        supplierName = ImageSaverPreferences.getPrefSupplierName(this);
        shippingMethod = ImageSaverPreferences.getShippingMethod(this);
        shippingType = ImageSaverPreferences.getShippingType(this);
        category = ImageSaverPreferences.getCategory(this);
        family = ImageSaverPreferences.getFamily(this);
        subFamily = ImageSaverPreferences.getSubFamily(this);

        if (type == Constants.TYPE_FILE) {
            return name + "_" + location + "_" + supplierName + "_" + shippingMethod + "_" + shippingType + "_" + category + "_" + family + "_" + subFamily;
        } else if (type == Constants.TYPE_FOLDER) {
            return name + "_" + supplierName + "_" + category;
        }
        return "";
    }

}

