package br.edu.ifsul.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Card is meant to be a card of Uno
 * It holds informations about the value and the color of the Card itself
 * 
 * @author Huriel Ferreira Lopes
 */
public class Card implements Serializable {
    private CardColor color;
    private CardValue value;
    
    public Card(CardColor color, CardValue value) {
        this.color = color;
        this.value = value;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public CardValue getValue() {
        return value;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.color);
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.color != other.color) {
            return false;
        }
        return this.value == other.value;
    }
}
