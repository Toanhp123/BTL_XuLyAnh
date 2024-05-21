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

public class NonLocalMean extends Fragment {

    private Mat mat;
    private static Uri src_image;
    private ImageProcessor imageProcessor;
    private OnImageProcessedListener mListener;

    public NonLocalMean() {
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
        if (context instanceof OnImageProcessedListener) {
            mListener = (OnImageProcessedListener) context;
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
        View view = inflater.inflate(R.layout.fragment_nlm, container, false);

        imageProcessor = new ImageProcessor();

        EditText edtH = view.findViewById(R.id.edtH);
        EditText edtHcolor = view.findViewById(R.id.edtHcolor);
        EditText edtTextTemplate = view.findViewById(R.id.edtTextTemplate);
        EditText edtSearch = view.findViewById(R.id.edtSearch);

        int h = edtH.getText().toString().isEmpty() ? 10 : Integer.parseInt(edtH.getText().toString());
        int hcolor = edtH.getText().toString().isEmpty() ? 10 : Integer.parseInt(edtHcolor.getText().toString());
        int template = edtH.getText().toString().isEmpty() ? 7 : Integer.parseInt(edtTextTemplate.getText().toString());
        int search = edtH.getText().toString().isEmpty() ? 21 : Integer.parseInt(edtSearch.getText().toString());

        Button btnNLM = view.findViewById(R.id.btnNLM);
        btnNLM.setOnClickListener(v -> {
            if (mListener != null && src_image != null) {
                mat = ImageProcessor.uriToMat(getContext(), src_image);
                Bitmap resultImage = imageProcessor.filterNLM(mat, h, hcolor, template, search);
                mListener.onImageProcessed(resultImage, "Áp dụng Non-local Means thành công");
            }
        });

        return view;
    }


}