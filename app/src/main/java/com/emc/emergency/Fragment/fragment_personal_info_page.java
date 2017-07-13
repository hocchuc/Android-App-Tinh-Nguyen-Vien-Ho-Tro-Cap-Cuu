package com.emc.emergency.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emc.emergency.R;
import com.emc.emergency.model.Personal_Infomation;
import com.emc.emergency.utils.SystemUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


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
    SharedPreferences sharedPreferences;
    String id_user = "ID_USER";

    SharedPreferences preferences1;
    String id_pi="ID_PI";
    private int id;
    Long idPI;
    public Personal_Infomation mItem;
    Bitmap bitmap = null;
    public  EditText txtNamePI;
    public  EditText txtEmailPI;
    public  EditText txtBirthdayPI;
    public  EditText txtPID;
    public  EditText txtWKPI;
    public  EditText txtPhonePI;
    public  EditText txtAddressPI;
    public  ImageView imgV;
    public  RadioButton radMale;
    public  RadioButton radFeMale;
    public ImageView mImageSex;
    public  FloatingActionButton btnEdit;
    /** Biến để chọn và chụp hình  */
    private static final int REQUEST_CHOOSE_PHOTO = 123;
    private static final int RESQUEST_TAKE_PHOTO = 321;
//    //Image properties
//    private String mCurrentImagePath = null;
//    private Uri mCapturedImageURI = null;
    private StorageReference imagesRef;
    /* biến dùng cho firebase */
    FirebaseStorage storage;
    StorageReference storageRef ;
    Uri uriAvatar = null;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            TextView tbTitle = (TextView) getActivity().findViewById(R.id.toolbar_title2);
            tbTitle.setText(R.string.Personal_info);
        }
    }

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
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        View view = inflater.inflate(R.layout.fragment_personal_info_page, container, false);
        txtNamePI = (EditText) view.findViewById(R.id.txtNamePI);
        txtEmailPI= (EditText) view.findViewById(R.id.txtEmailPI);
        txtBirthdayPI= (EditText) view.findViewById(R.id.txtBirthdayPI);
        txtPID= (EditText) view.findViewById(R.id.txtPI_ID);
        txtAddressPI= (EditText) view.findViewById(R.id.txtAddressPI);
        txtWKPI= (EditText) view.findViewById(R.id.txtWorkLocationPI);
        txtPhonePI= (EditText) view.findViewById(R.id.txtPhonePI);
        radFeMale= (RadioButton) view.findViewById(R.id.radFeMale);
        radMale= (RadioButton) view.findViewById(R.id.radMale);
        imgV = (ImageView) view.findViewById(R.id.imageItemHinh);
        btnEdit= (FloatingActionButton) view.findViewById(R.id.btnEditPI);
        mImageSex = (ImageView) view.findViewById(R.id.imageSex);
        new GetPersonalInfo(this.getActivity(), mItem).execute();

        imgV.setImageResource(R.drawable.profile3);
        imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyCustomDialog();

            }
        });
        imgV.setEnabled(false);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtNamePI.isEnabled()) {
                    txtEmailPI.setEnabled(true);
                    txtBirthdayPI.setEnabled(true);
                    txtPID.setEnabled(true);
                    txtAddressPI.setEnabled(true);
                    txtWKPI.setEnabled(true);
                    txtPhonePI.setEnabled(true);
                    txtNamePI.setEnabled(true);
                    imgV.setEnabled(true);
                    btnEdit.setImageResource(R.drawable.ic_save);
                    radFeMale.setVisibility(View.VISIBLE);
                    radMale.setVisibility(View.VISIBLE);

                }
                else {
                    Personal_Infomation pi1 = new Personal_Infomation();
                    pi1.setName_PI(txtNamePI.getText().toString());
                    pi1.setEmail_PI(txtEmailPI.getText().toString());
                    pi1.setPersonal_id(txtPID.getText().toString());
                    pi1.setAddress_PI(txtAddressPI.getText().toString());
                    pi1.setBirthday(txtBirthdayPI.getText().toString());
                    pi1.setPhone_PI(txtPhonePI.getText().toString());
                    pi1.setWork_location(txtWKPI.getText().toString());
//                    pi1.setAvatar(imgV.toString());
                    if (radMale.isChecked())
                    {
                        pi1.setSex__PI(true);
                    }
                    else pi1.setSex__PI(false);

                    Gson gson = new Gson();
                    String json = gson.toJson(pi1);

                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, json);
                    Request request = new Request.Builder()
                            .url(SystemUtils.getServerBaseUrl() + "personal_Infomations/" + idPI)
                            .put(body)
                            .addHeader("content-type", "application/json")
                            .build();


                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        try {
                            Response response = client.newCall(request).execute();
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
//                        Log.d("reponsePI_PUT",response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        });


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

    /**
     * Hiện dialog để chụp hình
     */
    private void xuLyCustomDialog() {
    // MaterialDialog
         new MaterialDialog.Builder(getContext())
                .title(R.string.SelectImage)
                .items(R.array.SelectImage)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0: {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
                                dialog.cancel();
                            }
                            case 1: {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
                                dialog.cancel();
                            }
                        }
                        }

                })
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // xu ly intent tra ve tu trinh gallery va camera
        try {
            if(resultCode == RESULT_OK){ // neu ket qua ok
                if(requestCode == REQUEST_CHOOSE_PHOTO){
                    try {
                        Uri imageUri = data.getData();
                        InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                        bitmap = BitmapFactory.decodeStream(is);
                        imgV.setImageBitmap(bitmap);
                        sendImageToFirebase(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else if(requestCode == RESQUEST_TAKE_PHOTO){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgV.setImageBitmap(bitmap);
                    sendImageToFirebase(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendImageToFirebase(Bitmap image) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // todo Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                sendPatchAvatarToPI(downloadUrl);

            }
        });
    }
    public void sendPatchAvatarToPI(Uri link){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        String strRequest = "{\n      \"avatar\": \""+link+"\"\n}";
        Log.d("PatchBody",strRequest);
        RequestBody body = RequestBody.create
                (mediaType, strRequest);
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl()+"personal_Infomations/"+idPI)
                .patch(body)
                .build();
        Log.d("PatchURL",SystemUtils.getServerBaseUrl()+"personal_Infomations/"+idPI);
        // TODO: 21-Jun-17 kiểm soát lỗi từ responge
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] getByteArrayFromImageView(ImageView imgv){
        // ham xu ly anh tu imageView sang BitmapDrawble => byte[]
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // COMPRESS to JPEG LOW QUALITY
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    /**
     * Hàm get thông tin personal_info đổ vào forrm
     */
    private class GetPersonalInfo extends AsyncTask<Void, Void,Personal_Infomation> {
        Activity activity;
        Personal_Infomation pi;

        public GetPersonalInfo(Activity activity, Personal_Infomation pi) {
            this.activity = activity;
            this.pi = pi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Personal_Infomation pi) {
            super.onPostExecute(pi);

            try {

                mItem = pi;
                imagesRef = storageRef.child("images/"+id+".jpg");
                txtNamePI.setText(pi.getName_PI().toString());
                txtEmailPI.setText(pi.getEmail_PI().toString());
                txtPhonePI.setText((pi.getPhone_PI().toString()));
                txtWKPI.setText((pi.getWork_location().toString()));
                txtAddressPI.setText(pi.getAddress_PI().toString());
                txtBirthdayPI.setText(pi.getBirthday().toString());
                txtPID.setText(pi.getPersonal_id().toString());

                imagesRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriAvatar = uri;
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .placeholder(R.drawable.profile3)
                                .error(R.drawable.material_drawer_circle_mask)
                                .priority(Priority.HIGH);
                        try {
                            Glide.with(getActivity()).load(uriAvatar).apply(options).into(imgV);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        Log.d("getDownloadUrlSuccess",uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                     e.printStackTrace();

                    }
                });

                try {
                    if(pi.getSex__PI()){
                        radMale.toggle();
                        mImageSex.setImageResource(R.drawable.icon_boy);

                    }
                    else {
                        radFeMale.toggle();
                        mImageSex.setImageResource(R.drawable.icon_girl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Personal_Infomation doInBackground(Void... params) {
            sharedPreferences = getActivity().getSharedPreferences(id_user, MODE_PRIVATE);
            id = sharedPreferences.getInt("id_user", -1);

//            Log.d("ID_USER after put:", String.valueOf(id));
            try {
                URL url = new URL(SystemUtils.getServerBaseUrl()+"users/"+id+"/personal_Infomation");
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
//                Log.d("JsonPI: ", jsonObj.toString());
                pi=new Personal_Infomation();

                if(jsonObj.has("work_location"))
                    pi.setWork_location(jsonObj.getString("work_location"));
                if(jsonObj.has("birthday"))
                   pi.setBirthday(jsonObj.getString("birthday"));
                if(jsonObj.has("phone_PI"))
                    pi.setPhone_PI(jsonObj.getString("phone_PI"));
                if(jsonObj.has("sex__PI"))
                    pi.setSex__PI(jsonObj.getBoolean("sex__PI"));
                if(jsonObj.has("email_PI"))
                    pi.setEmail_PI(jsonObj.getString("email_PI"));
                if(jsonObj.has("address_PI"))
                    pi.setAddress_PI(jsonObj.getString("address_PI"));
                if(jsonObj.has("personal_id"))
                    pi.setPersonal_id(jsonObj.getString("personal_id"));
                if(jsonObj.has("name_PI"))
                    pi.setName_PI(jsonObj.getString("name_PI"));
                if(jsonObj.has("id_PI")){
                    pi.setId_PI(jsonObj.getLong("id_PI"));
                    idPI=jsonObj.getLong("id_PI");
//                    Log.d("idPI",idPI.toString());

                    /* viết id_pi vào preferent */
                    preferences1 = getActivity().getSharedPreferences(id_pi, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putLong("id_PI",idPI);
                    editor.apply();
//                    Log.d("editorPI",editor.toString());

                }
//                Log.d("PI", pi.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return pi;
        }
    }

}
