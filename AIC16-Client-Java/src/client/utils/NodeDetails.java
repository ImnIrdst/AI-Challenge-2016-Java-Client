package client.utils;

import client.World;
import client.model.Node;

/**
 * Created by al on 2/13/16.
 */
public class NodeDetails {

    public static boolean isEnemyNode(World world, Node node) {
        return node.getOwner() != world.getMyID() && node.getOwner() != -1;
    }

    public static boolean isEnemyNode(World world, int nodeIndex) {
        return isEnemyNode(world, world.getMap().getNode(nodeIndex));
    }

    public static boolean isMyNode(World world, Node node) {
        return node.getOwner() == world.getMyID();
    }

    public static boolean isMyNode(World world, int nodeIndex) {
        return isMyNode(world, world.getMap().getNode(nodeIndex));
    }

    public static boolean isFreeNode(Node node) {
        return node.getOwner() == -1;
    }

    public static boolean isFreeNode(World world, int nodeIndex) {
        return isFreeNode(world.getMap().getNode(nodeIndex));
    }

    public static boolean isAllNeighboursMine(Node node) {
        return CGP.isNodeBorder(node);
    }
}
