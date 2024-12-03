package de.uulm.in.vs.grn.p4a;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

public class ReversedService {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private HashMap map;

    public ReversedService(String host, int port) throws IOException {
        this.socket = new Socket(host,port);
        this.out = this.socket.getOutputStream();
        this.in = this.socket.getInputStream();
        this.map = new HashMap<String,String>();
    }


    public static void main(String[] args){
        System.out.println("test");
        ReversedService service = null;
        try {
            service = new ReversedService("localhost",0);
            service.startService(); //TODO: make it so you can reconnect to the Service
            service.stopService();//only gets exectued when the startService loop ends and we stop listening for a Connection
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Works as a Update loop waiting for messages
    private void startService() throws IOException{
        boolean holdConnection = true;
        while(holdConnection){
            String request = getRequest();


            if(Objects.equals(request,"EXIT")){
                writeResponse(true,"BYE!");
                holdConnection = false;
            }
        }
    }
    //just a helper Method to write the response to the output with response Code
    private void writeResponse(Boolean success,String msg) throws IOException{
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
        String respCode = (success ? "RES:" : "ERR:");//Decides the error given the success bool
        outputStreamWriter.write(respCode +" "+ msg + "\n");
        outputStreamWriter.flush();
    }
    private String getRequest(){

        return null;
    }
    private void stopService() throws IOException{
        out.close();
        in.close();
        socket.close();
    }
}
