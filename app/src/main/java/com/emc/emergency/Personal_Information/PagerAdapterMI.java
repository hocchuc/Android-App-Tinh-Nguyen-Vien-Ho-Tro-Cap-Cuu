package com.emc.emergency.Personal_Information;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.emc.emergency.Personal_Information.fragment_allergic_list;
import com.emc.emergency.Personal_Information.fragment_disease_list;
import com.emc.emergency.Personal_Information.fragment_medicine_list;

/**
 * Created by hocan on 09-Jul-17.
 */

public class PagerAdapterMI extends FragmentStatePagerAdapter {
    boolean helper = false;
    Long id_victim = -99L;

    public PagerAdapterMI(FragmentManager fm) {
        super(fm);
    }

    public PagerAdapterMI(FragmentManager fm, boolean helper, Long id_victim) {
        super(fm);
        this.helper = helper;
        this.id_victim = id_victim;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0: {
                frag = new fragment_disease_list().newInstance(helper,id_victim);
                break;
            }
            case 1:
                frag = new fragment_allergic_list().newInstance(helper,id_victim);
                break;
            case 2:
                frag = new fragment_medicine_list().newInstance(helper,id_victim);
                break;

        }
        return frag;
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
        switch (position) {
            case 0:
                title = "Disease";
                break;
            case 1:
                title = "Allergic";
                break;
            case 2:
                title = "Medicine";
                break;
        }

        return title;
    }
}
