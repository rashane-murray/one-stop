package palisadoes.org.onestop.models;

/**
 * Created by stone on 8/30/17.
 */

public class Driver {

    String driverId;
    DriverProfile driverProfile;
    double latitude;
    double longitude;


    public Driver()
    {

    }


    public Driver(String driverId,DriverProfile driverProfile,double latitude,double longitude)
    {
        this.driverId = driverId;
        this.driverProfile = driverProfile;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DriverProfile getDriverProfile() {
        return driverProfile;
    }

    public void setDriverProfile(DriverProfile driverProfile) {
        this.driverProfile = driverProfile;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static class DriverProfile
    {
        String firstname;
        String govID;
        String lastname;
        String number;
       // String vehicle_type;
        String route;
        String status;

        public DriverProfile(String firstname, String govID, String lastname, String number, String route, String status) {
            this.firstname = firstname;
            this.govID = govID;
            this.lastname = lastname;
            this.number = number;
            this.route = route;
            this.status = status;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

       /* public String getVehicle_type() {
            return vehicle_type;
        }

        public void setVehicle_type(String vehicle_type) {
            this.vehicle_type = vehicle_type;
        }*/

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }
    }

}
