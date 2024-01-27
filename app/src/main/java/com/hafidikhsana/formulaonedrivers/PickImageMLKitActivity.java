package com.hafidikhsana.formulaonedrivers;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;

public class PickImageMLKitActivity extends AppCompatActivity {

    private ImageView imageToScan, placeholder;
    private Uri imageUri;

    private Button buttonTakePicture, buttonAnalyze;
    private TextView barcodeResultTextView;

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(PickImageMLKitActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
            } else {
                imageUri = o;
                imageToScan.setVisibility(View.VISIBLE);
                placeholder.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(o).into(imageToScan);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_image_mlkit);

        imageToScan = findViewById(R.id.pick_image_mlkit_image_to_scan);
        placeholder = findViewById(R.id.pick_image_mlkit_image_placeholder);
        buttonTakePicture = findViewById(R.id.pick_image_mlkit_button_capture);
        buttonAnalyze = findViewById(R.id.pick_image_mlkit_button_get_barcode);
        barcodeResultTextView = findViewById(R.id.pick_image_mlkit_barcode_result);

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    launcher.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                } else {
                    requestPermission();
                }
            }
        });

        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarcodeScanner scanner = BarcodeScanning.getClient();

                InputImage image;
                try {
                    image = InputImage.fromFilePath(PickImageMLKitActivity.this, imageUri);

                    try {
                        Task<List<Barcode>> result = scanner.process(image)
                                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                    @Override
                                    public void onSuccess(List<Barcode> barcodes) {
                                        for (Barcode barcode: barcodes) {
                                            int valueType = barcode.getValueType();
                                            if (valueType == Barcode.TYPE_URL) {
                                                String url = barcode.getUrl().getUrl();
                                                barcodeResultTextView.setText(url);
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PickImageMLKitActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (Exception exception) {
                        Toast.makeText(PickImageMLKitActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(PickImageMLKitActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }
}