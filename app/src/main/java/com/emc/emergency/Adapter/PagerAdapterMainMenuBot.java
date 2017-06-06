package com.emc.emergency.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.emc.emergency.Fragment.fragment_menu_page;
import com.emc.emergency.Fragment.fragment_menu_page2;


/**
 * Created by hocan on 24-Feb-17.
 */

public class PagerAdapterMainMenuBot extends FragmentStatePagerAdapter {

    public PagerAdapterMainMenuBot(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new fragment_menu_page();
                break;
            case 1:
                frag=new fragment_menu_page2();
                break;

        }
        return frag;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title="Menu1";
                break;
            case 1:
                title="Menu2";
                break;
        }

        return title;
    }

}

