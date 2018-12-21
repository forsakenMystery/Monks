/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Hamed Khashehchi
 */
public class Tree {

    private Fetch f;
    private static Node root = null;
    private ArrayList<Monks> monks;

    public int getTreeHeight(Node start) {
        int height = 0;
        if (start == null) {
            return height;
        }
        if (start.getChildren() == null) {
            return 1;
        }
        for (Node child : start.getChildren()) {
            height = Math.max(height, getTreeHeight(child));
        }
        return height + 1;
    }

    public static Node getRoot() {
        return root;
    }

    public static void setRoot(Node root) {
        Tree.root = root;
    }

    public Tree(ArrayList<Monks> monks) {
        this.f = null;
        tree(monks);
    }

    public Tree(Fetch f, double d) {
        this.f = f;
        tree(f.getTrainMonks(d));
    }

    private void buildTree(Node start) {
        System.out.println("start = " + start);
        if (start.isLeaf()) {
            System.out.println("done");
            return;
        }
        System.out.println(f.featureCounting(start.getMonks()));
        double entropyS = f.Entropy(f.featureCounting(start.getMonks()).get(0));
        System.out.println("entropy = " + entropyS);
        ArrayList<Pair<Integer, Integer>> get = f.featureCounting(start.getMonks()).get(0);
//        System.out.println("get = " + get);
        int nS = 0;
        for (Pair<Integer, Integer> p : get) {
            nS += p.getValue();
        }
        System.out.println("nS = " + nS);
        System.out.println("feature by class");
        System.out.println(f.featureByClassCounting(start.getMonks()));
        List<ArrayList<ArrayList<Pair<Integer, Integer>>>> featureByClassCounting = f.featureByClassCounting(start.getMonks());
//        for (int i = 0; i < f.getFeaturesNames().length; i++) {
//            if(!f.getFeaturesNames()[i].equals("ID"))
//        }
        ArrayList<Pair<Integer, Double>> gain = new ArrayList<>();
        System.out.println("featureByClassCounting.size() = " + featureByClassCounting.size());
        ArrayList<ArrayList<Integer>> count_them_all = new ArrayList<>();
        for (int k = 0; k < featureByClassCounting.get(0).size(); k++) {
            ArrayList<Integer> counting = new ArrayList<>();
            for (int i = 0; i < f.getFeatures()[k + 1].length; i++) {
                int s = 0;
                for (int j = 0; j < f.getClassValues().length; j++) {
                    Pair<Integer, Integer> get1 = featureByClassCounting.get(j).get(k).get(i);
                    s += get1.getValue();
                }
                counting.add(s);
            }
//            System.out.println("counting = " + counting);
            count_them_all.add(counting);
        }
        System.out.println("count_them_all = " + count_them_all);
        ArrayList<Pair<Integer, ArrayList<ArrayList<Pair<Integer, Integer>>>>> rootOfAllEvil = new ArrayList<>();
//        for (int i = 0; i < featureByClassCounting.size(); i++) {
//            ArrayList<ArrayList<Pair<Integer, Integer>>> classes = featureByClassCounting.get(i);
//            for (int j = 0; j < classes.size(); j++) {
//                ArrayList<Pair<Integer, Integer>> features = classes.get(j);
//                for (int k = 0; k < features.size(); k++) {
//                    Pair<Integer, Integer> feature = features.get(k);
//                    System.out.println("feature = " + feature);
//                }
//                System.out.println("===============");
//            }
//            System.out.println("****************");
//        }
        ArrayList<ArrayList<Pair<Integer, Integer>>> get0 = featureByClassCounting.get(0);
        ArrayList<ArrayList<Pair<Integer, Integer>>> get1 = featureByClassCounting.get(1);
        ArrayList<Pair<Integer, ArrayList<ArrayList<Pair<Integer, Integer>>>>> ans = new ArrayList<>();
        for (int i = 0; i < get0.size(); i++) {
            ArrayList<Pair<Integer, Integer>> featurePositive = get1.get(i);
            ArrayList<Pair<Integer, Integer>> featureNegative = get0.get(i);
//            System.out.println("featureNegative = " + featureNegative);
//            System.out.println("featurePositive = " + featurePositive);
            ArrayList<ArrayList<Pair<Integer, Integer>>> theirs = new ArrayList<>();
            for (int j = 0; j < f.getFeatures()[i + 1].length; j++) {
                ArrayList<Pair<Integer, Integer>> ours = new ArrayList<>();
                Pair<Integer, Integer> yours = new Pair<>(0, featureNegative.get(j).getValue());
                Pair<Integer, Integer> mine = new Pair<>(1, featurePositive.get(j).getValue());
                ours.add(yours);
                ours.add(mine);
//                System.out.println("ours = " + ours);
                theirs.add(ours);
            }
//            System.out.println("theirs = " + theirs);
            ans.add(new Pair<>(i, theirs));
        }
//        System.out.println("ans = " + ans);
        ArrayList<Pair<Integer, ArrayList<Double>>> entropies = new ArrayList<>();
        for (int i = 0; i < ans.size(); i++) {
            Pair<Integer, ArrayList<ArrayList<Pair<Integer, Integer>>>> get2 = ans.get(i);
            int featureId = get2.getKey() + 1;
            ArrayList<ArrayList<Pair<Integer, Integer>>> value = get2.getValue();
//            System.out.println("value = " + value);
            ArrayList<Double> enter = new ArrayList<>();
            for (int j = 0; j < value.size(); j++) {
                ArrayList<Pair<Integer, Integer>> get3 = value.get(j);
                double Entropy = f.Entropy(get3);
                enter.add(Entropy);
            }
            entropies.add(new Pair<>(featureId, enter));
        }
//        System.out.println("entropies = " + entropies);
        System.out.println(count_them_all.size());
        System.out.println(entropies.size());
        for (int i = 0; i < entropies.size(); i++) {
//            System.out.println("*******************");
//            System.out.println("i = " + i);
            Pair<Integer, ArrayList<Double>> get2 = entropies.get(i);
//            System.out.println("feature and entropies = " + get2);
            int featureID = get2.getKey();
//            System.out.println("featureID = " + featureID);
            ArrayList<Double> value = get2.getValue();
//            System.out.println("entropy of S = " + entropyS);
//            System.out.println("entropies = " + value);
//            System.out.println("these coef = " + count_them_all.get(i));
//            System.out.println("nS = " + nS);
            double an = entropyS;
//            System.out.println("====================");
            for (int j = 0; j < value.size(); j++) {
//                System.out.println("j = " + j);
                Double get3 = value.get(j);
//                System.out.println("get3 = " + get3);
                Integer get4 = count_them_all.get(i).get(j);
//                System.out.println("get4 = " + get4);
//                System.out.println("an = " + an);
                an -= ((double) value.get(j) * count_them_all.get(i).get(j) / nS);
            }
//            System.out.println("an = " + an);
            gain.add(new Pair<>(featureID, an));
        }
//        System.out.println("gain = " + gain);
        Collections.sort(gain, (t, t1) -> {
            if (t.getValue() > t1.getValue()) {
                return -1;
            } else if (t.getValue() < t1.getValue()) {
                return 1;
            }
            return 0;
        });
//        System.out.println("gain = " + gain);
        Pair<Integer, Double> get2 = gain.get(0);
        Integer feature_at_top = get2.getKey();
//        System.out.println("feature_at_top = " + feature_at_top);
        System.out.println(Arrays.toString(f.getFeatures()[feature_at_top]));
        for (int i = 0; i < f.getFeatures()[feature_at_top].length; i++) {
//            System.out.println("==============");
//            System.out.println("i = " + i);
            Node n = new Node();
            ArrayList<Monks> mon = new ArrayList<>();
            for (Monks m : start.getMonks()) {
//                System.out.println("m = " + m);
                if (m.getFeatures().get(feature_at_top).equals(f.getFeatures()[feature_at_top][i] + "")) {
                    mon.add(m);
                }
            }
//            System.out.println("mon = " + mon);
            n.setMonks(mon);
            double en = f.Entropy(f.featureCounting(n.getMonks()).get(0));
//            System.out.println("entropies = " + entropies);
//            System.out.println("gain = " + gain);
//            System.out.println("feature_at_top = " + feature_at_top);
//            System.out.println("start.getMonks() = " + start.getMonks());
//            System.out.println("n.getMonks() = " + n.getMonks());
            System.out.println(f.featureCounting(n.getMonks()));
//            System.out.println("en = " + en);
            if (en == 0) {
                n.setLeaf(true);
            }
            start.setFeature(feature_at_top);
            start.addChildren(n);
//            System.out.println("to the next branch:");
//            System.out.println("**************************");
//            System.out.println("************************");
            buildTree(n);
        }
    }

