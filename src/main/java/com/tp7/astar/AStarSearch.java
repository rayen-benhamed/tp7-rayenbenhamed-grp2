package com.tp7.astar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class AStarSearch {
    private final Graph graph;
    private final ToDoubleFunction<Node> heuristicProvider;

    public AStarSearch(Graph graph, ToDoubleFunction<Node> heuristicProvider) {
        this.graph = graph;
        this.heuristicProvider = heuristicProvider;
    }

    public SearchResult findPath(String startId, String goalId) {
        Node start = graph.getNode(startId);
        Node goal = graph.getNode(goalId);

        if (start == null || goal == null) {
            throw new IllegalArgumentException("Start or goal node not found in graph.");
        }

        for (Node node : graph.getNodes()) {
            node.resetScores();
            node.setH(heuristicProvider.applyAsDouble(node));
        }

        PriorityQueue<OpenEntry> openList = new PriorityQueue<>(
                Comparator.comparingDouble(OpenEntry::f).thenComparing(entry -> entry.node().getId())
        );
        Map<String, Double> openBestG = new HashMap<>();
        Set<String> closedList = new LinkedHashSet<>();

        start.setG(0.0);
        start.setParent(null);
        openList.add(new OpenEntry(start, start.getG(), start.getF()));
        openBestG.put(start.getId(), start.getG());

        int iteration = 0;

        while (!openList.isEmpty()) {
            OpenEntry entry = openList.poll();
            Node current = entry.node();

            Double expectedBestG = openBestG.get(current.getId());
            if (expectedBestG == null || entry.g() > expectedBestG) {
                continue;
            }

            openBestG.remove(current.getId());
            iteration++;

            if (current.equals(goal)) {
                printTrace(iteration, current, openBestG, closedList);
                return new SearchResult(reconstructPath(current), current.getG(), closedList.size(), true);
            }

            closedList.add(current.getId());

            for (Edge edge : graph.getEdges(current)) {
                Node neighbor = edge.getDestination();
                if (closedList.contains(neighbor.getId())) {
                    continue;
                }

                double gNew = current.getG() + edge.getWeight();
                Double openKnownG = openBestG.get(neighbor.getId());

                if (openKnownG == null || gNew < openKnownG) {
                    neighbor.setParent(current);
                    neighbor.setG(gNew);
                    openBestG.put(neighbor.getId(), gNew);
                    openList.add(new OpenEntry(neighbor, neighbor.getG(), neighbor.getF()));
                }
            }

            printTrace(iteration, current, openBestG, closedList);
        }

        return new SearchResult(List.of(), Double.POSITIVE_INFINITY, closedList.size(), false);
    }

    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;
        while (current != null) {
            path.add(0, current);
            current = current.getParent();
        }
        return path;
    }

    private void printTrace(int iteration, Node current, Map<String, Double> openBestG, Set<String> closedList) {
        List<Node> openNodes = graph.getNodes().stream()
                .filter(node -> openBestG.containsKey(node.getId()))
                .sorted(Comparator.comparingDouble(Node::getF).thenComparing(Node::getId))
                .collect(Collectors.toList());

        String openAsText = openNodes.stream()
                .map(node -> node.getId() + "(f=" + formatValue(node.getF()) + ")")
                .collect(Collectors.joining(", "));

        String closedAsText = String.join(", ", closedList);

        System.out.println("=== Iteration " + iteration + " ===");
        System.out.println("Noeud courant : " + current.getId()
                + " | g=" + formatValue(current.getG())
                + " h=" + formatValue(current.getH())
                + " f=" + formatValue(current.getF()));
        System.out.println("Open list  : [" + openAsText + "]");
        System.out.println("Closed list: [" + closedAsText + "]");
        System.out.println();
    }

    private String formatValue(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }
        return String.format("%.2f", value);
    }

    public static class SearchResult {
        private final List<Node> path;
        private final double totalCost;
        private final int developedNodes;
        private final boolean found;

        public SearchResult(List<Node> path, double totalCost, int developedNodes, boolean found) {
            this.path = path;
            this.totalCost = totalCost;
            this.developedNodes = developedNodes;
            this.found = found;
        }

        public List<Node> getPath() {
            return path;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public int getDevelopedNodes() {
            return developedNodes;
        }

        public boolean isFound() {
            return found;
        }
    }

    private record OpenEntry(Node node, double g, double f) {
    }
}
