package client;

import client.model.Node;
import client.pair.NodeIdPriorityPair;
import client.scoring.NodePriority;
import client.scoring.NodeScorePair;
import client.scoring.Scoring;
import client.utils.*;
import common.util.Log;
import jdk.nashorn.internal.runtime.WithObject;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;

import java.util.Arrays;

/**
 * AI class.
 * You should fill body of the method {@link #doTurn}.
 * Do not change name or modifiers of the methods or fields
 * and do not add constructor for this class.
 * You can add as many methods or fields as you want!
 * Use world parameter to access and modify game's
 * world!
 * See World interface for more details.
 */
public class AI {
	private String TAG = "AI";
	private boolean isInitialized = false;

	private void initialize(World world) {
		if (isInitialized) return;

		Logger.log(TAG, "My ID: " + world.getMyID(), true);
		Logger.log(TAG, "Map Levels: " + world.getLowArmyBound() + ", " + world.getMediumArmyBound(), true);

		isInitialized = true;
		APSP.initialize(world);
		CGP.initialize(world);
		NodeUtils.initialize(world);
		ArmyLevel.initialize(world);
	}

	public void doTurn(World world) {
		initialize(world);

		// fill this method, we've presented a stupid AI for example!

		// fill this method,
		NodePriority.computeAllNodesPriority(world);


		// Sort by priority
		NodeIdPriorityPair[] ids = new NodeIdPriorityPair[world.getMap().getNodes().length];
		for (int i = 0; i < ids.length; i++) ids[i] = new NodeIdPriorityPair(world.getMap().getNode(i));

		Arrays.sort(ids);

		// compute nodeScores
		Scoring.cleanScoresForAllNodes(world);
		Scoring.computeScoresForAllNodes(world, ids);

		//Log the Mine and Enemy Nodes
		Logger.log(TAG, "Ally Nodes: " + world.getMyNodes().length, true);
		Logger.log(TAG, "Enemy Nodes: " + world.getOpponentNodes().length, true);

		for (NodeIdPriorityPair id : ids) {
			Node node = world.getMap().getNode(id.id);

			String owner = "";
			if (NodeUtils.isAllyNode(node)) owner = "Ally ";
			if (NodeUtils.isEnemyNode(node)) owner = "Enemy ";
			if (NodeUtils.isEmptyNode(node)) owner = "Empty ";
			if (NodeUtils.isEmptyNode(node)) continue;

			String scores = "[ ";
			for (NodeScorePair neighbour : node.getNeighborScores()) {
				scores += neighbour.node.getArmyCount() + " " + neighbour.score + ", ";
			}
			scores += "]";

			Logger.log(TAG, owner + " Node: (" + node.getArmyCount() + ", "
					+ NodeUtils.getNearestEnemyDistanceToAllyNode(node) + ") " + node.getPriority() + scores, false);
		}

		for (Node cur : world.getMap().getNodes()) {
			if (cur.getNeighborScores().length == 0 || !NodeUtils.isAllyNode(cur)) continue;

			NodeScorePair[] neighbourScores = cur.getNeighborScores();
			Arrays.sort(neighbourScores);

			Node target = neighbourScores[0].node;


			// non Attacking
			world.moveArmy(cur, target, getMinimumToAttack(world, cur, target));


			// If Target is an empty node cancel its affect on other nodes.
			if (NodeUtils.isEmptyNode(target))
				Scoring.computeAffectOfANodeToAllAllyNodes(world, target, -1);
		}

		System.out.println("=======================================================");
	}

	public static int getMinimumToAttack(World world, Node source, Node target) {
		return source.getArmyCount() - source.getSelfNeed();
//		if (!NodeUtils.isEnemyNode(target)
//				|| (ArmyLevel.computeArmyLevel(source).ordinal() <= ArmyLevel.computeArmyLevel(target).ordinal()
//					&& !ArmyLevel.isStrong(source)))
//			return source.getArmyCount() - source.getSelfNeed();
//
//		if(source.getArmyCount() > world.getMediumArmyBound() * 2)
//			return source.getArmyCount() / 2;
//
//		switch (ArmyLevel.computeArmyLevel(target)) {
//			case STRONG:
//				return (int) (Math.min(world.getMediumArmyBound() * 1.5, source.getArmyCount()));
//			case MEDIOCRE:
//				return world.getMediumArmyBound();
//			case WEAK:
//				return world.getLowArmyBound();
//		}
//
//		return 0;
	}
}
