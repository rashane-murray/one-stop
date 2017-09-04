package palisadoes.org.onestop.models;

/**
 * Created by stone on 9/1/17.
 */

public class Passenger {

    String name;
    String phone;

    public Passenger(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
