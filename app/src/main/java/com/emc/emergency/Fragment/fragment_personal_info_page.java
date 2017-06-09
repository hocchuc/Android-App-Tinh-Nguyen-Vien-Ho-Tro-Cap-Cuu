package com.emc.emergency.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emc.emergency.Adapter.MyAccidentRecyclerViewAdapter;
import com.emc.emergency.Adapter.MyPersonalInfoRecyclerViewAdapter;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.Personal_Infomation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class fragment_personal_info_page extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    ArrayList<Personal_Infomation> arrPI;
        RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String id_user = "ID_USER";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_personal_info_page() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_personal_info_page newInstance(int columnCount) {
        fragment_personal_info_page fragment = new fragment_personal_info_page();
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
        View view = inflater.inflate(R.layout.fragment_personal_info_page, container, false);
        arrPI = new ArrayList<>();
        new GetPersonalInfo(this.getActivity(), arrPI).execute();

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview1);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyPersonalInfoRecyclerViewAdapter(getContext(),arrPI, mListener));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Personal_Infomation mItem);
    }

    class GetPersonalInfo extends AsyncTask<Void, Void, ArrayList<Personal_Infomation>> {
        Activity activity;
        ArrayList<Personal_Infomation> arrPI;

        public GetPersonalInfo(Activity activity, ArrayList<Personal_Infomation> arrPI) {
            this.activity = activity;
            this.arrPI = arrPI;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrPI.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Personal_Infomation> personalInfomations) {
            super.onPostExecute(personalInfomations);
//        arrAccidents.clear();
            arrPI.addAll(personalInfomations);
            recyclerView.getAdapter().notifyDataSetChanged();

        }

        @Override
        protected ArrayList<Personal_Infomation> doInBackground(Void... params) {
            ArrayList<Personal_Infomation> ds = new ArrayList<>();
            sharedPreferences = getActivity().getSharedPreferences(id_user, Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("id_user", -1);

            Log.d("ID_USER after put:", String.valueOf(id));
            try {
                URL url = new URL("https://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/personal_Infomations/"+id);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                JSONObject jsonObj = new JSONObject(builder.toString());
                Log.d("JsonPI: ", jsonObj.toString());
                Personal_Infomation pi;

                String work_location = jsonObj.getString("work_location");
                String birth = jsonObj.getString("birthday");
                String phone=jsonObj.getString("phone_PI");
                Boolean sex = jsonObj.getBoolean("sex__PI");
                String email = jsonObj.getString("email_PI");
                String address = jsonObj.getString("address_PI");
                Long personal_id = jsonObj.getLong("personal_id");
                String name = jsonObj.getString("name_PI");
                pi = new Personal_Infomation(name,sex,birth,personal_id,work_location,phone,address,email);
                Log.d("PI", pi.toString());
                ds.add(pi);
                Log.d("DSPI", ds.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return ds;
        }
    }
}
