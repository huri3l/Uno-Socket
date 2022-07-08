package br.edu.ifsul.game;

import br.edu.ifsul.model.Card;
import br.edu.ifsul.model.CardColor;
import br.edu.ifsul.util.SaveData;
import br.edu.ifsul.util.Translation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Match is meant to organize a Uno's match
 * 
 * @author Huriel Ferreira Lopes
 */
public class Match {
    private List<Card> deck = new ArrayList<>();
    private final List<Card> usedCards = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Card lastCardInTable;
    private boolean matchStarted = false;
    
    public Match() {
        
    }
    
    /**
     * This method is meant to start a match.
     *
     * @author Huriel Ferreira Lopes
     * @param restart a flag that says whether is a restart or not
     * @since 1.0
     */
    public void start(boolean restart) {
        startRound(restart);
    }
    
    /**
     * This method is meant restart a match.
     *
     * @author Huriel Ferreira Lopes
     * @param newPlayer the new player to enter the match
     * @since 1.0
     */
    public void restart(Player newPlayer) {     
        resetAllPlayersCards();
        resetUsedCards();
        
        sendMessageToOtherPlayers(newPlayer, 
                "Um novo jogador entrou! A partida sera reiniciada. Pressione "
                        + "ENTER para ver os status da nova partida.");
        
        start(true);
    }
    
    /**
     * This method is meant to end a match.
     *
     * @author Huriel Ferreira Lopes
     * @param winner the player that won the match
     * @since 1.0
     */
    public void end(Player winner) {        
        for (Player player : players) {
            PrintStream chat = (PrintStream) player.getOutput();
            
            if (player.equals(winner))
                SaveData.updatePlayerInfo(winner, true, false, false);
            else
                SaveData.updatePlayerInfo(player, false, false, true);

            if (chat != winner.getOutput()) {
                chat.println(player.getName() + " venceu a partida!");
                chat.println("Iniciando uma nova partida...");
            }
        }
        
        SaveData.handleRanking();
        
        winner.getOutput().println("Parabens!! Voce venceu a partida! "
                + "Vamos iniciar uma nova partida para voce e os demais jogadores");
    }
    
    /**
     * This method is meant to connect a user to the server.
     *
     * @author Huriel Ferreira Lopes
     * @param restart a flag that says whether is a restart or not
     * @since 1.0
     */
    public void startRound(boolean restart) {
        deck = Deck.generateDeck();
        Deck.shuffleDeck(deck);        
        lastCardInTable = Deck.flipFirstCard(deck, usedCards);
        
        if(restart) {
            resetAllPlayersCards();
            distributePlayersCards();
        }
    }
    
    /**
     * This method is meant end a round.
     *
     * @author Huriel Ferreira Lopes
     * @param roundWinner the player that won the round
     * @since 1.0
     */
    public void endRound(Player roundWinner) {
        boolean shouldEndMatch = false;
        Player winner = null;
        
        roundWinner.setPoints(roundWinner.getPoints() + 10);

        if (roundWinner.getPoints() >= 100) {
            shouldEndMatch = true;
            winner = roundWinner;
        }
        
        if(shouldEndMatch) 
            end(winner);
        else {
            if (SaveData.isPlayerAlreadyRegistered(roundWinner))
                SaveData.updatePlayerInfo(roundWinner, false, false, false);
            else {
                SaveData.writeNewPlayer(roundWinner);
                SaveData.updatePlayerInfo(roundWinner, false, false, false);
            }
            
            for(Player player : players) {
                player.getOutput().println("O jogador " + roundWinner.getName()
                        + " venceu o round! Reiniciando as cartas para jogar..."
                        + "\nPressione ENTER para ver suas opcoes e o status"
                        + " atualizado da partida.");
            }
            
            startRound(true);
        }
    }
    
