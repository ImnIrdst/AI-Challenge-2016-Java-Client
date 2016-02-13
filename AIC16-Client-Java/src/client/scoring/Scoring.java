package client.scoring;

import client.World;
import client.model.Node;

/**
 * Created by iman on 2/12/16.
 * 
 */
public class Scoring {

	public static void computeScoresForAllNodes(World world){
		for (Node u : world.getMap().getNodes()){
			computeSelfNeed(u);
			for (Node v : world.getMyNodes()){
				computeAffect(u, v);
			}
		}
	}

	/**
	 * Computes affect of node source on node target and fills the target.neighborsScore[]
	 * @param source: one that affects.
	 * @param target: one that updates.
	 */
	public static void computeAffect(Node source, Node target) {
		// if target is one of the source neighbors
		for (NodeScorePair neighbor: target.getNeighborScores()){
			neighbor.score += Scores.computeScoresFromPriority(source, neighbor.node);
		}
	}



	/**
	 * Compute Self Need
	 */
	public static void computeSelfNeed(Node u){

	}
}
