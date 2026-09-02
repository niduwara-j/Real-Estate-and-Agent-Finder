package com.dilshan.realestate.repository;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Appointment;
import com.dilshan.realestate.model.Client;
import com.dilshan.realestate.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClientOrderByAppointmentDateDesc(Client client);
    List<Appointment> findByAgentOrderByAppointmentDateDesc(Agent agent);
    List<Appointment> findByAgentAndStatus(Agent agent, AppointmentStatus status);
    List<Appointment> findByStatus(AppointmentStatus status);
    long countByStatus(AppointmentStatus status);
}
