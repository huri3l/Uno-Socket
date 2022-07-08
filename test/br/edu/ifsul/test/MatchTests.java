package br.edu.ifsul.test;

import br.edu.ifsul.game.Match;
import br.edu.ifsul.game.Player;

/**
 * MatchTests is supposed to simulate real cases in the game to test its
 * functions
 *
 * @author Huriel Ferreira Lopes
 */
public class MatchTests {
    public static void main(String[] args) {
        Match match = new Match();
        
        
        Player player1 = new Player();
        player1.setName("Huriel");
        
        Player player2 = new Player();
        player2.setName("Jovitera");
        
        Player player3 = new Player();
        player3.setName("ShaolinMatadorDePorco");
        
        match.addPlayer(player1);
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.start(false);
        
        // Teste - Cartas são jogáveis?
//        System.out.println("\n\n\nTeste - Carta do jogador e jogavel?");
//        System.out.println("Carta na mesa: " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        
//        for (Player currentPlayer : match.getPlayers()) {
//            for (Card currentCard : currentPlayer.getCards()) {
//                if (match.cardCanBePlayed(currentCard)) {
//                    System.out.println("Jogador '" + currentPlayer.getName() + "' possui uma carta compativel: " + currentCard.getColor() + " | " + currentCard.getValue());
//                }
//            }
//            
//        }
//        
//        // Teste - Antes e Depois de jogada
//        System.out.println("\n\n\nTeste - Antes e Depois de jogada?");
//        System.out.println("--- ANTES ---");
//        System.out.println("Carta na mesa: " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        System.out.println("Tamanho do lixo: " + match.getUsedCards().size());
//        
//        player1.addCard(new Card(CardColor.JOKER, CardValue.DRAW_FOUR));
//        System.out.println("Cartas do jogador Huriel");
//        for(Card currentCard : player1.getCards()) {
//            System.out.println("Carta: " + currentCard.getColor() + " | " + currentCard.getValue());
//        }
//        
//        match.playCard(player1, player1.getCards().get(7));
//        
//        System.out.println("--- DEPOIS ---");
//        System.out.println("Carta na mesa: " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        System.out.println("Tamanho do lixo: " + match.getUsedCards().size());
//        
//        // Teste - Jogada com carta Block
//        System.out.println("\n\n\nTeste - Jogada com carta Block");
//        System.out.println("Teste com player 1:");
//        Card blockCard = new Card(CardColor.BLUE, CardValue.BLOCK);
//        match.setLastCardInTable(blockCard);
//        
//        player1.addCard(blockCard);        
//        match.playCard(player1, player1.getCards().get(7));
//        
//        System.out.println("Status dos jogadores");
//        System.out.println("Player 1 esta bloqueado? (Nao deve estar): " + player1.getIsBlocked());
//        System.out.println("Player 2 esta bloqueado? (Deve estar): " + player2.getIsBlocked());
//        
//        System.out.println("\nTeste com player 2:");
//        match.setLastCardInTable(blockCard);
//        
//        player2.addCard(blockCard);        
//        match.playCard(player2, player2.getCards().get(7));
//        
//        System.out.println("Status dos jogadores");
//        System.out.println("Player 1 esta bloqueado? (Nao deve estar ainda): " + player1.getIsBlocked());
//        System.out.println("Player 2 esta bloqueado? (Nao deve estar): " + player2.getIsBlocked());
//               
//        match.playCard(player1, player1.getCards().get(0));
//        match.playCard(player2, player2.getCards().get(7));
//        
//        System.out.println("Status dos jogadores");
//        System.out.println("Player 1 esta bloqueado? (Deve estar): " + player1.getIsBlocked());
//        System.out.println("Player 2 esta bloqueado? (Nao deve estar): " + player2.getIsBlocked());
        
        // Teste - Jogada com carta Change_Color
//        System.out.println("\n\n\nTeste - Jogada com carta Change_Color");
//        Card changeColorCard = new Card(CardColor.JOKER, CardValue.CHANGE_COLOR);
//        Card blueFive = new Card(CardColor.BLUE, CardValue.FIVE);
//        Card yellowFive = new Card(CardColor.YELLOW, CardValue.FIVE);
//        player1.addCard(changeColorCard);
//        
//        match.playCard(player1, player1.getCards().get(7));
//        System.out.println("Carta jogada (Deve ser a CHANGE_COLOR): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        
//        player1.addCard(blueFive);
//        System.out.println("Carta a ser jogada: " + player1.getCards().get(7).getColor() + " | " + player1.getCards().get(7).getValue());
//        match.playCard(player1, player1.getCards().get(7));
//        System.out.println("Carta na mesa (Deve ser a CHANGE_COLOR ainda): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        
//        player1.addCard(yellowFive);
//        System.out.println("Carta a ser jogada: " + player1.getCards().get(8).getColor() + " | " + player1.getCards().get(8).getValue());
//        match.playCard(player1, player1.getCards().get(8));
//        System.out.println("Carta na mesa (Deve ser a YELLOW | FIVE): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());       
        
        // Teste - Jogada com carta Draw Four
//        System.out.println("\n\n\nTeste - Jogada com carta Draw Four");
//        System.out.println("Quantia de cartas do Player 2 (Antes de pegar +4): " + player2.getCards().size());
//        Card drawFourCard = new Card(CardColor.JOKER, CardValue.DRAW_FOUR);
//        player1.addCard(drawFourCard);
//        
//        match.playCard(player1, player1.getCards().get(7));
//        System.out.println("Carta jogada (Deve ser a JOKER | DRAW_FOUR): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        System.out.println("Player 2 deve pegar +4 (Deve ser true): " + player2.getDrawFour());
//        
//        match.playCard(player2, player2.getCards().get(4));
//        
//        System.out.println("Quantia de cartas do Player 2 (Apos pegar +4): " + player2.getCards().size());

        // Teste - Jogada com carta Draw Two
//        System.out.println("\n\n\nTeste - Jogada com carta Draw Two");
//        System.out.println("Quantia de cartas do Player 2 (Antes de pegar + 2): " + player2.getCards().size());
//        System.out.println("Player 2 deve pegar +2 (Deve ser false): " + player2.getDrawTwo());
//        Card drawTwoCard = new Card(CardColor.GREEN, CardValue.DRAW_TWO);
//        Card greenCard = new Card(CardColor.GREEN, CardValue.EIGHT);
//        
//        player1.addCard(drawTwoCard);        
//        match.playCard(player1, player1.getCards().get(7));
//        
//        System.out.println("Carta da mesa (Deve ser GREEN | DRAW_TWO): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        match.setLastCardInTable(greenCard);
//        System.out.println("Carta da mesa (Deve ser GREEN | EIGHT): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        
//        System.out.println("Player 2 deve pegar +2 (Deve ser true): " + player2.getDrawTwo());
//        match.playCard(player2, player2.getCards().get(4));
//        
//        System.out.println("Quantia de cartas do Player 2 (Apos pegar +2): " + player2.getCards().size());

        // Teste - Jogada com carta Reverse
//        System.out.println("\n\n\nTeste - Jogada com carta Reverse");
//        Card reverseCard = new Card(CardColor.BLUE, CardValue.REVERSE);
//        Card blueCard = new Card(CardColor.BLUE, CardValue.FIVE);
//        
//        player2.addCard(reverseCard);
//        
//        match.setLastCardInTable(blueCard);
//        System.out.println("Carta da mesa (Deve ser BLUE | FIVE): " + match.getLastCardInTable().getColor() + " | " + match.getLastCardInTable().getValue());
//        System.out.println("Proximo jogador a jogar (Deve ser o Player 3, ShaolinMatadorDePorco: " + match.getNextPlayer(player2).getName());
//        match.playCard(player2, player2.getCards().get(7));
//        System.out.println("Proximo jogador a jogar (Deve ser o Player 1, Huriel: " + match.getNextPlayer(player2).getName());

        // Teste - Uno e Contra-uno
//        System.out.println("\n\n\nTeste - Uno e Contra-uno");
//        System.out.println("Player 1 vai tentar chamar Uno. Deve aparecer mensagem que ele nao pode chamar ainda");
//        player1.callUno();
//        
//        System.out.println("Player 2 vai tentar chamar Contra-uno. Deve aparecer mensagem que nenhum jogador tem 1 carta");
//        match.counterUno(player2);
//        
//        System.out.println("Player 2 vai tentar chamar Contra-uno. Deve dar certo, pois o Player 1 tera so 1 carta e nao chamou Uno");    
//        player1.getCards().remove(0);
//        player1.getCards().remove(0);
//        player1.getCards().remove(0);
//        player1.getCards().remove(0);
//        player1.getCards().remove(0);
//        player1.getCards().remove(0);
//        
//        System.out.println("Total de cartas do jogador 1 (deve ser 1): " + player1.getCards().size());
//        match.counterUno(player2);
//        System.out.println("Total de cartas do jogador 1 (deve ser 3): " + player1.getCards().size());
//        
//        System.out.println("Player 1 vai chamar Uno. Deve conseguir e o Counter nao deve dar certo");
//        player1.getCards().remove(0);
//        player1.getCards().remove(0);
//        player1.callUno();
//        match.counterUno(player2);

        // Teste - Pontuação
        System.out.println("\n\n\nTeste - Pontuacao");        
        System.out.println("Player 1 ta com 0 cartas, vai tentar terminar o round");
        player1.getCards().remove(0);
        player1.getCards().remove(0);
        player1.getCards().remove(0);
        player1.getCards().remove(0);
        player1.getCards().remove(0);
        player1.getCards().remove(0);
        player1.getCards().remove(0);
        
        System.out.println("Cartas do Player 1: " + player1.getCards().size());
        
        System.out.println("Pontos do Player 1 antes de finalizar o round: " + player1.getPoints());
        System.out.println("Pontos do Player 2 antes de finalizar o round: " + player2.getPoints());
        
        match.endRound(player1);
        
        System.out.println("Pontos do Player 1 depois de finalizar o round: " + player1.getPoints());
        System.out.println("Pontos do Player 2 depois de finalizar o round: " + player2.getPoints());
        
        System.out.println("Vamos tentar finalizar a partida. Deve dar certo");
        for(int i = 0; i < 9; i++) {
            player1.getCards().remove(0);
            player1.getCards().remove(0);
            player1.getCards().remove(0);
            player1.getCards().remove(0);
            player1.getCards().remove(0);
            player1.getCards().remove(0);
            player1.getCards().remove(0);
            
            match.endRound(player1);
            System.out.println("Pontos do Player 1 depois de finalizar o round: " + player1.getPoints());
        }
    }
}
