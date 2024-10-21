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
