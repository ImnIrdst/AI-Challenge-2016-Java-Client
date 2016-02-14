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

    public static boolean isAllNeighboursSameAsOwner(Node node) {
        return CGP.isNodeBorder(node);
    }

    public static boolean isAllNeighboursMine(Node node){
        return isAllNeighboursSameAsOwner(node);
    }

    /**
     * this method checks if is a enemy of node source is nearby
     * works for both my node and enemy node
     * this means if its my node and a enemy node is nearby returns true
     * and if it is enemies node and at least one of my node is nearby returns true
     * @return a boolean value which tells if a enemy of node source is nearby
     */
    public static boolean isEnemyNearby(World world, Node node) {
        for (Node neighbour : node.getNeighbours()) {
            if (isMyNode(world, node) && isEnemyNode(world, neighbour))
                return true;
            if (isEnemyNode(world, node) && isMyNode(world, neighbour))
                return true;
        }
        return false;
    }
}
