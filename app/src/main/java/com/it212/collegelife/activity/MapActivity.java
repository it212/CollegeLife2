package com.it212.collegelife.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it212.collegelife.R;
import com.it212.collegelife.utils.Model;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.Projection;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements TencentMap.OnMapClickListener, TencentMap.OnCameraChangeListener ,TencentLocationListener {
    private TencentMap tencentMap;
    private Projection projection;
    private TextView tvCenterCoordinate;
    private UiSettings mapUiSettings;
    private LocationSource locationSource;
    private MapView mapView;
    private Button btn_local;
    private TencentLocationManager mLocationManager;
    private int error;
    private TencentLocationManager locationManager;
    private TencentLocationRequest request;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions, 0);
            }
        }
        mLocationManager = TencentLocationManager.getInstance(this);
        btn_local= (Button) findViewById(R.id.btn_local);
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_frag);
        tencentMap = mapFragment.getMap();
        tencentMap.getUiSettings().setZoomControlsEnabled(true);
        projection = tencentMap.getProjection();
        tencentMap.setOnMapClickListener(this);
        tencentMap.setOnCameraChangeListener(this);
        mapUiSettings = tencentMap.getUiSettings();
        mapUiSettings.setRotateGesturesEnabled(true);
        mapUiSettings.setTiltGesturesEnabled(true);
        mapView = tencentMap.getMapView();
        createTextView(mapView);
        setText2Center(mapView);
        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在 mThread 线程发起定位
                // 创建定位请求
                request = TencentLocationRequest.create();
                // 修改定位请求参数, 定位周期 30000 ms
                request.setInterval(1000000);
                locationManager = TencentLocationManager.getInstance(MapActivity.this);
                error = locationManager.requestLocationUpdates(request, MapActivity.this);

//                updateLocationStatus("开始定位: " + request + ", 坐标系="
//                        + DemoUtils.toString(mLocationManager.getCoordinateType()));
            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Point point = projection.toScreenLocation(latLng);
        if (marker!=null){
            marker.remove();
        }

        marker = tencentMap.addMarker(new MarkerOptions().
               position(new LatLng(latLng.latitude,latLng.longitude)).
               title(latLng.toString()).
               snippet(latLng.latitude+","+latLng.longitude));
//显示信息窗
        marker.showInfoWindow();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (tvCenterCoordinate != null &&
                tvCenterCoordinate.getVisibility() == View.VISIBLE) {
            setText2Center(tencentMap.getMapView());
        }
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {

    }
    protected void createTextView(MapView mapView) {
        tvCenterCoordinate = new TextView(this);
        tvCenterCoordinate.setTextSize(16);
        tvCenterCoordinate.setTextColor(Color.BLACK);
        tvCenterCoordinate.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Drawable drawable = getResources().getDrawable(R.mipmap.red_location);
        drawable.setBounds(0, 0,
                drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvCenterCoordinate.setCompoundDrawables(null, null, null, drawable);
        mapView.addView(tvCenterCoordinate);
    }
    protected void setText2Center(MapView mapView) {
        int mapWidth = mapView.getMeasuredWidth();
        int mapHeight = mapView.getMeasuredHeight();
        tvCenterCoordinate.setText(
                tencentMap.getCameraPosition().target.toString());
        tvCenterCoordinate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int tvWidth = tvCenterCoordinate.getMeasuredWidth();
        int tvHeight = tvCenterCoordinate.getMeasuredHeight();
        if (Build.VERSION.SDK_INT > 10) {
            tvCenterCoordinate.setX((mapWidth - tvWidth) / 2);
            tvCenterCoordinate.setY(mapHeight / 2 - tvHeight);
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = (mapWidth - tvWidth) / 2;
            lp.topMargin = mapHeight / 2 - tvHeight;
            tvCenterCoordinate.setLayoutParams(lp);
        }
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            Toast.makeText(MapActivity.this, "定位成功"+tencentLocation.getAddress(), Toast.LENGTH_LONG).show();
            //		LatLng yinke = new LatLng(39.981840,116.306420);
            CameraUpdate cameraYinke =
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(tencentLocation.getLatitude(),tencentLocation.getLongitude()), 19, 45f, 45f));
//            if (cbMapAnimation.isChecked()) {
//                tencentMap.animateCamera(cameraYinke, this);
//            } else {
//                tencentMap.moveCamera(cameraYinke);
//            }
            tencentMap.moveCamera(cameraYinke);


        } else {
            // 定位失败
            Toast.makeText(MapActivity.this, "定位失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }
}
