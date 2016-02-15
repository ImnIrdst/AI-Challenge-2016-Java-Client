package client.utils;

import client.World;
import client.model.Node;
import client.scoring.NodePriority;

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
}
