package com.example.luos.cst_project.Util;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luos on 2016/8/21.
 */
public class NetWorkTest {
    private DataFrame.Msg reciveMsg;
    private DataFrame.Msg.Builder msgBuilder = DataFrame.Msg.newBuilder();
    private DataFrame.User.Builder userBuilder = DataFrame.User.newBuilder();
    private NetWork netWork = new NetWork();

    @Test
    public void enpacket() throws Exception {
        DataFrame.Msg send_msg = msgBuilder
                .setUserOpt(Config.REQUEST_GET_OFFLINE_MSG)
                .setUser(
                        userBuilder.setUserID(3)
                ).build();
        byte[] data = netWork.Enpacket(send_msg);
        netWork.Depacket(data);
//        assertEquals(3,netWork.getMsg().getUser().getUserID());
    }

}