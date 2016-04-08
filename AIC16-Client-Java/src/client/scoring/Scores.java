package client.scoring;

import client.model.Node;
import client.utils.APSP;
import client.utils.ArmyLevel;
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
	public static final int DANGEROUS_CELL = -100;


	// Coefficients
	public static final int EDGES_COUNT_COEFFICIENT = -11;
	public static final int ENEMY_LEVEL_DIFFERENCE_COEFFICIENT = 50;
	public static final int ATTACKING_DIFFERENCE_COEF = 50;
	public static final int MULTIPLE_ALLYS_NEAR_COEFFICIENT = 85;
	public static final int FARTHEST_EMPTY_CELL_COEFFICIENT = 10;
	public static final int NEAREST_ENEMY_DISTANCE_COEFFICIENT = 20;

	// other
	public static final int DISTANCE_EXPONENTIAL_BASE = 5;

	public static long computeScoresFromPriority(Node source, Node neighbour, Node target) {
		long score = 0;

		// Add NodePriority Scores
		score += source.getPriority().SCORE;

		score += MULTIPLE_ALLYS_NEAR_COEFFICIENT * NodeUtils.getNearestAllyCount(source);



		if (source.getPriority() == NodePriority.EMPTY_SAFE)
			score /= (long) Math.pow(2, APSP.getDist(source, neighbour));

		score += source.getNeighbours().length * EDGES_COUNT_COEFFICIENT;

		// PairingNode with more edges must be captures first


		// Attacking phase.
		if (NodeUtils.isEnemyNode(source) && NodeUtils.isAllyNode(target)) {
			int difference = (target.getArmyCount() - ArmyLevel.getEnemyAndNeighboursApproxArmy(source));
			score += difference;
		}

		// don't go in middle of enemy (TODO: Change with don't engage)
		if (NodeUtils.isInDanger(source)) score += (-1) * NodeUtils.getDangerLevel(source);// + DANGEROUS_CELL ;

		// help border line first, don't lose nodes, loosing nodes == loosing game.
		if (NodeUtils.isAllyNode(source) && NodeUtils.isAllyNode(target)) {
			score += (-1) * NodeUtils.getNearestEnemyDistanceToNode(source) * NEAREST_ENEMY_DISTANCE_COEFFICIENT;
		}

		// Fade with distance.
		score /= (long) Math.pow(DISTANCE_EXPONENTIAL_BASE, APSP.getDist(source, neighbour));


		return score;
	}
}
