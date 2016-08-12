package com.example.luos.cst_project.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luos on 2016/8/9.
 */

public class TimeUtil {

    public static String getAbsoluteTime(){
        SimpleDateFormat sdf= new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
