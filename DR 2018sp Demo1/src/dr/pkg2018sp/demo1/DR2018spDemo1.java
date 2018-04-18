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

    public static void main(String[] args) {

        String path = "test.txt";
        CodeStatistics x = new CodeStatistics(path);
        x.load();
        System.out.println("Line of Code = " + x.getLines());
        HashMap<String, Integer> tokenCount = x.getTokenCount();
        System.out.println("Entropy = " + x.entropy());
        System.out.println("Volume = " + x.volume());
        
        GSRM g = new GSRM(path);
        g.load();
        System.out.println("Ext. of Alig. blocks : " + g.getAlignedBlockExt());
        g.showAlignedBlocks();
        g.showLiterals();
        
        System.out.println();
        TextualFeature tf = new TextualFeature("test1.txt");
        tf.prepare();
        tf.load();
        tf.printWords();
    }
    
}
