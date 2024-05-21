package com.example.btl_xulyanh.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.btl_xulyanh.R;
import com.example.btl_xulyanh.cIass.ImageProcessor;

import org.opencv.core.Mat;

public class Bilateral extends Fragment {

    private Mat mat;
    private Bilateral.OnImageProcessedListener mListener;
    private ImageProcessor imageProcessor;
    private static Uri src_image;

    public Bilateral() {
        // Required empty public constructor
    }

    public void setUri(Uri uri) {
        src_image = uri;
    }

    public interface OnImageProcessedListener {
        void onImageProcessed(Bitmap processedImage, String title);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Bilateral.OnImageProcessedListener) {
            mListener = (Bilateral.OnImageProcessedListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnImageProcessedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bilateral, container, false);

        imageProcessor = new ImageProcessor();

        EditText edtSigmaColor = view.findViewById(R.id.edtSigmaColor);
        EditText edtSigmaSpace = view.findViewById(R.id.edtSigmaSpace);
        EditText edtD = view.findViewById(R.id.edtD);

        int sigmaColor = edtSigmaColor.getText().toString().isEmpty() ? 250 : Integer.parseInt(edtSigmaColor.getText().toString());
        int sigmaSpace = edtSigmaSpace.getText().toString().isEmpty() ? 50 : Integer.parseInt(edtSigmaSpace.getText().toString());
        int d = edtD.getText().toString().isEmpty() ? 10 : Integer.parseInt(edtD.getText().toString());

        Button btnBilateral = view.findViewById(R.id.btnBilateral);
        btnBilateral.setOnClickListener(v -> {
            if (mListener != null && src_image != null) {
                mat = ImageProcessor.uriToMat(getContext(), src_image);
                Bitmap resultImage = imageProcessor.filterBilateral(mat, d, sigmaColor, sigmaSpace);
                mListener.onImageProcessed(resultImage, "Áp dụng Bilateral thành công");
            }
        });

        return view;
    }
}