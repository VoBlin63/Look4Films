package ru.buryachenko.hw_look4films.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import static ru.buryachenko.hw_look4films.activities.MainActivity.distance;
import static ru.buryachenko.hw_look4films.activities.MainActivity.lastPosition;
import static ru.buryachenko.hw_look4films.utils.Constants.DISTANCE_LOOK_FOR_CINEMAS;

//import com.google.maps.model.LatLng;


public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap ourMap;

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
        lastPosition.observe(this, newPosition -> setMyPosition(newPosition));
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
        LatLng currentPos = lastPosition.getValue();
        if (currentPos != null) {
            setMyPosition(currentPos);
        }
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
            PlacesSearchResponse responce = lookForCinema.await();
            float distance = DISTANCE_LOOK_FOR_CINEMAS + 1;
            for (PlacesSearchResult cinemas : responce.results) {
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

}

