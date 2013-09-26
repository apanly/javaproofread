/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.File;

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
    Analyzer ikAnalyzer = new IKAnalyzer();
    Directory indexDir = null;

    public void createIndex() {
        Document doc1 = new Document();
        doc1.add(new Field("desc", "天气预报", Store.YES, Index.ANALYZED));
        doc1.add(new Field("detail", "{'今天':'onedayweather','现在':'liveweather','type':'api'}", Store.YES, Index.ANALYZED));
        this.addIndex(doc1);
    }
    
    private void addIndex(Document doc){
        try {
            IndexWriterConfig config = new IndexWriterConfig(VERSION, ikAnalyzer);
            config.setOpenMode(OpenMode.CREATE);
            IndexWriter indexWriter = null;
            //使用IK中文分词器建立索引  
            indexWriter = new IndexWriter(FSDirectory.open(INDEXDIR), config);
            indexWriter.addDocument(doc);
            indexWriter.commit();
            System.out.println(indexWriter.numDocs());
            indexWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(){
         try {
            IndexWriterConfig config = new IndexWriterConfig(VERSION, ikAnalyzer);
            config.setOpenMode(OpenMode.CREATE);
            IndexWriter indexWriter = null;
            //使用IK中文分词器建立索引  
            indexWriter = new IndexWriter(FSDirectory.open(INDEXDIR), config);
            Document doc1 = new Document();
            doc1.add(new Field("id", "1", Store.YES, Index.ANALYZED));
            doc1.add(new Field("name", "我是中国人", Store.YES, Index.ANALYZED));
            doc1.add(new Field("content", "我是中国人", Store.YES, Index.ANALYZED));
            indexWriter.updateDocument(new Term("id", "1"), doc1);
            indexWriter.commit();
            indexWriter.close();
         }catch(Exception e){
             e.printStackTrace();
         }
    }
    private void delete(){
        try {
            IndexWriterConfig config = new IndexWriterConfig(VERSION, ikAnalyzer);
            config.setOpenMode(OpenMode.CREATE);
            IndexWriter indexWriter = null;
            //使用IK中文分词器建立索引  
            indexWriter = new IndexWriter(FSDirectory.open(INDEXDIR), config);
            indexWriter.deleteDocuments(new Term("id", "1"));
            indexWriter.deleteDocuments(new Term("id", "2"));
            indexWriter.commit();
            indexWriter.close();
         }catch(Exception e){
             e.printStackTrace();
         }
    }
    
    public static void main(String[] args) {
        FileOperate lucene = new FileOperate();
        lucene.createIndex();
    }
}
