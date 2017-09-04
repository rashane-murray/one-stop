package palisadoes.org.onestop.models;

/**
 * Created by stone on 8/31/17.
 */

public class PickUpInfo {

    String name;
    double pickUpLat;
    double pickUpLng;
    double dropoffLat;
    double dropoffLng;
    String status = "Idle";

    public PickUpInfo(String name, double pickUpLat, double pickUpLng,double dropoffLat,double dropoffLng,String status) {
        this.name = name;
        this.pickUpLat = pickUpLat;
        this.pickUpLng = pickUpLng;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
        this.status = "Requesting";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPickUpLat() {
        return pickUpLat;
    }

    public void setPickUpLat(double pickUpLat) {
        this.pickUpLat = pickUpLat;
    }

    public double getPickUpLng() {
        return pickUpLng;
    }

    public void setPickUpLng(double pickUpLng) {
        this.pickUpLng = pickUpLng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