    /**
     * This method is meant to show match status to a player.
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be shown the status
     * @param stillVerifyNewPlayers a flag that says wheter verify new players or not
     * @param previousQuantityOfPlayers the previous quantity of players
     * @since 1.0
     */
    public void showStatus(Player player, boolean stillVerifyNewPlayers,
            Integer previousQuantityOfPlayers) {
        PrintStream output = player.getOutput();

        if (players.size() > 1) {
            for (Player currentPlayer : players) {
                if (!currentPlayer.equals(player)) {
                    String playerName = currentPlayer.getName();
                    
                    if(playerName == null)
                        playerName = "Jogador nao identificado";
                    
                    output.println(playerName + ": " + "\n  - "
                            + currentPlayer.getPoints() + " pontos." + "\n  - "
                            + currentPlayer.getCards().size() + " cartas.");
                    
                    if(currentPlayer.getIsBlocked())
                        output.println(" - Esta bloqueado.");
                    
                    if(currentPlayer.getCanPlay())
                        output.println(" - E o jogador da vez.");
                }
            }

            output.println("Voce: " + "\n  - "
                    + player.getPoints() + " pontos." + "\n  - "
                    + player.getCards().size() + " cartas.");

            if(player.getIsBlocked())
                output.println(" - Esta bloqueado.");
            
            if(player.getCanPlay())
                output.println(" - E o jogador da vez.");
            
            output.println("Carta na mesa: "
                    + showLastCardInTable());

            output.println();
            output.println("Escolha uma das opcoes\n"
                    + "Se quiser sair do jogo, digite 'sair'\n"
                    + "1 - Jogar carta\n"
                    + "2 - Chamar Uno\n"
                    + "3 - Chamar Contra-Uno\n"
                    + "Sua escolha: ");
        } else {
            output.println("Aguardando a conexao de outros "
                    + "jogadores.\n" + "Pressione ENTER para verificar "
                    + "se outros jogadores se conectaram");

            stillVerifyNewPlayers = true;
            previousQuantityOfPlayers = players.size();
        }
    }
    
