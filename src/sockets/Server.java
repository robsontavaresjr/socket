/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    public static void main(String[] args) throws Exception {
        System.out.println("The capitalization server is running.");
        int clientNumber = 0;
        ServerSocket serverSocket = new ServerSocket(25000);
        try {
            while (true) {
                new myThread(serverSocket.accept(), clientNumber++).start();
            }
        } finally {
            serverSocket.close();
        }
    }
    
    
    private static class myThread extends Thread {
        private Socket socket;
        private int clientNumber;

        public myThread(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client No " + clientNumber + " at " + socket);
        }

        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                
                // Send a welcome message to the client.
                System.out.println("Hi, you are client #" + clientNumber + ".");
                System.out.print("Enter text: ");

                while (true) {
                    String input = br.readLine();
                    String returnMessage;
                    if (input == null || input.equals(".")) {
                        break;
                    }else{
                        if(input.equals("STOP")){
                            returnMessage = "STOPPED" + "\n";

                        }else{
                            returnMessage = input.toUpperCase() + "\n";
                        }
                    }
                    //Sending the response back to the client.
                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write(returnMessage);
                    System.out.println("Message sent to the client "+clientNumber+" is: "+returnMessage);
                    bw.flush();
                }
            } catch (IOException e) {
                System.out.println("Error handling client No " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client No " + clientNumber + " closed");
            }
        }
    }
    
}
