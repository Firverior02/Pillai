package com.loal.pillai;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;



public class ScannerFragment extends Fragment {

    private PreviewView previewView;
    private TextView codePreview;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ActivityResultLauncher<String> permissionLauncher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        previewView = view.findViewById(R.id.previewView);
        codePreview = view.findViewById(R.id.codePreview);

        // Ask for CAMERA permission
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                startCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }
        });

        // Check if access to CAMERA is already allowed
        if (allPermissionsGranted()) {
            // Permission granted
            startCamera();
        } else {
            // Ask for permission
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
        return view;
    }

    /**
     * Check if all required permissions have been granted
     * @return If granted
     */
    private boolean allPermissionsGranted() {
        // Check if CAMERA is granted
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Start the CameraX and bind it to the fragment
     */
    private void startCamera() {
        // A future instance that holds results of the asynchronous camera connection
        // can handle callbacks
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        // Listener for when camera future is done
        cameraProviderFuture.addListener(() -> {
            try {
                // Retrieves camera provider
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                // Binds camera lifecycle to the Fragment
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Catch errors
                Toast.makeText(getContext(), "Error starting camera", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    /**
     * Binds camera
     * @param cameraProvider The camera provider to bind
     */
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Init preview to display camera data
        Preview preview = new Preview.Builder().build();

        // Get back facing camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Strategy for processing data
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        // Analyze each frame
        imageAnalysis.setAnalyzer(executor, imageProxy -> {
            try (imageProxy) {
                if (imageProxy.getFormat() == ImageFormat.YUV_420_888) {
                    // Directly process the image in YUV_420_888 format
                    scanBarcode(imageProxy);
                } else {
                    // TODO: Handle other image formats
                }
            }
        });

        // Link camera preview and the actual preview in UI
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Bind camera lifecycle to fragment, this allows the camera to
        // start and stop whenever the fragment does so
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    /**
     * Scan a bitmap for a barcode
     * @param imageProxy The image to scan
     */
    @OptIn(markerClass = ExperimentalGetImage.class) private void scanBarcode(ImageProxy imageProxy) {
        BarcodeScanner scanner = BarcodeScanning.getClient();
        InputImage inputImage = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());

        scanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String rawValue = barcode.getRawValue();
                        codePreview.setText(rawValue);

                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).showButtons(rawValue);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }


    /**
     * Convert a image proxy to a bitmap
     * @param image the ImageProxy to convert
     * @return The bitmap
     */
    private Bitmap imageProxyToBitmap(ImageProxy image) {
        // The image contains three planes of data in a "YUV" format
        // YUV (YCbCr) is a colors pace. Y = Brightness, U = Blue projection, V = Red projection
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ImageProxy.PlaneProxy yPlane = planes[0];   // Brightness plane
        ImageProxy.PlaneProxy uPlane = planes[1];   // Blue projection
        ImageProxy.PlaneProxy vPlane = planes[2];   // Red projection

        ByteBuffer yBuffer = yPlane.getBuffer();
        ByteBuffer uBuffer = uPlane.getBuffer();
        ByteBuffer vBuffer = vPlane.getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        // New format
        byte[] nv21 = new byte[ySize + uSize + vSize];

        // U and V are swapped
        yBuffer.get(nv21, 0, ySize);  // Y plane is the same
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        // Create a YuvImage as a middle step
        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Compress into JPEG format
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);

        // Convert JPEG to Bitmap and return
        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}