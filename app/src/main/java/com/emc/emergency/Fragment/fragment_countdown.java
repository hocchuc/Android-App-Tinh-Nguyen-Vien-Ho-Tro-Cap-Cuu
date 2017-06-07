package com.emc.emergency.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.emc.emergency.Accident.AccidentActivity;
import com.emc.emergency.Chat.ChatBoxActivity;
import com.emc.emergency.MainMenuActivity;
import com.emc.emergency.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_countdown.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_countdown#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_countdown extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ActionProcessButton btnCancle;
    TextView txtCoundDown;
    private OnFragmentInteractionListener mListener;

    public fragment_countdown() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_countdown.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_countdown newInstance(String param1, String param2) {
        fragment_countdown fragment = new fragment_countdown();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fragment_countdown, container, false);
        btnCancle = (ActionProcessButton) v.findViewById(R.id.btnCancle_Rescue);
        txtCoundDown = (TextView) v.findViewById(R.id.txtCountDown);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnCancle.setMode(ActionProcessButton.Mode.PROGRESS);
        new CountDownTimer(6000, 1000) {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onTick(long millisUntilFinished) {
                txtCoundDown.setText(""+millisUntilFinished / 1000);
                btnCancle.setProgress(Math.toIntExact(millisUntilFinished / 1000)*20);
            }

            public void onFinish() {
                btnCancle.setProgress(0);
                txtCoundDown.setText("Sending request rescue to SOS Center");
                Intent intent = new Intent((MainMenuActivity)getActivity(), ChatBoxActivity.class);
                startActivity(intent);
            }
        }.start();
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitTransition();
                getFragmentManager().popBackStack();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
