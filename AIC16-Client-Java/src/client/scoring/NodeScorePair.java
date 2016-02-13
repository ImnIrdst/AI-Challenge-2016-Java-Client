package client.scoring;

import client.model.Node;

/**
 * Created by iman on 2/14/16.
 * Pairs an Score With a Node
 */
public class NodeScorePair implements Comparable<NodeScorePair> {
	public Node node;
	public long score;

	@Override
	public int compareTo(NodeScorePair o) {
		return Long.compare(score, o.score); // TODO: maybe must be reversed.
	}
}
