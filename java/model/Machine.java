package model;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Created by PC on 7/2/2015.
 */

public class Machine implements Serializable {

    private static final long serialVersionUID = 8286393242028201688L;

    private String FromNumber;
    private String Location;
    private String gpsLocation;
    private String userName;
    private GregorianCalendar datePositioned;

    public Machine() {
    }

    public Machine(String fromNumber) {
        this.FromNumber = fromNumber;
    }

    public Machine(String FromNumber, String Location) {
        this.FromNumber = FromNumber;
        this.Location = Location;
        this.gpsLocation = "";
    }



    public Machine(String FromNumber, String Location, String gpsLocation) {
        this.FromNumber = FromNumber;
        this.Location = Location;
        this.gpsLocation = gpsLocation;
    }

    public Machine(String FromNumber, String Location, String gpsLocation, String userName, GregorianCalendar datePositioned) {
        this.FromNumber = FromNumber;
        this.Location = Location;
        this.gpsLocation = gpsLocation;
        this.userName = userName;
        this.datePositioned = datePositioned;
    }

    public String getFromNumber() {
        return FromNumber;
    }

    public void setFromNumber(String FromNumber) {
        this.FromNumber = FromNumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GregorianCalendar getDatePositioned() {
        return datePositioned;
    }

    public void setDatePositioned(GregorianCalendar datePositioned) {
        this.datePositioned = datePositioned;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "FromNumber='" + FromNumber + '\'' +
                ", Location='" + Location + '\'' +
                ", gpsLocation='" + gpsLocation + '\'' +
                '}';
    }
}
