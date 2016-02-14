package client.score;

import client.World;
import client.model.Node;
import client.utils.*;

import java.util.*;

/**
 * Created by al on 2/12/16.
 * The famous score system
 */
public class ScoreSystem {

    private static World world;
    private static int nodeCount;

    // for debug purposes
    private static final boolean debug = true;
    private static final String TAG = "ScoreSystem";

    /**
     * TODO HashMap Integer,TreeSet ScoreHolder have better performance
     * this map tells node and it score list to all destinations
     * for example scoreMap(3) => sorted ArrayList of ScoreHolder( score 85 , score 93 , ... )
     */
    private static HashMap<Integer, ArrayList<ScoreHolder>> scoreMap;

    /**
     * initialize method , before calling this you should first call CGP initializer
     * @param world world of game
     */
    public static void initialize(World world) {

        ScoreSystem.world = world;
        nodeCount = world.getMap().getNodes().length;
        scoreMap = new HashMap<>();
        for (Node n : world.getMyNodes()) { //initialize hashMap's arrayList
            scoreMap.put(n.getIndex(), new ArrayList<>());
        }
        calculateScores();

        for (Node node : world.getMyNodes()) {

            // sorting the list
            ArrayList<ScoreHolder> tmp = scoreMap.get(node.getIndex());
            Collections.sort(tmp);
            scoreMap.put(node.getIndex(), tmp);

            // Logging
            Logger.log(TAG, tmp.size() + "", debug);
            for (ScoreHolder score : tmp) {
                Logger.log(TAG, score.toString(), debug);
            }
        }

    }

    /**
     * this method calculate scores from all of my nodes to all other nodes
     */
    private static void calculateScores() {

//        calculate nodesEdgeCount
        int[] nodesEdgesCount = new int[nodeCount];

        for (int i = 0; i < nodesEdgesCount.length; i++) {
            nodesEdgesCount[i] = world.getMap().getNode(i).getNeighbours().length;
        }


        for (Node node : world.getMap().getNodes()) {

            for (Node myNode : world.getMyNodes()) {

///             TODO enemyCGPDistance should make some influence on score
                int CGPDistance = CGP.calculateMyCGPDistance(myNode, node);

                int rawDistance = APSP.getDist(myNode, node);

                // calculating owner coef
                int ownerCoef = calculateOwnerCoef(node, myNode);

                if (rawDistance == 0) // don't stay in your place man
                    rawDistance = Consts.INF;

                int score = calculateScore(ownerCoef, rawDistance, nodesEdgesCount[node.getIndex()], CGPDistance);

                // adding this score to the score list of myNode
                scoreMap.get(myNode.getIndex()).add(new ScoreHolder(myNode.getIndex(), node.getIndex(), score));
            }
        }

    }


    private static int calculateScore(int ownerCoef, int rawDistance, int nodeEdgesCount, int myCGPDistance) {
        return ownerCoef +
                rawDistance * Consts.RAW_DISTANCE_COEF +
                Consts.EDGE_COUNT_COEF * (-1 * nodeEdgesCount) +
                myCGPDistance * Consts.CGP_DISTANCE_COEF;
    }

    private static int calculateOwnerCoef(Node otherNode, Node myNode) {
        int ownerCoef;
        if (otherNode.getOwner() == -1) {                     // owner is nobody
            ownerCoef = Consts.EMPTY_COEF;
        } else if (otherNode.getOwner() == world.getMyID()) { // i am spartacus !
            ownerCoef = Consts.OWNER_COEF;
        } else {                                         // owner is enemy
            if (ArmyLevel.ComputeArmyLevel(world, myNode).ordinal() > ArmyLevel.ComputeArmyLevel(world, otherNode).ordinal()) {
                ownerCoef = Consts.EMPTY_COEF;
            } else
                ownerCoef = Consts.ENEMY_COEF;
        }

        return ownerCoef;
    }


    /**
     * this method return a sorted list of ScoreHolder by score
     * @param myNode i should be owner of this node
     * @return null if i am not the owner of argument myNode
     */
    public static ArrayList<ScoreHolder> getScoresListFromNode(Node myNode) {
        return scoreMap.getOrDefault(myNode.getIndex(),null);
    }


}
