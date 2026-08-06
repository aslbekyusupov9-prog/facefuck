package com.aifacerating.app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aifacerating.app.R;
import com.aifacerating.app.utils.ImageHolder;
import com.aifacerating.app.utils.UserProfileManager;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {

    private PreviewView viewFinder;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private String selectedGender = "MALE";

    public static CameraFragment newInstance(String gender) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString("GENDER", gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        
        if (getArguments() != null) {
            selectedGender = getArguments().getString("GENDER", "MALE");
        }

        viewFinder = view.findViewById(R.id.view_finder);
        ImageView btnCapture = view.findViewById(R.id.btn_capture);
        ImageView btnClose = view.findViewById(R.id.btn_close_camera);

        cameraExecutor = Executors.newSingleThreadExecutor();

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 10);
        }

        btnCapture.setOnClickListener(v -> takePhoto());
        
        btnClose.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new UploadFragment())
                .commit();
        });

        return view;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();

                // Select front camera as default
                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        File photoFile = new File(requireContext().getCacheDir(), "face_scan_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
            outputOptions, 
            ContextCompat.getMainExecutor(requireContext()), 
            new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    try {
                        Bitmap fullBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        if (fullBitmap != null) {
                            // Respect Mirror Mode Setting if front camera is used
                            if (UserProfileManager.isMirrorMode(requireContext())) {
                                Matrix mirrorMatrix = new Matrix();
                                mirrorMatrix.setScale(-1, 1);
                                fullBitmap = Bitmap.createBitmap(fullBitmap, 0, 0, fullBitmap.getWidth(), fullBitmap.getHeight(), mirrorMatrix, true);
                            }

                            int w = fullBitmap.getWidth();
                            int h = fullBitmap.getHeight();
                            int cropW = (int) (w * 0.75);
                            int cropH = (int) (h * 0.75);
                            int startX = Math.max(0, (w - cropW) / 2);
                            int startY = Math.max(0, (h - cropH) / 2);
                            Bitmap croppedBitmap = Bitmap.createBitmap(fullBitmap, startX, startY, cropW, cropH);

                            try (FileOutputStream out = new FileOutputStream(photoFile)) {
                                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 92, out);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Uri savedUri = Uri.fromFile(photoFile);
                    ImageHolder.getInstance().setImage(null, savedUri);
                    
                    // Navigate to ResultFragment
                    requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ResultFragment.newInstance(selectedGender))
                        .commit();
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Toast.makeText(requireContext(), "Xatolik: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getContext(), "Kameraga ruxsat berilmagan!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UploadFragment())
                    .commit();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null && !cameraExecutor.isShutdown()) {
            cameraExecutor.shutdown();
        }
    }
}
