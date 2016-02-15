package client.scoring;

import client.World;
import client.model.Node;
import client.utils.ArmyLevel;
import client.utils.NodeUtils;

/**
 * Created by iman on 2/12/16.
 *
 */
public enum NodePriority {
	ALLY_BEAST(1),
	ALLY_WEAK(1),
	ALLY_MEDIOCRE(1),
	ALLY_STRONG(1),
	ALLY_SAFE(1),
	ALLY_ON_EDGE(1),
	ALLY_IN_DANGER(1),
	EMPTY_SAFE(200),
	EMPTY_IN_DANGER(1),
	ENEMY_WEAK(80),
	ENEMY_MEDIOCRE(80),
	ENEMY_STRONG(80);


	int score;

	NodePriority(int score) {
		this.score = score;
	}

	public static void computeAllNodesPriority(World world){
		for (Node node: world.getMap().getNodes())
			computeNodePriority(world, node);
	}

	// TODO: Reorder ifs.
	public static void computeNodePriority(World world, Node node){
		if (isEmptySafe(node)) node.priority = EMPTY_SAFE;
		else if (isAllyWeak(node)) node.priority = ALLY_WEAK;
		else if (isAllyMediocre(node)) node.priority = ALLY_MEDIOCRE;
		else if (isAllyStrong(node)) node.priority = ALLY_STRONG;
		else if (isEnemyWeak(node)) node.priority = ENEMY_WEAK;
		else if (isEnemyMediocre(node)) node.priority = ENEMY_MEDIOCRE;
		else if (isEnemyStrong(node)) node.priority = ENEMY_STRONG;
		else node = node;
	}



	// TODO: this only checks emptiness, add other states.
	public static boolean isEmptySafe(Node node){
		return NodeUtils.isEmptyNode(node);
	}

	// TODO: check around nodes.
	private static boolean isAllyWeak(Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.ComputeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.Low;
	}

	private static boolean isAllyMediocre(Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.ComputeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.Medium;
	}

	// TODO: check around nodes.
	private static boolean isAllyStrong(Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.ComputeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.High;
	}

	// TODO: check around nodes.
	private static boolean isEnemyWeak(Node node) {
		return NodeUtils.isEnemyNode(node)
				&& ArmyLevel.ComputeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.Low;
	}

	// TODO: check around nodes.
	private static boolean isEnemyMediocre(Node node) {
		return NodeUtils.isEnemyNode(node)
				&& ArmyLevel.ComputeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.Medium;
	}

	// TODO: check around nodes.
	private static boolean isEnemyStrong(Node node) {
		return NodeUtils.isEnemyNode(node)
				&& ArmyLevel.ComputeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.High;
	}
}
