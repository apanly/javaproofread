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
import org.apache.log4j.Logger;

public class CmdSearcher {

    private static Logger logger=Logger.getLogger(CmdSearcher.class);
    private File INDEXDIR = new File("./indexDir");

    public String[] search(String[] fields, String keyword, int startRow, int howManyRow) {
        Directory directory = null;
        IndexSearcher searcher = null;
        IndexReader reader = null;
        String[] resutlstr=new String[howManyRow];
        try {
            //使用 IKQueryParser类提供的parseMultiField方法构建多字段多条件查询  
            Query query = IKQueryParser.parseMultiField(fields, keyword);       
            logger.debug("查询条件:" + query.toString() + " : " + QueryParser.escape(keyword));
            directory = SimpleFSDirectory.open(INDEXDIR);
            reader = IndexReader.open(new SimpleFSDirectory(INDEXDIR));
            searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new IKSimilarity());//在索引器中使用IKSimilarity相似度评估器   
            if (searcher.maxDoc() <= 0) {
                logger.debug("没有搜索到相关内容1");
                return resutlstr;
            }
            TopScoreDocCollector results = TopScoreDocCollector.create(searcher.maxDoc(), false);
            searcher.search(query, results);
            //分页取出指定的doc(开始条数, 取几条)  
            StringBuffer tmpstr=new StringBuffer();
            ScoreDoc[] docs = results.topDocs(startRow, howManyRow).scoreDocs;
            if (docs.length <= 0) {
                logger.debug("没有搜索到相关内容2");
                
                return resutlstr;
            }
            for (int i = 0; i < docs.length; i++) {
                Document doc = searcher.doc(docs[i].doc);
                String desc = doc.get("desc");
                String cmd = doc.get("cmd");
                String id=doc.get("id");
                String type=doc.get("type");
                logger.debug("id:"+id+",desc:"+desc+",cmd:"+cmd+",type:"+type);
                tmpstr.append(id+",");
                tmpstr.append(desc+",");
                tmpstr.append(cmd+",");
                tmpstr.append(type);
                resutlstr[i]=tmpstr.toString();
                logger.debug(tmpstr.toString());
                tmpstr=new StringBuffer();
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
        return resutlstr;
    }

   

//    public static void main(String[] args) {
//        String word="天气预报";
//        StopAnalyzerChinese stopword=new StopAnalyzerChinese(word);
//        List<String> filterword=stopword.RunStopWord();
//        String words=stopword.listToString(filterword, " ");
//        CmdSearcher search = new CmdSearcher();
//        String[] result=search.search(new String[]{"desc"}, words, 0, 1);  //,1,10);可以翻页
//        System.out.println(result[0]);
//    }
}
