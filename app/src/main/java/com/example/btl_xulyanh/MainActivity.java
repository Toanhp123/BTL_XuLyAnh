package com.example.btl_xulyanh;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {
    ImageView imageViewInput, imageViewOutput;
    Button button, buttonNLM, buttonBilateral;
    PermissionManager permissionManager;
    ImageProcessor imageProcessor;
    Mat mat;

    private static final int PERMISSION_REQUEST_CODE = 123;

    private final ActivityResultLauncher<String> getImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            imageViewInput.setImageURI(uri);
            mat = ImageProcessor.uriToMat(this, uri);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager(this);

        imageProcessor = new ImageProcessor();

        imageViewInput = findViewById(R.id.imageViewInput);
        imageViewOutput = findViewById(R.id.imageViewOutput);

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            permissionManager.checkAndRequestPermission();
            if (permissionManager.status == 1) {
                openFileChooser();
            }
        });

        buttonNLM = findViewById(R.id.buttonNLM);
        buttonNLM.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.filterNLM(mat, imageViewOutput);
            }
        });

        buttonBilateral = findViewById(R.id.buttonBilateral);
        buttonBilateral.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.filterBilateral(mat, imageViewOutput);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Xử lý kết quả của yêu cầu quyền.
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                // Quyền không được cấp. Hiển thị thông báo cho người dùng biết rằng ứng dụng không thể thực hiện hành động mong muốn.
                permissionManager.showPermissionDeniedMessage();
            }
        }
    }

    private void openFileChooser() {
        // Sử dụng ActivityResultLauncher để mở hộp thoại truy cập bộ nhớ
        getImageLauncher.launch("image/*");
    }

}

