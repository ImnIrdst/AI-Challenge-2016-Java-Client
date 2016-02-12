package client.utils;

import java.util.Comparator;

/**
 * Created by al on 2/12/16.
 */
public class ScoreHolder implements Comparable<ScoreHolder> {

    private int srcIndex;
    private int dstIndex;


    private int score;

    ScoreHolder(int srcIndex, int dstIndex, int score) {
        this.score = score;
        this.srcIndex = srcIndex;
        this.dstIndex = dstIndex;
    }

    @Override
    public int compareTo(ScoreHolder scoreHolder) {
        return Integer.compare(score, scoreHolder.score);
    }

    public int getSrcIndex() {
        return srcIndex;
    }

    public int getDstIndex() {
        return dstIndex;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "src:" + srcIndex + " dst:" + dstIndex + " score:" + score;
    }


}
