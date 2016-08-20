package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.DataFrame;

/**
 * Created by luos on 2016/8/20.
 */

public interface IPresenter  {
    void onProcess(DataFrame.Msg msg);
}
