package client.pair;

import client.model.Node;
import client.scoring.NodePriority;

/**
 * Created by iman on 2/15/16.
 *
 */
public class NodeIdPriorityPair implements Comparable<NodeIdPriorityPair> {
	public int id;
	NodePriority priority;

	public NodeIdPriorityPair(int id, NodePriority priority) {
		this.id = id;
		this.priority = priority;
	}

	public NodeIdPriorityPair(Node node){
		this.id = node.getIndex();
		this.priority = node.priority;
	}

	@Override
	public int compareTo(NodeIdPriorityPair o) {
		// if (o.priority == null) return 0;
		return o.priority.compareTo(priority);
	}
}
