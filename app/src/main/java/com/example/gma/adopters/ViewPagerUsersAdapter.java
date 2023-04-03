package com.example.gma.adopters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gma.tabview.DriversFragment;
import com.example.gma.tabview.PublicFragment;

public class ViewPagerUsersAdapter extends FragmentPagerAdapter {
    public ViewPagerUsersAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new DriversFragment();
        }else {
            return new PublicFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Drivers";
        } else {
            return "Public";
        }
    }
}
