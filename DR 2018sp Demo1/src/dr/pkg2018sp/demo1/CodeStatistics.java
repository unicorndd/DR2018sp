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
    private HashMap<String, Integer> opr = new HashMap<String, Integer>();
    private HashMap<String, Integer> opd = new HashMap<String, Integer>();
    private String REG  = "\\w*[A-Za-z_]\\w*";                                  // REG, token, parameters
    private String REG1 = "^\\s*//";                                            // REG, lines begin with "//"
    private String REG2 = "^\\s*/\\*";                                          // REG, lines begin with "/*"
    private String REG3 = "\\*/\\s*$";                                          // REG, lines end with "*/"
    private String REG4 = "/\\*";                                               // REG, comments begin with "/*"
    private String REG5 = "\\*/";                                               // REG, comments end with "*/"
    private String REG6 = "[A-Za-z_]\\w*\\s*\\(";                               // REG, operators
    private String REG7 = "\\((\\s*\\w*[A-Za-z_]\\w*\\s*\\,)*(\\s*\\w*[A-Za-z_]\\w*\\s*){0,1}\\)";
                                                                                // REG, operands
    
    public CodeStatistics(String p){
        path = p;
    }
    
    public void load(){
        File inputFile = new File(path);
        Pattern p  = Pattern.compile(REG);
        Pattern p1 = Pattern.compile(REG1);
        Pattern p2 = Pattern.compile(REG2);
        Pattern p3 = Pattern.compile(REG3);
        Pattern p4 = Pattern.compile(REG4);
        Pattern p5 = Pattern.compile(REG5);
        boolean comment = false;
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
                
                Matcher m1 = p1.matcher(line);
                Matcher m2 = p2.matcher(line);
                Matcher m3 = p3.matcher(line);
                Matcher m4 = p4.matcher(line);
                Matcher m5 = p5.matcher(line);
                if(comment){
                    if(m3.find())
                        comment = false;
                    else if(m5.find()){
                        comment = false;
                        ++lines;
                        String s = line.substring(m5.end());
                        matchOperators(s);
                    }
                }
                else{
                    if(!m1.find()){
                        if(m2.find())
                            comment = true;
                        else if(m4.find()){
                            comment = true;
                            ++lines;
                            String s = line.substring(0, m4.start()-1);
                            matchOperators(s);
                        }
                        else{
                            ++lines;
                            matchOperators(line);
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println("CodeStatistics loaded !");
    }
    
    private void matchOperators(String line){
        Pattern p6 = Pattern.compile(REG6);
        Matcher m6 = p6.matcher(line);
        Pattern p7 = Pattern.compile(REG7);
        Matcher m7 = p7.matcher(line);
        while(m6.find()){
            String operator = m6.toMatchResult().group();
            int i = 0;
            while(operator.charAt(i)!=' ' && operator.charAt(i)!='(')
                ++i;
            operator = operator.substring(0, i);
            if(!operator.equals("if") && !operator.equals("while") && !operator.equals("for")){
                //System.out.println(operator);
                if(opr.containsKey(operator))
                    opr.put(operator, opr.get(operator)+1);
                else
                    opr.put(operator, 1);
            }
            
        }
        while(m7.find()){
            String operands = m7.toMatchResult().group();
            Pattern p71 = Pattern.compile(REG);
            Matcher m71 = p71.matcher(operands);
            while(m71.find()){
                String operand = m71.toMatchResult().group();
                if(opd.containsKey(operand))
                    opd.put(operand, opd.get(operand)+1);
                else
                    opd.put(operand, 1);
                //System.out.println(operand);
            }
        }
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
    
    public double volume(){
//        for(String key : opr.keySet()){
//            System.out.println(key + " :  " + opr.get(key));
//        }
//        System.out.println("------------------");
//        for(String key : opd.keySet()){
//            System.out.println(key + " :  " + opd.get(key));
//        }
        int ProgramVocabulary = opr.size() + opd.size();
        int ProgramLength = 0;
        for(int val : opr.values())
            ProgramLength += val;
        for(int val : opd.values())
            ProgramLength += val;
        
        return ProgramLength * Math.log(ProgramVocabulary) / Math.log(2);
    }
}
