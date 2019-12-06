package com.lenovo.smarttraffic.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MyDialog;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutPswd;
    private Context mContext;
    private MyDialog dialog;
    private Button bt_base, bt_cancel;
    private Handler handler = new Handler();
    private EditText[] e_number;


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new MyDialog(this, 0.8, 0.8, R.layout.network_content);

        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.login));

        mTextInputLayoutName = findViewById(R.id.textInputLayoutName);
        mTextInputLayoutPswd = findViewById(R.id.textInputLayoutPassword);

        mEditTextName = findViewById(R.id.editTextName);
        mTextInputLayoutName.setErrorEnabled(true);
        mEditTextPassword = findViewById(R.id.editTextPassword);
        mTextInputLayoutPswd.setErrorEnabled(true);
        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);
        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override/*内容要改变之前调用*/
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*从start位置开始，count个字符（空字符是0）将被after个字符替换*/

            }

            @Override/*内容要改变时调用*/
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本*/
            }

            @Override/*内容要改变之后调用*/
            public void afterTextChanged(Editable s) {
                //这个方法被调用，那么说明s字符串的某个地方已经被改变。
                checkName(s.toString(), false);
            }
        });

        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPwd(s.toString(), false);
            }
        });
    }

    //网络设置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.network_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                IpMyDialog();
                break;
            default:
                break;
        }
        return true;
    }

    private void IpMyDialog() {
        dialog.Do(v -> {
            bt_base = v.findViewById(R.id.btn_ok);
            bt_cancel = v.findViewById(R.id.btn_cancel);
            e_number = new EditText[4];
            int[] ID = {R.id.e_number1, R.id.e_number2, R.id.e_number3, R.id.e_number4};
            String[] ips = Ip.split("\\.");//分段取出IP
            for (int i = 0; i < e_number.length; i++) {
                e_number[i] = dialog.findViewById(ID[i]);
                e_number[i].setHint(ips[i]); //告诉用户这个文本框需要输入的内容是什么。
            }
            MaxNumber(e_number);//完成检查输入，输入位数达到3自动切换下一输入框，删除剩余0自动切换上一输入框
            EditText e_port = dialog.findViewById(R.id.e_port);//端口号
            e_port.setHint(Port);

            bt_base.setOnClickListener(view -> {
                String inputIp = "";
                String inputPort;
                for (int i = 0; i < e_number.length; i++) {
                    if (e_number[i].getText().length() == 0) {
                        inputIp += e_number[i].getHint().toString();
                    } else {
                        inputIp += e_number[i].getText().toString();
                    }
                    if (i < 3) {
                        inputIp += ".";
                    }
                }
                if (e_port.getText().length() > 0) {
                    inputPort = e_port.getText().toString();
                } else {
                    inputPort = e_port.getHint().toString();
                }
                super.SetPort(inputIp, inputPort);
                handler.post(() -> Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show());
                dialog.dismiss();
            });
            bt_cancel.setOnClickListener(view -> handler.post(() ->{
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }));
        }).show();
    }

    /**
     * 完成检查输入，输入位数达到3自动切换下一输入框，删除剩余0自动切换上一输入框
     *
     * @param e
     */
    private void MaxNumber(EditText[] e) {
        for (int i = 0; i < e.length; i++) {
            int finalI = i;
            //addTextChangedListener 文本监听
            e[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        if (charSequence.charAt(charSequence.length() - 1) == '.') {
                            if (finalI < 3 && charSequence.length() > 1) {
                                e[finalI + 1].requestFocus();
                                e[finalI].setText(charSequence.toString().substring(0, charSequence.length() - 1));
                                e[finalI].setSelection(i);
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() == 3 && !editable.toString().contains(".")) {
                        if (finalI < 3) {
                            e[finalI + 1].requestFocus();
                        } else if (editable.length() == 0) {
                            if (finalI > 0) {
                                e[finalI - 1].requestFocus();
                            }
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            hideSoftInput(v);
            if (!checkName(mEditTextName.getText(), true)) {
                return;
            }
            if
            (!checkPwd(mEditTextPassword.getText(), true)) {
                return;
            }
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean checkPwd(CharSequence pswd, boolean isLogin) {
        if (TextUtils.isEmpty(pswd)) {
            if (isLogin) {
                mTextInputLayoutPswd.setError(getString(R.string.error_pwd_empty));
                return false;
            }
        } else {
            mTextInputLayoutPswd.setError(null);
        }
        return true;
    }

    private boolean checkName(CharSequence name, boolean isLogin) {
        if (TextUtils.isEmpty(name)) {
            if (isLogin) {
                mTextInputLayoutName.setError(getString(R.string.error_login_empty));
                return false;
            }
        } else {
            mTextInputLayoutName.setError(null);
        }
        return true;
    }


    /**
     * 隐藏键盘输入法
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) InitApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN
                        || resultCode == InputMethodManager.RESULT_SHOWN) {
                    toggleSoftInput();
                }
            }
        });
    }

    /**
     * 软键盘切换
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) InitApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
