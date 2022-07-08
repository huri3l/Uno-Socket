package br.edu.ifsul.communication;

import br.edu.ifsul.game.Match;
import br.edu.ifsul.game.Player;
import br.edu.ifsul.model.Card;
import br.edu.ifsul.model.CardColor;
import br.edu.ifsul.model.CardValue;
import br.edu.ifsul.util.SaveData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server is where the whole game runs.
 * It handles all the server needs to the game and players integration
 * 
 * @author Huriel Ferreira Lopes
 */
public class Server extends Thread {

    private static List<Player> players;

    private final Player player;
    private static Socket connection;
    private static ServerSocket socket;
    
    private static final Match match = new Match();
    
    public Server(Player p) {
        player = p;
    }

    /**
     * This method is meant to handle a user connection
     *
     * @author Huriel Ferreira Lopes
     * @param args
     * @since 1.0
     */
    public static void main(String args[]) {
        players = new ArrayList<>();
        match.start(false);
        SaveData.resetAllPlayersPoints();
        
        System.out.println("Comecou a partida!");
        
        try {
            socket = new ServerSocket(2222);
            while (players.size() < 10) {
                connectNewPlayer(socket);
            }
            
            socket.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    
    @Override
    public void run() {
        handlePlayerAndServerIntegration();
    }

    /**
     * This method is meant to send a private message to a specific player.
     *
     * @author Huriel Ferreira Lopes
     * @param player is the Player that will receive the message
     * @param message a String with the message that will be sent to the player
     * @since 1.0
     */
    public static void sendToPlayer(Player player, String message) {
        PrintStream output = player.getOutput();

        System.out.println(message);
        output.println(message);
    }

    /**
     * This method is meant to send a message to all other players about a 
     * specific player action.
     *
     * @author Huriel Ferreira Lopes
     * @param output is the PrintStream of the player that has made an action
     * @param action a String with the action that the player has made
     * @param line a String with a better description of the player's action
     * @throws java.io.IOException if can't get player's output
     * @since 1.0
     */
    public void sendToOthers(PrintStream output, String action, String line) throws IOException {
        for (Player anotherPlayer : players) {
            PrintStream chat = (PrintStream) anotherPlayer.getOutput();

            if (chat != output) {
                chat.println(player.getName() + action + line);
            }
        }
    }
    
    /**
     * This method is meant to connect a new player to the server.
     *
     * @author Huriel Ferreira Lopes
     * @param socket is the ServerSocket 
     * @throws IOException if the server is not accepting new connections
     * @since 1.0
     */
    public static void connectNewPlayer(ServerSocket socket) throws IOException {
        System.out.print("Aguardando a conexao de jogadores... ");

        connection = socket.accept();
        Player player = new Player();
        player.setId(connection.getRemoteSocketAddress().toString());
        player.setIp(connection.getRemoteSocketAddress().toString());
        player.setSocket(connection);
        
        match.addPlayer(player);
        players.add(player); 
        
        System.out.println("O jogador com IP '"
                + connection.getRemoteSocketAddress() + "' se conectou!: ");

        Thread t = new Server(player);
        t.start();

        if (players.size() > 1 && players.size() < 11) {
            if (match.getMatchStarted()) {
                match.restart(player);
            }
            

            match.setMatchStarted(true);
            match.getPlayers().get(0).setCanPlay(true);
            
            match.getPlayers().get(0).setPoints(90);
            match.getPlayers().get(0).getCards().clear();
            match.getPlayers().get(0).addCard(new Card(CardColor.JOKER, CardValue.DRAW_FOUR));
        }
    }
    
    /**
     * This method is meant to organize the player and server communication.
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public void handlePlayerAndServerIntegration() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(player.getSocket().getInputStream()));          
            
            PrintStream output = new PrintStream(player.getSocket().getOutputStream());
            player.setOutput(output);
            
            setPlayerName(input);
            handlePlayerConnection(input, output);
            connection.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    
    /**
     * This method is meant to set read an input and set a player name.
     *
     * @author Huriel Ferreira Lopes
     * @param input is the player's input
     * @throws IOException if the input can't be read
     * @since 1.0
     */
    public void setPlayerName(BufferedReader input) throws IOException {
        String playerName = "";
        
        while(playerName == null || playerName.equals("")) {
            playerName = input.readLine();
            if(playerName != null) playerName = playerName.trim();
        }
        
        player.setName(playerName);
    }
    
    /**
     * This method treats if the user is connected or not to the server, and
     * handle the game for the user.
     *
     * @author Huriel Ferreira Lopes
     * @param input is the player's input
     * @param output is the player's output
     * @throws IOException if the line can't be read and the message to other
     * players can't be sent
     * @since 1.0
     */
    public void handlePlayerConnection(BufferedReader input, PrintStream output)
    throws IOException {
        String line = "";
        while (!line.equals("sair")) {
            int previousQuantityOfPlayers = 0;
            boolean stillVerifyNewPlayers = false;
            
            match.showStatus(player, stillVerifyNewPlayers, previousQuantityOfPlayers);
            line = input.readLine();
            if(line != null) line = line.trim();
            
            handleMenuOptions(line, previousQuantityOfPlayers, stillVerifyNewPlayers);
        }

        socket.close();
        sendToOthers(output, " saiu ", "do jogo!");
        player.getSocket().close();
    }
    
    /**
     * This method is responsible for handling the menu options
     *
     * @author Huriel Ferreira Lopes
     * @param userOption a String with the option the user chose
     * @param numberOfPlayers the number of players in the match
     * @param stillVerifyNewPlayers is a flag that says if the server should
     * verify new players connection
     * @since 1.0
     */
    public void handleMenuOptions(String userOption, Integer numberOfPlayers, 
            boolean stillVerifyNewPlayers) {
        PrintStream output = player.getOutput();
        
        if (match.getMatchStarted() && !stillVerifyNewPlayers) {
            switch (userOption) {
                case "sair":
                    break;
                case "":
                    break;
                case "1":
                    match.play(player);
                    if(player.getCards().isEmpty()) {
                        match.endRound(player);
                    }
                    break;
                case "2": {
                    String message = player.callUno(match.getPlayers());

                    if (!message.equals(""))
                        player.getOutput().println(message);

                    break;
                }
                case "3": {
                    String message = player.counterUno(match.getPlayers(), match.getDeck());
                    
                    if (!message.equals(""))
                        player.getOutput().println(message);
                        
                    break;
                }
                default:
                    output.println("Opcao invalida! Tente novamente.");
                    break;
            }
        } else {
            switch (userOption) {
                case "0":
                    userOption = "";
                default:
                    output.println("Verificando se novos jogadores se conectaram...");
                    if (match.getPlayers().size() > numberOfPlayers) {
                        output.println("Novos jogadores se conectaram!");
                        stillVerifyNewPlayers = false;
                    } else {
                        output.println("Nenhum jogador novo se conectou!");
                    }
                    break;
            }
        }
    }
}
