package com.emc.emergency.Personal_Information;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.emc.emergency.R;

import com.emc.emergency.Helper.Model.Medical_Information;
import com.emc.emergency.Helper.AsyncTask.GetMedicalInfo;
import com.emc.emergency.Helper.AsyncTask.InsertMedicalInfo;

import java.util.ArrayList;


public class fragment_disease_list extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
   // private OnListFragmentInteractionListener mListener;
    ArrayList<Medical_Information> arrMI;
    GetMedicalInfo getMedicalInfo;
    FloatingActionButton floatingActionButton;
    String nameMI,descriptionMI ="";
    TextInputEditText edtNameMI, edtDesMI;
    MaterialDialog dialog;
    RecyclerView recyclerView;
    TextView tbTitle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_disease_list() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_disease_list newInstance(int columnCount) {
        fragment_disease_list fragment = new fragment_disease_list();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);
        arrMI = new ArrayList<Medical_Information>();
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewList);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(context);
        mlayoutManager.setReverseLayout(true);
        mlayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setAdapter(new MyMedicalInfoRecyclerViewAdapter(getContext(),arrMI));

        getMedicalInfo = new GetMedicalInfo(this.getActivity(),arrMI,1,recyclerView.getAdapter());
        getMedicalInfo.execute();
        dialog=  new MaterialDialog.Builder(getContext())
                .title("Nhập thông tin bệnh")
                .inputType(InputType.TYPE_CLASS_TEXT )
                .customView(R.layout.dialog_medical_info, true)
                .negativeText(android.R.string.cancel)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        nameMI=edtNameMI.getText().toString();
                        descriptionMI = edtDesMI.getText().toString();
                        if(nameMI.matches("")) Toast.makeText(getContext(), "Bạn phải điền tên bệnh", Toast.LENGTH_SHORT).show();
                        InsertMedicalInfo medicalInfo = new InsertMedicalInfo(getActivity(),nameMI,"1",descriptionMI, recyclerView.getAdapter());
                        medicalInfo.excuteInsert();

                        Medical_Information medical_information = new Medical_Information(nameMI,1,descriptionMI);
                        arrMI.add(medical_information);
                        recyclerView.getAdapter().notifyItemInserted(arrMI.size()-1);



                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.show();
            }
        });
        edtNameMI = (TextInputEditText) dialog.getCustomView().findViewById(R.id.edtNameMI);
        edtDesMI = (TextInputEditText) dialog.getCustomView().findViewById(R.id.edtDescriptionMI);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(DummyItem item);
//    }
}
