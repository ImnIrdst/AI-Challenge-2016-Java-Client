package client.utils;

import client.World;
import client.model.Node;

/**
 * Created by iman on 2/12/16.
 *
 */
public class NodeUtils {
	public static World world;

	public static void initialize(World world){ NodeUtils.world = world; }

	/**
	 * checks if node v exists in u.neighbors list.
	 * @return v index in neighbors list if its exists and -1 if its not exists.
	 */ // TODO: Write an unit Test for This
	public static int isInNeighbors(Node u, Node v){
		for (int i=0 ; i<u.getNeighbours().length ; i++)
			if (u.getNeighbours()[i].getIndex() == v.getIndex()) return i;
		return -1;
	}

	public static boolean isAllyNode(Node node){
		return node.getOwner() == world.getMyID();
	}

	public static boolean isEnemyNode(Node node){
		return node.getOwner() == 1 - world.getMyID();
	}

	public static boolean isEmptyNode(Node node) {
		return !isAllyNode(node) && !isEnemyNode(node);
	}

	public static boolean isBoundaryNode(Node node) {
		for (Node neighbour : node.getNeighbours())
			if (isEmptyNode(neighbour)) return true;
		return false;
	}

	public static boolean isBorderLineNode(Node node){
		for (Node neighbour : node.getNeighbours())
			if (isEmptyNode(neighbour) || isEnemyNode(neighbour)) return true;
		return false;
	}

	public static boolean isInDanger(Node node) {
		return ArmyLevel.isEnemyAndNeighboursApproxStrong(node);
	}

	public static int getNearestAllyCount(Node node){
		int minDistance = getNearestAllyDistanceToNode(node);

		int result = 0;
		for (Node ally : world.getMyNodes())
			if (minDistance == APSP.getDist(node, ally)) result++;

		return result;
	}

	public static int getNearestEnemyDistanceToNode(Node node){
		int minDistance = Consts.INF;
		for (Node enemy : world.getOpponentNodes())
			minDistance = Math.min(minDistance, APSP.getDist(node, enemy));
		return minDistance;
	}

	public static int getNearestAllyDistanceToNode(Node node){
		int minDistance = Consts.INF;
		for (Node ally : world.getMyNodes())
			minDistance = Math.min(minDistance, APSP.getDist(node, ally));
		return minDistance;
	}

	public static int countAllyNodesInDistance(Node node, int distance){
		int count = 0;
		for (Node ally : world.getMyNodes())
			if (APSP.getDist(node, ally) == distance) count++;
		return count;
	}

	public static long getDangerLevel(Node node) {
		return ArmyLevel.getEnemyAndNeighboursApproxArmy(node);
	}

	public static int getEmptyNodesCount(){
		int cnt = 0;
		for (Node node : world.getMap().getNodes())
			if (NodeUtils.isEmptyNode(node)) cnt++;
		return cnt;
	}

	public static int getMyBoundaryNodesCount(){
		int cnt = 0;
		for (Node node : world.getMyNodes())
			if (NodeUtils.isBoundaryNode(node)) cnt++;
		return cnt;
	}
}
