package client.utils;

import client.World;
import client.model.Node;

import java.util.*;

/**
 * Created by al on 2/11/16.
 * <p>
 * Computes Connected sub Graph Parts
 */
public class CGP {

    private static World world;
    private static ArrayList<ArrayList<Node>> mySections;       // Connected sub Graph Parts of my nodes
    private static ArrayList<ArrayList<Node>> enemySections;    // Connected sub Graph Parts of enemy nodes
    private static final String TAG = "GPS";
    private static final boolean debug = false;

    /**
     * this map tells node is in which section
     * <p>
     * nodeSection(3) => 1 means node 3 is in section 1
     */
    private static HashMap<Integer, Integer> myNodeSectionMap;
    private static HashMap<Integer, Integer> enemyNodeSectionMap;

    public static void initialize(World world) {
        CGP.world = world;
        mySections = new ArrayList<>();
        enemySections = new ArrayList<>();
        myNodeSectionMap = new HashMap<>();
        enemyNodeSectionMap = new HashMap<>();
        computeSections();
        String mySectionsToString = "\n";
        int temp = 0;
        for (ArrayList<Node> section : mySections) {
            mySectionsToString += temp + " ";
            temp++;
            for (Node n : section) {
                mySectionsToString += " " + n.getIndex() + "," + n.getArmyCount();
            }
            mySectionsToString += "\n";
        }
        Logger.log("my" + TAG, mySectionsToString, debug);
        Logger.log("enemy" + TAG, enemySections.toString(), debug);
    }

    /**
     * computes Connected Graph Parts based on world map and fills section using BFS algorithm
     * TODO this algorithm add the nodes witch will be mine in next cycle to the section maybe this is a bad thing
     */
    private static void doBFS(Node[] nodes,
                              HashMap<Integer, Integer> nodeSectionsMap,
                              ArrayList<ArrayList<Node>> section) {

        Queue<Node> queue = new LinkedList<>();
        int[] vis = new int[world.getMap().getNodes().length];
        for (Node s : nodes) {
            if (vis[s.getIndex()] == 1)  // this node has been assigned to a section
                continue;
            queue.clear();
            queue.add(s);
            vis[s.getIndex()] = 1;

            if (!nodeSectionsMap.containsKey((s.getIndex()))) { // assign a section to this node
                nodeSectionsMap.put(s.getIndex(), section.size());
                ArrayList<Node> thisSection = new ArrayList<>();
                thisSection.add(s);
                section.add(thisSection);
            }

            while (!queue.isEmpty()) {
                Node u = queue.poll();
                for (Node v : u.getNeighbours()) { // v.getNeighbours() can't return null
                    if (vis[v.getIndex()] == 0 && s.getOwner() == v.getOwner()) {
                        queue.add(v);
                        vis[v.getIndex()] = 1;
                        int sectionNumber = nodeSectionsMap.get(s.getIndex());
                        nodeSectionsMap.put(v.getIndex(), sectionNumber);
                        section.get(sectionNumber).add(v);
                    }
                }
            }
        }
    }

    private static void computeSections() { // computes my sections and enemy sections

        doBFS(world.getMyNodes(), myNodeSectionMap, mySections);
        doBFS(world.getOpponentNodes(), enemyNodeSectionMap, enemySections);

    }

    public static boolean isNodeBorder(Node node) {
        for (Node neighbor : node.getNeighbours()) {

            // if neighbor owner is not node's owner this means it is not in border
            if (neighbor.getOwner() != node.getOwner())
                return true;
        }
        return false;
    }

    public static int getMySectionsCount() {
        return mySections.size();
    }

    public static int getEnemySectionsCount() {
        return enemySections.size();
    }

    /**
     * this function returns my sub connected graph parts
     *
     * @return list of sub connected graph parts that each contain a ArrayList PairingNode
     */
    public static ArrayList<ArrayList<Node>> getMyCGParts() {
        return mySections;
    }


    /**
     * this function returns enemy sub connected graph parts
     *
     * @return list of sub connected graph parts that each contain a ArrayList PairingNode
     */
    public static ArrayList<ArrayList<Node>> getEnemyCGParts() {
        return enemySections;
    }

    /**
     * this function tells PairingNode n is in witch sub connected graph
     *
     * @param node
     * @return the index of subconnected graph
     */
    public static int getPartNumberOfNode(Node node) {
        if (node.getOwner() == world.getMyID()) { // if this node is mine , then it is in myNodeSectionMap
            return myNodeSectionMap.get(node.getIndex());
        }
        return enemyNodeSectionMap.get(node.getIndex());
    }

    //    return a list of border nodes
    private static ArrayList<ArrayList<Node>> getCGPBorders(ArrayList<ArrayList<Node>> section) {
        ArrayList<ArrayList<Node>> answer = new ArrayList<>();

        for (ArrayList<Node> list : section) {

            ArrayList<Node> newList = new ArrayList<>(); // this sections bodernodes will be added in this arraylist
            answer.add(newList);

            for (Node n : list) {
                if (isNodeBorder(n)) {
                    newList.add(n);
                }
            }
        }
        return answer;
    }

    /**
     * this function is same as getMyCGParts but only has node which are border nodes
     *
     * @return list of sub connected graph parts that each contain a ArrayList PairingNode
     */
    public static ArrayList<ArrayList<Node>> getMyCGPBorders() {
        return getCGPBorders(mySections);
    }

    /**
     * this function is same as getEnemyCGParts but only has node which are border nodes
     *
     * @return list of sub connected graph parts that each contain a ArrayList PairingNode
     */
    public static ArrayList<ArrayList<Node>> getEnemyCGPBorders() {
        return getCGPBorders(enemySections);
    }


}
