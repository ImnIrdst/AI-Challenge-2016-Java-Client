package client.utils;

import client.World;
import client.model.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by al on 2/11/16.
 * <p>
 * Computes Connected sub Graph Parts
 */
public class CGP {


    private static World world;
    private static ArrayList<ArrayList<Node>> mySections;       // Connected sub Graph Parts of my nodes
    private static ArrayList<ArrayList<Node>> enemySections;    // Connected sub Graph Parts of enemy nodes

    //    for attacking system
    private static ArrayList<Node> myFrontLiners;
    private static ArrayList<Node> enemyFrontLiners;
    private static ArrayList<Node> myBackups;
    private static ArrayList<Node> enemyBackups;

    // for debug
    private static final String TAG = "GPS";
    private static final boolean debug = false;

    private static int[][] myCGPsToOthersDistances;
    private static int[][] enemyCGPsToOtherDistances;

    /**
     * this map tells node is in which section
     * <p>
     * nodeSection(3) => 1 means node 3 is in section 1
     */
    private static HashMap<Integer, Integer> myNodeSectionMap;
    private static HashMap<Integer, Integer> enemyNodeSectionMap;

    public static void initialize(World world) {

//        this object should be null for fist time of a cycle
        myFrontLiners = null;
        enemyFrontLiners = null;
        myBackups = null;
        enemyBackups = null;

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

        calculateCGPDistanceToNodes();
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
     * @return list of sub connected graph parts that each contain a ArrayList Node
     */
    public static ArrayList<ArrayList<Node>> getMyCGParts() {
        return mySections;
    }


    /**
     * this function returns enemy sub connected graph parts
     *
     * @return list of sub connected graph parts that each contain a ArrayList Node
     */
    public static ArrayList<ArrayList<Node>> getEnemyCGParts() {
        return enemySections;
    }

    /**
     * this function tells Node n is in witch sub connected graph
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
     * @return list of sub connected graph parts that each contain a ArrayList Node
     */
    public static ArrayList<ArrayList<Node>> getMyCGPBorders() {
        return getCGPBorders(mySections);
    }

    /**
     * this function is same as getEnemyCGParts but only has node which are border nodes
     *
     * @return list of sub connected graph parts that each contain a ArrayList Node
     */
    public static ArrayList<ArrayList<Node>> getEnemyCGPBorders() {
        return getCGPBorders(enemySections);
    }

    /**
     * this function returns minimum distance of Connected Graph which node src is in that
     */
    public static int calculateMyCGPDistance(Node src, Node dst) {
        return myCGPsToOthersDistances[CGP.getPartNumberOfNode(src)][dst.getIndex()];
    }

    /**
     * this function calculates minimum distance of node dst to enemy nodes
     */
    public static int calculateMinEnemyCGPDistance(Node dst) {

        int min = Consts.INF;

        for (int i = 0; i < CGP.getEnemySectionsCount(); i++) {
            min = Math.min(min, enemyCGPsToOtherDistances[i][dst.getIndex()]);
        }

        return min;
    }

    private static void calculateCGPDistanceToNodes() {

        int nodeCount = world.getMap().getNodes().length;
        ArrayList<ArrayList<Node>> myCGParts = CGP.getMyCGParts();
        ArrayList<ArrayList<Node>> enemiesCGParts = CGP.getEnemyCGParts();

        myCGPsToOthersDistances = new int[CGP.getMySectionsCount()][nodeCount];
        enemyCGPsToOtherDistances = new int[CGP.getEnemySectionsCount()][nodeCount];

        for (int i = 0; i < CGP.getMySectionsCount(); i++) {

            for (Node node : world.getMap().getNodes()) { // calculate minimum distance of a part to a node

                int minimumDistance = Consts.INF;
                for (Node myNodes : myCGParts.get(i)) {
                    minimumDistance = Math.min(minimumDistance, APSP.getDist(myNodes.getIndex(), node.getIndex()));
                }
                myCGPsToOthersDistances[i][node.getIndex()] = minimumDistance;
            }
        }


        for (int i = 0; i < CGP.getEnemySectionsCount(); i++) {

            for (Node node : world.getMap().getNodes()) { // calculate minimum distance of a part to a node

                int minimumDistance = Consts.INF;
                for (Node myNodes : enemiesCGParts.get(i)) {
                    minimumDistance = Math.min(minimumDistance, APSP.getDist(myNodes.getIndex(), node.getIndex()));
                }
                enemyCGPsToOtherDistances[i][node.getIndex()] = minimumDistance;
            }
        }
    }


    private static ArrayList<Node> getFrontLiners(ArrayList<ArrayList<Node>> CGPSection) {

        ArrayList<Node> frontLiners = new ArrayList<>();

        for (ArrayList<Node> borderPart : CGPSection) {
            frontLiners.addAll(borderPart.stream().filter(node -> NodeDetails.isEnemyNearby(world, node)).collect(Collectors.toList()));
        }
        return frontLiners;
    }

    private static ArrayList<Node> getAllBackups(ArrayList<ArrayList<Node>> CGPSection) {
        ArrayList<Node> frontLiners = new ArrayList<>();

        for (ArrayList<Node> borderPart : CGPSection) {
            frontLiners.addAll(borderPart.stream().filter(node -> NodeDetails.isAllNeighboursSameAsOwner(node)).collect(Collectors.toList()));
        }

        return frontLiners;
    }


    /**
     * this method get's the "Khate Moghadam" of mine
     *
     * @return arrayList of nodes which have a enemy nearby;
     */
    public static ArrayList<Node> getMyFrontLiners() {
        if (myFrontLiners == null) myFrontLiners = getFrontLiners(getMyCGParts());
        return myFrontLiners;
    }


    /**
     * this method get's the "Khate Moghadam" of enemy ( fuck em hard till they die )
     *
     * @return arrayList of nodes which have a enemy nearby;
     */
    public static ArrayList<Node> getEnemyFrontLiners() {
        if (enemyFrontLiners == null) enemyFrontLiners = getFrontLiners(getEnemyCGParts());
        return enemyFrontLiners;
    }

    /**
     * this method get's the nodes witch don't have a enemy node nearby
     */
    public static ArrayList<Node> getMyBackups() {
        if (myBackups == null) myBackups = getAllBackups(getMyCGParts());
        return myBackups;
    }

    /**
     * this method get's the nodes witch don't have a enemy node nearby
     * the enemy of my enemy node is my node :D
     */
    public static ArrayList<Node> getEnemyBackups() {
        if (enemyBackups == null) enemyBackups = getAllBackups(getEnemyCGParts());
        return enemyBackups;
    }

    /**
     * this method simple tells if my node is in "Khate Moghadam" or is front liner
     */
    public static boolean isMyNodeFrontLiner(Node myNode) {
        return getMyFrontLiners().contains(myNode);
    }


}
