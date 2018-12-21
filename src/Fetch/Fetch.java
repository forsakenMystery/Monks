/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fetch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Hamed Khashehchi
 */
public class Fetch {

    private String train;
    private String test;

    private ArrayList<Monks> trainSet;
    private ArrayList<Monks> testSet;

    private String[] featuresNames = {"class", "a1", "a2", "a3", "a4", "a5", "a6", "ID"};

    private int[] classValues = {0, 1};
    private int[] a1Values = {1, 2, 3};
    private int[] a2Values = {1, 2, 3};
    private int[] a3Values = {1, 2};
    private int[] a4Values = {1, 2, 3};
    private int[] a5Values = {1, 2, 3, 4};
    private int[] a6Values = {1, 2};

    private int[][] features = {classValues, a1Values, a2Values, a3Values, a4Values, a5Values, a6Values};

    public Fetch() {
    }

    public int[][] getFeatures() {
        return features;
    }

    public Fetch(String train, String test) {
        this.train = train;
        this.test = test;
        try {
            this.trainSet = (ArrayList<Monks>) readFromFile(train);
//            System.out.println("trainSet = " + trainSet);
            this.testSet = (ArrayList<Monks>) readFromFile(test);
//            System.out.println("testSet = " + testSet);
        } catch (IOException ex) {
            Logger.getLogger(Fetch.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }

    public double Entropy(ArrayList<Pair<Integer, Integer>> data) {
//        System.out.println("");
//        System.out.println("");
//        System.out.println("==========================");
//        System.out.println("Calculating entropy:");
//        System.out.println("data = " + data);
        double entropy = 0;
        int sum = 0;
        for (Pair<Integer, Integer> a : data) {
            sum += a.getValue();
//            System.out.println("sum = " + sum);
        }
//        System.out.println("data = " + data);
        for (Pair<Integer, Integer> a : data) {
//            System.out.println("a = " + a);
//            System.out.println(a.getValue());
//            System.out.println(a.getKey());

//            System.out.println("probablity = " + probablity);
            if (sum != 0) {
                double probablity = ((double) ((double) a.getValue() / (double) sum));
                if (probablity != 0) {
                    entropy += -(probablity) * log2(probablity);
                }
            } else {
                //do nothing
            }
        }
//        System.out.println("entropy = " + entropy);
        return entropy;
    }

    public List<Monks> getTrainMonks(double a) {
        assert a <= 1 && a > 0 : "it is not making sense to me";
        if (a == 1) {
            return new ArrayList<>(this.trainSet);
        } else {
            int step = ((int) Math.floor(a * this.trainSet.size()));
            return new ArrayList<>(this.trainSet.subList(0, step));
        }
    }

    public List<Monks> getTrainMonks() {
        return this.getTrainMonks(1);
    }

    public List<Monks> getTestMonks() {
        return new ArrayList<>(this.testSet);
    }

    public List<ArrayList<Pair<Integer, Integer>>> featureCounting(ArrayList<Monks> monks) {
//        System.out.println("monks = " + monks);
        ArrayList<ArrayList<Pair<Integer, Integer>>> map = new ArrayList<>();
        int i = 0;
        for (String s : featuresNames) {
            if (!s.equals("ID")) {
                // pair az pair ha niaze!!
                ArrayList<Pair<Integer, Integer>> an = new ArrayList<>();
                for (int m : features[i]) {
                    an.add(new Pair<>(m, 0));
                }
                map.add(an);
                i++;
            }
        }
//        System.out.println("map = " + map);
        for (Monks m : monks) {
            for (i = 0; i < m.getFeatures().size() - 1; i++) {
                ArrayList<Pair<Integer, Integer>> get = map.get(i);
                for (int k = 0; k < get.size(); k++) {
                    if (get.get(k).getKey() == Integer.parseInt(m.getFeatures().get(i))) {
                        get.set(k, new Pair<>(get.get(k).getKey(), get.get(k).getValue() + 1));
                    }
                }
            }
//            System.out.println("map = " + map);
//            System.out.println("===============\n================\n");
        }
        return map;
    }

    public List<ArrayList<ArrayList<Pair<Integer, Integer>>>> featureByClassCounting(ArrayList<Monks> monks) {
//        System.out.println("monks = " + monks);
        ArrayList<ArrayList<ArrayList<Pair<Integer, Integer>>>> map = new ArrayList<>();
        int y = 0;
        for (int a : classValues) {
            map.add(new ArrayList<>());
            int u = 0;
            ArrayList<ArrayList<Pair<Integer, Integer>>> get = map.get(y);
//            System.out.println(Integer.toHexString(System.identityHashCode(get)));

            for (String s : featuresNames) {
//                int lll = 0;

                if (!s.equals("ID") && !s.equals("class")) {
                    // pair az pair ha niaze!!
                    ArrayList<Pair<Integer, Integer>> an = new ArrayList<>();
//                    System.out.println(Integer.toHexString(System.identityHashCode(an)));
                    for (int m : features[u]) {
//                        System.out.println("m = " + m);
                        an.add(new Pair<>(m, 0));
//                        Pair<Integer, Integer> get1 = an.get(lll);
//                        System.out.println(Integer.toHexString(System.identityHashCode(get1)));
//                        lll++;
                    }
                    get.add(an);
                }
                u++;
            }
            map.set(y, get);
            y++;
        }
//        System.out.println("map = " + map);
        for (Monks m : monks) {
//            System.out.println("=====================\n==============\n");
            ArrayList<String> features = m.getFeatures();
            int i = 0;
            int kk = 0;
            int kl = 0;
//            System.out.println("m = " + m);
            for (String s : featuresNames) {
//                System.out.println("*******/***************\n");
//                System.out.println("s = " + s);
//                System.out.println("i = " + i);
//                System.out.println("kk = " + kk);
                if (!s.equals("ID") && !s.equals("class")) {
                    ArrayList<ArrayList<Pair<Integer, Integer>>> get = map.get(Integer.parseInt(features.get(0)));
                    //get is extracted class
//                    System.out.println("class = " + features.get(0));
//                    System.out.println("get = " + get);
                    ArrayList<Pair<Integer, Integer>> get1 = get.get(i);
//                    System.out.println("get1 = " + get1);
                    kl = 0;
                    for (Pair<Integer, Integer> ss : get1) {
//                        System.out.println("ss = " + ss + ", key = " + ss.getKey());
//                        System.out.println("kl = " + kl);
                        if (ss.getKey() == Integer.parseInt(features.get(kk))) {
                            get1.set(kl, new Pair<>(ss.getKey(), ss.getValue() + 1));
                            break;
                        }
                        kl++;
                    }
//                    get.set(kk, get1);
                    map.set(Integer.parseInt(features.get(0)), get);
//                    System.out.println("get1 = " + get1);
//                    System.out.println("get = " + get);
//                    System.out.println("map = " + map);
                    i++;
                }
                kk++;
            }
        }
        return map;
    }

    private double value(ArrayList<Pair<Integer, Integer>> S) {
        double end = 0;
        return end;
    }

    private List<Monks> readFromFile(String s) throws FileNotFoundException, IOException {
        ArrayList<Monks> monks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
//            System.out.println("========================");
            String line = "";
//            int i = 0;
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
//                System.out.println("i = " + ++i);
                String[] split = line.split(" ");
                split = Arrays.copyOfRange(split, 1, split.length);
                monks.add(new Monks(split));
            }
//            System.out.println("split = " + Arrays.toString(split));
//            System.out.println("monks = " + monks);
        }
        return monks;
    }

