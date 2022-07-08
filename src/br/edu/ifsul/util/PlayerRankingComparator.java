package br.edu.ifsul.util;

import java.util.Comparator;

/**
 * PlayerRankingComparator is meant to help comparing two player ranks
 *
 * @author Huriel Ferreira Lopes
 */
public class PlayerRankingComparator implements Comparator<PlayerRanking> {
    
    public PlayerRankingComparator() {}
    
    @Override
    public int compare(PlayerRanking p1, PlayerRanking p2) {
        return Integer.compare(p2.getWins(), p1.getWins());
    }
}
