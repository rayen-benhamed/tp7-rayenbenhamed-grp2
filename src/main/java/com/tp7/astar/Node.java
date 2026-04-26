package com.tp7.astar;

import java.util.Objects;

public class Node {
    private final String id;
    private double g;
    private double h;
    private double f;
    private Node parent;

    public Node(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        resetScores();
    }

    public String getId() {
        return id;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
        this.f = this.g + this.h;
    }

    public double getH() {
        return h;
    }

    // h(n) is provided externally; the node only stores the injected value.
    public void setH(double h) {
        this.h = h;
        this.f = this.g + this.h;
    }

    public double getF() {
        return f;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void resetScores() {
        this.g = Double.POSITIVE_INFINITY;
        this.h = 0.0;
        this.f = Double.POSITIVE_INFINITY;
        this.parent = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node node)) {
            return false;
        }
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
