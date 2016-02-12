package client;

import client.model.Node;
import client.utils.*;
import org.mockito.internal.util.collections.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
        Arrays.fill(isAPathAssignedToNode, false);

//        TODO find best score before doing in a loop , in other words sort myNodes by leastScore in their ScoreSystem
        for (Node source : myNodes) {

            for (ScoreHolder score : ScoreSystem.getScoresListFromNode(source)) {
                if (!isAPathAssignedToNode[score.getDstIndex()]) {
                    boolean breakFree = false;
                    for (Node dst : source.getNeighbours()) {   //TODO if Contst are good , neighbour will be choosen first
                        //TODO or set path , not just move
                        if (dst.getIndex() == score.getDstIndex()) {
                            isAPathAssignedToNode[score.getDstIndex()] = true;
//                    TODO armycount/2 is not good
                            world.moveArmy(score.getSrcIndex(), score.getDstIndex(), source.getArmyCount() / 2);
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

}
