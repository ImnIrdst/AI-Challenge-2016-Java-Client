package client;

import client.model.Node;
import client.pair.NodeIdPriorityPair;
import client.scoring.NodePriority;
import client.scoring.NodeScorePair;
import client.scoring.Scoring;
import client.utils.*;

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

		// Compute Nodes Priority
		NodePriority.computeAllNodesPriority(world);


		// Sort by priority
		NodeIdPriorityPair[] ids = new NodeIdPriorityPair[world.getMap().getNodes().length];
		for (int i = 0; i < ids.length; i++) ids[i] = new NodeIdPriorityPair(world.getMap().getNode(i));

		Arrays.sort(ids);

		// compute nodeScores
		Scoring.cleanScoresForAllNodes(world);
		Scoring.computeScoresForAllNodes(world, ids);

		//Log The Ally and Enemy Nodes
		Logger.log(TAG, "Ally Nodes: " + world.getMyNodes().length, true);
		Logger.log(TAG, "Enemy Nodes: " + world.getOpponentNodes().length, true);


		int[] nodeNextMoves = new int[world.getMap().getNodes().length];


		// do moves
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
					+ NodeUtils.getNearestEnemyDistanceToNode(node) + ") " + node.getPriority() + scores, false);
		}

		for (NodeIdPriorityPair id: ids) {
			Node cur = world.getMap().getNode(id.id);

			if (cur.getNeighborScores().length == 0 || !NodeUtils.isAllyNode(cur)) continue;

			NodeScorePair[] neighbourScores = cur.getNeighborScores();
			Arrays.sort(neighbourScores);

			Node target = neighbourScores[0].node;
            for (NodeScorePair neighbour: neighbourScores){
                if (nodeNextMoves[neighbour.node.getIndex()] <= world.getMediumArmyBound()) {
	                target = neighbour.node; break;
                }
            }

			int armyThatMustBeMoved = Math.min(
					cur.getArmyCount(),
					world.getMediumArmyBound() - nodeNextMoves[target.getIndex()] + 1
			);

//            if (NodeUtils.isEnemyNode(target))
//                // Attacking
//                world.moveArmy(cur, target, cur.getArmyCount());
//            else
			// non Attacking
			world.moveArmy(cur, target, armyThatMustBeMoved);

			nodeNextMoves[target.getIndex()] += armyThatMustBeMoved;

			// If Target is an empty node cancel its affect on other nodes.
			if (NodeUtils.isEmptyNode(target))
				Scoring.computeAffectOfANodeToAllAllyNodes(world, target, -1);
		}

		System.out.println("=======================================================");
	}

}
