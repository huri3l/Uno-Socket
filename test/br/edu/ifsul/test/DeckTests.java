package br.edu.ifsul.test;

import br.edu.ifsul.game.Deck;
import br.edu.ifsul.model.Card;
import br.edu.ifsul.game.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * DeckTests is supposed to simulate real cases in the game to test its functions
 * 
 * @author Huriel Ferreira Lopes
 */
public class DeckTests {
    public static void main(String[] args) {
        List<Card> deck = new ArrayList<>();
        
        // Teste - Geração de Baralho
        System.out.println("\n\n\nTeste - Geração de Baralho\n\n\n");
        deck = Deck.generateDeck();
        System.out.println("\nBaralho original:");
        for(Card card : deck) {
            System.out.println("Carta: " + card.getColor() + " " + card.getValue());
        }
        
        // Teste - Baralho embaralhado
        System.out.println("\n\n\nTeste - Baralho embaralhado\n\n\n");
        Deck.shuffleDeck(deck);
        System.out.println("\nBaralho embaralhado:");
        for(Card card : deck) {
            System.out.println("Carta: " + card.getColor() + " " + card.getValue());
        }
        System.out.println("Tamanho do baralho: " + deck.size());
        
        // Teste - Atribuir as cartas dos jogadores
        System.out.println("\n\n\nTeste - Atribuir as cartas dos jogadores\n\n\n");
        Player player1 = new Player();
        player1.setName("Jovitera");
        Player player2 = new Player();
        player2.setName("Huriel");
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Deck.dealCards(deck, players);
        for(Player player : players) {
            for(Card playerCard : player.getCards()) {
                System.out.println("Carta do jogador '" + player.getName() + "': " + playerCard.getColor() + " " + playerCard.getValue());
            }
        }
        System.out.println("\nBaralho após dar as cartas:");
        for(Card card : deck) {
            System.out.println("Carta: " + card.getColor() + " " + card.getValue());
        }
        System.out.println("Tamanho do baralho: " + deck.size());
        
        // Teste - Jogada
        System.out.println("\n\n\nTeste - jogada\n\n\n");
        player1.dropCard(deck, player1.getCards().get(3));
        for(Player player : players) {
            for(Card playerCard : player.getCards()) {
                System.out.println("Carta do jogador '" + player.getName() + "': " + playerCard.getColor() + " " + playerCard.getValue());
            }
            System.out.println("Quantidade de cartas do jogador: " + player.getCards().size());
        }
        
        System.out.println("\nBaralho após jogada:");
        for (Card card : deck) {
            System.out.println("Carta: " + card.getColor() + " " + card.getValue());
        }
        System.out.println("Tamanho do baralho: " + deck.size());
        
        
    }
}
