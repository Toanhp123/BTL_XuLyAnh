package com.example.btl_xulyanh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.btl_xulyanh.R;
import com.example.btl_xulyanh.cIass.ImageProcessor;

import org.opencv.core.Mat;

public class Bilateral extends Fragment {

    Button btnGetImgBilateral, btnBilateral;
    ImageView imgBilateral;
    EditText edtSigmaColor, edtSigmaSpace, edtD;
    Mat mat;
    ImageProcessor imageProcessor;

    public Bilateral() {
        // Required empty public constructor
    }

    private final ActivityResultLauncher<String> getImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            imgBilateral.setImageURI(uri);
            mat = ImageProcessor.uriToMat(getContext(), uri);
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bilateral, container, false);

        imageProcessor = new ImageProcessor();

        imgBilateral = view.findViewById(R.id.imgBilateral);

        edtSigmaColor = view.findViewById(R.id.edtSigmaColor);
        edtSigmaSpace = view.findViewById(R.id.edtSigmaSpace);
        edtD = view.findViewById(R.id.edtD);

        int sigmaColor = edtSigmaColor.getText().toString().isEmpty() ? 250 : Integer.parseInt(edtSigmaColor.getText().toString());
        int sigmaSpace = edtSigmaSpace.getText().toString().isEmpty() ? 50 : Integer.parseInt(edtSigmaSpace.getText().toString());
        int d = edtD.getText().toString().isEmpty() ? 10 : Integer.parseInt(edtD.getText().toString());

        btnBilateral = view.findViewById(R.id.btnBilateral);
        btnBilateral.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.filterBilateral(mat, imgBilateral, d, sigmaColor, sigmaSpace);
            }
        });

        btnGetImgBilateral = view.findViewById(R.id.btnGetImgBilateral);
        btnGetImgBilateral.setOnClickListener(v -> getImageLauncher.launch("image/*"));

        return view;
    }
}