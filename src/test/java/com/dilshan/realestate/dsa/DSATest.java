package com.dilshan.realestate.dsa;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Appointment;
import com.dilshan.realestate.model.Client;
import com.dilshan.realestate.model.Property;
import com.dilshan.realestate.model.enums.PropertyType;
import com.dilshan.realestate.model.enums.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DSATest {

    private Agent agent1;
    private Agent agent2;
    private Agent agent3;

    @BeforeEach
    void setUp() {
        agent1 = new Agent("Alice Smith", "alice@test.com", "pass", "123", "LIC-1", Specialization.RESIDENTIAL, 5, "Malabe", "Bio 1");
        agent1.setId(1L);
        agent1.setRating(4.5);

        agent2 = new Agent("Bob Johnson", "bob@test.com", "pass", "456", "LIC-2", Specialization.COMMERCIAL, 12, "Colombo", "Bio 2");
        agent2.setId(2L);
        agent2.setRating(4.9);

        agent3 = new Agent("Charlie Brown", "charlie@test.com", "pass", "789", "LIC-3", Specialization.LUXURY, 2, "Kandy", "Bio 3");
        agent3.setId(3L);
        agent3.setRating(3.8);
    }

    @Test
    @DisplayName("Test Custom Binary Search Tree (BST) Insertion and In-Order Traversal")
    void testAgentBST() {
        AgentBST bst = new AgentBST();
        bst.insert(agent1);
        bst.insert(agent2);
        bst.insert(agent3);

        assertEquals(3, bst.getSize());

        // In-Order traversal should be ascending by rating: Charlie (3.8) -> Alice (4.5) -> Bob (4.9)
        List<Agent> inOrder = bst.getInOrderList();
        assertEquals(3, inOrder.size());
        assertEquals("Charlie Brown", inOrder.get(0).getName());
        assertEquals("Alice Smith", inOrder.get(1).getName());
        assertEquals("Bob Johnson", inOrder.get(2).getName());

        // Descending rating traversal: Bob (4.9) -> Alice (4.5) -> Charlie (3.8)
        List<Agent> desc = bst.getDescendingRatingList();
        assertEquals("Bob Johnson", desc.get(0).getName());
        assertEquals("Charlie Brown", desc.get(2).getName());

        // Search by keyword
        List<Agent> searchResults = bst.searchByName("Colombo");
        assertEquals(1, searchResults.size());
        assertEquals("Bob Johnson", searchResults.get(0).getName());
    }

    @Test
    @DisplayName("Test Custom Selection Sort by Rating")
    void testSelectionSort() {
        List<Agent> agents = new ArrayList<>(List.of(agent1, agent2, agent3));

        // Descending Selection Sort (Highest rating first)
        List<Agent> sorted = SortEngine.selectionSortByRating(agents, true);
        assertEquals(4.9, sorted.get(0).getRating());
        assertEquals(4.5, sorted.get(1).getRating());
        assertEquals(3.8, sorted.get(2).getRating());
    }

    @Test
    @DisplayName("Test Custom Bubble Sort by Property Price")
    void testBubbleSortProperties() {
        Property p1 = new Property("P1", "Desc", 50000000.0, "Addr", "City", "State", "Zip", PropertyType.HOUSE, 3, 2, 1500, null, "FOR_SALE", agent1);
        Property p2 = new Property("P2", "Desc", 25000000.0, "Addr", "City", "State", "Zip", PropertyType.APARTMENT, 2, 1, 1000, null, "FOR_SALE", agent2);
        Property p3 = new Property("P3", "Desc", 80000000.0, "Addr", "City", "State", "Zip", PropertyType.VILLA, 4, 3, 2500, null, "FOR_SALE", agent3);

        List<Property> props = new ArrayList<>(List.of(p1, p2, p3));

        // Ascending Bubble Sort (Lowest price first)
        List<Property> sortedAsc = SortEngine.bubbleSortByPrice(props, true);
        assertEquals(25000000.0, sortedAsc.get(0).getPrice());
        assertEquals(50000000.0, sortedAsc.get(1).getPrice());
        assertEquals(80000000.0, sortedAsc.get(2).getPrice());

        // Descending Bubble Sort (Highest price first)
        List<Property> sortedDesc = SortEngine.bubbleSortByPrice(props, false);
        assertEquals(80000000.0, sortedDesc.get(0).getPrice());
        assertEquals(25000000.0, sortedDesc.get(2).getPrice());
    }

    @Test
    @DisplayName("Test Custom FIFO Appointment Queue")
    void testAppointmentQueue() {
        Client client = new Client("David", "david@test.com", "pass", "999", PropertyType.HOUSE, "Malabe");
        Appointment app1 = new Appointment(client, agent1, LocalDate.now(), LocalTime.of(10, 0), "Note 1");
        Appointment app2 = new Appointment(client, agent2, LocalDate.now(), LocalTime.of(11, 0), "Note 2");

        AppointmentQueue queue = new AppointmentQueue();
        assertTrue(queue.isEmpty());

        queue.enqueue(app1);
        queue.enqueue(app2);

        assertEquals(2, queue.size());
        assertEquals("Note 1", queue.peek().getNotes());

        Appointment dequeued = queue.dequeue();
        assertEquals("Note 1", dequeued.getNotes());
        assertEquals(1, queue.size());
        assertEquals("Note 2", queue.peek().getNotes());
    }
}
