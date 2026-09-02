package com.dilshan.realestate.model;

import com.dilshan.realestate.model.enums.PropertyType;
import com.dilshan.realestate.model.enums.Role;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Enumerated(EnumType.STRING)
    private PropertyType preferredPropertyType;

    private String preferredLocation;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Client() {
        super();
        setRole(Role.CLIENT);
    }

    public Client(String name, String email, String password, String contactNumber,
                  PropertyType preferredPropertyType, String preferredLocation) {
        super(name, email, password, contactNumber, Role.CLIENT);
        this.preferredPropertyType = preferredPropertyType;
        this.preferredLocation = preferredLocation;
    }

    public PropertyType getPreferredPropertyType() {
        return preferredPropertyType;
    }

    public void setPreferredPropertyType(PropertyType preferredPropertyType) {
        this.preferredPropertyType = preferredPropertyType;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
