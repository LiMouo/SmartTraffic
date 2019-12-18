package com.lenovo.smarttraffic.LifeAssistant_14;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.ToolbarMaster;

public class LifeAssistantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }

    private void InitView() {
        setContentView(R.layout.activity_life_assistant);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
