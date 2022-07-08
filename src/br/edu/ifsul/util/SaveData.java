package br.edu.ifsul.util;

import br.edu.ifsul.game.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * SaveData holds the methods responsibles for maintaining updated info
 * in the TextFile
 * 
 * @author Huriel Ferreira Lopes
 */
public class SaveData {    
    
    /**
     * This method is meant to register a new Player on the TextFile
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be registered
     * @since 1.0
     */
    public static void writeNewPlayer(Player player) {
        FileWriter file = null;
        try {
            file = new FileWriter("players.txt", true);
            PrintWriter pw = new PrintWriter(file);
            
            pw.println("Nome: " + player.getName() + " | Vitorias: 0 | Empates:"
                + " 0 | Derrotas: 0 | Ranking: 0 | Pontuacao na ultima partida: 0");

            file.close();
        } catch (IOException ex) {
            System.out.println("Houve um problema ao tentar registrar os dados de jogo.");
        }
    }
    
    /**
     * This method is meant to get a new Player info from the TextFile
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be retrieved
     * @since 1.0
     */
    public static String[] getPlayerInfo(Player player) {
        try {
            File file = new File("players.txt");
            Scanner scanner = new Scanner(file);
            
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                if(line.equals("")) continue;
                
                String savedName = line.split(" \\| ")[0].split(": ")[1];
                if (!savedName.equals(player.getName())) continue;
                
                return formatPlayerInfo(line.split(" \\| "));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        }
        
        return null;
    }
    
    public static boolean isPlayerAlreadyRegistered(Player player) {
        String[] playerInfo = getPlayerInfo(player);
        
        return playerInfo != null;
    }
    
    /**
     * This method is meant to format a player information from the TextFile,
     * getting only its data
     *
     * @author Huriel Ferreira Lopes
     * @param playerInfo the raw information
     * @since 1.0
     */
    public static String[] formatPlayerInfo(String[] playerInfo) {        
        for(int i = 0; i < playerInfo.length; i++) {
            playerInfo[i] = playerInfo[i].split(": ")[1];
        }
        
        return playerInfo;
    }
    
    /**
     * This method is meant to update a player info in the TextFile
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be updated
     * @param win indicates whether the player won or not
     * @param tie indicates whether the player tied or not
     * @param lost indicates whether the player lost or not
     * @since 1.0
     */
    public static void updatePlayerInfo(Player player, boolean win, boolean tie,
        boolean lost) {        
        String[] playerInfo = getPlayerInfo(player);

        if(playerInfo == null) {
            System.out.println("Nao foi possivel encontrar esse jogador nos"
                    + " nossos dados");
            return;
        }

        String savedName = playerInfo[0];

        if(!savedName.equals(player.getName())) return;

        try {
            Integer savedWins = Integer.parseInt(playerInfo[1]);
            Integer savedTies = Integer.parseInt(playerInfo[2]);
            Integer savedLosts = Integer.parseInt(playerInfo[3]);
            Integer savedRank = Integer.parseInt(playerInfo[4]);
            Integer savedPoints = Integer.parseInt(playerInfo[5]);

            if (win) {
                savedWins++;
            }

            if (tie) {
                savedTies++;
            }

            if (lost) {
                savedLosts++;
            }

            if (win || tie || lost) {
                savedPoints = 0;
                player.setPoints(0);

                resetAllPlayersPoints();
            } else {
                savedPoints += 10;
                player.setPoints(savedPoints);
            }

            if (savedPoints == 100) {

                if (!win) {
                    savedWins++;
                }

                savedPoints = 0;
                player.setPoints(0);

                resetAllPlayersPoints();
            }

            updatePlayerInFile(player, savedWins, savedTies, savedLosts, savedRank, savedPoints);
        } catch (NumberFormatException e) {
            System.out.println("Nao foi possivel ler as informacoes do jogador.");
        }
    }
    
