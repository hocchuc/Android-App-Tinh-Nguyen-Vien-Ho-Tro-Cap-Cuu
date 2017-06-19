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

import com.emc.emergency.Adapter.MyMedicalInfoRecyclerViewAdapter;
import com.emc.emergency.Adapter.MyPersonalInfoRecyclerViewAdapter;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.Medical_Info;
import com.emc.emergency.model.Personal_Infomation;
import com.emc.emergency.utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class fragment_medical_info_page extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    ArrayList<Medical_Info> arrMI;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String id_pi="ID_PI";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_medical_info_page() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_medical_info_page newInstance(int columnCount) {
        fragment_medical_info_page fragment = new fragment_medical_info_page();
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
        View view = inflater.inflate(R.layout.fragment_medical_info_page, container, false);
        arrMI = new ArrayList<>();
        new GetMedicalInfo(this.getActivity(), arrMI).execute();

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview2);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyMedicalInfoRecyclerViewAdapter(getContext(),arrMI, mListener));

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
        void onListFragmentInteraction(Medical_Info mItem);
    }

    class GetMedicalInfo extends AsyncTask<Void, Void, ArrayList<Medical_Info>> {
        Activity activity;
        ArrayList<Medical_Info> arrMI;

        public GetMedicalInfo(Activity activity, ArrayList<Medical_Info> arrMI) {
            this.activity = activity;
            this.arrMI = arrMI;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrMI.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Medical_Info> medical_infos) {
            super.onPostExecute(medical_infos);
//        arrAccidents.clear();
            arrMI.addAll(medical_infos);
            recyclerView.getAdapter().notifyDataSetChanged();

        }

        @Override
        protected ArrayList<Medical_Info> doInBackground(Void... params) {
            ArrayList<Medical_Info> ds = new ArrayList<>();
            sharedPreferences = getActivity().getSharedPreferences(id_pi, Context.MODE_PRIVATE);
            long id = sharedPreferences.getLong("id_PI", -1);

            Log.d("ID_PI after put:", String.valueOf(id));
            try {
                URL url = new URL(SystemUtils.getServerBaseUrl()+"personal_Infomations/"+id+"/medical_Info");
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
                JSONObject _embeddedObject = jsonObj.getJSONObject("_embedded");
                JSONArray miJsonArr = _embeddedObject.getJSONArray("medical_Infoes");

                Log.d("JsonObject MI", _embeddedObject.toString());
                Log.d("JsonArray MI", miJsonArr.toString());

                for (int i = 0; i < miJsonArr.length(); i++) {
                    Medical_Info medicalInfo = new Medical_Info();
                    JSONObject jsonObject = miJsonArr.getJSONObject(i);
                    if (jsonObject == null) continue;
                    if (jsonObject.has("id_MI"))
                        medicalInfo.setId_MI(Long.parseLong(jsonObject.getString("id_MI")));
                    if (jsonObject.has("name_MI"))
                         medicalInfo.setName_MI(jsonObject.getString("name_MI"));
                    if (jsonObject.has("type_MI"))
                        medicalInfo.setType_MI(jsonObject.getInt("type_MI"));
                    if (jsonObject.has("description"))
                        medicalInfo.setDescriptionMI(jsonObject.getString("description"));
                    ds.add(medicalInfo);
//                     Log.d("DS", ds.toString());
                }
                Log.d("DSMI", ds.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return ds;
        }
    }
}
