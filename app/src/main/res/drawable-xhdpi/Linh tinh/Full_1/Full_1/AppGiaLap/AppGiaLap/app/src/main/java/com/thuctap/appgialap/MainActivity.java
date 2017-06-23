package com.thuctap.appgialap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.thuctap.adapter.QuanLyThietBiAdapter;
import com.thuctap.adapter.ThietBiAdapter;
import com.thuctap.model.ThietBi;
import com.thuctap.task.LayDanhSachThietBiTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvThietBi;
    ArrayList<ThietBi> arrThietBi;
    ThietBiAdapter thietBiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        LayDanhSachThietBiTask task =  new LayDanhSachThietBiTask(MainActivity.this, arrThietBi, thietBiAdapter);
        task.execute();

        lvThietBi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(MainActivity.this, ManHinhDieuKhienActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("thietbi", arrThietBi.get(position));
                intent.putExtra("tb", bundle);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        lvThietBi = (ListView) findViewById(R.id.lvThietBi);
        arrThietBi =  new ArrayList<>();
        thietBiAdapter = new ThietBiAdapter(MainActivity.this, R.layout.thietbi_layout, arrThietBi);
        lvThietBi.setAdapter(thietBiAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuQuanLyThietBi:
                Intent intent =  new Intent(MainActivity.this, QuanLyThietBiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("arr", arrThietBi);
                intent.putExtra("tb", bundle);
                startActivity(intent);
                break;
            case R.id.mnuQuanLyUser:
                Intent intent1 =  new Intent(MainActivity.this, QuanLyNguoiDungActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
