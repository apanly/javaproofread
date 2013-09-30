/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import search.CmdSearcher;
import search.StopAnalyzerChinese;
import org.apache.log4j.Logger;

public class JServer implements Runnable {

    ServerSocket ss;
    private static Logger logger=Logger.getLogger(JServer.class);

    public JServer() throws Exception {
        ss = new ServerSocket(10000);
        new Thread(this).start();
    }

    @Override
    public void run() {
        int i = 0;
        logger.debug("server startup.");
        while (true) {
            try {
                Socket s = ss.accept();
                // 每个客户端一个处理线程
                new Handler(s, i).start();
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        try {
            new JServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Handler extends Thread {

    Socket s;
    int id;
    private static Logger logger=Logger.getLogger(Handler.class);

    public Handler(Socket s, int id) {
        
        this.s = s;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            logger.debug(id);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
            String pythonword = in.readLine();
            String[] suggestions =SpellCorrecter.suggest(pythonword, 1);
            String word=pythonword;
            if(suggestions.length>0){
                word=suggestions[0];
            }    
            StopAnalyzerChinese stopword=new StopAnalyzerChinese(word);
            List<String> filterword=stopword.RunStopWord();
            String words=stopword.listToString(filterword, " ");
            logger.debug(words);
            CmdSearcher search = new CmdSearcher();
            String[] result=search.search(new String[]{"desc"}, words, 0, 1);  //,1,10);可以翻页
            logger.debug(result.length);
            if(result[0]!=null){
                out.write(result[0]);
            }else{
                out.write("n,default,http://www.baidu.com,browser");
            }
            out.flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}