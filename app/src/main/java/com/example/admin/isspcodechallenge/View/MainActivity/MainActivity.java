package com.example.admin.isspcodechallenge.View.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.isspcodechallenge.R;
import com.example.admin.isspcodechallenge.View.MainActivity.Adapters.RecyclerViewAdapter;
import com.example.admin.isspcodechallenge.model.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityContract.view {
    public static final String TAG = "MainActivityTAG";
    private static final int MY_PERMISSIONS_REQUEST_READ_Location = 123;

    MainActivityPresenter presenter;
    @BindView(R.id.RecyclerView)
    android.support.v7.widget.RecyclerView RecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainActivityPresenter();
        presenter.attach(this);
        if(presenter.CheckInternetConection()) {
            presenter.getPermision();
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("No Internet Connection");
            dialog.setMessage("Please Check Internet Connection and Reopen the App");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CloseApp();
                }
            });
            dialog.show();
        }
    }
    public void CloseApp(){
        this.finish();
        System.exit(0);
    }

    @Override
    public void showError(String MSG) {
        Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showError: " + MSG);
    }

    @Override
    public void showToast(String MSG) {
        Toast.makeText(this, MSG, Toast.LENGTH_SHORT).show();
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_Location);
            }
        } else {
            presenter.getLocationCoord();
        }
    }

    @Override
    public void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                presenter.getResults(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showError(e.getMessage());
            }
        });
    }

    @Override
    public void ShowRecyclerView(List<Response> responsesList) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(responsesList);
        RecyclerView.setAdapter(adapter);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_Location: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    presenter.getLocationCoord();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Location Permission");
                    dialog.setMessage("We Need to get your location to use this app");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_Location);
                        }
                    });
                    dialog.show();
                }
                return;
            }
        }
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
