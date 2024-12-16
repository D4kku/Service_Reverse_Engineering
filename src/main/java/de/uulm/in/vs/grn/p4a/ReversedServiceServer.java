package de.uulm.in.vs.grn.p4a;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReversedServiceServer {
    public static void main(String[] args){
        int port = 3211;
        try (ServerSocket server = new ServerSocket(port)) { //Server hat socket auf dem port "port"

            //durch den Executor kann man den als thread implementierten service gleichzeitig
            // mehrere user bedienen. Ist zwar nicht unbedingt gefragt worden, war aber zum Testen angenehmer und 3 zeilen code
            try (ExecutorService threadPool = Executors.newCachedThreadPool()) {

                //this is the Server loop which only continues when there is a new connection ergo a new user who wants this weird service
                while (true) {
                    Socket s = server.accept();
                    threadPool.execute(new ReversedServiceHandler(s)); //runs the service for the new connection in a separate thread
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
