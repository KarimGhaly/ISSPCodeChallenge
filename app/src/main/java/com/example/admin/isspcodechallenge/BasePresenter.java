package com.example.admin.isspcodechallenge;

/**
 * Created by Admin on 12/6/2017.
 */

public interface BasePresenter  <V extends BaseView>{
    void attach(V view);
    void detach();
}
