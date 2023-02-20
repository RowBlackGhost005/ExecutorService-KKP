/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 
 * @author Luis Marin
 */
public class KnockKnockServer {


    public static void main(String[] args) {
        
        try {
            ServerSocket serverSocket = null;
            serverSocket = new ServerSocket(4444);
	    System.out.println("KnockKnockServer Ready!");
            Socket clientSocket = null;

            //Servicio de pool de hilos
            Executor service = Executors.newCachedThreadPool();
            
            //Acepta todas las conexiones asignando la conexión a un protocolo kockknock nuevo
            while (true){
                clientSocket = serverSocket.accept();
                
                service.execute(new KnockKnockProtocol(clientSocket));
                
                System.out.println("Attending a new client on port: " + clientSocket.getPort() + " . . .");
            }

        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
    }    
}



    

