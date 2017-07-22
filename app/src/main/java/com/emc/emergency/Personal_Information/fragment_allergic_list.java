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
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.emc.emergency.R;

import com.emc.emergency.Helper.Model.Medical_Information;
import com.emc.emergency.Helper.AsyncTask.GetMedicalInfo;
import com.emc.emergency.Helper.AsyncTask.InsertMedicalInfo;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class fragment_allergic_list extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    ArrayList<Medical_Information> arrMI;
    GetMedicalInfo getMedicalInfo;
    FloatingActionButton floatingActionButton;
    String nameMI,descriptionMI ="";
    EditText edtNameMI, edtDesMI;
    MaterialDialog dialog;
    RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_allergic_list() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_allergic_list newInstance(int columnCount) {
        fragment_allergic_list fragment = new fragment_allergic_list();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_allergic_list, container, false);
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
        getMedicalInfo = new GetMedicalInfo(this.getActivity(),arrMI,2,recyclerView.getAdapter());
        getMedicalInfo.execute();
        dialog=  new MaterialDialog.Builder(getContext())
                .title("Nhập thông tin dị ứng")
                .inputType(InputType.TYPE_CLASS_TEXT )
                .customView(R.layout.dialog_medical_info, true)
                .negativeText(android.R.string.cancel)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        nameMI=edtNameMI.getText().toString();
                        descriptionMI = edtDesMI.getText().toString();
                        if(nameMI.matches("")) Toast.makeText(getContext(), "Bạn phải điền tên dị ứng", Toast.LENGTH_SHORT).show();
                        InsertMedicalInfo medicalInfo = new InsertMedicalInfo(getActivity(),nameMI,"2",descriptionMI,recyclerView.getAdapter());
                        medicalInfo.excuteInsert();

                        Medical_Information medical_information = new Medical_Information(nameMI,2,descriptionMI);
                        arrMI.add(medical_information);
                        recyclerView.getAdapter().notifyItemInserted(arrMI.size()-1);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
