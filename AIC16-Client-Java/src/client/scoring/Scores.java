package client.scoring;

import client.model.Node;
import client.utils.APSP;

/**
 * Created by iman on 2/14/16.
 *
 * Score Measures in different situations
 */
public class Scores {
	public static final int EMPTY_SAFE_NODE_WITH_DIRECT_EDGE = 100;

	public static long computeScoresFromPriority(Node source, Node target) {
		return source.priority.score / ((long) Math.pow(10, APSP.getDist(source, target)));
	}
}
