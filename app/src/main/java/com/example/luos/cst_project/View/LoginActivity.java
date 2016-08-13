package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    public static final String USERDATA = "user_data";
    public static final String FriendList="friend_list";
    public static final String SEX = "sex";
    public static final String STUDENTNUMBER = "student_number";

    private ILoginPresenter loginPresenter;

    String username_text;
    String password_text;
    boolean showProgressbar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();

        loginPresenter = new ILoginPresenterCompl(this);
        Log.d("Test_onCreate","excute loginPresenter instance...");
        loginPresenter.setProgressBarVisible(View.INVISIBLE);
    }

    private void Init() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login_btn = (Button) findViewById(R.id.login);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                username_text = username.getText().toString().trim();
                password_text = password.getText().toString().trim();
                //检查合法性
                if(username_text.equals("")||password_text.equals(""))
                {
                    makeTextShort("用户名或密码不能为空");
                    return;
                }
                if(showProgressbar){
                    setProgressbarVisible(View.VISIBLE);
                }
                loginPresenter.doLogin(username_text,password_text);

        }

    }

    @Override
    public void processMessage(Message msg) {
        Log.d("Test_processMesage","LoginActivity processMessage()....."+msg.what);
        ArrayList<DataFrame.User> friendlist = new ArrayList<DataFrame.User>();

        switch (msg.what){
            case Config.LOGIN_SUCCESS:
                loginPresenter.getOffMsg(LoginActivity.self.getUserID());
                setPreference(username_text, password_text);
                ToFriendListAcivity(friendlist);
                onToastResult(Config.LOGIN_SUCCESS);
                break;
            case Config.LOGIN_FAILED:
                onToastResult(Config.LOGIN_FAILED);
                break;
            case Config.SEND_NOTIFICATION:
                Bundle bundle = msg.getData();
                Log.d("Test_Bundle",bundle.toString());
                ArrayList data = bundle.getParcelableArrayList("msgList");
                List<DataFrame.PersonalMsg> msgList = (List<DataFrame.PersonalMsg>) data.get(0);
                Log.d("Test_msgList",msgList.toString());
                saveMessageToDb(msgList);
                sendNotifycation(msgList);
            default:
                break;
        }

    }

    @Override
    public void onToastResult(int result) {
        switch (result){
            case Config.LOGIN_SUCCESS:
                Log.d("Test_Toast","success");
                makeTextShort("Login success");
                break;
            case Config.LOGIN_FAILED:
                Log.d("Test_Toast","failed");
                makeTextShort("Login Failed");
                setProgressbarVisible(View.INVISIBLE);
                showProgressbar = false;
                break;
        }

    }

    @Override
    public void onClear() {
        username.setText("");
        password.setText("");
    }

    @Override
    public void setProgressbarVisible(int visibility) {
        progressbar.setVisibility(visibility);
    }

    @Override
    public void ToFriendListAcivity( ArrayList<DataFrame.User> friends) {
        Intent intent=new Intent(this, FriendListActivity.class);
        startActivity(intent);
        intent.putExtra(LoginActivity.FriendList,friends);
    }

}
