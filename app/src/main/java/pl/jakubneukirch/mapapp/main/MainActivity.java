package pl.jakubneukirch.mapapp.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.app.MapApp;
import pl.jakubneukirch.mapapp.base.BaseActivity;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;
import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.di.ActivityModule;
import pl.jakubneukirch.mapapp.saved.SavedActivity;
import pl.jakubneukirch.mapapp.view.InfoDialog;
import pl.jakubneukirch.mapapp.view.SpinnerButton;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView, OnMapReadyCallback {

    public static final int CODE_PERMISSION_FINE_LOCATION = 0;

    private static final int CAMERA_ZOOM = 16;
    public static final String KEY_ROUTE_ID = "route_id";
    public static final int NO_ROUTE_ID = -1;

    @BindView(R.id.mapToolbar)
    Toolbar standardToolbar;
    @BindView(R.id.spinnerButton)
    SpinnerButton spinnerButton;
    @BindView(R.id.toggleLayout)
    LinearLayout toggleLayout;
    @BindView(R.id.followToggleButton)
    SwitchCompat followToggleButton;
    @BindView(R.id.followingStatusTextView)
    TextView followingStatusTextView;

    private GoogleMap map;
    private AlertDialog permissionDialog = null;
    private AlertDialog providerDialog = null;
    private InfoDialog infoDialog = null;

    private Polyline polyline = null;
    private PolylineOptions polylineOptions = null;

    private boolean showInfoMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long routeId = getIntent().getLongExtra(KEY_ROUTE_ID, NO_ROUTE_ID);
        presenter.onCreate(routeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    public void setup() {
        ButterKnife.bind(this);
        checkPermissions();
        setupToolbar();
        setupMap();
        setupListeners();
        setupSpinnerButton();
    }

    private void setupSpinnerButton() {
        spinnerButton.setItems(getResources().getStringArray(R.array.spinner_screens));
        spinnerButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.itemScreenSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void openSavedScreen() {
        final Intent intent = new Intent(this, SavedActivity.class);
        startActivity(intent);
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(standardToolbar);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupListeners() {
        followToggleButton.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                presenter.followingToggleOn();
            } else {
                presenter.followingToggleOff(System.currentTimeMillis());
            }
        });
    }

    @SuppressLint("NewApi")
    private void checkPermissions() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_PERMISSION_FINE_LOCATION);
        } else {
            presenter.locationPermissionGranted();
        }
    }

    private Boolean isPermissionGranted(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
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

    @SuppressLint("MissingPermission")
    @Override
    public void showMyLocation(boolean show) {
        if (map != null && isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) && show) {
            map.setMyLocationEnabled(true);
            updateCameraZoom();
        }
    }

    @Override
    public void setLocationSource(LocationSource source) {
        map.setLocationSource(source);
    }

    @Override
    public void setLocation(Location location) {
        setCameraPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void setLocation(LocationDbEntity location) {
        setCameraPosition(new LatLng(location.getLat(), location.getLon()));
    }

    @Override
    public void drawPolyLine(Location location) {
        drawPolyLine(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void drawPolyLine(LocationDbEntity location) {
        drawPolyLine(location.getLat(), location.getLon());
    }

    @Override
    public void drawPolyLine(double lat, double lon) {
        if (polyline != null) {
            polyline.remove();
        }
        if (polylineOptions == null) {
            setupPolyLineOptions();
        }
        polylineOptions.add(new LatLng(lat, lon));
        polyline = map.addPolyline(polylineOptions);
    }

    @Override
    public void drawPath(List<LocationDbEntity> list) {
        if (polyline != null) {
            polyline.remove();
        }
        if (polylineOptions == null) {
            setupPolyLineOptions();
        }
        for (LocationDbEntity loc : list) {
            polylineOptions.add(new LatLng(loc.getLat(), loc.getLon()));
        }
        if (map != null) {
            polyline = map
                    .addPolyline(
                            polylineOptions
                    );
            if (polyline.getPoints().size() > 0) {
                setCameraPosition(polyline.getPoints().get(0));
            }

        }
    }

    private void setCameraPosition(LatLng position) {
        if(map != null){
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, CAMERA_ZOOM);
            map.animateCamera(update);
        }
    }

    private void setupPolyLineOptions() {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(R.color.route);
        polylineOptions.width(5);
        polylineOptions.visible(true);
    }

    @Override
    public void clearPolyLine() {
        if (polyline != null) {
            polyline.remove();
            polyline = null;
            polylineOptions = null;
        }
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
        presenter.mapLoaded();
    }

    @Override
    public void setupMovableMap() {
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
    }

    @Override
    public void setupStaticMap() {
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(false);
    }

    private void updateCameraZoom() {
        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(CAMERA_ZOOM);
        map.animateCamera(zoom);
    }

    @Override
    public void setupFollowingToolbar() {
        if (toggleLayout != null) {
            toggleLayout.setVisibility(View.VISIBLE);
        }
        showInfoMenu = false;
        invalidateOptionsMenu();
    }

    @Override
    public void setupInfoToolbar() {
        if (toggleLayout != null) {
            toggleLayout.setVisibility(View.GONE);
        }
        showInfoMenu = true;
        invalidateOptionsMenu();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showInfoMenu) {
            getMenuInflater().inflate(R.menu.menu_info, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.infoMenuItem) {
            presenter.infoItemClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPlaces(ArrayList<PlaceDetails> places) {
        if (infoDialog == null) {
            infoDialog = new InfoDialog(this);
        }
        infoDialog.setInfo(places);
        infoDialog.setCancelable(true);
        infoDialog.show();
    }

    @Override
    public void setToggleCaptionStatus(boolean on) {
        followingStatusTextView.setText(on ? getString(R.string.following) : getString(R.string.follow));
    }
}
