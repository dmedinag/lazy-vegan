package com.dmedinag.android.lazyvegan;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.support.v4.content.FileProvider.getUriForFile;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_logo;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final String TAG = "MainActivity";
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        // TODO: PUT THE LOGO WHERE IT BELONGS

    }

    public void onClickOpenCameraButton(View v) {
        Log.d(TAG, "Entered click listener");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ComponentName camera = intent.resolveActivity(getPackageManager());

        if ( camera != null ) {

            File imagePath = new File(this.getFilesDir(), "images");
            File imageFile = new File(imagePath, "default_image.jpg");

            Log.d(TAG, "mImageFile path: " + imageFile.getAbsolutePath());

            mImageUri = getUriForFile(this, "com.dmedinag.lazyvegan.fileprovider", imageFile);

            Log.d(TAG, "mImageFile Uri:  " + mImageUri.toString());

            String cameraPackage = camera.getPackageName();
            this.grantUriPermission(cameraPackage, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Log.d(TAG, "Permission granted to " + cameraPackage + " in " + mImageUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case Activity.RESULT_OK: {
                switch (requestCode) {
                    case REQUEST_IMAGE_CAPTURE: {
                        Log.i(TAG, "Got to the callback");
                        Log.d(TAG, "The uri is " + mImageUri.toString());
                        Log.d(TAG, "The path is " + mImageUri.getPath());
                        Bitmap imageBitmap;
                        try {
                            Log.i(TAG, "Successfully retrieved picture, saved at " + mImageUri.toString());
                            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);

                            // TODO: Call the API with the image above

                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "Tried to retrieve the file, but this one doesn't exist");
//                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.e(TAG, "IO Exception here");
//                            e.printStackTrace();
                        }
                        break;
                    }
                    default: {
                        Log.e(TAG, "onActivityResult received a result with an invalid request code");
                        // TODO: Deal with error: unrecognized request code
                    }
                }
            }
            case Activity.RESULT_CANCELED: {
                Log.w(TAG, "onActivityResult received a response for a canceled request");
                // TODO: Deal with error: the user canceled the request
                break;
            }
            default: {
                Log.e(TAG, "onActivityResult received an error result code");
                // TODO: Deal with error: error while executing the other activity
            }
        }
    }
}
