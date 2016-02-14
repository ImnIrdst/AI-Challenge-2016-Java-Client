package client.utils;

import client.World;
import client.model.Graph;
import client.model.Node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

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

    public static boolean isAllNeighboursMine(Node node) {
        return isAllNeighboursSameAsOwner(node);
    }

    /**
     * this method checks if is a enemy of node source is nearby
     * works for both my node and enemy node
     * this means if its my node and a enemy node is nearby returns true
     * and if it is enemies node and at least one of my node is nearby returns true
     *
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

    /**
     * compute if enemy is moving from a node
     */
    public static boolean isEnemyMoving(World currentWorld, Graph lastState, Node enemyNode) {

        int lastArmyLevel = ArmyLevel.ComputeArmyLevel(currentWorld,
                lastState.getNode(enemyNode.getIndex())).ordinal();

        int currentArmyLevel = ArmyLevel.ComputeArmyLevel(currentWorld, enemyNode).ordinal();

        return lastArmyLevel > currentArmyLevel;

    }

    /**
     * compute last army level of a node
     */
    public static ArmyLevel.ArmyLevelEnum lastArmyLevel(World lastWorld, Node node) {
        return ArmyLevel.ComputeArmyLevel(lastWorld, lastWorld.getMap().getNode(node.getIndex()));
    }

    public static int getCombinedArmyCount(World world, Node enemyNode) {

        int count = 0;
        for (Node node : enemyNode.getNeighbours()) {

            if (isMyNode(world, node)) {
                count += node.getArmyCount();
            }
        }
        return count;
    }

    public static boolean amIBetterThan(World world, Node myNode, Node enemyNode) {
        int myArmyLevel = ArmyLevel.ComputeArmyLevel(world, myNode).ordinal();
        int enemyArmyLevel = ArmyLevel.ComputeArmyLevel(world, enemyNode).ordinal();
        if (myArmyLevel > enemyArmyLevel)
            return true;
        else if (myArmyLevel == enemyArmyLevel
                && myArmyLevel == ArmyLevel.ArmyLevelEnum.High.ordinal()
                && myNode.getArmyCount() > 85)
            return true;
        return false;
    }


    public static boolean amIBetterCombinedToAttack(World world, Node enemyNode) {
        Node tmpNode = new Node(85);
        tmpNode.setArmyCount(getCombinedArmyCount(world, enemyNode));
        return amIBetterThan(world, tmpNode, enemyNode);

    }

    public static TreeSet<Node> getSortedNearbyMyNodes(World world, Node enemyNode) {


        TreeSet<Node> nodeTreeSet = new TreeSet<>(
                (Comparator<Node>) (o1, o2) -> Integer.compare(o1.getArmyCount(), o2.getArmyCount())
        );

        for (Node node : enemyNode.getNeighbours())
            if (isMyNode(world, node))
                nodeTreeSet.add(node);

        return nodeTreeSet;
    }
}
