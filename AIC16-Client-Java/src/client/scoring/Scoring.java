package client.scoring;

import client.World;
import client.model.Node;
import client.pair.NodeIdPriorityPair;
import client.utils.NodeUtils;

/**
 * Created by iman on 2/12/16.
 * 
 */
public class Scoring {

	public static void computeScoresForAllNodes(World world, NodeIdPriorityPair[] ids){
		for (NodeIdPriorityPair id : ids){
			Node u = world.getMap().getNode(id.id);
			computeSelfNeed(u);
			computeAffectOfANodeToAllAllyNodes(world, u, +1);
		}
	}

	/**
	 * computes affect of a node to all ally nodes.
	 * @param sign define that effect is positive or negative(cancellation)
	 */
	public static void computeAffectOfANodeToAllAllyNodes(World world, Node u, int sign){
		for (Node v : world.getMyNodes()) computeAffect(u, v, sign);
	}

	/**
	 * Computes affect of node source on node target and fills the target.neighborsScore[]
	 * @param source: one that affects.
	 * @param target: one that updates.
	 */
	public static void computeAffect(Node source, Node target, int sign) {

		for (NodeScorePair neighbor: target.getNeighborScores()){
			int score = 0;

			score += Scores.computeScoresFromPriority(source, neighbor.node, target);

			neighbor.score += sign * score;
		}
	}



	/**
	 * Computes how much of an node army must be remained in itself.
	 * Only can be computed for ally nodes.
	 */
	public static void computeSelfNeed(Node u){
		if(!NodeUtils.isAllyNode(u)) return;

	}
}
