package com.example.luos.cst_project.Util;

import android.provider.BaseColumns;

/**
 * Created by luos on 2016/8/15.
 */

public class FriendDbContract {
    public FriendDbContract() {
    }

    public static abstract class FriendsEntry implements BaseColumns {
        public static final String TABLE_NAME = "friend";
        public static final String USER_ID = "user_id";
        public static final String FRIEND_ID = "friend_id";
        public static final String NICKNAME = "nickName";
        public static final String TYPE = "type";
        public static final String CONTENT = "content";
        public static final String TIME = "time";
    }


}
