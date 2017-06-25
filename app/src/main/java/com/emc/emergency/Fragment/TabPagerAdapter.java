package com.emc.emergency.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by hocan on 24-Jun-17.
 */

class TabPagerAdapter extends PagerAdapter {
    public TabPagerAdapter(FragmentManager fragmentManager) {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public CharSequence getPageTitle(int position) {

                return position+"";


    }
}
