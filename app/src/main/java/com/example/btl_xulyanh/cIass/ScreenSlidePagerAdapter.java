package com.example.btl_xulyanh.cIass;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.btl_xulyanh.fragment.Bilateral;
import com.example.btl_xulyanh.fragment.NonLocalMean;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NonLocalMean();
            case 1:
                return new Bilateral();
            default:
                return new NonLocalMean();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
