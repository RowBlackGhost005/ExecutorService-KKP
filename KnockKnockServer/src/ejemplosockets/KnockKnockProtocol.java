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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Luis Marin
 */
public class KnockKnockProtocol implements Runnable{
    private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUMJOKES = 5;

    private int state = WAITING;
    private int currentJoke = 0;

    private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
    private String[] answers = { "Turnip the heat, it's cold in here!",
                                   "I didn't know you could yodel!",
                                   "Bless you!",
                                   "Is there an owl in here?",
                                   "Is there an echo in here?" };
    
    
    //Atributos para el manejo del socket
    PrintWriter out = null;
    BufferedReader in = null;
    Socket socket = null;
    
    /**
     * Inicializa los flujos del socket para iniciar el KnockKnockProtocol
     * @param socket Socket a utilizar para la comunicación.
     * @throws IOException 
     */
    public KnockKnockProtocol(Socket socket) throws IOException{

        this.socket = socket;

        this.out = null;
        this.in = null;

        out = new PrintWriter(this.socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(
                this.socket.getInputStream()));
    }
    
    /**
     * Método run que mantiene la conexión y el estado del protocolo.
     */
    @Override
    public void run() {
        
        //Mismo código que estaba en KnockKnockServer
        //Cambiado para que el protocolo se encargue del estado de la conexión y
        //el envío y recepción de los mensajes
        try {
            
            String inputLine, outputLine;

            outputLine = processInput(null);
            out.println(outputLine);
            
            while ((inputLine = in.readLine()) != null) {
                outputLine = processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
            
            out.close();
            in.close();
            socket.close();
            socket.close();   
            
        } catch (IOException ex) {
            Logger.getLogger(KnockKnockProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  public String processInput(String theInput) {
    String theOutput = null;

    if (state == WAITING) {
      theOutput = "Knock! Knock!";
      state = SENTKNOCKKNOCK;
    } else if (state == SENTKNOCKKNOCK) {
      if (theInput.equalsIgnoreCase("Who's there?")) {
        theOutput = clues[currentJoke];
        state = SENTCLUE;
      } else {
        theOutput = "You're supposed to say \"Who's there?\"! " +
		"Try again. Knock! Knock!";
      }
    } else if (state == SENTCLUE) {
      if (theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
        theOutput = answers[currentJoke] + " Want another? (y/n)";
        state = ANOTHER;
      } else {
        theOutput = "You're supposed to say \"" + 
		clues[currentJoke] + 
		" who?\"" + 
		"! Try again. Knock! Knock!";
        state = SENTKNOCKKNOCK;
      }
    } else if (state == ANOTHER) {
      if (theInput.equalsIgnoreCase("y")) {
         theOutput = "Knock! Knock!";
         if (currentJoke == (NUMJOKES - 1))
           currentJoke = 0;
         else
           currentJoke++;
           state = SENTKNOCKKNOCK;
         } else {
           theOutput = "Bye.";
           state = WAITING;
         }
      }
      return theOutput;
    }    

}
