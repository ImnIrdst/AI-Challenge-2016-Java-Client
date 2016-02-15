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
	public static final int DISTANCE_EXPONENTIAL_BASE = 10;

	public static long computeScoresFromPriority(Node source, Node neighbour, Node target) {
		long score = 0;
		score += source.priority.score * source.getNeighbours().length;


		if (NodeUtils.isEnemyNode(source) && NodeUtils.isAllyNode(target)
				&& ArmyLevel.ComputeArmyLevel(source).ordinal() > ArmyLevel.ComputeArmyLevel(target).ordinal()){

			int diff = ArmyLevel.ComputeArmyLevel(source).ordinal() - ArmyLevel.ComputeArmyLevel(target).ordinal();

			score += (diff * 10) * source.priority.score;
		}
		score /= (long) Math.pow(DISTANCE_EXPONENTIAL_BASE, APSP.getDist(source, neighbour));
		return score;
	}
}
