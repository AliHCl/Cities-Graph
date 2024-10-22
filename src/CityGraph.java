import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * The Graph class represents a collection of cities and their connections (edges).
 * Each city is represented as a key in the adjacency list, and its value is a list of edges
 * that indicate other cities connected to it and the distance between them.
 */
class Graph {
    // adjList is a HashMap where the key is the name of the cities (String)
    // and the value is a list of edges (LinkedList<Edge>) that represent connections to other cities.
    HashMap<String, LinkedList<Edge>> adjList;

    // Constructor that initializes the adjacency list to store cities and their edges.
    public Graph() {
        adjList = new HashMap<>();
    }

    /**
     * Adds a two-way connection (edge) between two cities with a specified distance.
     * Throws an exception if the edge already exists.
     *
     * @param city1    the name of the first city
     * @param city2    the name of the second city
     * @param distance the distance between the two cities
     * @throws IllegalArgumentException if the edge already exists between the two cities
     */
    void addEdge(String city1, String city2, int distance) throws IllegalArgumentException {
        // Check if the connection (edge) already exists between the cities
        if (edgeExists(city1, city2)) {
            throw new IllegalArgumentException("Edge already exists: " + city1 + " - " + city2);
        }

        // Ensure both cities are in the adjacency list before adding the edge
        adjList.putIfAbsent(city1, new LinkedList<>());
        adjList.putIfAbsent(city2, new LinkedList<>());

        // Add an edge from city1 to city2 and from city2 to city1 (since the graph is undirected)
        adjList.get(city1).add(new Edge(city2, distance));
        adjList.get(city2).add(new Edge(city1, distance));
    }

    /**
     * Checks whether an edge already exists between two cities.
     *
     * @param city1 the first city
     * @param city2 the second city
     * @return true if an edge exists, false otherwise
     */
    private boolean edgeExists(String city1, String city2) {
        if (adjList.containsKey(city1)) {
            for (Edge edge : adjList.get(city1)) {
                if (edge.destination.equals(city2)) {
                    return true; // Edge found between the two cities
                }
            }
        }
        return false; // No edge found
    }
}

/**
 * The Edge class represents a connection from one city to another with a specific distance.
 */
class Edge {
    String destination; // The destination city
    int distance;       // The distance to the destination city

    // Constructor for the Edge class to initialize the destination and distance
    public Edge(String destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
}

/**
 * The UCS (Uniform Cost Search) class performs the search algorithm to find the shortest path between two cities.
 */
class UCS {
    private Graph graph;

    // Constructor to initialize UCS with a specific graph (set of cities and edges)
    public UCS(Graph graph) {
        this.graph = graph;
    }

    /**
     * Finds the shortest path between the start city and the goal city using the UCS algorithm.
     * Prints the path and the total cost if found, otherwise informs the user that no path exists.
     *
     * @param start the starting city
     * @param goal  the goal city
     */
    public void findWay(String start, String goal) {
        LinkedList<String> notCheckList = new LinkedList<>();  // Cities yet to be checked
        HashMap<String, Integer> costMap = new HashMap<>();    // Stores the minimum cost to reach each city
        HashMap<String, String> wayMap = new HashMap<>();      // Keeps track of the path (previous city for each city)

        // Initialize the starting city
        notCheckList.add(start);
        costMap.put(start, 0);
        wayMap.put(start, null);

        String bestGoalWay = null; // Variable to store the best way (if found)
        int bestGoalCost = Integer.MAX_VALUE; // Variable to store the lowest cost to the goal

        // Main loop to explore the graph and find the shortest path
        while (!notCheckList.isEmpty()) {
            String currentCity = null;
            int currentCost = Integer.MAX_VALUE;

            // Find the city with the lowest cost from the cities yet to be checked
            for (String city : notCheckList) {
                int cost = costMap.get(city);
                if (cost < currentCost) {
                    currentCost = cost;
                    currentCity = city;
                }
            }

            // If no city is found, break out of the loop
            if (currentCity == null) {
                break;
            }

            // Check if the current city is the goal and update the best path if necessary
            if (currentCity.equals(goal)) {
                if (currentCost < bestGoalCost) {
                    bestGoalCost = currentCost;
                    bestGoalWay = currentCity;
                }
            }

            // Remove the current city from the list of cities to be checked
            notCheckList.remove(currentCity);

            // Explore all neighboring cities and update their costs if a better path is found
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

        // If a path to the goal was found, print the path and its cost, otherwise inform the user no path was found
        if (bestGoalWay != null) {
            printWay(wayMap, start, goal);
            System.out.println("Best way cost: " + bestGoalCost);
        } else {
            System.out.println("No way found from " + start + " to " + goal);
        }
    }

    /**
     * Helper method to print the path from the start city to the goal city.
     * It reconstructs the path using the wayMap.
     *
     * @param wayMap the map that stores the previous city for each city in the path
     * @param start  the starting city
     * @param goal   the goal city
     */
    private void printWay(HashMap<String, String> wayMap, String start, String goal) {
        LinkedList<String> way = new LinkedList<>();
        String city = goal;

        // Reconstruct the path by following the wayMap from the goal to the start
        while (city != null) {
            way.addFirst(city);
            city = wayMap.get(city);
        }

        // Print the path in the format "city1 --> city2 --> ... --> goal"
        System.out.println("Way: " + String.join(" --> ", way));
    }

    /**
     * Prompts the user to enter the start and goal cities, and then runs UCS to find the path.
     */
    public void runUCS() {
         // Get all cities from the graph's adjacency list
         LinkedList<String> cities = new LinkedList<>(graph.adjList.keySet());

         // Check all pairs of cities
         for (int i = 0; i < cities.size(); i++) {
             for (int j = i + 1; j < cities.size(); j++) {
                 String start = cities.get(i);
                 String goal = cities.get(j);
 
                 // Run UCS to find the shortest path between each pair
                 System.out.println("Finding path between " + start + " and " + goal + ":");
                 findWay(start, goal);
                 System.out.println("------------------------");
             }
         }
    }
}

/**
 * The main class that manages the input of cities and edges from the user,
 * builds the graph, and then runs the UCS algorithm to find the shortest path.
 */
public class CityGraph {
    public static void main(String[] args) {
        // Create a scanner to receive user input for city connections
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph(); // Create a new graph to store cities and edges

        System.out.println("Enter city connections or type 'exit' to stop:");

        // Loop to receive input of city connections until 'exit' is entered
        while (true) {
            String input = scanner.nextLine();

            // Exit the loop if the user types "exit"
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Parse the input to extract city1, city2, and distance
            String[] parts = input.split(" ");
            if (parts.length != 3) {
                System.out.println("Invalid input. Please enter in format: city1 city2 distance");
                continue;
            }

            String city1 = parts[0];
            String city2 = parts[1];
            int distance;

            // Convert the distance to an integer and check for input validity
            try {
                distance = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid distance. Please enter a valid number for distance.");
                continue;
            }

            // Add the edge to the graph, checking for duplicate edges
            try {
                graph.addEdge(city1, city2, distance);
                System.out.println("Edge added: " + city1 + " - " + city2 + " (" + distance + " km)");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // After all cities and edges have been added, run UCS to find the shortest path
        System.out.println("Cities and Edges added Successfully");

        UCS ucs = new UCS(graph); // Create a UCS instance with the constructed graph
        ucs.runUCS();             // Run UCS to get user input for start and goal cities
    }
}
