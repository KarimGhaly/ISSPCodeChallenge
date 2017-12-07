package com.example.admin.isspcodechallenge.View.MainActivity;

import com.example.admin.isspcodechallenge.DataSource.RetrofitHelper;
import com.example.admin.isspcodechallenge.model.ResultClass;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12/6/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter{
    MainActivityContract.view view;
    @Override
    public void attach(MainActivityContract.view view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void getPermision() {
        view.getLocationPermission();
    }

    @Override
    public void getLocationCoord() {
        view.getLocation();
    }

    @Override
    public void getResults(String Lat, String Long) {
        retrofit2.Call<ResultClass> getResult = RetrofitHelper.getResults(Lat,Long);
        getResult.enqueue(new Callback<ResultClass>() {
            @Override
            public void onResponse(retrofit2.Call<ResultClass> call, Response<ResultClass> response) {
                view.ShowRecyclerView(response.body().getResponse());
            }

            @Override
            public void onFailure(retrofit2.Call<ResultClass> call, Throwable t) {
                view.showError(t.getMessage());
            }
        });
    }

    @Override
    public boolean CheckInternetConection() {
        return view.isNetworkAvailable();
    }


}
