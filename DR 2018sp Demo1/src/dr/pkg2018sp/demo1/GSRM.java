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
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.util.HashMap;

public class GSRM {
    private String path = "";
    private String REG  = "[=><]";
    private String REG1 = "\\b\\d+\\b";
    private int literalLength = 0;
    private int codeLength = 0;
    private int alignedBlockExt = 0;
    private int codeLines = 0;
    private HashMap<Integer, Integer>line_size = new HashMap<Integer, Integer>();
    
    public GSRM(String s){
        path = s;
    }
    
    public void load(){
        Pattern p   = Pattern.compile(REG);
        Pattern p1  = Pattern.compile(REG1);
        File infile = new File(path);
        int hold = 1;
        int pos = -1;
        int begin = 0;
        
        try{
            Scanner newLine = new Scanner(infile);
            while(newLine.hasNextLine()){
                ++codeLines;
                String line = newLine.nextLine();
                codeLength += line.length();
                Matcher m1 = p1.matcher(line);
                while(m1.find()){
                    literalLength += m1.end() - m1.start();
                }
                Matcher m = p.matcher(line);
                if(m.find()){
                    if(m.start() == pos){
                        ++hold;
                        if(hold == 3)
                            begin = codeLines - 2;
                    }
                    else{
                        if(hold >= 3){
                            line_size.put(begin, hold);
                            alignedBlockExt += hold;
                        }
                        hold = 1;
                        pos = m.start();
                    }
                }
                else{
                    if(hold >= 3){
                            line_size.put(begin, hold);
                            alignedBlockExt += hold;
                        }
                    hold = 1;
                    pos = -1;
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println("GSRM loaded !");
    }
    
    public int getLiteralLength(){
        return literalLength;
    }
    
    public int getCodeLength(){
        return codeLength;
    }
    
    public int getAlignedBlockExt(){
        return alignedBlockExt;
    }
    
    public int getLinesOfCode(){
        return codeLines;
    }
    
    public void showAlignedBlocks(){
        for(int key : line_size.keySet()){
            System.out.println("Start line : " + key + "\t\tExtent : " + line_size.get(key));
        }
    }
    
    public double getLiteralFre(){
        return (double)literalLength / codeLength;
    }
}