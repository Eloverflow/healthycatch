/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MyLocationDemoActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        SensorEventListener{

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private float[] mRotationMatrix = new float[16];
    private float mDeclination;


    private Marker personMarker;
    private Marker point1;
    private Marker point2;
    private Marker point3;
    private Marker point4;
    private Marker point5;
    private Marker point6;
    private Marker point7;
    private Marker point8;
    private Marker point9;
    private Marker pointEnd;
    private Handler handler;


    MediaPlayer mp1;
    MediaPlayer mp2;
    MediaPlayer mp3;
    MediaPlayer mp4;
    MediaPlayer mp5;
    MediaPlayer mp6;
    MediaPlayer mp7;
    MediaPlayer mp8;
    MediaPlayer mp9;
    MediaPlayer mpEnd;

    Toast toast;
    int score =0;
    private TextView ourTextView;



    private ArrayList<Marker> markerArray = new ArrayList<Marker>();
    private static final ArrayList<LatLng> markerPosition = new ArrayList<LatLng>();



    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location_demo);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button button1=(Button)findViewById(R.id.button);
        Button button2=(Button)findViewById(R.id.button2);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Regular.ttf");
        button1.setTypeface(typeFace);
        //button1.setPaintFlags(button1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        button2.setTypeface(typeFace);

        ourTextView = (TextView) findViewById(R.id.textViewTest);




       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 10, this);
    }



    @Override
    public void onMapReady(GoogleMap map) {

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startAnim();
            }
        };

        findViewById(R.id.button).setOnClickListener(onClickListener);

        mp1 = MediaPlayer.create(this, R.raw.sound);
        mp2 = MediaPlayer.create(this, R.raw.sound);
        mp3 = MediaPlayer.create(this, R.raw.sound);
        mp4 = MediaPlayer.create(this, R.raw.sound);
        mp5 = MediaPlayer.create(this, R.raw.sound);
        mp6 = MediaPlayer.create(this, R.raw.sound);
        mp7 = MediaPlayer.create(this, R.raw.sound);
        mp8 = MediaPlayer.create(this, R.raw.sound);
        mp9 = MediaPlayer.create(this, R.raw.sound);
        mpEnd = MediaPlayer.create(this, R.raw.sound);

        mMap = map;


        mMap.setOnMyLocationButtonClickListener(this);
        //mMap.getUiSettings().setAllGesturesEnabled(false);
       // mMap.getUiSettings().setZoomControlsEnabled(false);
        enableMyLocation();
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(20);


       /* Location location = LocationServices.FusedLocationApi.getLastLocation(
                mMap);*/
        //CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(46.772049, -71.280106));



        //Location location = mMap.getMyLocation();
       //Log.d("location", location.toString());

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


        // starting point 46.812012, -71.280069
        // ending point 46.812311 -71.279923

        /*Start*/
        point1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812012, -71.280069))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.apple)));
        point2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812046, -71.280100))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.strawberry)));
        point3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812088, -71.280123))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cheese)));
        point4 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812132, -71.280130))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.strawberry)));
        point5 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812176, -71.280123))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.radish)));
        point6 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812222, -71.280093))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.apple)));
        point7 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812251, -71.280054))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.apple)));
        point8 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812272, -71.280014))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cheese)));
        point9 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812290, -71.279972))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.raspberry)));


        /*End*/
        pointEnd = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812311, -71.279923))
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.apple)));

        /*Store*/
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(46.812219, -71.279897))
                .title("Mon Magasin du coin")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.store)));


        personMarker =
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(46.812012, -71.280069))
                        .title("Brisbane")
                        .snippet("Population: 2,074,200")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.boy)));





    }

    public void startAnim() {
        handler = new Handler();

        animateMarker(personMarker, new LatLng(46.812012, -71.280069), false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                point1.remove();
                mp1.start();
                score+=20;
                String newScore = String.valueOf(score);
                ourTextView.setText(newScore);
                toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                
                toast.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }

                }, 200);
            }

        }, 250);



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateMarker(personMarker, new LatLng(46.812046, -71.280100), false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        point2.remove();
                        mp2.start();
                        score+=20;
                        String newScore = String.valueOf(score);
                        ourTextView.setText(newScore);
                        toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                        
                        toast.show();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }

                        }, 200);
                    }

                }, 250);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateMarker(personMarker, new LatLng(46.812088, -71.280123), false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                point3.remove();
                                mp3.start();
                                score+=20;
                                String newScore = String.valueOf(score);
                                ourTextView.setText(newScore);
                                toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                
                                toast.show();

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }

                                }, 200);
                            }

                        }, 250);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animateMarker(personMarker, new LatLng(46.812132, -71.280130), false);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        point4.remove();
                                        mp4.start();
                                        score+=20;
                                        String newScore = String.valueOf(score);
                                        ourTextView.setText(newScore);
                                        toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                        
                                        toast.show();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toast.cancel();
                                            }

                                        }, 200);
                                    }

                                }, 250);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        animateMarker(personMarker, new LatLng(46.812176, -71.280123), false);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                point5.remove();
                                                mp5.start();
                                                score+=20;
                                                String newScore = String.valueOf(score);
                                                ourTextView.setText(newScore);
                                                toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                                
                                                toast.show();

                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        toast.cancel();
                                                    }

                                                }, 200);
                                            }

                                        }, 250);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                animateMarker(personMarker, new LatLng(46.812222, -71.280093), false);
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        point6.remove();
                                                        mp6.start();
                                                        score+=20;
                                                        String newScore = String.valueOf(score);
                                                        ourTextView.setText(newScore);
                                                        toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                                        
                                                        toast.show();

                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                toast.cancel();
                                                            }

                                                        }, 200);
                                                    }

                                                }, 250);

                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        animateMarker(personMarker, new LatLng(46.812251, -71.280054), false);
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                point7.remove();
                                                                mp7.start();
                                                                score+=20;
                                                                String newScore = String.valueOf(score);
                                                                ourTextView.setText(newScore);
                                                                toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                                                
                                                                toast.show();

                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        toast.cancel();
                                                                    }

                                                                }, 200);
                                                            }

                                                        }, 250);

                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                animateMarker(personMarker, new LatLng(46.812272, -71.280014), false);
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        point8.remove();
                                                                        mp8.start();
                                                                        score+=20;
                                                                        String newScore = String.valueOf(score);
                                                                        ourTextView.setText(newScore);
                                                                        toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                                                        
                                                                        toast.show();

                                                                        handler.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                toast.cancel();
                                                                            }

                                                                        }, 200);
                                                                    }

                                                                }, 250);

                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        animateMarker(personMarker, new LatLng(46.812290, -71.279972), false);
                                                                        handler.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                point9.remove();
                                                                                mp9.start();
                                                                                score+=20;
                                                                                String newScore = String.valueOf(score);
                                                                                ourTextView.setText(newScore);
                                                                                toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                                                                
                                                                                toast.show();

                                                                                handler.postDelayed(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        toast.cancel();
                                                                                    }

                                                                                }, 200);
                                                                            }

                                                                        }, 250);

                                                                        handler.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                animateMarker(personMarker, new LatLng(46.812311, -71.279923), false);
                                                                                handler.postDelayed(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        pointEnd.remove();
                                                                                        score+=20;
                                                                                        String newScore = String.valueOf(score);
                                                                                        ourTextView.setText(newScore);
                                                                                        toast = Toast.makeText(getApplicationContext(), "+20", Toast.LENGTH_SHORT);
                                                                                        
                                                                                        toast.show();

                                                                                        handler.postDelayed(new Runnable() {
                                                                                            @Override
                                                                                            public void run() {
                                                                                                toast.cancel();
                                                                                            }

                                                                                        }, 200);
                                                                                    }

                                                                                }, 250);

                                                                            }

                                                                        }, 500);
                                                                    }

                                                                }, 500);
                                                            }

                                                        }, 500);
                                                    }

                                                }, 500);
                                            }

                                        }, 500);
                                    }

                                }, 500);
                            }

                        }, 500);
                    }

                }, 500);
            }

        }, 500);



    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

  /*  public void registerOnLocationListener(LocationListener listener) {
        locationListeners.add(listener);

        Location lastLocation = getLocation();
        if (lastLocation != null) {
            Log.d(LOG_NAME, "location service added listener, pushing last known location to listener");
            listener.onLocationChanged(new Location(lastLocation));
        }
    }
*/
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Yes", "location yea");
        GeomagneticField field = new GeomagneticField(
                (float)location.getLatitude(),
                (float)location.getLongitude(),
                (float)location.getAltitude(),
                System.currentTimeMillis()
        );

        // getDeclination returns degrees
        mDeclination = field.getDeclination();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float bearing = (float) (Math.toDegrees(orientation[0]) + mDeclination);
            updateCamera(bearing);
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final LinearInterpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateCamera(float bearing) {
        CameraPosition oldPos = mMap.getCameraPosition();

        CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }
}
