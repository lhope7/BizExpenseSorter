/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biz.expense.sorter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author LeesMac
 */
public class SortAndSave {

    public void sas(ArrayList sites, String source, String destination) {
        String newSaveFile = destination;
        String line = null;
        ArrayList<String> splitLine = new ArrayList<String>();
        double[] totals = new double[sites.size()];

        try {
            //Reads specified file
            FileReader fileReader = new FileReader(source);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                Collections.addAll(splitLine, line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));
                splitLine.add("\n");
            }
            bufferedReader.close();
        } //Exception handling
        catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + source + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                    + source + "'");

        }
//        int index = 0;
//        for(Object o:splitLine){
//            System.out.println(index+" "+o);
//            index++;
//        }

        try {
            File writeNewFile = new File(newSaveFile);
            FileWriter fileWriter = new FileWriter(writeNewFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);

            for (int x = 0; x < 54; x++) {
                if (!"\n".equals(splitLine.get(x))) {
                    bw.write(splitLine.get(x) + ",");
                } else {
                    bw.write(splitLine.get(x));
                }
            }

            for (int y = 0; y < sites.size(); y++) {

                for (int x = 60; x < splitLine.size(); x += 9) {
                    //System.out.println("x is: " + splitLine.get(x));
                    //System.out.println("y is: " + sites.get(y));
                    //System.out.println(splitLine.get(x).toString().equals(sites.get(y).toString()));
                    if (splitLine.get(x).toString().equals(sites.get(y).toString())) {

                        bw.write(splitLine.get(x - 6) + ",");
                        bw.write(splitLine.get(x - 5) + ",");
                        bw.write(splitLine.get(x - 4) + ",");
                        bw.write(splitLine.get(x - 3) + ",");
                        bw.write(splitLine.get(x - 2) + ",");
                        bw.write(splitLine.get(x - 1) + ",");
                        bw.write(splitLine.get(x) + ",");
                        bw.write(splitLine.get(x + 1) + ",");
                        bw.write(splitLine.get(x + 2));
                        String d = splitLine.get(x - 4).replace("\"", "");
                        d = d.replace(",", "");
                        totals[y] += Double.parseDouble(d);
                    }
                }

            }
            int s = 0;
            for (double i : totals) {
                bw.write(sites.get(s) + ":," + i + "\n");
                s++;
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