    private double test(ArrayList<Monks> test) {
//        System.out.println("test.size() = " + test.size());
//        System.out.println("");
//        System.out.println("");
//        System.out.println("");
//        System.out.println("start testing");
//        System.out.println("==========================");
//        System.out.println("**************************");
//        System.out.println("//////////////////////////");
//        System.out.println("--------------------------");
//        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\");
//        System.out.println("**************************");
//        System.out.println("===========================");
        double accuracy = 0;
        int correct = 0;
        int incorrect = 0;
        int KKKK = 0;
        for (Monks m : test) {
//            System.out.println("KKKK = " + KKKK++);
            Node start = root;
            int feature = start.getFeature();
//            System.out.println("=====================");
//            System.out.println("m = " + m);
            ArrayList<String> features = m.getFeatures();
            int real = Integer.parseInt(features.get(0));
            int search = Integer.parseInt(features.get(feature));
//            System.out.println("start = " + start);
//            System.out.println("*********************");
            while (!start.isLeaf()) {
                feature = start.getFeature();
                search = Integer.parseInt(features.get(feature));
//                System.out.println("start = " + start);
//                System.out.println("search = " + search);
//                System.out.println("feature = " + feature);
                int k;
//                System.out.println(Arrays.toString(f.getFeatures()[feature]));
                for (k = 0; k < f.getFeatures()[feature].length; k++) {
                    if (f.getFeatures()[feature][k] == search) {
                        break;
                    }
                }
                start = start.getChildren().get(k);
//                System.out.println("k = " + k);
//                System.out.println("start = " + start);
            }
//            System.out.println("******************");
//            System.out.println("start = " + start);
            if (start.getChildren().isEmpty()) {
                //since we are not sure we guess! or an educated guess
                int guess = 0;
                guess = (int) Math.floor(Math.random() * 2);
                int l = f.getFeatures()[0][guess];
                if (l == real) {
                    correct++;
                } else {
                    incorrect++;
                }
            } else {
                for (int i : start.getClasses()) {
//                    System.out.println("i = " + i);
//                    System.out.println("real = " + real);
                    if (i == real) {
                        correct++;
                    } else {
                        incorrect++;
                    }
                }
            }
//            System.out.println("incorrect = " + incorrect);
//            System.out.println("corrct = " + correct);
        }
        System.out.println("correct = " + correct);
        System.out.println("incorrect = " + incorrect);
        accuracy = (double) correct / (correct + incorrect);
        System.out.println("accuracy = " + accuracy);
        return accuracy;
    }

