package com.example.btl_xulyanh.cIass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor {
    @NonNull
    public static Mat uriToMat(Context context, Uri uri) {
        Mat mat = new Mat();
        try {
            // Đọc ảnh từ URI sử dụng BitmapFactory
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Chuyển đổi Bitmap thành Mat sử dụng Utils của OpenCV
            Utils.bitmapToMat(bitmap, mat);

            // Giải phóng tài nguyên
            assert inputStream != null : "inputStream is null";
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mat;
    }

    public static Uri bitmapToUri(@NonNull Context inContext, @NonNull Bitmap inImage) {
        File cacheDir = inContext.getCacheDir();  // Lấy thư mục cache của ứng dụng
        File tempFile = new File(cacheDir, "temp_image_" + System.currentTimeMillis() + ".jpg");  // Tạo tệp tạm thời với tên duy nhất

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out);  // Lưu ảnh dưới dạng JPEG với chất lượng cao nhất
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(tempFile);  // Trả về URI của tệp tạm thời
    }

    public Bitmap filterNLM(Mat inputMat, int h, int hcolor, int templateWindowSie, int searchWindowSize) {
        Mat resultMat = new Mat();
        Photo.fastNlMeansDenoisingColored(inputMat, resultMat, h, hcolor, templateWindowSie, searchWindowSize);

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        return resultBitmap;
    }

    public Bitmap filterBilateral(Mat input, int d, int sigmaColor, int sigmaSpace) {
        //chuyển RGBA sang RGB
        Imgproc.cvtColor(input, input, Imgproc.COLOR_BGRA2BGR);

        Mat resultMat = new Mat();
        Imgproc.bilateralFilter(input, resultMat, d, sigmaColor, sigmaSpace);

        //chuyển RGB sang RGBA
        Imgproc.cvtColor(resultMat, resultMat, Imgproc.COLOR_RGB2RGBA);

        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        return resultBitmap;
    }
}
