package com.lenovo.smarttraffic.BusQueryCatalog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.lenovo.smarttraffic.R;

public class BusQueryActivity extends AppCompatActivity {

    private ExpandableListView EL_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }

    private void InitView() {
        setContentView(R.layout.activity_bus_query);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EL_main = findViewById(R.id.EL_main);
    }
}
