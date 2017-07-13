package com.emc.emergency.task;

import android.os.AsyncTask;
import android.util.Log;

import com.emc.emergency.model.Accident;
import com.emc.emergency.utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Admin on 13/7/2017.
 */

public class GetAllAccident extends AsyncTask<Void, Void, ArrayList<Accident>> {
    ArrayList<Accident> arrAccidents = new ArrayList<>();
    private ReturnDataAllAccident returnDataAllAccident = null;

    public GetAllAccident(ReturnDataAllAccident returnDataAllAccident) {
        this.returnDataAllAccident = returnDataAllAccident;
    }

    @Override
    protected void onPostExecute(ArrayList<Accident> accidents) {
        returnDataAllAccident.handleReturnDataAllAccident(accidents);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        arrAccidents.clear();
    }

    @Override
    protected ArrayList<Accident> doInBackground(Void... params) {
        ArrayList<Accident> ds = new ArrayList<>();
        try {
            URL url = new URL(SystemUtils.getServerBaseUrl() + "accidents");
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
                    accident.setLong_AC(jsonObject.getDouble("long_AC"));
                if (jsonObject.has("lat_AC"))
                    accident.setLat_AC(jsonObject.getDouble("lat_AC"));
                if (jsonObject.has("status_AC"))
                    accident.setStatus_AC(jsonObject.getString("status_AC"));
                if (jsonObject.has("address"))
                    accident.setAddress(jsonObject.getString("address"));
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
