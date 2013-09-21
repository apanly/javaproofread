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

public class JServer implements Runnable {

    ServerSocket ss;

    public JServer() throws Exception {
        ss = new ServerSocket(10000);
        new Thread(this).start();
    }

    @Override
    public void run() {
        int i = 0;
        System.out.println("server startup.");
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

    public Handler(Socket s, int id) {
        this.s = s;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
            String pythonword = in.readLine();
            String[] suggestions =SpellCorrecter.suggest(pythonword, 1);
            if(suggestions.length>0){
                out.write(suggestions[0]);
            }else{
                out.write("correct");
            }
            out.flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}