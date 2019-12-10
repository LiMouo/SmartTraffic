package com.lenovo.smarttraffic.ViolationHome;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;

public class Violation_Result extends AppCompatActivity {

    private RecyclerView RV_left,RV_right;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }

    private void InitView() {
        setContentView(R.layout.activity_violation__result);
        RV_left = findViewById(R.id.RV_left);
        RV_right = findViewById(R.id.RV_right);
        String FirstNumber =getIntent().getExtras().getString("carnumber");
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
    }
    private void InitData() {
    }
}
