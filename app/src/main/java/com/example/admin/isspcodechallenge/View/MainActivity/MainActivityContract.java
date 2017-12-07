package com.example.admin.isspcodechallenge.View.MainActivity;

import com.example.admin.isspcodechallenge.BasePresenter;
import com.example.admin.isspcodechallenge.BaseView;
import com.example.admin.isspcodechallenge.model.Response;

import java.util.List;

/**
 * Created by Admin on 12/6/2017.
 */

public interface MainActivityContract {

    interface view extends BaseView{
        void getLocationPermission();
        void getLocation();
        void ShowRecyclerView(List<Response> responsesList);
        boolean isNetworkAvailable();
    }
    interface Presenter extends BasePresenter<view>{
        void getPermision();
        void getLocationCoord();
        void getResults(String Lat,String Long);
        boolean CheckInternetConection();
    }
}
