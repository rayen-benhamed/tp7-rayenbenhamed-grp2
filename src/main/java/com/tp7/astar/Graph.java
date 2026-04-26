package com.tp7.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private final Map<String, Node> nodes = new LinkedHashMap<>();
    private final Map<Node, List<Edge>> adjacencyList = new LinkedHashMap<>();

    public Node addNode(String id) {
        return nodes.computeIfAbsent(id, key -> {
            Node node = new Node(key);
            adjacencyList.put(node, new ArrayList<>());
            return node;
        });
    }

    public void addEdge(String sourceId, String destinationId, double weight) {
        Node source = addNode(sourceId);
        Node destination = addNode(destinationId);
        adjacencyList.get(source).add(new Edge(source, destination, weight));
    }

    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public List<Node> getNodes() {
        return new ArrayList<>(nodes.values());
    }
}
