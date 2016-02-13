package client;

import client.model.Node;
import client.scoring.NodeScorePair;
import client.scoring.Scoring;
import client.utils.APSP;
import client.utils.CGP;

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
    private World world;

    private void initialize(World world){
        this.world = world;
        APSP.initialize(world);
        CGP.initialize(world);
    }

    public void doTurn(World world) {
        initialize(world);

        // fill this method, we've presented a stupid AI for example!
        Node[] myNodes = world.getMyNodes();

        // compute nodeScores
        Scoring.computeScoresForAllNodes(world);

        for (Node cur : myNodes) {
            if(cur.neighborScores.length == 0) continue;

            NodeScorePair[] neighbourScores = cur.neighborScores;
            Arrays.sort(neighbourScores);
            world.moveArmy(cur, neighbourScores[0].node, cur.getArmyCount());
        }
    }

}