    public void traverse(Node start) {
        for (Node n : start.getChildren()) {
            System.out.println("n = " + n);
            traverse(n);
        }
    }

    private void tree(List<Monks> trainMonks) {
        root = new Node();
        this.monks = (ArrayList<Monks>) trainMonks;
        this.root.setMonks(monks);
        System.out.println("monks = " + monks);
        System.out.println("root = " + root);
        buildTree(root);
    }

    public static void main(String[] args) {
        Fetch f = new Fetch("C:\\Users\\Hamed Khashehchi\\Downloads\\Telegram Desktop\\ID3_1397\\ID3_1397\\monks\\monks-1.train", "C:\\Users\\Hamed Khashehchi\\Downloads\\Telegram Desktop\\ID3_1397\\ID3_1397\\monks\\monks-1.test");
//        System.out.println(f.getTrainMonks(0.1).size());
//        System.out.println(f.getTrainMonks(0.1));
//        System.out.println(f.getTrainMonks(1).size());
//        List<ArrayList<Pair<Integer, Integer>>> featureCounting = f.featureCounting((ArrayList<Monks>) f.getTrainMonks(0.1));
//        System.out.println("featureCounting = " + featureCounting);
//        double Entropy = f.Entropy(featureCounting.get(0));
//        System.out.println("Entropy = " + Entropy);
//        System.out.println("testing:");
//        List<ArrayList<ArrayList<Pair<Integer, Integer>>>> featureByClassCounting = f.featureByClassCounting((ArrayList<Monks>) f.getTrainMonks(0.1));
//        System.out.println("featureByClassCounting = " + featureByClassCounting);
        Tree t = new Tree(f, 0.1);
        System.out.println("root = " + root);
        t.traverse(root);
        t.test((ArrayList<Monks>) f.getTestMonks());
        int treeHeight = t.getTreeHeight(root);
        System.out.println("treeHeight = " + treeHeight);
    }

}
