package org.busywait.mycamera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String URI = "URI";
    private ImageView src;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        src = findViewById(R.id.src);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //setImage(mainViewModel.getUri());

        requestCameraPermissions();

        findViewById(R.id.cameraBtn).setOnClickListener(view -> {
            handleButton();
        });
    }

    private void requestCameraPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
        }
        //else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            //showInContextUI(...);
        //}
        else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void setImage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            src.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "File not found: " + uri, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleButton() {
        src.setImageBitmap(null);
        try {
            File f = File.createTempFile("IMG_", ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            Uri uri = FileProvider.getUriForFile(MainActivity.this,
                    MainActivity.this.getPackageName() + ".provider", f);
            mainViewModel.setUri(uri);
            mCameraContent.launch(uri);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }
    }

    private ActivityResultLauncher<Uri> mCameraContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    setImage(mainViewModel.getUri());
                }
                else {
                    Toast.makeText(MainActivity.this, "Image not taken/saved", Toast.LENGTH_SHORT).show();
                }
            });

        private ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        Toast.makeText(this, "Is not usable without camera", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            });
}