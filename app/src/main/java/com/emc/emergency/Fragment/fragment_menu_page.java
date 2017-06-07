package com.emc.emergency.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.emc.emergency.Accident.AccidentActivity;

import com.emc.emergency.MainMenuActivity;
import com.emc.emergency.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_menu_page.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_menu_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_menu_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnAccident,btnRequest, btnReport, btnPersonalInfomation;
    private onFragmentMenu1Interaction mListener;

    public fragment_menu_page() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_menu_page.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_menu_page newInstance(String param1, String param2) {
        fragment_menu_page fragment = new fragment_menu_page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((MainMenuActivity)getActivity(), AccidentActivity.class);
                startActivity(intent);
            }
        });
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_countdown fragmentCountdown = new fragment_countdown();
                FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(fragmentCountdown,"Count down");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((MainMenuActivity)getActivity(), AccidentActivity.class);
                startActivity(intent);
            }
        });
        btnPersonalInfomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((MainMenuActivity)getActivity(), AccidentActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fragment_menu_page, container, false);
        btnPersonalInfomation = (Button) v.findViewById(R.id.btnPI);
        btnReport = (Button) v.findViewById(R.id.btnReportAccident);
        btnRequest = (Button) v.findViewById(R.id.btnRequestRescue);
        btnAccident = (Button) v.findViewById(R.id.button_accident);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentMenu1Interaction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentMenu1Interaction) {
            mListener = (onFragmentMenu1Interaction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onFragmentMenu1Interaction {
        // TODO: Update argument type and name
        public void onFragmentMenu1Interaction(Uri uri);
    }
}
