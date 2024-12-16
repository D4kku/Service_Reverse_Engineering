package de.uulm.in.vs.grn.p4a;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

public class ReversedServiceHandler extends Thread{
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private HashMap<String,String> map;

    public ReversedServiceHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = this.socket.getOutputStream();
        this.in = this.socket.getInputStream();
        //as this is defined on a per instance/thread basis the data is neither persistent nor shared but since this
        // behavior never happens in the trace i figured it should be fine
        this.map = new HashMap<String,String>();//this acts as a kind of data base as it mirrors the protocol behavior
    }

    // I implemented the service in a way where this class only serves as Requesthandler so multiple people can access the same server
    public void run(){
        try (this.socket){
            startService();
            stopService();//only happens when startService finished so when the connections is finished
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Works as the main Programm loop waiting for messages and only stopping when the connection is supposed to end
    private void startService() throws IOException{
        boolean holdConnection = true;
        System.out.println("Starting Thread" + threadId());
        //Time for some shotty code again
        while(holdConnection){
            String[] request = getRequest();
            System.out.println(request[0]);
            if(Objects.equals(request[0],"PUT") && request.length == 3){
                //this works as the getRequest() array size only gets to be 3 long if it is in the "PUT key value" format
                //and since we don't care what the key or value are it can be as "malformed" as it wants since as far as we care we only need 2 arguments
                map.put(request[1], request[2]); //uuuuuugh i don't like this array thing
                writeResponse(true,"OK");
            }
            else if(Objects.equals(request[0],"GET") && request.length == 2){
                String value = map.get(request[1]);
                if(value!= null ) writeResponse(true,value);//since hash maps return null if the key is not specified
                else writeResponse(false,"Unknown Key!");
            }
            //In the given trace we have to analyise we only stop when the messages is exactly EXIT with no more values
            //but since there is also no Request with a malformed EXIT prompt with more values added to it we also dont get
            // a specified way we should act in that scenario so its undefined behavior and i just choose to make it
            // stop even if there is something after EXIT
            else if(Objects.equals(request[0],"EXIT")){
                writeResponse(true,"BYE!");
                holdConnection = false;
            }
            else{//happens if the requested commands aren't valid
                writeResponse(false,"Unknown Command!");
            }
        }
    }
    //just a helper Method to write the response to the output with response Code
    private void writeResponse(Boolean success,String msg) throws IOException{
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
        String respCode = (success ? "RES:" : "ERR:");//Decides the error given the success bool. I did it this way so i dont have to remeber the status Code
        outputStreamWriter.write(respCode +" "+ msg + "\n");
        outputStreamWriter.flush();
    }
    private String[] getRequest() throws IOException {
        //this reads the entire user response and converts the parts of the string which are seperated by a space in to an element of an array
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));//no raw bytes for me anymore :(
        return bufferedReader.readLine().split(" ");
    }
    private void stopService() throws IOException{
        System.out.println("Stopping Thread:" + threadId());
        out.close();
        in.close();
        socket.close();
    }
}
