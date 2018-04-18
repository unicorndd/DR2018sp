/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dr.pkg2018sp.demo1;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.*;
/**
 *
 * @author unico
 */
public class TextualFeature {
    private String path = "";
    private HashMap<String, Integer> mp = new HashMap<String, Integer>();
    private ArrayList<String> keywords = new ArrayList<>(Arrays.asList("abstract",
            "continue", "for", "new", "switch", "assert", "default", "goto", "package", 
            "synchronized", "boolean", "do", "if", "private", "this", "break", "double", 
            "implements", "protected", "throw", "byte", "else", "import", "public", 
            "throws", "case", "enum", "instanceof", "return", "transient", "catch",
            "extends", "int", "short", "try", "char", "final", "interface", "static", 
            "void", "class", "finally", "long", "strictfp", "volatile", "const", 
            "float", "native", "super", "while"));
    
    public TextualFeature(String s){
        path = s;
    }
    
    public void prepare(){
        delMultiComments();
    }
    
    private void delMultiComments(){
        String reg1 = "/\\*[^\\n\\r]*$";
        String reg2 = "^[^\\n\\r]*\\*/";
        String reg3 = "/\\*[^\\n\\r]*\\*/";
        Pattern p1 = Pattern.compile(reg1);
        Pattern p2 = Pattern.compile(reg2);
        Pattern p3 = Pattern.compile(reg3);
        boolean hold = false;
        
        File infile = new File(path);
        try{
            Scanner reader = new Scanner(infile);
            FileWriter outfile = new FileWriter("file1.txt");
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                if(hold){
                    Matcher m2 = p2.matcher(line);
                    if(m2.find()){
                        line = m2.replaceFirst("");
                        hold = false;
                    }
                    else{
                        outfile.write("\r\n");
                        continue;
                    }
                }
                
                Matcher m3 = p3.matcher(line);
                if(m3.find())
                    line = m3.replaceAll("");
                
                Matcher m1 = p1.matcher(line);
                if(m1.find()){
                    line = m1.replaceAll("");
                    hold = true;
                }
                
                outfile.write(line);
                outfile.write("\r\n");
            }
            outfile.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void load(){
        File infile = new File(path);
        String reg_term = "\\b(\\$|[A-Za-z_])\\w*\\b";
        Pattern p_term = Pattern.compile(reg_term);
        try{
            Scanner lineReader = new Scanner(infile);
            while(lineReader.hasNextLine()){
                String line = lineReader.nextLine();
                Matcher m_term = p_term.matcher(line);
                while(m_term.find()){
                    String term = m_term.toMatchResult().group();
                    if(keywords.contains(term))
                        continue;
                    if(mp.containsKey(term)){
                        mp.put(term, mp.get(term) + 1);
                    }
                    else{
                        mp.put(term, 1);
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void printWords(){
        for(String key : mp.keySet()){
            System.out.println(key);
        }
    }
}
