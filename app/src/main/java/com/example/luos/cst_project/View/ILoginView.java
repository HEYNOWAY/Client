package com.example.luos.cst_project.View;

import com.example.luos.cst_project.Model.DataFrame;
import com.example.luos.cst_project.Model.User;
import com.example.luos.cst_project.Util.DbUtil;

import java.util.ArrayList;

/**
 * Created by luos on 2016/6/25.
 */

public interface ILoginView {
    void setProgressbarVisible(int visibility);
    void makeToast(String text);
}
