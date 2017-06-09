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
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;

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
public class fragment_accident_page extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    ArrayList<Accident> accidentList;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String id_user="ID_USER";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_accident_page() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_accident_page newInstance(int columnCount) {
        fragment_accident_page fragment = new fragment_accident_page();
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
        View view = inflater.inflate(R.layout.fragment_accident_page, container, false);
        accidentList = new ArrayList<>();

        new GetAccidents(getActivity(), accidentList).execute();

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyAccidentRecyclerViewAdapter(getContext(),accidentList, mListener));

            //Log.d("listtruocasyn", accidentList.toString());

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
        void onListFragmentInteraction(Accident item);
    }

    class GetAccidents extends AsyncTask<Void, Void, ArrayList<Accident>> {
        Activity activity;
        ArrayList<Accident> arrAccidents;
//    AccidentAdapter accidentsAdapter;

        public GetAccidents(Activity activity, ArrayList<Accident> arrAccidents) {
            this.activity = activity;
            this.arrAccidents = arrAccidents;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrAccidents.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Accident> accidents) {
            super.onPostExecute(accidents);
//        arrAccidents.clear();
            arrAccidents.addAll(accidents);
            recyclerView.getAdapter().notifyDataSetChanged();

        }

        @Override
        protected ArrayList<Accident> doInBackground(Void... params) {
            ArrayList<Accident> ds = new ArrayList<>();
            try {
                URL url = new URL("https://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/accidents");
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
                JSONArray accidentsSONArray = _embeddedObject.getJSONArray("accidents");

                Log.d("JsonObject", _embeddedObject.toString());
                Log.d("JsonArray", accidentsSONArray.toString());

                for (int i = 0; i < accidentsSONArray.length(); i++) {
                    Accident accident = new Accident();
                    JSONObject jsonObject = accidentsSONArray.getJSONObject(i);
                    if (jsonObject == null) continue;
                    if (jsonObject.has("id_AC"))
                        accident.setId_AC(Long.parseLong(jsonObject.getString("id_AC")));
                    if (jsonObject.has("description_AC"))
                        accident.setDescription_AC(jsonObject.getString("description_AC"));
                    if (jsonObject.has("date_AC"))
                        accident.setDate_AC(jsonObject.getString("date_AC"));
                    if (jsonObject.has("long_AC"))
                        accident.setLong_AC((float) jsonObject.getDouble("long_AC"));
                    if (jsonObject.has("lat_AC"))
                        accident.setLat_AC((float) jsonObject.getDouble("lat_AC"));
                    if (jsonObject.has("status_AC"))
                        accident.setStatus_AC(jsonObject.getString("status_AC"));
                    if (jsonObject.has("adress"))
                        accident.setAddress(jsonObject.getString("adress"));
                    // Log.d("Accident", accident.toString());
                    ds.add(accident);
//                     Log.d("DS", ds.toString());
                }
                Log.d("ds", ds.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return ds;
        }
    }
}
