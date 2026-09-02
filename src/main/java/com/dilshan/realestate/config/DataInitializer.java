package com.dilshan.realestate.config;

import com.dilshan.realestate.model.*;
import com.dilshan.realestate.model.enums.AppointmentStatus;
import com.dilshan.realestate.model.enums.PropertyType;
import com.dilshan.realestate.model.enums.Specialization;
import com.dilshan.realestate.repository.*;
import com.dilshan.realestate.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final PropertyRepository propertyRepository;
    private final AppointmentRepository appointmentRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    public DataInitializer(UserRepository userRepository,
                           AgentRepository agentRepository,
                           ClientRepository clientRepository,
                           AdminRepository adminRepository,
                           PropertyRepository propertyRepository,
                           AppointmentRepository appointmentRepository,
                           FeedbackRepository feedbackRepository,
                           UserService userService) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
        this.propertyRepository = propertyRepository;
        this.appointmentRepository = appointmentRepository;
        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already exists
        }

        System.out.println(">>> Seeding Real Estate Platform Initial Data...");

        // 1. Create Admin
        Admin admin = new Admin(
                "System Administrator",
                "admin@realestate.com",
                userService.hashPassword("admin123"),
                "+94 77 123 4567",
                "Headquarters",
                true
        );
        adminRepository.save(admin);

        // 2. Create Agents
        Agent agent1 = new Agent(
                "Sarah Johnson",
                "sarah.agent@realestate.com",
                userService.hashPassword("agent123"),
                "+94 71 234 5678",
                "RE-LIC-2023-891",
                Specialization.RESIDENTIAL,
                8,
                "Malabe, Colombo, Battaramulla",
                "Dedicated residential real estate specialist with over 8 years of experience helping families find their dream home."
        );
        agent1.setProfilePicture("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=500&auto=format&fit=crop&q=80");
        agent1.setVerified(true);
        agent1.setAvailable(true);
        agent1 = agentRepository.save(agent1);

        Agent agent2 = new Agent(
                "Michael Rodriguez",
                "michael.agent@realestate.com",
                userService.hashPassword("agent123"),
                "+94 76 345 6789",
                "RE-LIC-2021-442",
                Specialization.COMMERCIAL,
                12,
                "Colombo 03, Colombo 07, Kandy",
                "Commercial property strategist specializing in prime office spaces, retail outlets, and investment portfolios."
        );
        agent2.setProfilePicture("https://images.unsplash.com/photo-1560250097-0b93528c311a?w=500&auto=format&fit=crop&q=80");
        agent2.setVerified(true);
        agent2.setAvailable(true);
        agent2 = agentRepository.save(agent2);

        Agent agent3 = new Agent(
                "Alice Smith",
                "alice.agent@realestate.com",
                userService.hashPassword("agent123"),
                "+94 70 456 7890",
                "RE-LIC-2024-102",
                Specialization.LUXURY,
                6,
                "Malabe, Kurunegala, Kandy",
                "Passionate about luxury estates, beachfront villas, and modern architectural masterpieces."
        );
        agent3.setProfilePicture("https://images.unsplash.com/photo-1580489944761-15a19d654956?w=500&auto=format&fit=crop&q=80");
        agent3.setVerified(true);
        agent3.setAvailable(true);
        agent3 = agentRepository.save(agent3);

        Agent agent4 = new Agent(
                "David Lee",
                "david.agent@realestate.com",
                userService.hashPassword("agent123"),
                "+94 72 567 8901",
                "RE-LIC-2025-901",
                Specialization.RESIDENTIAL,
                3,
                "Kurunegala, Negombo",
                "Up-and-coming agent focused on first-time home buyers and rental properties."
        );
        agent4.setProfilePicture("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=80");
        agent4.setVerified(false); // Waiting for verification
        agent4.setAvailable(true);
        agent4 = agentRepository.save(agent4);

        // 3. Create Clients
        Client client1 = new Client(
                "John Doe",
                "client@realestate.com",
                userService.hashPassword("client123"),
                "+94 77 987 6543",
                PropertyType.HOUSE,
                "Malabe"
        );
        client1 = clientRepository.save(client1);

        Client client2 = new Client(
                "Emily Watson",
                "emily@gmail.com",
                userService.hashPassword("password123"),
                "+94 78 876 5432",
                PropertyType.APARTMENT,
                "Colombo"
        );
        client2 = clientRepository.save(client2);

        // 4. Create Properties
        Property p1 = new Property(
                "Modern Luxury Villa with Pool",
                "Spacious 4-bedroom luxury villa with private infinity pool, landscaped garden, and high-end smart home automation in prime Malabe neighborhood.",
                75000000.0,
                "45 Green View Avenue",
                "Malabe",
                "Western Province",
                "10115",
                PropertyType.VILLA,
                4,
                4,
                3800.0,
                "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800&auto=format&fit=crop&q=80",
                "FOR_SALE",
                agent1
        );
        propertyRepository.save(p1);

        Property p2 = new Property(
                "Ocean View Luxury Penthouse",
                "Breathtaking 360-degree ocean views, private rooftop terrace, designer kitchen, and 24/7 concierge service in the heart of Colombo.",
                98000000.0,
                "12 Galle Face Terrace",
                "Colombo",
                "Western Province",
                "00300",
                PropertyType.APARTMENT,
                3,
                3,
                2400.0,
                "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=800&auto=format&fit=crop&q=80",
                "FOR_SALE",
                agent3
        );
        propertyRepository.save(p2);

        Property p3 = new Property(
                "Prime Commercial Tower Office Suite",
                "Fully fitted grade-A commercial office space with conference rooms, fiber connectivity, and dedicated basement parking.",
                45000000.0,
                "108 World Trade Center",
                "Colombo",
                "Western Province",
                "00100",
                PropertyType.COMMERCIAL,
                0,
                2,
                1850.0,
                "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&auto=format&fit=crop&q=80",
                "FOR_SALE",
                agent2
        );
        propertyRepository.save(p3);

        Property p4 = new Property(
                "Cozy Suburban Family Home",
                "Charming 3-bedroom single-family house with large backyard, solar power, and close proximity to premier international schools.",
                32000000.0,
                "78 Lake Road",
                "Kurunegala",
                "North Western Province",
                "60000",
                PropertyType.HOUSE,
                3,
                2,
                1950.0,
                "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800&auto=format&fit=crop&q=80",
                "FOR_SALE",
                agent1
        );
        propertyRepository.save(p4);

        // 5. Create Feedbacks
        Feedback f1 = new Feedback(
                client1,
                agent1,
                5,
                "Sarah is an exceptional agent! She helped us inspect 4 properties and negotiated a fantastic deal on our new home in Malabe. Highly recommended!"
        );
        feedbackRepository.save(f1);

        Feedback f2 = new Feedback(
                client2,
                agent1,
                4,
                "Very professional and knowledgeable about the local market. Quick responses throughout the entire process."
        );
        feedbackRepository.save(f2);

        Feedback f3 = new Feedback(
                client1,
                agent2,
                5,
                "Michael found us the perfect commercial space for our expansion in record time. Outstanding market insight."
        );
        feedbackRepository.save(f3);

        Feedback f4 = new Feedback(
                client2,
                agent3,
                5,
                "Alice's attention to detail with luxury properties is unmatched. Wonderful experience!"
        );
        feedbackRepository.save(f4);

        // Recalculate Ratings for agents
        agent1.recalculateRating();
        agentRepository.save(agent1);
        agent2.recalculateRating();
        agentRepository.save(agent2);
        agent3.recalculateRating();
        agentRepository.save(agent3);

        // 6. Create Appointments
        Appointment app1 = new Appointment(
                client1,
                agent1,
                LocalDate.now().plusDays(2),
                LocalTime.of(10, 30),
                "Viewing the Modern Luxury Villa in Malabe."
        );
        app1.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(app1);

        Appointment app2 = new Appointment(
                client2,
                agent2,
                LocalDate.now().plusDays(4),
                LocalTime.of(14, 0),
                "Consultation on commercial lease in Colombo 03."
        );
        app2.setStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(app2);

        System.out.println(">>> Real Estate Platform Initial Data Seeded Successfully!");
        System.out.println(">>> [Default Admin] Email: admin@realestate.com | Password: admin123");
        System.out.println(">>> [Default Agent] Email: sarah.agent@realestate.com | Password: agent123");
        System.out.println(">>> [Default Client] Email: client@realestate.com | Password: client123");
    }
}
