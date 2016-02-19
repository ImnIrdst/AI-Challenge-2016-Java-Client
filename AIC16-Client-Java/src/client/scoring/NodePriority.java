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
	ENEMY_STRONG(300),

	EMPTY_IN_DANGER(1),
	ALLY_BEAST(1),

	ALLY_STRONG_SAFE(-200),
	ALLY_MEDIOCRE_SAFE(40),
	ALLY_WEAK_SAFE(50),

	ALLY_STRONG_IN_DANGER(-100),
	ALLY_MEDIOCRE_IN_DANGER(40),
	ALLY_WEAK_IN_DANGER(50),

	EMPTY_SAFE(600),

	ENEMY_MEDIOCRE(400),
	ENEMY_WEAK(500);

	int SCORE;

	NodePriority(int score) {
		this.SCORE = score;
	}

	public static void computeAllNodesPriority(World world) {
		for (Node node : world.getMap().getNodes())
			computeNodePriority(node);
	}

	// TODO: Reorder ifs.
	public static void computeNodePriority(Node node) {
		if (isEmptySafe(node)) node.setPriority(EMPTY_SAFE);

		else if (isAllyWeak(node) && !NodeUtils.isInDanger(node)) node.setPriority(ALLY_WEAK_SAFE);
		else if (isAllyMediocre(node) && !NodeUtils.isInDanger(node)) node.setPriority(ALLY_MEDIOCRE_SAFE);
		else if (isAllyStrong(node) && !NodeUtils.isInDanger(node)) node.setPriority(ALLY_STRONG_SAFE);

		else if (isAllyWeak(node) && NodeUtils.isInDanger(node)) node.setPriority(ALLY_WEAK_IN_DANGER);
		else if (isAllyMediocre(node) && NodeUtils.isInDanger(node)) node.setPriority(ALLY_MEDIOCRE_IN_DANGER);
		else if (isAllyStrong(node) && NodeUtils.isInDanger(node)) node.setPriority(ALLY_STRONG_IN_DANGER);

		else if (isEnemyWeak(node)) node.setPriority(ENEMY_WEAK);
		else if (isEnemyMediocre(node)) node.setPriority(ENEMY_MEDIOCRE);
		else if (isEnemyStrong(node)) node.setPriority(ENEMY_STRONG);
	}




	// TODO: this only checks emptiness, add other states.
	public static boolean isEmptySafe(Node node) {
		return NodeUtils.isEmptyNode(node);
	}

	// TODO: check around nodes.
	private static boolean isAllyWeak(Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.computeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.WEAK;
	}

	private static boolean isAllyMediocre(Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.computeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.MEDIOCRE;
	}

	// TODO: check around nodes.
	private static boolean isAllyStrong(Node node) {
		return NodeUtils.isAllyNode(node)
				&& ArmyLevel.computeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.STRONG;
	}

	// TODO: check around nodes.
	private static boolean isEnemyWeak(Node node) {
		return NodeUtils.isEnemyNode(node)
				&& ArmyLevel.computeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.WEAK;
	}

	// TODO: check around nodes.
	private static boolean isEnemyMediocre(Node node) {
		return NodeUtils.isEnemyNode(node)
				&& ArmyLevel.computeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.MEDIOCRE;
	}

	// TODO: check around nodes.
	private static boolean isEnemyStrong(Node node) {
		return NodeUtils.isEnemyNode(node)
				&& ArmyLevel.computeArmyLevel(node) == ArmyLevel.ArmyLevelEnum.STRONG;
	}
}
