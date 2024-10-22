import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

// Graph class for representing and managing the graph (cities and their connections)
class Graph {
    // adjList is a HashMap where the key is the name of the cities (String)
    // and the value is a list of edges (LinkedList<Edge>) that represent connections.
    HashMap<String, LinkedList<Edge>> adjList;

    // Constructor for the Graph class that initializes the adjList.
    public Graph() {
        adjList = new HashMap<>();
    }

    // Method to add an edge (connection) between two cities with a specified distance
    void addEdge(String city1, String city2, int distance) throws IllegalArgumentException {
        // Check if the edge already exists
        if (edgeExists(city1, city2)) {
            throw new IllegalArgumentException("Edge already exists: " + city1 + " - " + city2);
        }

        // If city1 is not already in adjList, initialize it with a new LinkedList.
        adjList.putIfAbsent(city1, new LinkedList<>());
        // If city2 is not already in adjList, initialize it with a new LinkedList.
        adjList.putIfAbsent(city2, new LinkedList<>());

        // Add a new edge to the adjacency list for city1 pointing to city2 with the given distance.
        adjList.get(city1).add(new Edge(city2, distance));
        // Add a new edge to the adjacency list for city2 pointing back to city1 with the same distance.
        adjList.get(city2).add(new Edge(city1, distance));
    }

    private boolean edgeExists(String city1, String city2) {
        if (adjList.containsKey(city1)) {
            for (Edge edge : adjList.get(city1)) {
                if (edge.destination.equals(city2)) {
                    return true; // Edge already exists
                }
            }
        }
        return false; // Edge does not exist
    }

}

// Edge class to represent a connection from one city to another with a specified distance
class Edge {
    // Destination (the name of the other city) and the distance between the two cities
    String destination;
    int distance;

    // Constructor for the Edge class that initializes the destination and distance.
    public Edge(String destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
}

class UCS {
    private Graph graph;

    public UCS(Graph graph) {
        this.graph = graph;
    }

    // Find the way from the start city to the goal city
    public void findWay(String start, String goal) {
        LinkedList<String> notCheckList = new LinkedList<>(); 
        HashMap<String, Integer> costMap = new HashMap<>(); 
        HashMap<String, String> wayMap = new HashMap<>(); 

        notCheckList.add(start);
        costMap.put(start, 0);
        wayMap.put(start, null);

        String bestGoalWay = null;
        int bestGoalCost = Integer.MAX_VALUE;

        // Find the city with the lowest cost
        while (!notCheckList.isEmpty()) {
            String currentCity = null;
            int currentCost = Integer.MAX_VALUE;

            for (String city : notCheckList) {
                int cost = costMap.get(city);
                if (cost < currentCost) {
                    currentCost = cost;
                    currentCity = city;
                }
            }

            if (currentCity == null) {
                break;
            }
            
            // Goal test 
            if (currentCity.equals(goal)) {
                if (currentCost < bestGoalCost) {
                    bestGoalCost = currentCost;
                    bestGoalWay = currentCity;
                }
            }

            notCheckList.remove(currentCity);

            for (Edge edge : graph.adjList.get(currentCity)) {
                int newCost = currentCost + edge.distance;

                Integer oldCost = costMap.get(edge.destination);
                if (oldCost == null || newCost < oldCost) {
                    costMap.put(edge.destination, newCost);
                    notCheckList.add(edge.destination);
                    wayMap.put(edge.destination, currentCity);
                }
            }
        }

        if (bestGoalWay != null) {
            printWay(wayMap, start, goal);
            System.out.println("Best way cost: " + bestGoalCost);
        } else {
            System.out.println("No way found from " + start + " to " + goal);
        }
    }

    private void printWay(HashMap<String, String> wayMap, String start, String goal) {
        LinkedList<String> way = new LinkedList<>();
        String city = goal;

        while (city != null) {
            way.addFirst(city);
            city = wayMap.get(city);
        }

        System.out.println("Way: " + String.join(" --> ", way));
    }
}

public class CityGraph {
    public static void main(String[] args) {
        // Create a scanner to receive user input
        Scanner scanner = new Scanner(System.in);
        // Create an instance of the Graph class to store the city graph
        Graph graph = new Graph();

        // Initial message to guide the user for entering data
        System.out.println("Enter city connections or type 'exit' to stop:");

        // Loop to receive inputs from the user until 'exit' is entered
        while (true) {
            // Read a line of input from the user
            String input = scanner.nextLine();

            // If the user enters 'exit', break out of the loop.
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Split the input into three parts: city1, city2, and distance
            String[] parts = input.split(" ");
            // Check if the input contains exactly three parts
            if (parts.length != 3) {
                System.out.println("Invalid input. Please enter in format: city1 city2 distance");
                continue; // Continue to the next iteration for valid input
            }

            // Retrieve the city names and distance from the input
            String city1 = parts[0];
            String city2 = parts[1];
            int distance;

            // Convert the third part of the input to an integer and validate it
            try {
                distance = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid distance. Please enter a valid number for distance.");
                continue; // Continue to the next iteration for valid input
            }

            // Try to add the edge, catching any exceptions if the edge already exists
            try {
                graph.addEdge(city1, city2, distance);
                System.out.println("Edge added: " + city1 + " - " + city2 + " (" + distance + " km)");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage()); // Print the exception message
            }
        }

        // Print a final message in Console
        System.out.println("Cities and Edges added Successfully");
    }
}
