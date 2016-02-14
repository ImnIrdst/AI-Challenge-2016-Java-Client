package client;

import client.model.Graph;
import client.model.Node;
import client.score.ScoreHolder;
import client.score.ScoreSystem;
import client.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * AI class.
 * You should fill body of the method {@link #doTurn}.
 * Do not change name or modifiers of the methods or fields
 * and do not add constructor for this class.
 * You can add as many methods or fields as you want!
 * Use world parameter to access and modify game's
 * world!
 * See World interface for more details.
 */
public class AI {

    private boolean debug = true;
    private final String TAG = "AIClass";
    private Graph lastState = null;

    public void doTurn(World world) {
        // fill this method, we've presented a stupid AI for example!
        APSP.initialize(world);
        CGP.initialize(world);
        ScoreSystem.initialize(world);


        if (lastState == null) // if its first turn
            lastState = world.getMap();

        Node[] myNodes = world.getMyNodes();
        Logger.log(TAG, world.getMyNodes().length + " tedade node haye man", debug);
        boolean[] isAPathAssignedToNode = new boolean[world.getMap().getNodes().length];

//        important job is fighting ! going to backup ! etc
        boolean[] isImportantJobAssignedToNode = new boolean[world.getMap().getNodes().length];

        Arrays.fill(isImportantJobAssignedToNode, false);
        Arrays.fill(isAPathAssignedToNode, false);

//        bechehaBirizin(world, lastState, isImportantJobAssignedToNode);

        for (Node source : myNodes) {


            if (isImportantJobAssignedToNode[source.getIndex()])
                continue;

            if (!NodeDetails.isAllNeighboursMine(source)) { //bfs to the best node
                Node target = world.getMap().getNode(ScoreSystem.getScoresListFromNode(source).get(0).getDstIndex());
                Node moveTo = APSP.getNeighborOnThePath(source,target);
                world.moveArmy(source,moveTo,source.getArmyCount());

                continue;
            }

//            there is a neighbour free node 100%

            for (ScoreHolder score : ScoreSystem.getScoresListFromNode(source)) {
//                this loop's main concern is freeNodes , so we must mark it down so no two nodes will go to a free node
                if (isAPathAssignedToNode[score.getDstIndex()])
                    continue;

                boolean breakFree = false;

                for (Node dst : source.getNeighbours()) {
                    if (dst.getIndex() == score.getDstIndex()) {
                        isAPathAssignedToNode[score.getDstIndex()] = true;
                        world.moveArmy(score.getSrcIndex(), score.getDstIndex(), source.getArmyCount());
                        breakFree = true;
                        break;
                    }
                }

                if (breakFree)
                    break;
            }
        }

//        saving the last state for next turn
        lastState = new Graph(world.getMap().getNodes().clone());

    }

    /**
     * YA ABALFAZL
     * ALLAHO AKBAR
     * this is a ENTEHARI OPERATION
     */
    public void bechehaBirizin(World world, Graph lastState, boolean[] isImportantJobAssigned) {

        ArrayList<Node> nearbyEnemies = CGP.getEnemyFrontLiners(); // Fuck'em

        for (Node enemyNode : nearbyEnemies) {

            boolean isEnemyMoving = NodeDetails.isEnemyMoving(world, lastState, enemyNode);
            boolean amIBetterCombinedToAttack = NodeDetails.amIBetterCombinedToAttack(world, enemyNode);

            if (amIBetterCombinedToAttack && !isEnemyMoving) { //Fuck'em Right Now
                for (Node node : enemyNode.getNeighbours()) {
                    if (NodeDetails.isMyNode(world, node)) {
                        isImportantJobAssigned[node.getIndex()] = true;
                        world.moveArmy(node, enemyNode, node.getArmyCount());
                    }
                }
            } else if (amIBetterCombinedToAttack && isEnemyMoving) {
                TreeSet<Node> myNearbyNodes = NodeDetails.getSortedNearbyMyNodes(world, enemyNode);
                world.moveArmy(myNearbyNodes.first(), enemyNode, myNearbyNodes.first().getArmyCount());
                isImportantJobAssigned[myNearbyNodes.first().getIndex()] = true;
                for (Node node : myNearbyNodes.last().getNeighbours()) { //TODO maybe bad algorithm
                    if(!NodeDetails.isMyNode(world,node))
                        continue;
                    isImportantJobAssigned[node.getIndex()] =true;
                    world.moveArmy(node,myNearbyNodes.first(),node.getArmyCount());
                }
            }
        }
    }

}
