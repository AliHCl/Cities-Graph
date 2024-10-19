import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

class Graph {
    HashMap<String, LinkedList<Edge>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    void addEdge(String city1, String city2, int distance) {
        adjList.putIfAbsent(city1, new LinkedList<>());
        adjList.putIfAbsent(city2, new LinkedList<>());

        adjList.get(city1).add(new Edge(city2, distance));
        adjList.get(city2).add(new Edge(city1, distance));
    }

    void displayGraph() {
        for (String city : adjList.keySet()) {
            System.out.print(city + " is connected to: ");
            for (Edge edge : adjList.get(city)) {
                System.out.print(edge.destination + " (" + edge.distance + " km), ");
            }
            System.out.println();
        }
    }
}

class Edge {
    String destination;
    int distance;

    public Edge(String destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
}

public class CityGraph {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph();

        System.out.println("Enter city connections or type 'exit' to stop:");

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 3) {
                System.out.println("Invalid input. Please enter in format: city1 city2 distance");
                continue;
            }

            String city1 = parts[0];
            String city2 = parts[1];
            int distance;

            try {
                distance = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid distance. Please enter a valid number for distance.");
                continue;
            }

            graph.addEdge(city1, city2, distance);
            System.out.println("Edge added: " + city1 + " - " + city2 + " (" + distance + " km)");
        }

        System.out.println("\nGraph connections:");
        graph.displayGraph();
    }
}
