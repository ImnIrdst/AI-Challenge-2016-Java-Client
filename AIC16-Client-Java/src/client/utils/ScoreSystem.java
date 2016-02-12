package client.utils;

import client.World;
import client.model.Node;

import java.util.*;

/**
 * Created by al on 2/12/16.
 */
public class ScoreSystem {

    private static World world;
    private static int nodeCount;
    //    private static ArrayList<ScoreHolder> scores; //score[i][j] => score node[j] from node i point of view
    private static int[][] myCGPsToOthersDistances;
    private static int[][] enemyCGPsToOtherDistances;
    private static final boolean debug = true;
    private static final String TAG = "ScoreSystem";

    /**
     * this map tells node and it score list to all destinations
     * for example scoreMap(3) => sorted ArrayList of ScoreHolder( score 85 , score 93 , ... )
     */
    private static HashMap<Integer, ArrayList<ScoreHolder>> scoreMap;

    /**
     * initialize method , before calling this you should first call CGP initializer
     *
     * @param world
     */
    public static void initialize(World world) {
        ScoreSystem.world = world;
        nodeCount = world.getMap().getNodes().length;
        scoreMap = new HashMap<>();

        for (Node n : world.getMyNodes()) { //initialize hashMap's arrayList
            scoreMap.put(n.getIndex(), new ArrayList<>());
        }
        calculateScores();

        for (Node n : world.getMyNodes()) {
            ArrayList<ScoreHolder> tmp = scoreMap.get(n.getIndex());
            Collections.sort(tmp);
            Logger.log(TAG, tmp.size() + "", debug);
            for (ScoreHolder score : tmp) {
                Logger.log(TAG, score.toString(), debug);
            }
            scoreMap.put(n.getIndex(), tmp);
        }

    }

    //    TODO check if i am better than enemy , attack !
    //    TODO neighbours of High level edge nodes must have a better priority
    private static void calculateScores() {

//        TODO this loop is bad for defence , add the defence mechanism method later

//        calculate nodesEdgeCount
        int[] nodesEdgesCount = new int[nodeCount];

        for (int i = 0; i < nodesEdgesCount.length; i++) {
            nodesEdgesCount[i] = world.getMap().getNode(i).getNeighbours().length;
        }


        calculateCGPDistanceToNodes();

        for (Node n : world.getMap().getNodes()) {

            for (Node myNode : world.getMyNodes()) {

                int CGPDistance = myCGPsToOthersDistances[CGP.getPartNumberOfNode(myNode)][n.getIndex()];
                int rawDistance = APSP.getDist(myNode.getIndex(), n.getIndex());
                int ownerCoef;
                if (n.getOwner() == -1) {     // owner is nodoby
                    ownerCoef = Consts.EMPTY_COEF;
                } else if (n.getOwner() == world.getMyID()) { // i am spartacus !
                    ownerCoef = Consts.OWNER_COEF;
                } else { // owner is enemy
//                    if (ArmyLevel.ComputeArmyLevel(world, myNode).ordinal() > ArmyLevel.ComputeArmyLevel(world, n).ordinal()) {
//                        ownerCoef = Consts.OWNER_COEF;
//                    } else
                    ownerCoef = Consts.ENEMY_COEF;
                }
                if (rawDistance == 0) // dont stay in your place man
                    rawDistance = Consts.INF;

                int score = ownerCoef + rawDistance * Consts.RAW_DISTANCE_COEF +
                        Consts.EDGE_COUNT_COEF * (-1 * nodesEdgesCount[n.getIndex()]) +
                        CGPDistance * Consts.CGP_DISTANCE_COEF;
                scoreMap.get(myNode.getIndex()).add(new ScoreHolder(myNode.getIndex(), n.getIndex(), score));
            }
        }

    }

    private static void calculateCGPDistanceToNodes() {
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


    public static ArrayList<ScoreHolder> getScoresListFromNode(Node myNode) {
        return scoreMap.get(myNode.getIndex());
    }


}
