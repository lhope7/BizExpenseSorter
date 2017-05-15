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

/**
 *
 * @author LeesMac
 */
public class SortCSV {

    protected void sortAndSave(ArrayList sites, String source, String destination) {

        String newSaveFile = destination;
        String line = null;
        ArrayList<String> originalReport = new ArrayList<String>();
        ArrayList<String> newFile = new ArrayList<String>();
        try {
            //Reads specified file
            FileReader fileReader = new FileReader(source);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                originalReport.add(line);
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
        for (int x = 0; x < 6; x++) {
            newFile.add(originalReport.get(x));
        }
        for (int y = 0; y < sites.size(); y++) {
            for (int x = 6; x < originalReport.size();) {
                if (originalReport.get(x).contains(sites.get(y).toString())) {
                    newFile.add(originalReport.get(x));
                    originalReport.remove(x);
                    x++;
                } else if (x < originalReport.size()) {
                    //if(x==(originalReport.size()-1)){
                    //newFile.add("\n");}
                    x++;
                }
            }
        }
        //prints contents of the new file we wish to compile to the console,

        System.out.println("\nThis is the new report\n");
        for (int x = 0; x < newFile.size(); x++) {
            System.out.println(newFile.get(x));
        }
        try {
            File writeNewFile = new File(newSaveFile);
            FileWriter fileWriter = new FileWriter(writeNewFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for (int x = 0; x < newFile.size(); x++) {
                bw.write(newFile.get(x));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
