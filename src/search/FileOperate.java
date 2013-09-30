/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class FileOperate {

    private File INDEXDIR = new File("./indexDir");
    private static Version VERSION = Version.LUCENE_35;
    private IndexWriter indexWriter = null;
    Analyzer ikAnalyzer = new IKAnalyzer();
    Directory indexDir = null;

    public FileOperate() {
        try {
            IndexWriterConfig config = new IndexWriterConfig(VERSION, ikAnalyzer);
            config.setOpenMode(OpenMode.CREATE);
            //使用IK中文分词器建立索引  
            indexWriter = new IndexWriter(FSDirectory.open(INDEXDIR), config);
            System.out.println("一共有" + indexWriter.maxDoc() + "索引");
            System.out.println("还剩" + indexWriter.numDocs() + "索引");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addIndex(Document doc) {
        try {
            indexWriter.addDocument(doc);
            indexWriter.optimize();
            System.out.println(indexWriter.numDocs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Term term, Document doc) {
        try {
//            indexWriter.updateDocument(new Term("id", "1"), doc1);
            indexWriter.updateDocument(term, doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * lucene.delete(new Term("id", "1"));
    * @param term 
    */
    public void delete(Term term) {
        try {
            indexWriter.deleteDocuments(term);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit(){
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 初始化命令
     * id   desc  cmd  type
     * @param args 
     */
    public static void main(String[] args) {
        FileOperate lucene = new FileOperate();
        
        try {
            String tmparray[] = new String[3];
            FileReader reader = new FileReader(FileOperate.class.getResource("/cmd.dic").getFile());
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            int number=1;
            Document doc = new Document();
            while ((str = br.readLine()) != null) {
                tmparray=str.split("\\s{1,}");
                doc.add(new Field("id",String.valueOf(number), Store.YES, Index.ANALYZED));
                doc.add(new Field("desc",tmparray[0], Store.YES, Index.ANALYZED));
                doc.add(new Field("cmd", tmparray[1], Store.YES, Index.ANALYZED));
                doc.add(new Field("type", tmparray[2], Store.YES, Index.ANALYZED));
                lucene.addIndex(doc);
                doc=new Document();
                number++;
            }
            br.close();
            reader.close();
            lucene.submit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
