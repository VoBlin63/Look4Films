package ru.buryachenko.hw_look4films.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.NearbySearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.R;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static ru.buryachenko.hw_look4films.utils.Constants.DISTANCE_LOOK_FOR_CINEMAS;
import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.REFRESH_POSITION_PERIOD;


public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap ourMap;
    private LatLng lastPosition = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return res;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        MainActivity.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                REFRESH_POSITION_PERIOD, 10, locationListener);
        checkEnabled();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.locationManager.removeUpdates(locationListener);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        ourMap = googleMap;
        if (lastPosition != null) {
            setMyPosition(lastPosition);
        }
        ourMap.getUiSettings().setZoomControlsEnabled(true);
        ourMap.getUiSettings().setZoomGesturesEnabled(true);
        ourMap.setBuildingsEnabled(true);
    }

    private void setMyPosition(LatLng position) {
        if ((ourMap == null) || (position == null))
            return;
        ourMap.clear();
        LatLng finish = null;
        NearbySearchRequest lookForCinema = new NearbySearchRequest(App.getInstance().geoApiContext)
                .location(new com.google.maps.model.LatLng(position.latitude, position.longitude))
                .radius(DISTANCE_LOOK_FOR_CINEMAS)
                .keyword("cinema");
        try {
            PlacesSearchResponse response = lookForCinema.await();
            float distance = DISTANCE_LOOK_FOR_CINEMAS + 1;
            for (PlacesSearchResult cinemas : response.results) {
                LatLng dest = new LatLng(cinemas.geometry.location.lat, cinemas.geometry.location.lng);
                if (distance(position, dest) < distance) {
                    finish = dest;
                    distance = distance(position, dest);
                }
                ourMap.addMarker(new MarkerOptions()
                        .position(dest)
                        .icon(vectorToBitmap(R.drawable.ic_route_finish, Color.GREEN))
                        .title(cinemas.name));
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        addRoute(position, finish);
        ourMap.addMarker(new MarkerOptions().position(position)
                .icon(vectorToBitmap(R.drawable.ic_route_start, Color.RED))
                .title("Вы тут"));
        ourMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 11F));
    }

    private void addRoute(LatLng start, LatLng finish) {
        if ((start == null) || (finish == null))
            return;
        DirectionsResult way = null;
        try {
            way = DirectionsApi.newRequest(App.getInstance().geoApiContext)
                    .origin(new com.google.maps.model.LatLng(start.latitude, start.longitude))
                    .mode(TravelMode.WALKING)
                    .destination(new com.google.maps.model.LatLng(finish.latitude, finish.longitude)).await();
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        if (way != null) {
            List<com.google.maps.model.LatLng> path = way.routes[0].overviewPolyline.decodePath();
            PolylineOptions line = new PolylineOptions();
            LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
            for (int i = 0; i < path.size(); i++) {
                line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
                latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
            }
            line.width(9F).color(Color.BLUE);
            ourMap.addPolyline(line);
        }
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

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            showLocation(MainActivity.locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                Log.d(LOGTAG, " GPS Status: " + status);
            }
        }
    };

    private void checkEnabled() {
        if (!MainActivity.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MainActivity.snackMessage(App.getInstance().getString(R.string.fragmentMapNoGPS));
        }
    }

    private void showLocation(Location location) {
        if (location == null)
            return;
        lastPosition = new LatLng(location.getLatitude(), location.getLongitude());
        setMyPosition(lastPosition);
    }

    private static float distance(LatLng src, LatLng dest) {
        if ((src == null) || (dest == null))
            return 0F;
        Location a = new Location("A");
        a.setLatitude(src.latitude);
        a.setLongitude(src.longitude);
        Location b = new Location("B");
        b.setLatitude(dest.latitude);
        b.setLongitude(dest.longitude);
        return a.distanceTo(b);
    }

}

