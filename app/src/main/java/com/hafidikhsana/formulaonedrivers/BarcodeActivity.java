package com.hafidikhsana.formulaonedrivers;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BarcodeActivity extends AppCompatActivity {

    PreviewView previewView;
    ImageView imageToScan;
    Uri imageUri;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    Button buttonTakePicture, buttonAnalyze;
    private TextView barcodeResultTextView;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        imageToScan = findViewById(R.id.image_to_scan);
        buttonTakePicture = findViewById(R.id.button_capture);
        buttonAnalyze = findViewById(R.id.button_get_barcode);
        previewView = findViewById(R.id.preview_view);
        barcodeResultTextView = findViewById(R.id.barcode_result);

        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .enableAllPotentialBarcodes()
                        .build();


        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarcodeScanner scanner = BarcodeScanning.getClient();

                InputImage image;
                try {
                    image = InputImage.fromFilePath(BarcodeActivity.this, imageUri);

                    try {
                        Task<List<Barcode>> result = scanner.process(image)
                                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                    @Override
                                    public void onSuccess(List<Barcode> barcodes) {
                                        for (Barcode barcode: barcodes) {
                                            Rect bounds = barcode.getBoundingBox();
                                            Point[] corners = barcode.getCornerPoints();

                                            String rawValue = barcode.getRawValue();

                                            int valueType = barcode.getValueType();
                                            // See API reference for complete list of supported types
                                            switch (valueType) {
                                                case Barcode.TYPE_WIFI:
                                                    String ssid = barcode.getWifi().getSsid();
                                                    String password = barcode.getWifi().getPassword();
                                                    int type = barcode.getWifi().getEncryptionType();
                                                    break;
                                                case Barcode.TYPE_URL:
                                                    String title = barcode.getUrl().getTitle();
                                                    String url = barcode.getUrl().getUrl();
                                                    barcodeResultTextView.setText(url);
                                                    break;
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(BarcodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (Exception exception) {
                        Toast.makeText(BarcodeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(BarcodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

//        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//
//        BarcodeScannerOptions options =
//                new BarcodeScannerOptions.Builder()
//                        .setBarcodeFormats(
//                                Barcode.FORMAT_QR_CODE,
//                                Barcode.FORMAT_AZTEC)
//                        .enableAllPotentialBarcodes()
//                        .build();
//
//        cameraProviderFuture.addListener(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                    bindImageAnalysis(cameraProvider);
//                } catch (ExecutionException | InterruptedException e) {
//                    Toast.makeText(BarcodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, ContextCompat.getMainExecutor(this));
    }

    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(BarcodeActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
            } else {
                imageUri = o;
                Glide.with(getApplicationContext()).load(o).into(imageToScan);
            }
        }
    });

    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            @ExperimentalGetImage
            public void analyze(@NonNull ImageProxy imageProxy) {
                int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();

                Image mediaImage = imageProxy.getImage();
                ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                if (mediaImage != null) {
                    InputImage image = InputImage.fromByteBuffer(buffer,
                            /* image width */ 480,
                            /* image height */ 360,
                            rotationDegrees,
                            InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
                    );

                    BarcodeScanner scanner = BarcodeScanning.getClient();
                    Task<List<Barcode>> result = scanner.process(image)
                            .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                @Override
                                public void onSuccess(List<Barcode> barcodes) {
                                    for (Barcode barcode: barcodes) {
                                        Rect bounds = barcode.getBoundingBox();
                                        Point[] corners = barcode.getCornerPoints();

                                        String rawValue = barcode.getRawValue();

                                        int valueType = barcode.getValueType();
                                        Log.d("BARCODE", barcode.toString());
                                        // See API reference for complete list of supported types
                                        switch (valueType) {
                                            case Barcode.TYPE_WIFI:
                                                String ssid = barcode.getWifi().getSsid();
                                                String password = barcode.getWifi().getPassword();
                                                int type = barcode.getWifi().getEncryptionType();
                                                break;
                                            case Barcode.TYPE_URL:
                                                String title = barcode.getUrl().getTitle();
                                                String url = barcode.getUrl().getUrl();
                                                barcodeResultTextView.setText(url);
                                                break;
                                            case Barcode.FORMAT_ALL_FORMATS:
                                                String info = barcode.toString();
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                }
                            });
                }

                imageProxy.close();
            }
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,
                imageAnalysis, preview);
    }
}