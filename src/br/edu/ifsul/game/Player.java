/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsul.game;

import br.edu.ifsul.model.Card;
import br.edu.ifsul.util.Translation;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Player has all the necessary functions to play Uno as a player
 * 
 * @author Huriel Ferreira Lopes
 */
public class Player implements Serializable {
    private String id;
    private String ip;
    private String name;
    private PrintStream output;
    private Socket socket;
    
    private List<Card> cards = new ArrayList<>();
    private boolean isBlocked = false;
    private boolean isReadyToPlay = false;
    private boolean drawFour = false;
    private boolean drawTwo = false;
    private boolean calledUno = false;
    private boolean canPlay = false;
    private Integer points = 0;
    
    public Player() {
    }
    
    /**
     * This method calls Uno.
     *
     * @author Huriel Ferreira Lopes
     * @param players all the match players to be notified
     * @since 1.0
     */
    public void callUno(List<Player> players) {
        if(cards.size() == 1) {
            calledUno = true;
            
            for (Player player : players) {
                PrintStream chat = (PrintStream) player.getOutput();

                if (chat != output) {
                    chat.println(player.getName() + " chamou Uno!");
                }
            }
        } else {
            output.println("Voce tem mais que uma carta! Nao pode chamar Uno ainda.");
        }
    }
    
    /**
     * This method calls Counter-Uno.
     *
     * @author Huriel Ferreira Lopes
     * @param players the players to receive Counter-Uno call
     * @param deck the deck that may give cards to countered players
     * @since 1.0
     */
    public void counterUno(List<Player> players, List<Card> deck) {
        boolean hasPlayerToCounter = false;
        for (Player currentPlayer : players) {
            if (currentPlayer.getCards().size() == 1 && !currentPlayer.getCalledUno()) {
                currentPlayer.drawTwo(deck);

                for (Player player : players) {
                    PrintStream chat = (PrintStream) player.getOutput();

                    chat.println("Jogador '" + currentPlayer.getName() + "' nao disse Uno e pescou 2 cartas.");
                }

                hasPlayerToCounter = true;
            }
        }

        if (!hasPlayerToCounter) {
            output.println("Nenhum jogador pode receber seu Contra-Uno.");
        }
    }
    
    /**
     * This method shows cards and fish option to play.
     *
     * @author Huriel Ferreira Lopes
     * @param title the title of the notification
     * @since 1.0
     */
    public void showCards(String title) {
        int index = 2;
        
        output.println(title);
        output.println("0 - Voltar");
        output.println("1 - Pescar");
        for (Card currentCard : cards) {
            output.println((index++) + " - " 
                    + Translation.getCardName(currentCard));
        }

        output.println("Sua escolha: ");
    }
    
    /**
     * This method is responsible for handling a dropped card
     *
     * @author Huriel Ferreira Lopes
     * @param deck the deck with receive the dropped card
     * @param card the card to be dropped
     * @since 1.0
     */
    public Card dropCard(List<Card> deck, Card card) {
        Card selectedCard = removeCard(card);
        deck.add(selectedCard);
        
        return selectedCard;
    }
    
    /**
     * This method handle a user draw four action.
     *
     * @author Huriel Ferreira Lopes
     * @param deck the deck that holds the cards to be drawed
     * @since 1.0
     */
    public void drawFour(List<Card> deck) {
        for(int i = 0; i < 4; i++) {
            Card card = deck.remove(0);
            addCard(card);
        }
    }
    
    /**
     * This method handle a user draw two action.
     *
     * @author Huriel Ferreira Lopes
     * @param deck the deck that holds the cards to be drawed
     * @since 1.0
     */
    public void drawTwo(List<Card> deck) {
        for(int i = 0; i < 2; i++) {
            Card card = deck.remove(0);
            addCard(card);
        }
    }
    
    /**
     * This method handle a normal draw action.
     *
     * @author Huriel Ferreira Lopes
     * @param deck the deck that holds the cards to be drawed
     * @since 1.0
     */
    public void draw(List<Card> deck) {
        Card card = deck.remove(0);
        addCard(card);
    }
    
    /**
     * This method adds a card to the player personal deck.
     *
     * @author Huriel Ferreira Lopes
     * @param card the card to be added
     * @since 1.0
     */
    public void addCard(Card card) {
        cards.add(card);
    }
    
    /**
     * This method removes a card from the user personal deck.
     *
     * @author Huriel Ferreira Lopes
     * @param card the card to be removed
     * @since 1.0
     */
    public Card removeCard(Card card) {
        Card cardToRemove = null;
        for(Card cardToVerify : cards) {
            if(cardToVerify.equals(card)) {
                cardToRemove = cardToVerify;
                break;
            } else {
                cardToRemove = null;
            }
        }
        
        if(cardToRemove != null) {
            return cards.remove(cards.indexOf(card));
        }
        
        return null;
    }
    
    /**
     * This method reset the user cards.
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public void resetCards() {
        cards.clear();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrintStream getOutput() {
        return output;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public boolean getDrawFour() {
        return drawFour;
    }

    public void setDrawFour(boolean drawFour) {
        this.drawFour = drawFour;
    }

    public boolean getDrawTwo() {
        return drawTwo;
    }

    public void setDrawTwo(boolean drawTwo) {
        this.drawTwo = drawTwo;
    }

    public boolean getCalledUno() {
        return calledUno;
    }

    public void setCalledUno(boolean calledUno) {
        this.calledUno = calledUno;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public boolean getCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean getIsReadyToPlay() {
        return isReadyToPlay;
    }

    public void setIsReadyToPlay(boolean isReadyToPlay) {
        this.isReadyToPlay = isReadyToPlay;
    }
    
    
}
