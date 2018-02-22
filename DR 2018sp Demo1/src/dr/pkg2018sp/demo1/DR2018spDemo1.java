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
public class DR2018spDemo1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String path = "test.txt";
        CodeStatistics x = new CodeStatistics(path);
        x.load();
        System.out.println("Line of Code = " + x.getLines());
        HashMap<String, Integer> tokenCount = x.getTokenCount();
        //for(String key : tokenCount.keySet()){
        //    System.out.println(key + "\t\t" + tokenCount.get(key));
        //}
        System.out.println("Entropy = " + x.entropy());
        System.out.println("Volume = " + x.volume());
        
        GSRM g = new GSRM(path);
        g.load();
        //System.out.println(g.getLiteralLength());
        //System.out.println(g.getCodeLength());
        //System.out.println("Numeric Literals Frequency : " + g.getLiteralFre());
        System.out.println("Ext. of Alig. blocks : " + g.getAlignedBlockExt());
        //System.out.println(g.getLinesOfCode());
        g.showAlignedBlocks();
        g.showLiterals();
    }
    
}
