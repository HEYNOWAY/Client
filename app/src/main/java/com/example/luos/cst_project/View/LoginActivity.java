package com.example.luos.cst_project.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Presenter.ILoginPresenter;
import com.example.luos.cst_project.Presenter.ILoginPresenterCompl;
import com.example.luos.cst_project.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    public static final String TAG = "LoginActivity";
    private EditText usernameEdt;
    private EditText passwordEdt;
    private Button loginBtn;
    private ProgressBar progressbar;
    private CheckBox saveUserChk;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ILoginPresenter loginPresenter;

    private String username;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()....");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()...");
        super.onDestroy();
    }

    private void Init() {
        //初始化控件
        usernameEdt = (EditText) findViewById(R.id.username);
        passwordEdt = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        saveUserChk = (CheckBox) findViewById(R.id.auto_save_password);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        //初始化设置
        setSaveUser();
        setPresenter();
        setActionBarHide();
        setProgressbarVisible(View.INVISIBLE);

        //监听
        loginBtn.setOnClickListener(this);
    }

    /**
     * 保存用户账户和密码到Preferences里面
     * 用来记住账号
     */
    private void setSaveUser() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = sharedPreferences.getBoolean("isRememberPassword", false);
        if (isRemember) {
            String get_account = sharedPreferences.getString("account", "");
            String get_password = sharedPreferences.getString("password", "");
            usernameEdt.setText(get_account);
            passwordEdt.setText(get_password);
            saveUserChk.setChecked(true);
        }
    }

    private void setPresenter() {
        loginPresenter = new ILoginPresenterCompl(this);
    }

    private void setActionBarHide() {
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Log.i(TAG, "onClick()..." + R.id.login);
                username = usernameEdt.getText().toString();
                password = passwordEdt.getText().toString();

                if (isAccountAndPswValue()) {
                    setProgressbarVisible(View.VISIBLE);
                    editor = sharedPreferences.edit();
                    if (saveUserChk.isChecked()) {
                        editor.putBoolean("isRememberPassword", true);
                        editor.putString("account", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.commit();
                    loginPresenter.doLogin(username, password);
                }

                break;
        }
    }

    public boolean isAccountAndPswValue() {
        if (usernameEdt.getText().toString().trim().equals("")) {
            usernameEdt.setError("用户名不能为空");
            return false;
        } else if (passwordEdt.getText().toString().trim().equals("")) {
            passwordEdt.setError("密码不能为空");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void processMessage(Message msg) {
        Log.i(TAG, "processMessga()...");
        switch (msg.what) {
            case Config.LOGIN_SUCCESS:
                Log.i(TAG, "login success...");
                loginPresenter.getOffMsg(LoginActivity.self.getUserID());
                Intent intent = new Intent(this, FriendListActivity.class);
                startActivity(intent);
                break;

            case Config.LOGIN_FAILED:
                Log.i(TAG, "login failed...");
                clearText();
                makeTextShort("用户名或密码错误");
                setProgressbarVisible(View.INVISIBLE);
                break;

            case Config.SEND_NOTIFICATION:
                Log.i(TAG, "send notificaiton...");
                Bundle bundle = msg.getData();
                ArrayList data = bundle.getParcelableArrayList("msgList");
                List<DataFrame.PersonalMsg> msgList = (List<DataFrame.PersonalMsg>) data.get(0);
                loginPresenter.saveMessageToDb(msgList);
                sendNotifycation(msgList);
                break;
        }
    }

    public void clearText() {
        usernameEdt.setText("");
        passwordEdt.setText("");
    }

    @Override
    public void makeToast(String text) {
        makeTextShort(text);
    }

    @Override
    public void setProgressbarVisible(int visibility) {
        progressbar.setVisibility(visibility);
    }


}
