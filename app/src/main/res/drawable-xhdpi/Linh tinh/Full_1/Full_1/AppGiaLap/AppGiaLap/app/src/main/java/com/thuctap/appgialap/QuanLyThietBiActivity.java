package com.thuctap.appgialap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.thuctap.adapter.QuanLyThietBiAdapter;
import com.thuctap.adapter.ThietBiAdapter;
import com.thuctap.model.ThietBi;
import com.thuctap.task.LayDanhSachThietBiTask;
import com.thuctap.task.LayDanhSachThietBiTask2;
import com.thuctap.task.ThemThietBiTask;
import com.thuctap.task.XoaThietBiTask;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import static java.security.AccessController.getContext;

public class QuanLyThietBiActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ArrayList<ThietBi> arrThietBi;
    ListView lvThietBi;
    QuanLyThietBiAdapter adapter;
    //ThietBiAdapter thietBiAdapter;
    Button btnThemUser;
    Bitmap bitmap;
    String picArray;
    private ImageView imgHinhThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thiet_bi);
        addControls();
        addEvents();
    }

    private void addControls() {
        arrThietBi =  new ArrayList<>();
        lvThietBi = (ListView) findViewById(R.id.lvThietBi);
        btnThemUser = (Button) findViewById(R.id.btnThemUser);

        adapter =  new QuanLyThietBiAdapter(QuanLyThietBiActivity.this, R.layout.quanlytb_layout, arrThietBi);
        lvThietBi.setAdapter(adapter);

        LayDanhSachThietBiTask2 getDanhSach = new LayDanhSachThietBiTask2(QuanLyThietBiActivity.this,
                arrThietBi,
                adapter);
        getDanhSach.execute();
    }

    private void addEvents() {
        lvThietBi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xuLySuaThietBi(position);
            }
        });

        btnThemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemThietBi();
            }
        });

        lvThietBi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog =  new AlertDialog.Builder(QuanLyThietBiActivity.this);
                dialog.setTitle("Xác nhận!!");
                dialog.setMessage("Bạn có muốn xóa thiết bị này.");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyXoaThietBi(position);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                //xuLyXoaThietBi(position);
                return false;
            }
        });

    }

    private void xuLyXoaThietBi(int position) {
        int id =  arrThietBi.get(position).getMaThietBi();
        XoaThietBiTask task =  new XoaThietBiTask(QuanLyThietBiActivity.this, id);
        task.execute("http://10.0.3.2:8080/RestProject/Rest/thietbisDeleter/DeleteThietBis");
    }

    private void xuLySuaThietBi(int position) {

    }

    private void xuLyThemThietBi() {
        final Dialog d =  new Dialog(this);

        d.setContentView(R.layout.themthietbi_layout);
        d.setTitle("Thêm thiết bị mới: ");
        d.setCanceledOnTouchOutside(false);
        final EditText edtTenThietBi = (EditText) d.findViewById(R.id.edtTenThietBi);
        Button btnThem = (Button) d.findViewById(R.id.btnThem);
        Button btnQuayVe = (Button) d.findViewById(R.id.btnQuayLai);
        imgHinhThem= (ImageView) d.findViewById(R.id.imgHinh1);

        imgHinhThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

            btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat =  new SimpleDateFormat("HH:mm:ss");
                java.util.Date date =  new java.util.Date();
                String tenThietBi =  edtTenThietBi.getText().toString();
                if(tenThietBi.equals("")){
                    Toast.makeText(QuanLyThietBiActivity.this, "Bạn chưa nhập tên thiết bị.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ngayHienTai = dateFormat.format(date);
                String gioHienTai =  timeFormat.format(date);
                ThemThietBiTask task =  new ThemThietBiTask(QuanLyThietBiActivity.this,
                        tenThietBi,
                        ngayHienTai,
                        gioHienTai,
                        picArray,
                        d);
                task.execute("http://10.0.3.2:8080/RestProject/Rest/thietbisCreater/NewThietbis");
            }
        });

        btnQuayVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
//        {
//            if (resultCode == RESULT_OK) {
//                try {
//                    final Uri imageUri = data.getData();
//                    final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
//                    bitmap = BitmapFactory.decodeStream(imageStream);
//                    if(imgHinhThem!=null) imgHinhThem.setImageBitmap(bitmap);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//                }
//
//            }else {
//                Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
//            }
//            try {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] data2 = baos.toByteArray();
//                String strBase64 = android.util.Base64.encodeToString(data2, android.util.Base64.NO_WRAP);
//                //strBase64.replaceAll("\\r\\n|\\r|\\n", " "); // loai bo xuong dong
//                picArray = strBase64;
//                Log.d("picArray",picArray);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
}
