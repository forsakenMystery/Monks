/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fetch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Hamed Khashehchi
 */
public class Monks {
    private ArrayList<String> features;

    public Monks(String... features) {
        this.features = new ArrayList<>(Arrays.asList(features));
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return features.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
