package com.hafidikhsana.formulaonedrivers;

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

import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RealTimeMLKitActivity extends AppCompatActivity {

    PreviewView previewView;
    TextView barcodeResultTextView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_mlkit);

        previewView = findViewById(R.id.realtime_mlkit_preview_view);
        barcodeResultTextView = findViewById(R.id.realtime_mlkit_barcode_result);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindImageAnalysis(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(RealTimeMLKitActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.TYPE_URL)
                        .build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            @ExperimentalGetImage
            public void analyze(@NonNull ImageProxy imageProxy) {
                int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                Image mediaImage = imageProxy.getImage();

                if (mediaImage != null) {
                    InputImage image = InputImage.fromMediaImage(mediaImage, rotationDegrees);

                    BarcodeScanner scanner = BarcodeScanning.getClient(options);
                    Task<List<Barcode>> result = scanner.process(image);
//                            .addOnSuccessListener(detecBarcode -> {
//                                for (Barcode barcode: detecBarcode) {
//                                    Log.d("XXXXXXX", detecBarcode.toString());
//                                    Toast.makeText(RealTimeMLKitActivity.this, barcode.getRawValue(), Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.d("XXXXXXX", e.toString());
//                                Toast.makeText(RealTimeMLKitActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                            })
//                            .addOnCompleteListener(detectedObjects -> {
//                                Log.d("XXXXXXX", "here");
//                                scanner.close();
//                                mediaImage.close();
//                                imageProxy.close();
//                            });
                    try {
                        List<Barcode> detectedObjects = Tasks.await(result);
                        Log.d("XXX", String.valueOf(detectedObjects.size()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("XXX", e.toString());
                    }
                }

                return;
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