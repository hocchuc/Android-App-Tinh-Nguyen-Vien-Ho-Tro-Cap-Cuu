package com.emc.emergency.Personal_Information;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.emc.emergency.Helper.AsyncTask.GetAllUser;
import com.emc.emergency.Helper.AsyncTask.ReturnDataAllUser;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.Utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_contract.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_contract#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_contract extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvContract;
    private CheckBox ckbYes, ckbNo;
    private Button btnSend;
    int id_user;
    User user;
    ArrayList<User> userList;
    Boolean flag;

    SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public fragment_contract() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_contract.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_contract newInstance(String param1, String param2) {
        fragment_contract fragment = new fragment_contract();
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
        final View view = inflater.inflate(R.layout.fragment_fragment_contract, container, false);

//        Log.d("id_user_volunteer", String.valueOf(id_user));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "users")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                userList = new ArrayList<>();
                JSONArray usersJSONArray = null;

                sharedPreferences = getActivity().getSharedPreferences("ID_USER", MODE_PRIVATE);
                id_user = sharedPreferences.getInt("id_user", -1);

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject _embeddedObject = jsonObject.getJSONObject("_embedded");
                    usersJSONArray = _embeddedObject.getJSONArray("users");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("jsonObj", jsonObject.toString());
                for (int i = 0; i < usersJSONArray.length(); i++) {
                    User user1 = new User();
                    try {
                        JSONObject jsonObj = usersJSONArray.getJSONObject(i);
                        if (jsonObj.has("id_user"))
                            user1.setId_user(Long.parseLong((jsonObj.getString("id_user"))));
                        if (jsonObj.has("username"))
                            user1.setUser_name(jsonObj.getString("username"));
                        if (jsonObj.has("token"))
                            user1.setToken(jsonObj.getString("token"));
                        if (jsonObj.has("password"))
                            user1.setPassword(jsonObj.getString("password"));
                        if (jsonObj.has("long_PI"))
                            user1.setLong_PI(jsonObj.getDouble("long_PI"));
                        if (jsonObj.has("lat_PI"))
                            user1.setLat_PI(jsonObj.getDouble("lat_PI"));
                        if (jsonObj.has("is_signup_volunteer"))
                            user1.setId_signup_volumteer(jsonObj.getBoolean("is_signup_volunteer"));
                        if (jsonObj.has("id_user_type")) {
                            String user_type = jsonObj.getString("id_user_type");
                            User_Type user_type1 = new User_Type();
                            try {
                                JSONObject jsonObject1 = new JSONObject(user_type);
                                if (jsonObject1.has("id_user_type"))
                                    user_type1.setId_user_type(jsonObject1.getLong("id_user_type"));
                                if (jsonObject1.has("name_user_type"))
                                    user_type1.setName_user_type(jsonObject1.getString("name_user_type"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            user1.setUser_type(user_type1);
                        }
//                    Log.d("User1", user1.toString());
                        userList.add(user1);
                        Log.d("DS_User", userList.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = true;
                        for (int j = 0; j < userList.size(); j++) {
                            if ((userList.get(j).getId_user() == id_user) && (!userList.get(j).getId_signup_volumteer())) {
                                if (ckbYes.isChecked()) {
                                    OkHttpClient client = new OkHttpClient();

                                    MediaType mediaType = MediaType.parse("application/json");
                                    RequestBody body = RequestBody.create(mediaType, "{\n\t\"is_signup_volunteer\": " + String.valueOf(user.getId_signup_volumteer()) + "\n}");
                                    Request request = new Request.Builder()
                                            .url(SystemUtils.getServerBaseUrl() + "users/" + id_user)
                                            .patch(body)
                                            .addHeader("content-type", "application/json")
                                            .build();
                                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                    if (SDK_INT > 8) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);

                                        try {
                                            Response response = client.newCall(request).execute();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        getActivity().finish();
                                    }
                                } else
                                    Toast.makeText(getActivity(), "Chưa chọn.!" + String.valueOf(user.getId_signup_volumteer()), Toast.LENGTH_SHORT).show();
                            } else flag = false;
                        }
                        if(!flag) Toast.makeText(getActivity(), "Bạn đã đăng ký làm tình nguyện viên. Hãy đợi duyệt.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        String contract = "Tình nguyện viên không được trả lương và được hưởng những quyền lợi sau:\n"
                + " - Trợ cấp ổn định nơi ở, tính theo thời gian thực hiện nhiệm vụ.\n"
                + " - Khoản trợ cấp này được trả ngay từ khi công việc bắt đầu.\n"
                + " - Nhận hỗ trợ sinh hoạt phí dành cho tình nguyện viên hàng tháng.\n"
                + " - Được hưởng bảo hiểm thương tật vĩnh viễn, bảo hiểm y tế và nhân thọ.\n"
                + " - Nghỉ phép hai ngày rưỡi trong một tháng.\n"
                + " - Nhận trợ cấp tái ổn định cuộc sống khi hoàn tất tốt nhiệm vụ.";

        user = new User();
        user.setId_signup_volumteer(true);

        btnSend = (Button) view.findViewById(R.id.btnSend);
        tvContract = (TextView) view.findViewById(R.id.tvContract);
        tvContract.setText(contract);
        ckbNo = (CheckBox) view.findViewById(R.id.ckbNo);
        ckbYes = (CheckBox) view.findViewById(R.id.ckbYes);

        return view;
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
