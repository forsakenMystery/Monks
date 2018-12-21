/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fetch;

import java.util.ArrayList;
import java.util.HashSet;
import javafx.util.Pair;

/**
 *
 * @author Hamed Khashehchi
 */
public class Node {

    private ArrayList<Node> children;
    private boolean leaf;
    private ArrayList<Monks> monks;
    private int feature;
    private HashSet<Integer> classes;

    public HashSet<Integer> getClasses() {
        return classes;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public int getFeature() {
        return feature;
    }

    public void setMonks(ArrayList<Monks> monks) {
        this.monks = monks;
        monks.forEach((m) -> {
            this.classes.add(Integer.parseInt(m.getFeatures().get(0)));
        });
    }

    public ArrayList<Monks> getMonks() {
        return monks;
    }

    public Node() {
        this.leaf = false;
        this.feature = -1;
        this.children = new ArrayList<>();
        this.classes = new HashSet<>();
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public void addChildren(Node l) {
        this.children.add(l);
    }

    @Override
    public String toString() {
        return "{" + this.monks + ", " + isLeaf() + ", " + feature + ", classes:" + this.classes + "}"; //To change body of generated methods, choose Tools | Templates.
    }

}
