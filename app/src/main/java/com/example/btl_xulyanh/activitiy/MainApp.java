package com.example.btl_xulyanh.activitiy;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.btl_xulyanh.R;
import com.example.btl_xulyanh.cIass.ImageProcessor;
import com.example.btl_xulyanh.cIass.ScreenSlidePagerAdapter;
import com.example.btl_xulyanh.fragment.Bilateral;
import com.example.btl_xulyanh.fragment.NonLocalMean;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.util.Objects;

public class MainApp extends AppCompatActivity implements NonLocalMean.OnImageProcessedListener, Bilateral.OnImageProcessedListener {

    private ImageView imageView;
    private NonLocalMean nonLocalMean;
    private Bilateral bilateral;
    private Uri src_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        setSupportActionBar(findViewById(R.id.toolBarMain));

        imageView = findViewById(R.id.imageView);
        nonLocalMean = new NonLocalMean();
        bilateral = new Bilateral();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Non-local means");
                    break;
                case 1:
                    tab.setText("Bilateral");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemHome) {
            finish();
        } else if (item.getItemId() == R.id.itemButton) {
            getImageLauncher.launch("image/*");
        }
        return super.onOptionsItemSelected(item);
    }

    private final ActivityResultLauncher<String> getImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            imageView.setImageURI(uri);
            nonLocalMean.setUri(uri);
            bilateral.setUri(uri);
        }
    });

    @Override
    public void onImageProcessed(Bitmap processedImage) {
        imageView.setImageBitmap(processedImage);
        src_image = ImageProcessor.bitmapToUri(this, processedImage);
        nonLocalMean.setUri(src_image);
        bilateral.setUri(src_image);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Xóa tệp ảnh tạm thời khi Activity bị hủy
        if (src_image != null) {
            File file = new File(Objects.requireNonNull(src_image.getPath()));
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    Log.e("TAG", "không xóa được file");
                }
                else{
                    Log.e("TAG", "xóa được file");
                }
            }
        }
    }
}