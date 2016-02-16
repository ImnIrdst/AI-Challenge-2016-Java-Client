package client;

import client.model.Node;
import client.pair.NodeIdPriorityPair;
import client.scoring.NodePriority;
import client.scoring.NodeScorePair;
import client.scoring.Scoring;
import client.utils.*;
import common.util.Log;
import jdk.nashorn.internal.runtime.WithObject;

import java.util.Arrays;

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
    private String TAG = "AI";
    private boolean isInitialized = false;

    private void initialize(World world){
        if (isInitialized) return;

        Logger.log(TAG, "My ID: " + world.getMyID(), true);
        Logger.log(TAG, "Map Levels: " + world.getLowArmyBound() + ", " + world.getMediumArmyBound() , true);

        isInitialized = true;
        APSP.initialize(world);
        CGP.initialize(world);
        NodeUtils.initialize(world);
        ArmyLevel.initialize(world);
    }

    public void doTurn(World world) {
        initialize(world);

        // fill this method, we've presented a stupid AI for example!
        Logger.log(TAG, "My nodes qty: " + world.getMyNodes().length, true);



        // fill this method,
        NodePriority.computeAllNodesPriority(world);



        // Sort by priority
        NodeIdPriorityPair[] ids = new NodeIdPriorityPair[world.getMap().getNodes().length];
        for (int i=0 ; i<ids.length ; i++) ids[i] = new NodeIdPriorityPair(world.getMap().getNode(i));

        Arrays.sort(ids);

        // compute nodeScores
        Scoring.computeScoresForAllNodes(world, ids);

        //Log the Mine and Enemy Nodes
        Logger.log(TAG, "Ally Nodes: " + world.getMyNodes().length, true);
        Logger.log(TAG, "Enemy Nodes: " + world.getOpponentNodes().length, true);

        for (NodeIdPriorityPair id : ids){
            Node node = world.getMap().getNode(id.id);

            String owner = "";
            if (NodeUtils.isAllyNode(node)) owner = "Ally ";
            if (NodeUtils.isEnemyNode(node)) owner = "Enemy ";
            if (NodeUtils.isEmptyNode(node)) owner = "Empty ";
//            if (NodeUtils.isEmptyNode(node)) continue;
            Logger.log(TAG, owner + " Node: (" + node.getIndex() + ", "
                    + ArmyLevel.getEnemyAndNeighboursApproxArmy(node) + ", "
                    + node.getArmyCount() + ") " + node.getPriority(), false);
        }

        for (Node cur : world.getMap().getNodes()) {
            if(cur.getNeighborScores().length == 0  || !NodeUtils.isAllyNode(cur)) continue;

            NodeScorePair[] neighbourScores = cur.getNeighborScores();
            Arrays.sort(neighbourScores);

            Node target = neighbourScores[0].node;
            world.moveArmy(cur, target, cur.getArmyCount());

            // If Target is an empty node cancel its affect on other nodes.
            Scoring.computeAffectOfANodeToAllAllyNodes(world, target, -1);

            if (NodeUtils.isEnemyNode(target) && NodeUtils.isAllyNode(cur)) {
                int diff = ArmyLevel.getEnemyAndNeighboursApproxArmy(cur) - ArmyLevel.getEnemyAndNeighboursApproxArmy(target);
                Logger.log(TAG, "" + diff, false);
            }
        }

        System.out.println("=======================================================");
    }

}
