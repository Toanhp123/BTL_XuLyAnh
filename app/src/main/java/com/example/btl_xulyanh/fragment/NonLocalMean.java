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

public class NonLocalMean extends Fragment {

    Button btnNLM, btnGetIMG;
    ImageView imgNLM;
    EditText edtH, edtHcolor, edtTextTemplate, edtSearch;
    Mat mat;
    ImageProcessor imageProcessor;

    public NonLocalMean() {
    }

    private final ActivityResultLauncher<String> getImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            imgNLM.setImageURI(uri);
            mat = ImageProcessor.uriToMat(getContext(), uri);
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nlm, container, false);

        imageProcessor = new ImageProcessor();

        imgNLM = view.findViewById(R.id.imgNLM);

        edtH = view.findViewById(R.id.edtH);
        edtHcolor = view.findViewById(R.id.edtHcolor);
        edtTextTemplate = view.findViewById(R.id.edtTextTemplate);
        edtSearch = view.findViewById(R.id.edtSearch);

        int h = edtH.getText().toString().isEmpty() ? 10 : Integer.parseInt(edtH.getText().toString());
        int hcolor = edtH.getText().toString().isEmpty() ? 10 : Integer.parseInt(edtH.getText().toString());
        int template = edtH.getText().toString().isEmpty() ? 7 : Integer.parseInt(edtH.getText().toString());
        int search = edtH.getText().toString().isEmpty() ? 21 : Integer.parseInt(edtH.getText().toString());

        btnNLM = view.findViewById(R.id.btnNLM);
        btnNLM.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.filterNLM(mat, imgNLM, h, hcolor, template, search);
            }
        });

        btnGetIMG = view.findViewById(R.id.btnGetImgNLM);
        btnGetIMG.setOnClickListener(v -> getImageLauncher.launch("image/*"));

        return view;
    }
}