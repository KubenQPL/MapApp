package pl.jakubneukirch.mapapp.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.app.MapApp;
import pl.jakubneukirch.mapapp.base.BaseActivity;
import pl.jakubneukirch.mapapp.di.ActivityModule;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView, OnMapReadyCallback {

    public static final int CODE_PERMISSION_FINE_LOCATION = 0;

    @BindView(R.id.standardToolbar)
    Toolbar standardToolbar;

    private GoogleMap map;
    private AlertDialog permissionDialog = null;
    private AlertDialog providerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.onCreate();
    }

    @Override
    public void setup() {
        ButterKnife.bind(this);
        setupToolbar();
        setupMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(standardToolbar);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_PERMISSION_FINE_LOCATION);
            } else {
                presenter.locationPermissionGranted();
            }
        } else {
            presenter.locationPermissionGranted();
        }
    }

    @Override
    public void setLocation(Location location) {
        final CameraUpdate latLng = CameraUpdateFactory
                .newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        map.moveCamera(latLng);
        map.animateCamera(zoom);
    }

    @Override
    public void askForPermissions() {
        if (permissionDialog == null) {
            permissionDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_needed)
                    .setPositiveButton(R.string.ask_again, (DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                        checkPermissions();
                    })
                    .setNegativeButton(R.string.exit, (DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                        finish();
                    })
                    .setCancelable(false)
                    .create();
        }
        permissionDialog.show();
    }

    @Override
    public void askForProvider() {
        if (providerDialog == null) {
            providerDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.gps_needed)
                    .setPositiveButton(R.string.turn_on, (DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                        turnOnGps();
                    })
                    .create();
        }
        providerDialog.show();
    }

    private void turnOnGps() {
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_PERMISSION_FINE_LOCATION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.locationPermissionGranted();
            } else {
                presenter.locationPermissionDenied();
            }
        }
    }

    @Override
    public void injectDependencies() {
        MapApp.appComponent
                .getActivityComponent(new ActivityModule(this))
                .inject(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }
}
