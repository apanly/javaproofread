package boot;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

//import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SpellCorrecter {
	public static SpellChecker sp;
	static {
		try{
			//创建目录
			 File dict = new File("./myindex");
			 Directory directory = FSDirectory.open(dict);
			//实例化拼写检查器
			//NGramDistance ngram = new NGramDistance();
			sp = new SpellChecker(directory);
			//sp = new SpellChecker(directory,ngram);
			//第一次创建字典
                        //File dictionary = new File(SpellCorrecter.class.getResource("pipimovieUTF8.txt").getFile());
			//对词典进行索引
			//sp.indexDictionary(new PlainTextDictionary(dictionary));
		} catch (IOException e){ 
			e.printStackTrace();
		}
	}
	
	
	public static String[] suggest(String movie,int suggestionNumber) throws IOException{		 
		//获取建议的关键字
		String[] suggestions = sp.suggestSimilar(movie, suggestionNumber);
		return suggestions;
		
	}
	
	

//	public static void main(String[] args) throws IOException {
//
//		//“错误”的搜索
//		String movie = "天汽预报";
//		 
//
//		//建议个数
//		final int suggestionNumber = 2;
//		 
//
//		//获取建议的关键字
//		String[] suggestions = SpellCorrecter.suggest(movie, suggestionNumber);
//		 
//
//		//显示结果
//		System.out.println("Your Term:" + movie);
//		 #
//
//		for (String word : suggestions) {
//			System.out.println("Did you mean:" + word);
//		}
//		 
//
//	}

}
