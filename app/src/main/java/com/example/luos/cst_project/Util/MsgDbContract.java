package com.example.luos.cst_project.Util;

import android.provider.BaseColumns;

/**
 * Created by luos on 2016/8/13.
 */

public final class MsgDbContract {

    public MsgDbContract(){}

    public static abstract class MsgEntry implements BaseColumns{
        public static final String TABLE_NAME = "message";
        public static final String SEND_ID = "send_id";
        public static final String RECEVICE_ID = "recevice_id";
        public static final String TYPE = "type";
        public static final String DIRECTION = "direction";
        public static final String CONTENT = "content";
        public static final String TIME = "time";

    }

}
