package br.edu.ifsul.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Client is the one using the app, that plays the game
 * It holds informations about the client that is important to connect with the server
 * 
 * @author Huriel Ferreira Lopes
 */
public class Client extends Thread {

    private static boolean done = false;
    private final Socket connection;

    public Client(Socket s) {
        connection = s;
    }

    /**
     * This method is meant to connect a user to the server.
     *
     * @author Huriel Ferreira Lopes
     * @param args
     * @since 1.0
     */
    public static void main(String args[]) {
        try {
            Socket connection = new Socket("127.0.0.1", 2222);
            PrintStream output = new PrintStream(connection.getOutputStream());
            
            BufferedReader userInput = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            System.out.println("Ola jogador(a)! Seja bem-vindo(a) ao Uno!");
            System.out.print("Para entrar em uma partida, digite o seu nome: ");
            String username = userInput.readLine();
            if(username != null) username = username.trim();
            
            output.println(username);
            
            Thread t = new Client(connection);
            t.start();
            String line;
            while (true) {
                line = userInput.readLine();
                if(line != null) line = line.trim();
                if (done) {
                    break;
                }
                output.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ola jogador(a)!\nNao e possivel se conectar pois "
                    + "a partida nao esta aceitando novos jogadores");
        }
    }
    
    @Override
    public void run() {
        handleConnection();
    }
    
    /**|
     * This method is meant to handle user's connection, setting when its
     * connected or not.
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public void handleConnection() {
        try {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String line;
            while (true) {
                line = entrada.readLine();
                if(line != null) line = line.trim();
                
                if (line == null) {
                    System.out.println("Conexao encerrada!");
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        done = true;
    }
}
