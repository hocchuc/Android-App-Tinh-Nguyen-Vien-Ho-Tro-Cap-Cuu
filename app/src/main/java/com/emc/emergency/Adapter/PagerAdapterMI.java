package com.emc.emergency.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.emc.emergency.Fragment.fragment_allergic_list;
import com.emc.emergency.Fragment.fragment_disease_list;
import com.emc.emergency.Fragment.fragment_medicine_list;

/**
 * Created by hocan on 09-Jul-17.
 */

public class PagerAdapterMI extends FragmentStatePagerAdapter {
    public PagerAdapterMI(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:{
                frag=new fragment_disease_list();
                break;}
            case 1:
                frag=new fragment_allergic_list();
                break;
            case 2:
                frag=new fragment_medicine_list();
                break;

        }
        return frag;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title="Disease";
                break;
            case 1:
                title="Allergic";
                break;
            case 2:
                title="Medicine";
                break;
        }

        return title;
    }
}
