package com.dilshan.realestate.dsa;

import com.dilshan.realestate.model.Agent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Binary Search Tree (BST) implementation to index, store, and search Real Estate Agents.
 * Keys are based on Agent Rating and ID to ensure balanced, deterministic binary tree indexing.
 * Time Complexity: Insert: O(log n) avg, Search: O(log n) avg, In-Order Traversal: O(n)
 */
public class AgentBST {

    public static class Node {
        public Agent agent;
        public Node left;
        public Node right;

        public Node(Agent agent) {
            this.agent = agent;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private int size;

    public AgentBST() {
        this.root = null;
        this.size = 0;
    }

    public void insert(Agent agent) {
        if (agent == null) return;
        root = insertRecursive(root, agent);
        size++;
    }

    private Node insertRecursive(Node current, Agent agent) {
        if (current == null) {
            return new Node(agent);
        }

        // Compare by rating first, then by ID for deterministic placement
        if (agent.getRating() < current.agent.getRating()) {
            current.left = insertRecursive(current.left, agent);
        } else if (agent.getRating() > current.agent.getRating()) {
            current.right = insertRecursive(current.right, agent);
        } else {
            // Equal rating: order by agent id or name
            if (agent.getId() != null && current.agent.getId() != null && agent.getId() < current.agent.getId()) {
                current.left = insertRecursive(current.left, agent);
            } else {
                current.right = insertRecursive(current.right, agent);
            }
        }
        return current;
    }

    /**
     * Search agents by name substring recursively across the BST.
     */
    public List<Agent> searchByName(String keyword) {
        List<Agent> results = new ArrayList<>();
        if (keyword == null || keyword.isBlank()) {
            return getInOrderList();
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        searchByNameRecursive(root, lowerKeyword, results);
        return results;
    }

    private void searchByNameRecursive(Node node, String keyword, List<Agent> results) {
        if (node == null) return;
        searchByNameRecursive(node.left, keyword, results);
        if (node.agent.getName().toLowerCase().contains(keyword) ||
            (node.agent.getServiceAreas() != null && node.agent.getServiceAreas().toLowerCase().contains(keyword))) {
            results.add(node.agent);
        }
        searchByNameRecursive(node.right, keyword, results);
    }

    /**
     * Returns agents in descending order of rating (Highest to Lowest) via Reverse In-Order Traversal.
     */
    public List<Agent> getDescendingRatingList() {
        List<Agent> list = new ArrayList<>();
        reverseInOrderRecursive(root, list);
        return list;
    }

    private void reverseInOrderRecursive(Node node, List<Agent> list) {
        if (node == null) return;
        reverseInOrderRecursive(node.right, list);
        list.add(node.agent);
        reverseInOrderRecursive(node.left, list);
    }

    /**
     * In-order traversal (Ascending rating).
     */
    public List<Agent> getInOrderList() {
        List<Agent> list = new ArrayList<>();
        inOrderRecursive(root, list);
        return list;
    }

    private void inOrderRecursive(Node node, List<Agent> list) {
        if (node == null) return;
        inOrderRecursive(node.left, list);
        list.add(node.agent);
        inOrderRecursive(node.right, list);
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }
}
