package client.scoring;

import client.World;
import client.model.Node;
import client.pair.NodeIdPriorityPair;
import client.utils.ArmyLevel;
import client.utils.Logger;
import client.utils.NodeUtils;

/**
 * Created by iman on 2/12/16.
 */
public class Scoring {

	private static String TAG = "Scoring: ";

	public static void cleanScoresForAllNodes(World world) {
		for (Node node : world.getMap().getNodes()) {
			node.setSelfNeed(0);
			for (NodeScorePair neighbour : node.getNeighborScores()) {
				neighbour.score = 0;
			}
		}
	}

	public static void computeScoresForAllNodes(World world, NodeIdPriorityPair[] ids) {
		for (NodeIdPriorityPair id : ids) {
			Node u = world.getMap().getNode(id.id);
			computeSelfNeed(u);
			computeAffectOfANodeToAllAllyNodes(world, u, +1);
		}
	}

	/**
	 * computes affect of a node to all ally nodes.
	 *
	 * @param sign define that effect is positive or negative(cancellation)
	 */
	public static void computeAffectOfANodeToAllAllyNodes(World world, Node u, int sign) {
		for (Node v : world.getMyNodes()) computeAffect(u, v, sign);
	}

	/**
	 * Computes affect of node source on node target and fills the target.neighborsScore[]
	 *
	 * @param source: one that affects.
	 * @param target: one that updates.
	 */
	public static void computeAffect(Node source, Node target, int sign) {

		for (NodeScorePair neighbor : target.getNeighborScores()) {
			int score = 0;

			score += Scores.computeScoresFromPriority(source, neighbor.node, target);

			neighbor.score += sign * score;
		}
	}


	/**
	 * Computes how much of an node army must be remained in itself.
	 * Only can be computed for ally nodes.
	 */
	public static void computeSelfNeed(Node node) {
		if (!NodeUtils.isAllyNode(node)) return;
//		Logger.log(TAG,
//				"EmptyNodes: " + NodeUtils.getEmptyNodesCount() + " " +
//				"MyBoundaryNodes: " + NodeUtils.getMyBoundaryNodesCount(), false);
//		if (NodeUtils.getEmptyNodesCount() <= NodeUtils.getMyBoundaryNodesCount()){
		if (ArmyLevel.canProduceStrongArmy(node))
			node.setSelfNeed(node.getArmyCount() - ArmyLevel.getExceedStrongArmy(node));
//			else
//				node.setSelfNeed(node.getArmyCount()/2);
//		}
	}
}

