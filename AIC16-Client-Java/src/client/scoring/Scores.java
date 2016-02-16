package client.scoring;

import client.model.Node;
import client.utils.APSP;
import client.utils.ArmyLevel;
import client.utils.CGP;
import client.utils.NodeUtils;

/**
 * Created by iman on 2/14/16.
 * <p>
 * Score Measures in different situations
 */
public class Scores {
	public static final int EMPTY_SAFE_NODE_WITH_DIRECT_EDGE = 100;

	// Scores
	public static final int BORDER_ALLYS_SCORE = 400;

	// Coefficients
	public static final int EDGES_COUNT_COEFFICIENT = 10;
	public static final int ENEMY_LEVEL_DIFFERENCE_COEFFICIENT = 50;

	// other
	public static final int DISTANCE_EXPONENTIAL_BASE = 10;

	public static long computeScoresFromPriority(Node source, Node neighbour, Node target) {
		long diff, score = 0;

		// Add NodePriority Scores
		score += source.getPriority().SCORE;

		// Node with more edges must be captures first
		score += source.getNeighbours().length * EDGES_COUNT_COEFFICIENT;

		// Attacking phase.
		if (NodeUtils.isEnemyNode(source) && NodeUtils.isAllyNode(target)) {

			diff = ArmyLevel.getEnemyAndNeighboursApproxArmy(target) - source.getArmyCount();

			score += diff * source.getPriority().SCORE;
		}


//		if (NodeUtils.isAllyNode(source) &&
//				NodeUtils.isAllyNode(target) && CGP.isNodeBorder(source)) {
//			score += BORDER_ALLYS_SCORE /
//					(long) Math.pow(DISTANCE_EXPONENTIAL_BASE, NodeUtils.getNearestEnemyDistanceToAllyNode(source));
//		}
		// Supporting phase
//		if (NodeUtils.isAllyNode(source) && NodeUtils.isAllyNode(target) && !NodePriority.isInDanger(source)){
//
//		}
		// Fade with distance.
		score /= (long) Math.pow(DISTANCE_EXPONENTIAL_BASE, APSP.getDist(source, neighbour));


		return score;
	}
}
