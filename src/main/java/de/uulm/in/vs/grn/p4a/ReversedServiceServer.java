package de.uulm.in.vs.grn.p4a;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReversedServiceServer {
    public static void main(String[] args) throws IOException {
        int port = 3211;
        ServerSocket server = new ServerSocket(port);

        ExecutorService threadPool = Executors.newCachedThreadPool();

        while(true){
            Socket s = server.accept();
            threadPool.execute(new ReversedServiceHandler(s));
        }

    }
}
