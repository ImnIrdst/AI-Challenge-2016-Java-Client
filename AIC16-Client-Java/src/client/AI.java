package client;

import client.model.Node;
import client.score.ScoreHolder;
import client.score.ScoreSystem;
import client.utils.*;

import java.util.ArrayList;
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

    private boolean debug = true;
    private final String TAG = "AIClass";

    public void doTurn(World world) {
        // fill this method, we've presented a stupid AI for example!
        APSP.initialize(world);
        CGP.initialize(world);
        ScoreSystem.initialize(world);

        Node[] myNodes = world.getMyNodes();
        Logger.log(TAG, world.getMyNodes().length + " tedade node haye man", debug);
        boolean[] isAPathAssignedToNode = new boolean[world.getMap().getNodes().length];

//        important job is fighting ! going to backup ! etc
        boolean[] isImportantJobAssignedToNode = new boolean[world.getMap().getNodes().length];

        Arrays.fill(isImportantJobAssignedToNode, false);
        Arrays.fill(isAPathAssignedToNode, false);

        for (Node source : myNodes) {



            if (isImportantJobAssignedToNode[source.getIndex()])
                continue;

            if (!NodeDetails.isAllNeighboursMine(source)) { //bfs to the best node
                Path.bfsToTheBestNode(world,source);
                continue;
            }

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

    }

}
