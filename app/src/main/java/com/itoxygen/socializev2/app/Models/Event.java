package com.itoxygen.socializev2.app.Models;

/**
 * Basic Event object with necessary info for app.
 */
public class Event {

    private String name;
    private String date;
    private String time;
    private String invitees;
    private String location;
    private String address;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;

    public Event(String name, String date, String time, String invitees, String location, String address) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.invitees = invitees;
        this.location = location;
        this.address = address;
    }

    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInvitees() {
        return invitees;
    }

    public void setInvitees(String invitees) {
        this.invitees = invitees;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
