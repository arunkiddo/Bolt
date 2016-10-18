package com.chiragaggarwal.bolt.run.map;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chiragaggarwal.bolt.R;
import com.chiragaggarwal.bolt.location.NullUserLocation;
import com.chiragaggarwal.bolt.location.UserLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RunMapFragment extends Fragment implements OnMapReadyCallback, RunMapView {
    private static final int ZOOM_LEVEL_STREETS = 17;
    private UserLocation lastUserLocation = new NullUserLocation();
    private GoogleMap googleMap;
    private RunMapPresenter runMapPresenter;

    @BindView(R.id.map_view)
    public MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_run, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        runMapPresenter = new RunMapPresenter(this);
    }

    @Override
    public final void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setBuildingsEnabled(false);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
    }

    @Override
    public final void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public final void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public final void onSaveInstanceState(Bundle var1) {
        super.onSaveInstanceState(var1);
        mapView.onSaveInstanceState(var1);
    }

    @SuppressWarnings("MissingPermission")
    public void updateLocation(UserLocation userLocation) {
        mapView.setVisibility(View.VISIBLE);
        runMapPresenter.extendPolyline(lastUserLocation, userLocation);
        googleMap.setMyLocationEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation.toLatLng(), ZOOM_LEVEL_STREETS);
        googleMap.moveCamera(cameraUpdate);
        lastUserLocation = userLocation;
    }

    public void clearMap() {
        googleMap.clear();
    }

    @Override
    public void plotPolyline(LatLng lastLatLng, LatLng currentLatLng) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(lastLatLng)
                .add(currentLatLng);
        googleMap.addPolyline(polylineOptions);
    }
}
