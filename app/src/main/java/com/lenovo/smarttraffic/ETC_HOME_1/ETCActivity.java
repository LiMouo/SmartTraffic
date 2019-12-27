package com.lenovo.smarttraffic.ETC_HOME_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.ToolbarMaster;

import org.w3c.dom.Text;

public class ETCActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mMenu;
    private TextView mShow_money;
    private Spinner mSpinner;
    private EditText mIn_money;
    private Button mBtn_query,mBtn_inmoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_t_c);
        InitView();
    }

    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ToolbarMaster.MenuCreate();
        ToolbarMaster.setTitle("我的账户");
        mShow_money = findViewById(R.id.show_money);
        mSpinner = findViewById(R.id.spinner);
        mIn_money = findViewById(R.id.in_money);
        mBtn_query = findViewById(R.id.btn_query);
        mBtn_inmoney = findViewById(R.id.btn_inmoney);

    }

    @Override
    public void onClick(View v) {

    }
}
