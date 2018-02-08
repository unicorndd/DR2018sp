/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dr.pkg2018sp.demo1;

/**
 *
 * @author unico
 */
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.lang.Math;

public class CodeStatistics {
    private String path = "";
    private int lines = 0;
    private HashMap<String, Integer> hmp = new HashMap<String, Integer>();
    private String REG = "\\w*[A-Za-z_]\\w*";
    
    public CodeStatistics(String p){
        path = p;
    }
    
    public void load(){
        File inputFile = new File(path);
        Pattern p = Pattern.compile(REG);
        try{
            Scanner newLine = new Scanner(inputFile);
            while(newLine.hasNextLine()){
                String line = newLine.nextLine();
                Matcher m = p.matcher(line);
                while(m.find()){
                    String key = m.toMatchResult().group();
//                    System.out.println(key);
                    if(hmp.containsKey(key)){
                        hmp.put(key, hmp.get(key)+1);
                    }
                    else
                        hmp.put(key, 1);
                }
                
                ++lines;
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println("Loaded !");
    }
    
    public int getLines(){
        return lines;
    }
    
    public HashMap<String, Integer> getTokenCount(){
        return hmp;
    }
    
    public double entropy(){
        double total = 0;
        for(int val : hmp.values()){
            total += val;
        }
        double entropy = 0;
        for(int val : hmp.values()){
            entropy -= (val/total) * Math.log(val/total) / Math.log(2);
        }
        return entropy;
    }
}
