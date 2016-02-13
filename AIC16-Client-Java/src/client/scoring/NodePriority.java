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
	ALLY_STRONG(1),
	ALLY_SAFE(1),
	ALLY_ON_EDGE(1),
	EMPTY_SAFE(100),
	EMPTY_IN_DANGER(1),
	ENEMY_WEAK(1),
	ENEMY_MEDIOCRE(1),
	ENEMY_STRONG(1),
	ALLY_IN_DANGER(1);

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
		if (isEmptySafe(world, node)) node.priority = EMPTY_SAFE;
		else if (isAllyWeak(world, node)) node.priority = ALLY_WEAK;
		else if (isAllyStrong(world, node)) node.priority = ALLY_STRONG;
		else if (isEnemyWeak(world , node)) node.priority = ENEMY_WEAK;
		else if (isEnemyMediocre(world, node)) node.priority = ENEMY_MEDIOCRE;
		else if (isEnemyStrong(world, node)) node.priority = ENEMY_STRONG;
	}
	// TODO: this only checks emptiness, add other states.
	public static boolean isEmptySafe(World world, Node node){
		return NodeUtils.isEmptyNode(node);
	}

	// TODO: check around nodes.
	private static boolean isAllyWeak(World world, Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.ComputeArmyLevel(world, node) == ArmyLevel.ArmyLevelEnum.Low;
	}

	// TODO: check around nodes.
	private static boolean isAllyStrong(World world, Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.ComputeArmyLevel(world, node) == ArmyLevel.ArmyLevelEnum.High;
	}

	// TODO: check around nodes.
	private static boolean isEnemyWeak(World world, Node node) {
		return NodeUtils.isEmptyNode(node)
				&& ArmyLevel.ComputeArmyLevel(world, node) == ArmyLevel.ArmyLevelEnum.Low;
	}

	// TODO: check around nodes.
	private static boolean isEnemyMediocre(World world, Node node) {
		return NodeUtils.isEmptyNode(node)
				&& ArmyLevel.ComputeArmyLevel(world, node) == ArmyLevel.ArmyLevelEnum.Medium;
	}

	// TODO: check around nodes.
	private static boolean isEnemyStrong(World world, Node node) {
		return NodeUtils.isEmptyNode(node)
				&& ArmyLevel.ComputeArmyLevel(world, node) == ArmyLevel.ArmyLevelEnum.High;
	}
}
