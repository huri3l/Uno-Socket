package br.edu.ifsul.util;

import br.edu.ifsul.model.Card;
import br.edu.ifsul.model.CardColor;
import br.edu.ifsul.model.CardValue;

/**
 * Translation is meant to translate items to Portuguese, end-user's main language
 * 
 * @author Huriel Ferreira Lopes
 */
public class Translation {
    
    /**
     * This method translates a Card info to a friendly-user format
     *
     * @param card is the Card to be translated
     * @return a String with the Portuguese Card Name
     * @since 1.0
     */
    public static String getCardName(Card card) {
        StringBuilder sb = new StringBuilder();
        
        switch (card.getValue()) {
            case BLOCK:
                sb.append("Bloquear");
                break;
            case CHANGE_COLOR:
                sb.append("Troca Cor");
                break;
            case DRAW_FOUR:
                sb.append("Mais Quatro");
                break;
            case DRAW_TWO:
                sb.append("Mais Dois");
                break;
            case EIGHT:
                sb.append("Oito");
                break;
            case FIVE:
                sb.append("Cinco");
                break;
            case FOUR:
                sb.append("Quatro");
                break;
            case NINE:
                sb.append("Nove");
                break;
            case ONE:
                sb.append("Um");
                break;
            case REVERSE:
                sb.append("Inverte");
                break;
            case SEVEN:
                sb.append("Sete");
                break;
            case SIX:
                sb.append("Seis");
                break;
            case THREE:
                sb.append("Tres");
                break;
            case TWO:
                sb.append("Dois");
                break;
            case ZERO:
                sb.append("Zero");
                break;
            default:
                break;
        }
        
        switch (card.getColor()) {
            case BLUE:
                sb.append(" Azul");
                break;
            case GREEN:
                sb.append(" Verde");
                break;
            case RED:
                sb.append(" Vermelho");
                break;
            case YELLOW:
                sb.append(" Amarelo");
                break;
            default:
                break;
        }
        
        return sb.toString();
    }
    
    /**
     * This method translates a Card Color to a friendly-user format
     *
     * @param cardColor is the Card Color to be translated
     * @return a String with the Portuguese Card Color
     * @since 1.0
     */
    public static String getColor(CardColor cardColor) {
        StringBuilder sb = new StringBuilder();
        
        switch (cardColor) {
            case BLUE:
                sb.append("Azul");
                break;
            case GREEN:
                sb.append("Verde");
                break;
            case RED:
                sb.append("Vermelho");
                break;
            case YELLOW:
                sb.append("Amarelo");
                break;
            default:
                break;
        }
        
        return sb.toString();
    }
}
