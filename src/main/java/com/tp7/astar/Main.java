package com.tp7.astar;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();

        graph.addEdge("Tunis", "Sousse", 140);
        graph.addEdge("Tunis", "Kairouan", 160);
        graph.addEdge("Sousse", "Sfax", 130);
        graph.addEdge("Kairouan", "Sfax", 110);
        graph.addEdge("Kairouan", "Gafsa", 220);
        graph.addEdge("Sfax", "Gafsa", 170);
        graph.addEdge("Sfax", "Gabes", 140);
        graph.addEdge("Gabes", "Tozeur", 210);
        graph.addEdge("Gafsa", "Tozeur", 95);

        Map<String, Double> heuristicTable = new HashMap<>();
        heuristicTable.put("Tunis", 430.0);
        heuristicTable.put("Sousse", 300.0);
        heuristicTable.put("Kairouan", 250.0);
        heuristicTable.put("Sfax", 210.0);
        heuristicTable.put("Gafsa", 90.0);
        heuristicTable.put("Gabes", 180.0);
        heuristicTable.put("Tozeur", 0.0);

        AStarSearch search = new AStarSearch(
                graph,
                node -> heuristicTable.getOrDefault(node.getId(), 0.0)
        );

        AStarSearch.SearchResult result = search.findPath("Tunis", "Tozeur");
        printResult(result);
    }

    private static void printResult(AStarSearch.SearchResult result) {
        if (!result.isFound()) {
            System.out.println("Aucun chemin trouve vers Tozeur.");
            return;
        }

        String pathAsText = result.getPath().stream()
                .map(Node::getId)
                .collect(Collectors.joining(" -> "));

        System.out.println("Chemin optimal : " + pathAsText);
        System.out.println("Cout total     : " + formatValue(result.getTotalCost()));
        System.out.println("Noeuds developpes : " + result.getDevelopedNodes());
        System.out.println("Rayen Ben Hamed - Groupe 2");
    }

    private static String formatValue(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }
        return String.format("%.2f", value);
    }
}
