package com.example.luos.cst_project.Presenter;

import com.example.luos.cst_project.Model.DataFrame;


public interface IPresenter {
    void onProcess(DataFrame.Msg msg);
}
