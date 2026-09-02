package com.dilshan.realestate.service;

import com.dilshan.realestate.dsa.AppointmentQueue;
import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Appointment;
import com.dilshan.realestate.model.Client;
import com.dilshan.realestate.model.enums.AppointmentStatus;
import com.dilshan.realestate.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> findByClient(Client client) {
        return appointmentRepository.findByClientOrderByAppointmentDateDesc(client);
    }

    public List<Appointment> findByAgent(Agent agent) {
        return appointmentRepository.findByAgentOrderByAppointmentDateDesc(agent);
    }

    public List<Appointment> findPendingByAgent(Agent agent) {
        return appointmentRepository.findByAgentAndStatus(agent, AppointmentStatus.PENDING);
    }

    public Appointment bookAppointment(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }

    public void updateStatus(Long id, AppointmentStatus status) {
        appointmentRepository.findById(id).ifPresent(app -> {
            app.setStatus(status);
            appointmentRepository.save(app);
        });
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

    /**
     * Build an in-memory FIFO queue for pending appointment requests.
     */
    public AppointmentQueue getPendingQueueForAgent(Agent agent) {
        AppointmentQueue queue = new AppointmentQueue();
        List<Appointment> pending = appointmentRepository.findByAgentAndStatus(agent, AppointmentStatus.PENDING);
        for (Appointment a : pending) {
            queue.enqueue(a);
        }
        return queue;
    }

    public long countTotalAppointments() {
        return appointmentRepository.count();
    }

    public long countPendingAppointments() {
        return appointmentRepository.countByStatus(AppointmentStatus.PENDING);
    }
}
