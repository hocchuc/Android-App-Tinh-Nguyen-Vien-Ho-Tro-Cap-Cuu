package com.emc.emergency.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.emc.emergency.model.Accident;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Admin on 16/6/2017.
 */
public class GetAccidents extends AsyncTask<Void, Void, ArrayList<Accident>> {
    Activity activity;
    ArrayList<Accident> arrAccidents;

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
                // Log.d("DS", ds.toString());
            }
            Log.d("ds", ds.toString());
        } catch (Exception ex) {
            Log.e("LOI ", ex.toString());
        }
        return ds;
    }
}