    /**
     * This method is meant to handle the ranking of the players when something 
     * updates
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public static void handleRanking() {       
        try {
            String filePath = "players.txt";
            Scanner sc = new Scanner(new File(filePath));
            
            List<PlayerRanking> playersRank = new ArrayList<>();

            while (sc.hasNextLine()) {
                String playerName = sc.nextLine().split(" \\| ")[0].split(": ")[1];
                
                Player tempPlayer = new Player();
                tempPlayer.setName(playerName);
                
                String[] playerInfo = getPlayerInfo(tempPlayer);
                
                if (playerInfo == null) continue;
                
                PlayerRanking playerRank = new PlayerRanking();
                
                playerRank.setName(playerName);
                
                
                
                playerRank.setWins(Integer.parseInt(playerInfo[1]));
                
                playersRank.add(playerRank);
            }
            
            Collections.sort(playersRank, new PlayerRankingComparator());
            
            int idx = 1;
            String newFileContents = "";
            for(PlayerRanking playerRank : playersRank) {
                sc = new Scanner(new File(filePath));
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String playerName = line.split(" \\| ")[0].split(": ")[1];
                    
                    if (!playerRank.getName().equals(playerName))
                        continue;
                    
                    String rank = line.split("Ranking: ")[0];
                    rank = rank.concat("Ranking: " + idx);
                    String points = line.split("Pontuacao na ultima partida: ")[1];
                    
                    String newLine = rank.concat(" | Pontuacao na ultima partida: "
                            + points);
                    
                    newFileContents += newLine.concat(System.lineSeparator());
                }
                idx ++;
            }
            
            FileWriter writer = new FileWriter(filePath);
            writer.append(newFileContents);
            writer.flush();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        } catch (IOException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        }
    }

    /**
     * This method is meant to update a player data in the TextFile
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be updated
     * @param wins the updated amount of wins
     * @param ties the updated amount of ties
     * @param losts the updated amount of losts
     * @param rank the updated rank
     * @param points the updated points
     * @since 1.0
     */
    public static void updatePlayerInFile(Player player, int wins, int ties, 
            int losts, int rank, int points) {
        try {
            String filePath = "players.txt";
            Scanner sc = new Scanner(new File(filePath));
            StringBuilder str = new StringBuilder();
            
            while (sc.hasNextLine()) {
                str.append(sc.nextLine()).append(System.lineSeparator());
            }
            
            String fileContents = str.toString();
            sc.close();
            
            String oldLine = getPlayerLine(player);
            
            if (oldLine == null) return;
            
            String newLine = "Nome: " + player.getName() + " | Vitorias: "
                    + wins + " | Empates: " + ties + " | Derrotas: "
                    + losts + " | Ranking: " + rank
                    + " | Pontuacao na ultima partida: " + points;
            
            fileContents = fileContents.replace(oldLine, newLine);
            
            FileWriter writer = new FileWriter(filePath);
            writer.append(fileContents);
            writer.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        } catch (IOException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        }
    }
    
    /**
     * This method is meant to get a player information in the TextFile
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be updated
     * @return raw string line that holds the player information in the TextFile
     * @since 1.0
     */
    public static String getPlayerLine(Player player) {
        try {            
            File file = new File("players.txt");
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.equals("")) continue;

                String savedName = line.split(" \\| ")[0].split(": ")[1];
                if (savedName.equals(player.getName())) {
                    return line;
                }
            }           
        } catch (FileNotFoundException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        }
        
        return null;
    }
    
    /**
     * This method is meant to reset all player points
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public static void resetAllPlayersPoints() {
        try {
            String filePath = "players.txt";
            Scanner sc = new Scanner(new File(filePath));
            StringBuilder str = new StringBuilder();
            
            while (sc.hasNextLine()) {
                str.append(sc.nextLine()).append(System.lineSeparator());
            }
            
            String fileContents = str.toString();
            String newFileContents = "";
            sc.close();
            
            for(String oldLine : fileContents.split(System.lineSeparator())) {
                oldLine = oldLine.split("Pontuacao na ultima partida: ")[0];
                String newLine = oldLine.concat("Pontuacao na ultima partida: 0");
                
                newFileContents += newLine.concat(System.lineSeparator());
            }
            
            FileWriter writer = new FileWriter(filePath);
            writer.append(newFileContents);
            writer.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        } catch (IOException ex) {
            System.out.println("Nao foi possivel ler os dados registrados.");
        }
    }
}
