package com.example.luos.cst_project.Presenter;


import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Util.NetWork;


/**
 * Created by luos on 2016/8/13.
 */

public class BaseIPresenter implements IPresenter {
    private static final String TAG = "BaseIPresenter";
    private DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    private DataFrame.User.Builder userBuilder = DataFrame.User.newBuilder();
    private NetWork netWork;


    public BaseIPresenter() {
    }

    public void stopWork(){
        NetWork.getInstance().setOnWork(false);
    }

    public void setInstanceNull(){
        NetWork.getInstance().setInstanceNull();
    }

    public void exitRequest(int userId) {
        netWork = NetWork.getInstance();
        DataFrame.Msg send_msg = msgBuilder
                .setUserOpt(Config.REQUEST_EXIT)
                .setUser(
                        userBuilder.setUserID(userId)
                ).build();
         netWork.writeToSrv(send_msg);
         netWork.addIPresenter(this);

    }

    @Override
    public void onProcess(DataFrame.Msg msg) {

    }
}
