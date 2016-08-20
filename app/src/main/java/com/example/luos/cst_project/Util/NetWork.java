package com.example.luos.cst_project.Util;

import android.util.Log;

import com.example.luos.cst_project.Model.Config;
import com.example.luos.cst_project.Model.DataFrame;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luos on 2016/6/25.
 */

public class NetWork extends Thread {
            }


            Log.i(TAG, "reciveMsg()...start recive messages");
            byte[] buffer = new byte[1024 * 4];
            int len = dis.read(buffer);
            if (len != -1) {
            }
            dos.flush();
            e.printStackTrace();

}
