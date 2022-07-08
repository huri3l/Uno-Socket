package br.edu.ifsul.util;

/**
 * PlayerRanking is meant to hold the wins of a player
 *
 * @author Huriel Ferreira Lopes
 */
public class PlayerRanking {
    private String name;
    private Integer wins;
    
    public PlayerRanking() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }    
}
