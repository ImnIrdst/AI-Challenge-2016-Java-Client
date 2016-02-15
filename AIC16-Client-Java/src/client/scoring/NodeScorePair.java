package client.scoring;

import client.model.Node;

/**
 * Created by iman on 2/14/16.
 * Pairs an Score With a Node
 */
public class NodeScorePair implements Comparable<NodeScorePair> {
	public Node node;
	public long score;

	public NodeScorePair(Node node, long score) {
		this.node = node;
		this.score = score;
	}

	@Override
	public String toString() {
		return "(" + node.getIndex() + ", " + score + ") ";
	}

	@Override
	public int compareTo(NodeScorePair o) {
		return Long.compare(o.score, score); // TODO: maybe must be reversed.
	}
}
