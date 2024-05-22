package com.example.btl_xulyanh.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_xulyanh.R;
import com.example.btl_xulyanh.cIass.PermissionManager;

import org.opencv.android.OpenCVLoader;

public class StartApp extends AppCompatActivity {

    Button btnStart, btnExit;
    private static final int PERMISSION_REQUEST_CODE = 123;
    PermissionManager permissionManager;

    static {
        // Khối static initializer
        if (!OpenCVLoader.initLocal()) {
            Log.e("TAG", "Không thể khởi tạo OpenCV");
        } else {
            Log.e("TAG", "OpenCV đã được khởi tạo thành công");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        setSupportActionBar(findViewById(R.id.toolBarStart));

        permissionManager = new PermissionManager(this);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            permissionManager.checkAndRequestPermission();
            if (permissionManager.status == 1) {
                Intent intentStartApp = new Intent(this, MainApp.class);
                startActivity(intentStartApp);
            }
        });

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> finish());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Xử lý kết quả của yêu cầu quyền.
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentStartApp = new Intent(this, MainApp.class);
                startActivity(intentStartApp);
            } else {
                // Quyền không được cấp. Hiển thị thông báo cho người dùng biết rằng ứng dụng không thể thực hiện hành động mong muốn.
                permissionManager.showPermissionDeniedMessage();
            }
        }
    }
}

