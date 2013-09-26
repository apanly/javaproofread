/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class StopAnalyzerChinese {

    private String word;

    public StopAnalyzerChinese(String word) {
        this.word=word;
    }
    
    public List RunStopWord(){
        cutword();
        return stopWord();
    }

    private List stopWord(){
         List<String> list = new ArrayList<String>();
        try {
            Analyzer analyzer = new StopAnalyzer(Version.LUCENE_35, new File("stopword.dic"));
            TokenStream stream = analyzer.tokenStream("content", new StringReader(word));
            stream.addAttribute(CharTermAttribute.class);
            while (stream.incrementToken()) {
                CharTermAttribute charTermAttribute = stream.getAttribute(CharTermAttribute.class);
                //System.out.println(new String(charTermAttribute.toString()));
                list.add(new String(charTermAttribute.toString()));
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public String listToString(List list, String separator) {  
        StringBuilder sb = new StringBuilder();   
        for (int i = 0; i < list.size(); i++) {  
            sb.append(list.get(i));  
            if (i < list.size() - 1) {  
                sb.append(separator);  
            }  
        }  
        return sb.toString();  
    }  

    private  void cutword() {
        try {
            StringBuffer tmp=new StringBuffer("");
            Analyzer analyzer = new IKAnalyzer();
            TokenStream ts = analyzer.tokenStream("content", new StringReader(word));
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            while (ts.incrementToken()) {
                tmp.append(new String(term.buffer(), 0, term.length()));
                tmp.append(" ");
                //System.out.println(new String(term.buffer(), 0, term.length()));
            }
            word=tmp.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
}
