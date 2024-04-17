package com.example.btl_xulyanh;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private final Context context;
    public int status = 0;

    public PermissionManager(Context context) {
        this.context = context;
    }

    public void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            status = 1;
        } else {
            showExplanationAndRequestPermission();
        }
    }

    public void requestPermission() {
        // Yêu cầu quyền trực tiếp từ người dùng.
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    public void showExplanationAndRequestPermission() {
        // Tạo và hiển thị hộp thoại AlertDialog để giải thích lý do cần quyền.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cần quyền truy cập ảnh");
        builder.setMessage("Ứng dụng cần quyền truy cập ảnh để hiển thị danh sách ảnh. Vui lòng cấp quyền để tiếp tục sử dụng.");
        builder.setPositiveButton("Cấp quyền", (dialogInterface, i) -> {
            // Yêu cầu quyền từ người dùng khi họ chấp nhận.
            requestPermission();
        });
        builder.setNegativeButton("Từ chối", (dialogInterface, i) -> {
            // Thực hiện hành động khi người dùng hủy bỏ.
            Toast.makeText(context, "Bạn đã từ chối cấp quyền truy cập ảnh.", Toast.LENGTH_SHORT).show();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showPermissionDeniedMessage() {
        // Hiển thị thông báo cho người dùng biết rằng quyền đã bị từ chối và hành động không thể thực hiện.
        Toast.makeText(context, "Ứng dụng không thể truy cập vào ảnh mà bạn lưu trữ trên thiết bị.", Toast.LENGTH_SHORT).show();
    }
}