    /**
     * This method handles a user play.
     *
     * @author Huriel Ferreira Lopes
     * @param currentPlayer the player playing
     * @since 1.0
     */
    public void play(Player currentPlayer) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    currentPlayer.getSocket().getInputStream()));
            String userOption = "";
            Card selectedCard = null;

            while (!userOption.equals("0")) {
                currentPlayer.showCards("Selecione uma opcao:");
                userOption = input.readLine();
                if(userOption != null) userOption = userOption.trim();
                
                if(!userOption.equals("0")) {
                    if(userOption.equals("1")) {
                        if (!playerCanPlay(currentPlayer)) return;
                        
                        currentPlayer.draw(deck);
                        currentPlayer.setCanPlay(false);
                        getNextPlayer(currentPlayer).setCanPlay(true);
                        
                        getNextPlayer(currentPlayer).getOutput().println(
                                "O jogador " + currentPlayer.getName()
                                + " pescou, entao e sua vez de jogar."
                                + " Pressione ENTER para ver suas opcoes"
                                + " e o status atualizado da partida.");
                        
                        return;
                    }
                    
                    try {
                        int cardIndex = Integer.parseInt(userOption) - 2;
                        
                        if (cardIndex > currentPlayer.getCards().size()) {
                            currentPlayer.getOutput().println(
                                    "Nao foi possivel identificar a"
                                    + " carta escolhida! Tente novamente.");
                            return;
                        }

                        selectedCard = currentPlayer.getCards().get(cardIndex);
                        break;
                    } catch (NumberFormatException e) {
                        if (!userOption.equals("")) {
                            currentPlayer.getOutput().println("Nao foi possivel"
                                    + " identificar a carta escolhida!"
                                    + " Tente novamente.");
                        }
                    }
                }
            }
            
            if(selectedCard == null) {
                currentPlayer.getOutput().println("Nao foi possível identificar"
                        + "a carta escolhida! Tente novamente.");
                return;
            }
            
            playCard(currentPlayer, selectedCard);
        } catch (IOException ex) {
            currentPlayer.getOutput().println("Ocorreu um erro no servidor e nao "
                    + "podemos identificar a carta escolhida! Tente novamente.");
        }
    }
    
    /**
     * This method gives players cards.
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public void distributePlayersCards() {
        for(Player player : players) {           
            Deck.dealCards(deck, player);    
        }
    }
    
    /**
     * This method resets the Used Cards Deck.
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public void resetUsedCards() {
        for (Iterator<Card> it = usedCards.iterator(); it.hasNext();) {
            usedCards.clear();
        }
    }
    
    /**
     * This method resets the cards of all players.
     *
     * @author Huriel Ferreira Lopes
     * @since 1.0
     */
    public void resetAllPlayersCards() {
        for(Player player : players) {
            player.resetCards();
        }
    }
    
    /**
     * This method handles a card play.
     *
     * @author Huriel Ferreira Lopes
     * @param currentPlayer the player playing
     * @param card the card being played
     * @since 1.0
     */
    public void playCard(Player currentPlayer, Card card) {
        if(!playerCanPlay(currentPlayer)) return;
        
        if(!cardCanBePlayed(card)) {
            String userOption = "";
            
            while(!userOption.equals("0") && !userOption.equals("1")) {
                currentPlayer.getOutput().println("Sua carta nao pode ser jogada!"
                        + " Deseja tentar outra ou pescar?\n"
                        + "0 - Voltar (tentar outra carta)\n"
                        + "1 - Pescar\n"
                        + "Sua escolha: ");
                
                BufferedReader input;
                try {
                    input = new BufferedReader(new InputStreamReader(currentPlayer.getSocket().getInputStream()));
                    
                    userOption = input.readLine();
                    if(userOption != null) userOption = userOption.trim();

                    switch (userOption) {
                        case "0":
                            play(currentPlayer);
                            break;
                        case "1":
                            currentPlayer.draw(deck);
                            currentPlayer.setCanPlay(false);
                            getNextPlayer(currentPlayer).setCanPlay(true);
                            
                            getNextPlayer(currentPlayer).getOutput().println(
                                    "O jogador " + currentPlayer.getName() +
                                    " pescou, entao e sua vez de jogar." + 
                                    " Pressione ENTER para ver suas opcoes" + 
                                    " e o status atualizado da partida.");
                            break;
                        default:
                            currentPlayer.getOutput().println("Opcao invalida!");
                    }
                } catch (IOException ex) {
                    currentPlayer.getOutput().println("Ocorreu um erro no servidor "
                            + "e nao podemos detectar sua opcao");
                }
                
            }
            return;
        }      
        
        Card droppedCard = currentPlayer.dropCard(usedCards, card);
        lastCardInTable = droppedCard;
        notifyNewCardInTable(currentPlayer, droppedCard);

        switch (droppedCard.getValue()) {
            case BLOCK: {
                Player nextPlayer = getNextPlayer(currentPlayer);
                Player nextAfterBlock = getNextPlayer(nextPlayer);
                
                nextPlayer.getOutput().println("Voce foi bloqueado pelo jogador "
                        + currentPlayer.getName() + ". Devido a isso, ficara"
                        + " uma rodada sem jogar.");
                
                nextAfterBlock.getOutput().println("O jogador anterior foi"
                       + " bloqueado, entao e sua vez de jogar! Pressione ENTER"
                       + " para ver suas opcoes e o status atualizado da partida.");
                
                nextPlayer.setIsBlocked(true);
                nextPlayer.setCanPlay(false);
                nextAfterBlock.setCanPlay(true);
                
                break;
            }
            case CHANGE_COLOR: {
                Player nextPlayer = getNextPlayer(currentPlayer);
                setJokerCardColor(currentPlayer);
                
                String message = "O jogador " + currentPlayer.getName() + " trocou"
                        + " a cor da mesa para " 
                        + Translation.getColor(droppedCard.getColor()) + "!";
                sendMessageToOtherPlayers(currentPlayer, message);
                
                nextPlayer.setCanPlay(true);
                break;
            }
            case DRAW_FOUR: {
                Player nextPlayer = getNextPlayer(currentPlayer);
                
                setJokerCardColor(currentPlayer);

                String message = "O jogador " + currentPlayer.getName() + " trocou"
                        + " a cor da mesa para "
                        + Translation.getColor(droppedCard.getColor()) + "!";
                sendMessageToOtherPlayers(currentPlayer, message);
                
                nextPlayer.getOutput().println("O jogador " + currentPlayer.getName()
                        + " jogou um Mais Quatro para voce. Devido a isso, quatro "
                        + "cartas serao adicionadas a sua mao.");
                nextPlayer.drawFour(deck);
                
                nextPlayer.setCanPlay(true);
                break;
            }
            case DRAW_TWO: {
                Player nextPlayer = getNextPlayer(currentPlayer);
                nextPlayer.getOutput().println("O jogador " + currentPlayer.getName()
                        + " jogou um Mais Dois para voce. Devido a isso, duas "
                        + "cartas serao adicionadas a sua mao.");
                nextPlayer.drawTwo(deck);
                
                nextPlayer.setCanPlay(true);
                break;
            }
            case REVERSE: {
                Collections.reverse(players);
                Player nextPlayer = getNextPlayer(currentPlayer);
                
                String message = "O jogador " + currentPlayer.getName() + " trocou"
                        + " a ordem da mesa!";
                sendMessageToOtherPlayers(currentPlayer, message);
                
                nextPlayer.setCanPlay(true);
                break;
            }
            default: {
                Player nextPlayer = getNextPlayer(currentPlayer);
                
                nextPlayer.setCanPlay(true);
                break;
            }
        }
        
        currentPlayer.setCanPlay(false);
        
        if(!getNextPlayer(currentPlayer).getIsBlocked()) {
            getNextPlayer(currentPlayer).getOutput().println("E sua vez de "
                    + "jogar! Pressione ENTER para ver suas opcoes de jogo"
                    + " e o status atualizado da partida.");
        }
    }
    
    /**
     * This method sends a message to all users about the new card in table.
     *
     * @author Huriel Ferreira Lopes
     * @param currentPlayer the player playing a card
     * @param card the card played
     * @since 1.0
     */
    public void notifyNewCardInTable(Player currentPlayer, Card card) {
        String message = "O jogador " + currentPlayer.getName() + " jogou"
                + " a carta " + Translation.getCardName(card) + "!";
        
        sendMessageToOtherPlayers(currentPlayer, message);
    }
    
    /**
     * This method send a message to all other players but a specific one.
     *
     * @author Huriel Ferreira Lopes
     * @param currentPlayer the player that will not receive the message
     * @param message the message to be sent
     * @since 1.0
     */
    public void sendMessageToOtherPlayers (Player currentPlayer, String message) {
        for (Player anotherPlayer : players) {
            PrintStream chat = (PrintStream) anotherPlayer.getOutput();

            if (chat != currentPlayer.getOutput()) {
                chat.println(message);
            }
        }
    }
    
    /**
     * This method get the next player to play in the current order.
     *
     * @author Huriel Ferreira Lopes
     * @param currentPlayer the current player playing
     * @return the next player in the current order
     * @since 1.0
     */
    public Player getNextPlayer(Player currentPlayer) {
        if(players.indexOf(currentPlayer) != players.size() - 1) {
            return players.get(players.indexOf(currentPlayer) + 1);
        }
        
        return players.get(0);
    }
    
    /**
     * This method sets a color to the Change Color Card.
     *
     * @author Huriel Ferreira Lopes
     * @param player the player that dropped the card
     * @since 1.0
     */
    public void setJokerCardColor(Player player) {
        String userChoice = "";
        
        while(!userChoice.equals("1") && !userChoice.equals("2") 
                && !userChoice.equals("3") && !userChoice.equals("4")) {
            player.getOutput().println("Escolha uma cor para o proximo jogador:\n"
                    + "1 - Amarela\n"
                    + "2 - Azul\n"
                    + "3 - Verde\n"
                    + "4 - Vermelho\n"
                    + "Sua selecao: ");
            
            BufferedReader input;
            try {
                input = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
                
                userChoice = input.readLine();
                if(userChoice != null) {
                    userChoice = userChoice.trim();
                
                    switch (userChoice) {
                        case "1":
                            lastCardInTable.setColor(CardColor.YELLOW);
                            break;
                        case "2":
                            lastCardInTable.setColor(CardColor.BLUE);
                            break;
                        case "3":
                            lastCardInTable.setColor(CardColor.GREEN);
                            break;
                        case "4":
                            lastCardInTable.setColor(CardColor.RED);
                            break;
                        default:
                            player.getOutput().println("A opcao selecionada e invalida! Tente novamente.");
                    }
                }
            } catch (IOException ex) {
                player.getOutput().println("Houve um erro ao ler a opcao selecionada.");
            }
        }
    }
    
    /**
     * This method verifies if the player can play.
     *
     * @author Huriel Ferreira Lopes
     * @param currentPlayer the player to be verified
     * @return if the user can play or not
     * @since 1.0
     */
    public boolean playerCanPlay(Player currentPlayer) {
        if (currentPlayer.getIsBlocked()) {  
            currentPlayer.setIsBlocked(false);
            currentPlayer.setCanPlay(false);
            
            currentPlayer.getOutput().println("\nVoce foi bloqueado em "
                    + "outra rodada e nao pode jogar, por favor, aguarde.\n");
            
            return false;
        } else if(!currentPlayer.getCanPlay()) {
            currentPlayer.getOutput().println("\nAinda nao e sua vez de jogar, "
                    + "por favor, aguarde.\n");
            return false;
        }

        return true;
    }
    
    /**
     * This method checks if a card can be played.
     *
     * @author Huriel Ferreira Lopes
     * @param playerCard the card that the player is trying to play
     * @return if the card can be played or not
     * @since 1.0
     */
    public boolean cardCanBePlayed(Card playerCard) {        
        return playerCard.getColor().equals(CardColor.JOKER) ||
                playerCard.getValue().equals(lastCardInTable.getValue()) ||
                playerCard.getColor().equals(lastCardInTable.getColor());
    }
    
    /**
     * This method adds a player to the list of players.
     *
     * @author Huriel Ferreira Lopes
     * @param player the player to be added
     * @since 1.0
     */
    public void addPlayer(Player player) {
        players.add(player);
        Deck.dealCards(deck, player);
    }
    
    /**
     * This method shows the current card in table.
     *
     * @author Huriel Ferreira Lopes
     * @return the current card in table name
     * @since 1.0
     */
    public String showLastCardInTable() {
        return Translation.getCardName(lastCardInTable);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Card getLastCardInTable() {
        return lastCardInTable;
    }

    public void setLastCardInTable(Card lastCardInTable) {
        this.lastCardInTable = lastCardInTable;
    }

    public List<Card> getUsedCards() {
        return usedCards;
    }

    public boolean getMatchStarted() {
        return matchStarted;
    }

    public void setMatchStarted(boolean matchStarted) {
        this.matchStarted = matchStarted;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }
}
