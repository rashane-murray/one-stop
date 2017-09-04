package palisadoes.org.onestop.models;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by stone on 9/1/17.
 */

public class DriverMarker {
    Marker marker;
    String driverId;

    public DriverMarker(Marker marker, String driverId) {
        this.marker = marker;
        this.driverId = driverId;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
