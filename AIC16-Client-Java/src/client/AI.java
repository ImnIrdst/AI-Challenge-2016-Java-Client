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
    private World world;
    private boolean isInitialized = false;

    private void initialize(World world){
        if (isInitialized) return;

        Logger.log(TAG, "My ID: " + world.getMyID(), true);
        Logger.log(TAG, "Map Levels: " + world.getLowArmyBound() + ", " + world.getMediumArmyBound() , true);

        isInitialized = true;
        this.world = world;
        APSP.initialize(world);
        CGP.initialize(world);
        NodeUtils.initialize(world);
        ArmyLevel.initialize(world);
    }

    public void doTurn(World world) {
        initialize(world);

        // fill this method, we've presented a stupid AI for example!
        Node[] myNodes = world.getMyNodes();

        Logger.log(TAG, "My nodes qty: " + world.getMyNodes().length, true);



        // fill this method,
        NodePriority.computeAllNodesPriority(world);
        NodeIdPriorityPair[] ids = new NodeIdPriorityPair[world.getMap().getNodes().length];
        for (int i=0 ; i<ids.length ; i++) ids[i] = new NodeIdPriorityPair(world.getMap().getNode(i));

        Arrays.sort(ids);

        for (NodeIdPriorityPair id : ids){
            Node node = world.getMap().getNode(id.id);

            String owner = "";
            if (NodeUtils.isAllyNode(node)) owner = "Ally ";
            if (NodeUtils.isEnemyNode(node)) owner = "Enemy ";
            if (NodeUtils.isEmptyNode(node)) owner = "Empty ";
            Logger.log(TAG, owner + " Node: (" + node.getIndex() + ", " + node.getOwner() + ", "
                    + node.getArmyCount() + ") " + node.getPriority(), true);
        }

        // compute nodeScores
        Scoring.computeScoresForAllNodes(world);

        boolean[] emptyNodesAssigned = new boolean[world.getMap().getNodes().length];

        for (Node cur : world.getMap().getNodes()) {
            if(cur.neighborScores.length == 0  || !NodeUtils.isAllyNode(cur)) continue;

            NodeScorePair[] neighbourScores = cur.neighborScores;
            Arrays.sort(neighbourScores);
            for (NodeScorePair neighbour : neighbourScores) {
                if (NodeUtils.isEmptyNode(neighbour.node)
                        && emptyNodesAssigned[neighbour.node.getIndex()]) continue;

                if (NodeUtils.isEmptyNode(neighbour.node))
                    emptyNodesAssigned[neighbour.node.getIndex()] = true;

                world.moveArmy(cur, neighbour.node, cur.getArmyCount()); break;
            }
        }

        Logger.log(TAG, Arrays.toString(emptyNodesAssigned), false);
        System.out.println("=======================================================");
        System.out.println("=======================================================");
        System.out.println("=======================================================");
    }

}
