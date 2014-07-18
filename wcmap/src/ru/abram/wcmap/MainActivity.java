package ru.abram.wcmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {
	
	public static final String APP_GPS_ADDRESS = "gpsAdress";
	public static final String APP_GPS_ADDRESS_LAT = "lat";
	public static final String APP_GPS_ADDRESS_LON = "lon";
	SharedPreferences mGPS;
	
	private LocationManager locationManager;
	SupportMapFragment mapFragment;
	GoogleMap map;
	final String TAG = "myLogs";
	Marker marker;
	double lat, lon;
	String sLat, sLon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGPS = getSharedPreferences(APP_GPS_ADDRESS, Context.MODE_PRIVATE);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		map = mapFragment.getMap();
		if (map == null) {
			finish();
			return;
		}
		init();
		
		if(mGPS.contains(APP_GPS_ADDRESS)) {
		    lat = Double.parseDouble(mGPS.getString(APP_GPS_ADDRESS_LAT, ""));
		    lon = Double.parseDouble(mGPS.getString(APP_GPS_ADDRESS_LON, ""));
		}
		//myself marker
		map.addMarker(new MarkerOptions()
		.position(new LatLng(54.976961, 73.327263))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		.title("IT's Me!"))
		.showInfoWindow();
		
		map.addMarker(new MarkerOptions()
		.position(new LatLng(54.9767, 73.3275))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
		.title("WC"));
		
		map.addMarker(new MarkerOptions()
		.position(new LatLng(54.976, 73.3279))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
		.title("WC"));
		
		map.addMarker(new MarkerOptions()
		.position(new LatLng(54.9765, 73.32795))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		.title("Urn"));
		
		map.addMarker(new MarkerOptions()
		.position(new LatLng(54.976961, 73.327))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		.title("Urn"));
		
	CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(54.976961, 73.327263))
		.zoom(17)		
		.build();
	CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
	map.animateCamera(cameraUpdate);
	
	}

	private void init() {
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				Log.d(TAG, "onMapClick: " + latLng.latitude + ","
						+ latLng.longitude);
			}
		});

		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {
				Log.d(TAG, "onMapLongClick: " + latLng.latitude + ","
						+ latLng.longitude);
			}
		});

		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition camera) {
				Log.d(TAG, "onCameraChange: " + camera.target.latitude + ","
						+ camera.target.longitude);
			}
		});
	}

	private void _getLocation() {
	    // Get the location manager
	    LocationManager locationManager = (LocationManager) 
	            getSystemService(LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    String bestProvider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(bestProvider);
	    LocationListener loc_listener = new LocationListener() {

	        public void onLocationChanged(Location l) {}

	        public void onProviderEnabled(String p) {}

	        public void onProviderDisabled(String p) {}

	        public void onStatusChanged(String p, int status, Bundle extras) {}
	    };
	    locationManager
	            .requestLocationUpdates(bestProvider, 0, 0, loc_listener);
	    location = locationManager.getLastKnownLocation(bestProvider);
	    try {
	        lat = location.getLatitude();
	        lon = location.getLongitude();
	    } catch (NullPointerException e) {
	        lat = -1.0;
	        lon = -1.0;
	    }
	}
	public void onClickTest(View view) {
		//lat = 0;
		//lon = 0;
		_getLocation();
		Editor editor = mGPS.edit();
		sLat = Double.toString(lat);
		sLon = Double.toString(lon);
		editor.putString(APP_GPS_ADDRESS_LAT, sLat);
		editor.putString(APP_GPS_ADDRESS_LON, sLon);
		editor.apply();
		map.addMarker(new MarkerOptions()
			.position(new LatLng(lat, lon))
			.title("Wc!"));
		CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(lat, lon))
			.zoom(17)
			.bearing(45)
			.tilt(20)			
			.build();
		CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
		map.animateCamera(cameraUpdate);/*
		//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
		//		new LatLngBounds(new LatLng(-39, 112), new LatLng(-11, 154)),
		//		100);*/
		
		map.animateCamera(cameraUpdate);
	}
}