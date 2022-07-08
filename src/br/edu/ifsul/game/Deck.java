package br.edu.ifsul.game;

import br.edu.ifsul.model.Card;
import br.edu.ifsul.model.CardColor;
import br.edu.ifsul.model.CardValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck has all the necessary functions to work with a Uno Deck
 * 
 * @author Huriel Ferreira Lopes
 */
public class Deck {
  
    public Deck() {
        
    }
    
    /**
     * This method generates a new deck
     *
     * @return a List of Uno Cards
     * @since 1.0
     */
    public static List<Card> generateDeck() {
        List<Card> deck = new ArrayList<>();
        
        CardColor cardsColors[] = CardColor.values();
        CardValue cardsValues[] = CardValue.values();
        
        for(CardColor cardColor : cardsColors) {
            if(cardColor.equals(CardColor.JOKER)) {
                for (int i = 0; i < 4; i++) {
                    deck.add(new Card(cardColor, CardValue.CHANGE_COLOR));
                    deck.add(new Card(cardColor, CardValue.DRAW_FOUR));
                }
            } else {
                deck.add(new Card(cardColor, CardValue.ZERO));
                
                for (CardValue cardValue : cardsValues) {
                    if (cardValue.equals(CardValue.CHANGE_COLOR)
                            || cardValue.equals(CardValue.DRAW_FOUR)
                            || cardValue.equals(CardValue.ZERO)) {
                        continue;
                    }
                    
                    Card newCard = new Card(cardColor, cardValue);
                    for (int i = 0; i < 2; i++) {
                        deck.add(newCard);
                    }
                }
            }
        }     
        
        return deck;
    }
    
    /**
     * This method shuffles a given deck
     *
     * @param deck the deck to be shuffled
     * @since 1.0
     */
    public static void shuffleDeck(List<Card> deck) {
        Collections.shuffle(deck);
    }
    
    /**
     * This method adds a card to a given deck
     *
     * @param deck the deck where the card will be added
     * @param card the card to be added
     * @since 1.0
     */
    public static void addCard(List<Card> deck, Card card) {
        deck.add(card);
    }
    
    /**
     * This method deal cards from a deck to a list of players
     *
     * @param deck the deck that holds the cards to be dealt
     * @param players the players that will receive its cards
     * @since 1.0
     */
    public static void dealCards(List<Card> deck, List<Player> players) {
        for(Player player : players) {
            for (int i = 0; i < 7; i++) {
                Card selectedCard = deck.remove(0);

                player.addCard(selectedCard);
            }
        }
    }
    
    /**
     * This method deal cards from a deck to a specific player
     *
     * @param deck the deck that holds the cards to be dealt
     * @param player the player that will receive the cards
     * @since 1.0
     */
    public static void dealCards(List<Card> deck, Player player) {
        for (int i = 0; i < 7; i++) {
            Card selectedCard = deck.remove(0);

            player.addCard(selectedCard);
        }
    }
    
    /**
     * This method flips the first card of a deck
     *
     * @param deck the deck where the card to be flipped is
     * @param usedCards another deck that holds the cards used in the game
     * @return the flipped card
     * @since 1.0
     */
    public static Card flipFirstCard(List<Card> deck, List<Card> usedCards) {
        Card firstCard = deck.remove(0);
        
        while(firstCard.getColor().equals(CardColor.JOKER)) {
            deck.add(firstCard);
            firstCard = deck.remove(0);
        }
        
        usedCards.add(firstCard);
        
        return firstCard;
    }
}
