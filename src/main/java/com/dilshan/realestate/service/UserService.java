package com.dilshan.realestate.service;

import com.dilshan.realestate.model.Admin;
import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Client;
import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.Role;
import com.dilshan.realestate.repository.AdminRepository;
import com.dilshan.realestate.repository.AgentRepository;
import com.dilshan.realestate.repository.ClientRepository;
import com.dilshan.realestate.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;

    public UserService(UserRepository userRepository,
                       AgentRepository agentRepository,
                       ClientRepository clientRepository,
                       AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public Client registerClient(Client client) {
        if (isEmailTaken(client.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        client.setPassword(hashPassword(client.getPassword()));
        client.setRole(Role.CLIENT);
        return clientRepository.save(client);
    }

    public Agent registerAgent(Agent agent) {
        if (isEmailTaken(agent.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        agent.setPassword(hashPassword(agent.getPassword()));
        agent.setRole(Role.AGENT);
        agent.setVerified(false); // Requires admin approval
        return agentRepository.save(agent);
    }

    public Admin registerAdmin(Admin admin) {
        if (isEmailTaken(admin.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        admin.setPassword(hashPassword(admin.getPassword()));
        admin.setRole(Role.ADMIN);
        return adminRepository.save(admin);
    }

    public Optional<User> authenticate(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (checkPassword(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) return false;
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
