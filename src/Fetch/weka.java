/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fetch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Hamed Khashehchi
 */
public class weka {

    static void changeWeka() throws FileNotFoundException, IOException {
        String train = "C:\\Users\\Hamed Khashehchi\\Downloads\\Telegram Desktop\\ID3_1397\\ID3_1397\\monks\\monks-1.test";
        String trainWeka = "C:\\Users\\Hamed Khashehchi\\Downloads\\Telegram Desktop\\ID3_1397\\ID3_1397\\monks\\monks-2.arff";
        try (BufferedReader br = new BufferedReader(new FileReader(train))) {
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
                split = Arrays.copyOfRange(split, 1, split.length - 1);
                System.out.println("split = " + split);
                String fileContent = "";
                int i = 0;
                for (String s : split) {
                    if (split.length - 1 != i++) {
                        fileContent += s + ",";
                    } else {
                        fileContent += s + "\n";
                    }
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(trainWeka, true));
                writer.write(fileContent);
                writer.close();
            }
//            System.out.println("split = " + Arrays.toString(split));
//            System.out.println("monks = " + monks);
        }
    }

    public static void main(String[] args) throws IOException {
        weka.changeWeka();
    }
}
