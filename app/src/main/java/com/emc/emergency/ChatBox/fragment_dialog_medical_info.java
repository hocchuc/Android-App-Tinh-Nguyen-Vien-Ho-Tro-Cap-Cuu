package com.emc.emergency.ChatBox;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emc.emergency.Helper.Model.Medical_Information;
import com.emc.emergency.Personal_Information.PagerAdapterMI;
import com.emc.emergency.R;

import java.util.ArrayList;


public class fragment_dialog_medical_info extends DialogFragment implements ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener{
    ViewPager mViewPager;
    ArrayList<Medical_Information> arrMI;
    Long id_victim = -99L;
    public fragment_dialog_medical_info() {
        // Required empty public constructor
    }


    public static fragment_dialog_medical_info newInstance(Long id_victim) {
        fragment_dialog_medical_info fragment = new fragment_dialog_medical_info();
        Bundle args = new Bundle();
        args.putLong("id_victim",id_victim);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_DeviceDefault_Light_Dialog);
        if(!getArguments().isEmpty()) {
            this.id_victim = getArguments().getLong("id_victim");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.pager_layout, container, false);
        getDialog().setTitle("Medical Info");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabDots);

        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapterMI pagerAdapterMI = null;
        if(id_victim!=-99L)
        pagerAdapterMI = new PagerAdapterMI(getChildFragmentManager(),true,id_victim);
        else pagerAdapterMI = new PagerAdapterMI(getChildFragmentManager());
        arrMI = new ArrayList<>();
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager2);
        mViewPager.setAdapter(pagerAdapterMI);
        tabLayout.setupWithViewPager(mViewPager, true);


        mViewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
