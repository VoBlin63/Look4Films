package ru.buryachenko.hw_look4films.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
//import com.google.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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


public class FragmentMap extends Fragment implements OnMapReadyCallback {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return res; //inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (MainActivity.lastPosition != null) {
            googleMap.clear();
            LatLng start = MainActivity.lastPosition;
            //ищем куда идем
            LatLng finish = new com.google.android.gms.maps.model.LatLng(start.latitude + 0.09 * (Math.random() - 0.5), start.longitude + 0.09 * (Math.random() - 0.5));
            NearbySearchRequest lookForCinema  = new NearbySearchRequest(App.getInstance().geoApiContext)
                        .location(new com.google.maps.model.LatLng(start.latitude,start.longitude))
                    .radius(7000)
                    .keyword("cinema");
            List<com.google.maps.model.LatLng> cinemas = new ArrayList<>();
            try {
                PlacesSearchResponse res = lookForCinema.await();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + res.toString());
                for (PlacesSearchResult r : res.results) {
                    cinemas.add(new com.google.maps.model.LatLng(r.geometry.location.lat,r.geometry.location.lng));
                }

            } catch (ApiException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
            //строим маршрут
            DirectionsResult way = null;
            try {
                //TODO здесь com.google.maps.model.LatLng и com.google.android.gms.maps.model.LatLng - как эту фигню свести к одному ?!
                way = DirectionsApi.newRequest(App.getInstance().geoApiContext)
                        .origin(new com.google.maps.model.LatLng(start.latitude,start.longitude))
                        .destination(cinemas.get(0)).await();
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
                line.width(11F).color(Color.BLUE);
                googleMap.addPolyline(line);
            }
            googleMap.addMarker(new MarkerOptions().position(start).icon(vectorToBitmap(R.drawable.ic_home, Color.RED)).title("Вы тут"));
            googleMap.addMarker(new MarkerOptions().position(finish).icon(vectorToBitmap(R.drawable.ic_favorite, Color.RED)).title("Вам сюда"));
            googleMap.setBuildingsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.lastPosition,13F));
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

}

