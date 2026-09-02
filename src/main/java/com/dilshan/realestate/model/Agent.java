package com.dilshan.realestate.model;

import com.dilshan.realestate.model.enums.Role;
import com.dilshan.realestate.model.enums.Specialization;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agents")
@PrimaryKeyJoinColumn(name = "user_id")
public class Agent extends User {

    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private int yearsOfExperience;
    private String serviceAreas; // Comma separated, e.g. "Malabe, Colombo, Kandy"
    private String profilePicture;
    private String bio;

    private boolean isVerified = false;
    private boolean isAvailable = true;

    private double rating = 5.0;
    private int reviewCount = 0;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<>();

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Agent() {
        super();
        setRole(Role.AGENT);
    }

    public Agent(String name, String email, String password, String contactNumber,
                 String licenseNumber, Specialization specialization, int yearsOfExperience,
                 String serviceAreas, String bio) {
        super(name, email, password, contactNumber, Role.AGENT);
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.serviceAreas = serviceAreas;
        this.bio = bio;
        this.isVerified = false;
        this.isAvailable = true;
        this.rating = 5.0;
        this.reviewCount = 0;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getServiceAreas() {
        return serviceAreas;
    }

    public void setServiceAreas(String serviceAreas) {
        this.serviceAreas = serviceAreas;
    }

    public String getProfilePicture() {
        return profilePicture != null && !profilePicture.isBlank() ? profilePicture : "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=400";
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
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

    public void recalculateRating() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            this.rating = 5.0;
            this.reviewCount = 0;
            return;
        }
        double sum = 0;
        for (Feedback f : feedbacks) {
            sum += f.getRating();
        }
        this.rating = Math.round((sum / feedbacks.size()) * 10.0) / 10.0;
        this.reviewCount = feedbacks.size();
    }
}
