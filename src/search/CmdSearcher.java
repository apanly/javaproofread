/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

public class CmdSearcher {

    private File INDEXDIR = new File("./indexDir");

    public void search(String[] fields, String keyword, int startRow, int howManyRow) {
        Directory directory = null;
        IndexSearcher searcher = null;
        IndexReader reader = null;
        try {
            //使用 IKQueryParser类提供的parseMultiField方法构建多字段多条件查询  
            Query query = IKQueryParser.parseMultiField(fields, keyword);       
            System.out.println("查询条件:" + query.toString() + " : " + QueryParser.escape(keyword));
            directory = SimpleFSDirectory.open(INDEXDIR);
            reader = IndexReader.open(new SimpleFSDirectory(INDEXDIR));
            searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new IKSimilarity());//在索引器中使用IKSimilarity相似度评估器   
            if (searcher.maxDoc() <= 0) {
                return;
            }
            TopScoreDocCollector results = TopScoreDocCollector.create(searcher.maxDoc(), false);
            searcher.search(query, results);
            //分页取出指定的doc(开始条数, 取几条)  
            ScoreDoc[] docs = results.topDocs(startRow, howManyRow).scoreDocs;
            for (int i = 0; i < docs.length; i++) {
                Document doc = searcher.doc(docs[i].doc);
                String title = doc.get("desc");
                String content = doc.get("detail");
                System.out.println(title + " : " + content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   

    public static void main(String[] args) {
        String word="今天的天气预报";
        StopAnalyzerChinese stopword=new StopAnalyzerChinese(word);
        List<String> filterword=stopword.RunStopWord();
        String words=stopword.listToString(filterword, " ");
        System.out.println(words);
        CmdSearcher search = new CmdSearcher();
        search.search(new String[]{"desc"}, words, 0, 10);  //,1,10);可以翻页
    }
}
