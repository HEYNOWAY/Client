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

public class LoginActivity extends BaseActivity implements ILoginView,View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button login_btn;
    private ProgressBar progressbar;
    private CheckBox save_user;
    private static final String TAG = "LoginActivity";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private ILoginPresenter loginPresenter;

    private String username_text;
    private String password_text;
    boolean showProgressbar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate()....");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        Init();
        loginPresenter = new ILoginPresenterCompl(this);
    }

    private void Init() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login_btn = (Button) findViewById(R.id.login);
        save_user = (CheckBox) findViewById(R.id.auto_save_password);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        login_btn.setOnClickListener(this);
        setProgressbarVisible(View.INVISIBLE);
        setSave_user();
    }

    private void setSave_user(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = sp.getBoolean("isRememberPassword", false);
        if(isRemember){
            String get_account = sp.getString("account","");
            String get_password = sp.getString("password","");
            username.setText(get_account);
            password.setText(get_password);
            save_user.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()...");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                Log.i(TAG,"onClick()..."+R.id.login);
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                //检查合法性
                if(isAccountAndPswValue())
                {
                    setProgressbarVisible(View.VISIBLE);

                    editor = sp.edit();
                    if(save_user.isChecked()){
                        editor.putBoolean("isRememberPassword",true);
                        editor.putString("account",username_text);
                        editor.putString("password",password_text);
                    } else {
                        editor.clear();
                    }
                    editor.commit();

                    loginPresenter.doLogin(username_text,password_text);
                }
                break;
            default:
                break;

        }

    }

    public boolean isAccountAndPswValue(){
        if(username.getText().toString().trim().equals("")){
            username.setError("用户名不能为空");
            return false;
        } else if(password.getText().toString().trim().equals("")){
            password.setError("密码不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void processMessage(Message msg) {
        Log.i(TAG,"processMessga()...");
        ArrayList<DataFrame.User> friendlist = new ArrayList<DataFrame.User>();
        switch (msg.what){
            case Config.LOGIN_SUCCESS:
                Log.i(TAG,"login success...");
                loginPresenter.getOffMsg(LoginActivity.self.getUserID());
                Intent intent=new Intent(this, FriendListActivity.class);
                startActivity(intent);
                break;
            case Config.LOGIN_FAILED:
                Log.i(TAG,"login failed...");
                setProgressbarVisible(View.INVISIBLE);
                onClear();
                break;
            case Config.SEND_NOTIFICATION:
                Log.i(TAG,"send notificaiton...");
                Bundle bundle = msg.getData();
                ArrayList data = bundle.getParcelableArrayList("msgList");
                List<DataFrame.PersonalMsg> msgList = (List<DataFrame.PersonalMsg>) data.get(0);
                loginPresenter.saveMessageToDb(msgList);
                sendNotifycation(msgList);
            default:
                break;
        }

    }

    @Override
    public void makeToast(String text) {
        makeTextShort(text);
    }

    public void onClear() {
        username.setText("");
        password.setText("");
    }

    @Override
    public void setProgressbarVisible(int visibility) {
        progressbar.setVisibility(visibility);
    }


    @Override
    public void onfinish() {
        Log.i(TAG,"onfinish()....");
        onfinish();
    }
}
