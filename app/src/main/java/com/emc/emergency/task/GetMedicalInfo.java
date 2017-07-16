package com.emc.emergency.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emc.emergency.R;
import com.emc.emergency.model.Medical_Information;
import com.emc.emergency.utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetMedicalInfo extends AsyncTask<Void, Void, ArrayList<Medical_Information>> {
    SharedPreferences sharedPreferences;
    String id_pi="ID_PI";
        Activity activity;
        ArrayList<Medical_Information> arrMI;
        int medical_type=0;
        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;


        public GetMedicalInfo(Activity activity, ArrayList<Medical_Information> arrMI, int medical_type, RecyclerView.Adapter adapter) {
            this.activity = activity;
            this.arrMI = arrMI;
            this.medical_type = medical_type;
            recyclerView = (RecyclerView) activity.findViewById(R.id.RecyclerViewList);
            this.adapter = adapter;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrMI.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Medical_Information> medical_informations) {
            super.onPostExecute(medical_informations);
            arrMI.addAll(medical_informations);
            Log.d("arrMI",arrMI.toString());
            adapter.notifyDataSetChanged();

        }

        @Override
        protected ArrayList<Medical_Information> doInBackground(Void... params) {
            ArrayList<Medical_Information> ds = new ArrayList<>();
            sharedPreferences = activity.getSharedPreferences(id_pi, Context.MODE_PRIVATE);
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
                    Medical_Information medicalInfo = new Medical_Information();
                    JSONObject jsonObject = miJsonArr.getJSONObject(i);
                    if (jsonObject == null) continue;
                    if (jsonObject.has("type_MI"))
                        medicalInfo.setType_MI(jsonObject.getInt("type_MI"));
                    if(medicalInfo.getType_MI()!=medical_type) continue;
                    if (jsonObject.has("id_MI"))
                        medicalInfo.setId_MI(Long.parseLong(jsonObject.getString("id_MI")));
                    if (jsonObject.has("name_MI"))
                         medicalInfo.setName_MI(jsonObject.getString("name_MI"));

                    if (jsonObject.has("description"))
                        medicalInfo.setDescriptionMI(jsonObject.getString("description"));
                    ds.add(medicalInfo);
//                     Log.d("DS", ds.toString());
                }
                if(ds.size()>0) Log.d("DSMI", ds.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return ds;
        }
    }