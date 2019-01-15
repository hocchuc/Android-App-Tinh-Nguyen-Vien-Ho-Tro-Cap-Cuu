package com.emc.emergency.Accidents_List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.Accident;
import com.emc.emergency.Helper.AsyncTask.ReturnDataAllAccident;
import com.emc.emergency.Helper.Utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


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
    public SharedPreferences sharedPreferences2;
    int id_user;
    User user1;
    User_Type user_type;
    MyAccidentRecyclerViewAdapter adapter;
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
        user1 = new User();
        user_type = new User_Type();


        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        sharedPreferences2 = getContext().getSharedPreferences("ID_USER", MODE_PRIVATE);
        id_user = sharedPreferences2.getInt("id_user", -1);

        GetUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAccidentRecyclerViewAdapter(getContext(), accidentList, mListener);
        recyclerView.setAdapter(adapter);

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

    private void GetUser() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "users/" + id_user)
                .get()
                .build();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().string());
                        user1 = new User();
                        user_type = new User_Type();
                        if (jsonObj.has("id_user_type")) {
                            String user_Type = jsonObj.getString("id_user_type");
                            JSONObject jsonObject = new JSONObject(user_Type);
                            if (jsonObject.has("name_user_type"))
                                user_type.setName_user_type(jsonObject.getString("name_user_type"));
                            user1.setUser_type(user_type);
                        }
                        new GetAccidents(getActivity(), accidentList).execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
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
            Log.d("-------onPostExecute", (accidents != null) ? accidents.size() +"" : 0 +"");
            if(accidents == null) return;
            for (Accident accident : accidents) {
                if ((user1.getUser_type().getName_user_type().equals("user")) && (accident.getStatus_AC().equals("Done")))
                    accidentList.add(accident);
                else if (((!user1.getUser_type().getName_user_type().equals("user"))) && (accident.getStatus_AC().equals("Active")))
                    accidentList.add(accident);
            }
            adapter.setList(accidentList);
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        protected ArrayList<Accident> doInBackground(Void... params) {
            ArrayList<Accident> ds = new ArrayList<>();
            try {
                URL url = new URL(SystemUtils.getServerBaseUrl() + "accident/GetAllAccident");
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }

                JSONArray accidentsSONArray = new JSONArray(builder.toString());
                Log.d("JsonArray", accidentsSONArray.toString());

                for (int i = 0; i < accidentsSONArray.length(); i++) {
                    Accident accident = new Accident();
                    JSONObject jsonObject = accidentsSONArray.getJSONObject(i);
                    if (jsonObject == null) continue;
                    if (jsonObject.has("id_AC"))
                        accident.setId_AC(Long.parseLong(jsonObject.getString("id_AC")));
                    if (jsonObject.has("description_AC"))
                        accident.setDescription_AC(jsonObject.getString("description_AC"));
                    if (jsonObject.has("date_AC")) {
                        accident.setDate_AC(jsonObject.getString("date_AC"));
                    }
                    if (jsonObject.has("long_AC"))
                        accident.setLong_AC(jsonObject.getDouble("long_AC"));
                    if (jsonObject.has("lat_AC"))
                        accident.setLat_AC(jsonObject.getDouble("lat_AC"));
                    if (jsonObject.has("status_AC"))
                        accident.setStatus_AC(jsonObject.getString("status_AC"));
                    if (jsonObject.has("address"))
                        accident.setAddress(jsonObject.getString("address"));
                    if (jsonObject.has("firebaseKey"))
                        accident.setFirebaseKey(jsonObject.getString("firebaseKey"));
                    if (jsonObject.has("id_victim"))
                        accident.setId_user(jsonObject.getLong("id_victim"));
                    if (jsonObject.has("request_AC"))
                        accident.setRequest_AC(jsonObject.getBoolean("request_AC"));
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

//    public void displayAccidentList(ArrayList<Accident> accidents) {
//        accidentList.addAll(accidents);
//        recyclerView.getAdapter().notifyDataSetChanged();
//    }
}
