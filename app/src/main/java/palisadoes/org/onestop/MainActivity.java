package palisadoes.org.onestop;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import palisadoes.org.onestop.models.Driver;
import palisadoes.org.onestop.models.DriverMarker;
import palisadoes.org.onestop.models.Passenger;
import palisadoes.org.onestop.models.PickUpInfo;



public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    private Marker pickUpMarker = null;
    private Marker destMarker=null;
    private List<Driver> drivers = new ArrayList<>();
    private Context mContext;
    private List<DriverMarker> taxiMarkers = new ArrayList<>();
    private FloatingActionButton mFab;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    SharedPreferences mSharedPrefs;
    private Driver mCurrentRequestDriver = null;
    private boolean mInTrip = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mSharedPrefs = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickUpMarker!=null && destMarker!=null)
                {

                    showAcceptDialog();
                }else{
                    Toast.makeText(mContext,"Set a destination",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();

            }
        };


        addPassengerToDatabase();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint("Enter destination");




        autocompleteFragment.setBoundsBias(new LatLngBounds(

                new LatLng(17.83760350609207,-78.4368896484375),
        new LatLng(18.46918890441719,-76.168212890625)
                ));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(destMarker==null) {
                    destMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName() + "").
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag)));
                    destMarker.setDraggable(true);
                }else {
                    destMarker.setPosition(place.getLatLng());
                    destMarker.setTitle(place.getName()+"");
                }

                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        getDriverLocations();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        placeDriverMarkers();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    if(mMap!=null){
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        }
                    }


                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(lastLocation!=null) {
                LatLng center = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                if (pickUpMarker == null) {
                    pickUpMarker = mMap.addMarker(new MarkerOptions().position(center).title("Place Pickup here"));
                    pickUpMarker.setDraggable(true);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
            }




        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void checkRequest()
    {
        mUser = mAuth.getCurrentUser();
        if(mUser!=null) {
            if (mCurrentRequestDriver != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference reference = firebaseDatabase.getReference()
                        .child("drivers").child(mCurrentRequestDriver.getDriverId()).child("requests");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(mUser.getUid())) {
                            Toast.makeText(mContext,"Present",Toast.LENGTH_SHORT).show();
                            DataSnapshot snapshot = dataSnapshot.child(mUser.getUid());
                            Map<String, Object> mapObj = (Map<String, Object>) snapshot.getValue();
                            String status = (String) mapObj.get("status");
                            if (status.contentEquals("Accept")) {
                                if(mCurrentRequestDriver.getDriverProfile()!=null)
                                    Toast.makeText(mContext, mCurrentRequestDriver.getDriverProfile().
                                            getFirstname()+ "on his way", Toast.LENGTH_SHORT).show();
                                placeDriverMarkers();
                                mInTrip = true;
                            }

                            if(status.contentEquals("Cancel"))
                            {
                                mInTrip = false;
                                mFab.setEnabled(true);
                                mCurrentRequestDriver = null;
                                placeDriverMarkers();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    private void showAcceptDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Send Request?");
// Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext,"Procesing Request",Toast.LENGTH_SHORT).show();
                drawRoute(pickUpMarker.getPosition(),destMarker.getPosition());
                mFab.setEnabled(false);
                sendPickupRequest();


                // User clicked OK button
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // dialog.dismiss();
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addPassengerToDatabase()
    {
        mUser = mAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference().child("passengers");
        final String name = mSharedPrefs.getString("name",null);
        final String phone = mSharedPrefs.getString("phone",null);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mUser!=null) {

                    if (!dataSnapshot.hasChild(mUser.getUid())) {
                        Toast.makeText(mContext, "Still here", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                            Toast.makeText(mContext, "Im here", Toast.LENGTH_SHORT).show();
                            reference.child(mUser.getUid()).setValue(new Passenger(name, phone));
                        }
                    }
                }else{
                    Toast.makeText(mContext,"User is null",Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void placeDriverMarkers()
    {
        if(mMap!=null)
        {
            for(DriverMarker marker: taxiMarkers)
            {
                marker.getMarker().remove();
            }
            taxiMarkers = new ArrayList<>();
            for (Driver driver: drivers)
            {
                LatLng loc = new LatLng(driver.getLatitude(),driver.getLongitude());
                //String name = driver.getDriverProfile().getFirstname();
                if(!mInTrip) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi)));

                    taxiMarkers.add(new DriverMarker(marker, driver.getDriverId()));
                }else{
                    if(driver.getDriverId().contentEquals(mCurrentRequestDriver.getDriverId()))
                    {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_green)));

                        taxiMarkers.add(new DriverMarker(marker, driver.getDriverId()));
                        break;
                    }
                }
            }
        }
    }

    private void sendPickupRequest() {

        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            Toast.makeText(this,"Okay",Toast.LENGTH_SHORT).show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference reference = firebaseDatabase.getReference().child("drivers");


            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String, Object> objectMap = (HashMap<String, Object>)
                            dataSnapshot.getValue();
                    drivers = new ArrayList<Driver>();
                    float bestDistanceSoFar = Float.MAX_VALUE;
                    Driver bestDriver = null;
                    if(pickUpMarker!=null & destMarker!=null) {
                        LatLng pickupPoint = pickUpMarker.getPosition();
                        //Toast.makeText(mContext,objectMap.values().size()+"",Toast.LENGTH_SHORT).show();
                        for (Object obj : objectMap.values()) {
                            Map<String, Object> mapObj = (Map<String, Object>) obj;
                            Driver driver = new Driver();
                            String driverId = (String) mapObj.get("driverId");
                            double latitude = (Double) mapObj.get("latitude");
                            double longitude = (Double) mapObj.get("longitude");

                           /* DataSnapshot snapshot = dataSnapshot.child("driverProfile");
                            if(snapshot!=null) {
                                Map<String, Object> profileMap = (HashMap<String, Object>)
                                        snapshot.getValue();
                                String firstname = (String) profileMap.get("firstname");
                                String govId = (String) profileMap.get("govID");
                                String lastname = (String) profileMap.get("lastname");
                                String number = (String) profileMap.get("number");
                                String route = (String) profileMap.get("route");
                                String status = (String) profileMap.get("status");
                                driver.setDriverProfile(new
                                        Driver.DriverProfile(firstname,govId,lastname,number,route,status));
                            }*/

                          //  Driver.DriverProfile  profile= (Driver.DriverProfile) mapObj.get("driverProfile");
                            driver.setDriverId(driverId);
                            driver.setLatitude(latitude);
                            driver.setLongitude(longitude);
                            //driver.setDriverProfile(profile);

                            float distance =  distance((float)latitude,(float) longitude,
                                    (float)pickupPoint.latitude,(float)pickupPoint.longitude);
                            if(distance<bestDistanceSoFar)
                            {
                                bestDistanceSoFar = distance;
                                bestDriver = driver;
                            }

                        }
                        if(bestDriver!=null)
                        {
                            Toast.makeText(mContext,bestDriver.getDriverId(),Toast.LENGTH_SHORT).show();
                            reference.child(bestDriver.getDriverId()).child("requests").child(mUser.getUid()).
                                    setValue(new PickUpInfo(mUser.getDisplayName(),
                                            pickupPoint.latitude,pickupPoint.longitude,
                                            destMarker.getPosition().latitude,destMarker.getPosition().longitude,"Requesting"));
                            mCurrentRequestDriver = bestDriver;
                            checkRequest();
                        }else{
                            Toast.makeText(mContext,"No Drivers found",Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(this,"No user",Toast.LENGTH_SHORT).show();
        }
    }

    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    private void getDriverLocations()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference().child("drivers");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> objectMap = (HashMap<String, Object>)
                        snapshot.getValue();
                drivers = new ArrayList<Driver>();
                //Toast.makeText(mContext,objectMap.values().size()+"",Toast.LENGTH_SHORT).show();

                for (Object obj : objectMap.values()) {

                        Map<String, Object> mapObj = (Map<String, Object>) obj;
                        Driver driver = new Driver();
                        driver.setDriverId((String)mapObj.get("driverId"));
                        //Toast.makeText(mContext,driver.getDriverId()+"",Toast.LENGTH_SHORT).show();
                       // driver.setDriverProfile((Driver.DriverProfile)mapObj.get("driverProfile"));
                        driver.setLatitude((Double)mapObj.get("latitude"));
                        driver.setLongitude((Double)mapObj.get("longitude"));

                    drivers.add(driver);

                }

                placeDriverMarkers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void drawRoute(LatLng origin, LatLng dest)
    {
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJsonParser parser = new DirectionsJsonParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions!=null)
                mMap.addPolyline(lineOptions);
        }
    }


}
