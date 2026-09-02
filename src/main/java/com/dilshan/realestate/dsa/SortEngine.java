package com.dilshan.realestate.dsa;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Sorting Engine showcasing classical Data Structures & Algorithms.
 * Includes Selection Sort, Bubble Sort, and QuickSort with in-place operations.
 */
public class SortEngine {

    /**
     * Selection Sort to sort Real Estate Agents by rating.
     * Time Complexity: O(n^2), Space Complexity: O(1) in-place.
     *
     * @param agents List of agents to sort
     * @param descending If true, sorts from highest rating to lowest rating
     * @return Sorted new list
     */
    public static List<Agent> selectionSortByRating(List<Agent> agents, boolean descending) {
        if (agents == null || agents.size() <= 1) {
            return agents != null ? new ArrayList<>(agents) : new ArrayList<>();
        }

        List<Agent> list = new ArrayList<>(agents);
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            int targetIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (descending) {
                    if (list.get(j).getRating() > list.get(targetIdx).getRating()) {
                        targetIdx = j;
                    }
                } else {
                    if (list.get(j).getRating() < list.get(targetIdx).getRating()) {
                        targetIdx = j;
                    }
                }
            }
            if (targetIdx != i) {
                Agent temp = list.get(i);
                list.set(i, list.get(targetIdx));
                list.set(targetIdx, temp);
            }
        }
        return list;
    }

    /**
     * Bubble Sort to sort Real Estate Properties by Price.
     * Time Complexity: O(n^2) worst/avg, O(n) best with early termination flag.
     *
     * @param properties List of properties to sort
     * @param ascending If true, lowest price to highest price
     * @return Sorted new list
     */
    public static List<Property> bubbleSortByPrice(List<Property> properties, boolean ascending) {
        if (properties == null || properties.size() <= 1) {
            return properties != null ? new ArrayList<>(properties) : new ArrayList<>();
        }

        List<Property> list = new ArrayList<>(properties);
        int n = list.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                boolean condition = ascending
                        ? list.get(j).getPrice() > list.get(j + 1).getPrice()
                        : list.get(j).getPrice() < list.get(j + 1).getPrice();

                if (condition) {
                    Property temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break; // Optimized early exit
        }
        return list;
    }

    /**
     * QuickSort to sort Agents by Years of Experience.
     * Time Complexity: O(n log n) average, Space Complexity: O(log n).
     */
    public static List<Agent> quickSortByExperience(List<Agent> agents, boolean descending) {
        if (agents == null || agents.size() <= 1) {
            return agents != null ? new ArrayList<>(agents) : new ArrayList<>();
        }
        List<Agent> list = new ArrayList<>(agents);
        quickSortRecursive(list, 0, list.size() - 1, descending);
        return list;
    }

    private static void quickSortRecursive(List<Agent> list, int low, int high, boolean descending) {
        if (low < high) {
            int pi = partition(list, low, high, descending);
            quickSortRecursive(list, low, pi - 1, descending);
            quickSortRecursive(list, pi + 1, high, descending);
        }
    }

    private static int partition(List<Agent> list, int low, int high, boolean descending) {
        int pivot = list.get(high).getYearsOfExperience();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = descending
                    ? list.get(j).getYearsOfExperience() >= pivot
                    : list.get(j).getYearsOfExperience() <= pivot;

            if (condition) {
                i++;
                Agent temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Agent temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
}