    /**
     * @return the train
     */
    public String getTrain() {
        return train;
    }

    /**
     * @return the test
     */
    public String getTest() {
        return test;
    }

    /**
     * @return the featuresNames
     */
    public String[] getFeaturesNames() {
        return featuresNames;
    }

    /**
     * @return the classValues
     */
    public int[] getClassValues() {
        return classValues;
    }

    /**
     * @return the a1Values
     */
    public int[] getA1Values() {
        return a1Values;
    }

    /**
     * @return the a2Values
     */
    public int[] getA2Values() {
        return a2Values;
    }

    /**
     * @return the a3Values
     */
    public int[] getA3Values() {
        return a3Values;
    }

    /**
     * @return the a4Values
     */
    public int[] getA4Values() {
        return a4Values;
    }

    /**
     * @return the a5Values
     */
    public int[] getA5Values() {
        return a5Values;
    }

    /**
     * @return the a6Values
     */
    public int[] getA6Values() {
        return a6Values;
    }

    public static void main(String[] args) {
        Fetch f = new Fetch("C:\\Users\\Hamed Khashehchi\\Downloads\\Telegram Desktop\\ID3_1397\\ID3_1397\\monks\\monks-1.train", "C:\\Users\\Hamed Khashehchi\\Downloads\\Telegram Desktop\\ID3_1397\\ID3_1397\\monks\\monks-1.test");
        System.out.println(" = " + f.getTestMonks().size());
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
    }
}
