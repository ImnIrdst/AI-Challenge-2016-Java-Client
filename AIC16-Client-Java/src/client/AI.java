package client;
import client.model.Node;
import client.utils.ArmyLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

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

    public void doTurn(World world) {
        // fill this method, we've presented a stupid AI for example!

        Node[] myNodes = world.getMyNodes();
        for (Node source : myNodes) {

            // get neighbours
            Node[] neighbours = source.getNeighbours();
            if (neighbours.length > 0) {

                Node[] freeNodes = world.getFreeNodes();
                Queue<Node> free_neighbors= new LinkedList<Node>();
                Queue<Node> enemy_neighbors= new LinkedList<Node>();

                for (Node neighbor : neighbours) {
                    if (neighbor.getOwner() == -1) // Check if neighbor if free
                        free_neighbors.add(neighbor);
                    else if (neighbor.getOwner() != world.getMyID()) // Check if neighbor is for enemy
                        enemy_neighbors.add(neighbor);
                }

                for (Node destination: free_neighbors) {
                    world.moveArmy(source, destination, source.getArmyCount()/free_neighbors.size());
                }

                if (free_neighbors.isEmpty()) {
                    for (Node destination: enemy_neighbors) {
                        if (ArmyLevel.ComputeArmyLevel(world, destination) == ArmyLevel.ArmyLevelEnum.Low &&
                                ArmyLevel.ComputeArmyLevel(world, source) != ArmyLevel.ArmyLevelEnum.Low)
                            world.moveArmy(source, destination, source.getArmyCount());
                        else if (ArmyLevel.ComputeArmyLevel(world, destination) == ArmyLevel.ArmyLevelEnum.Medium &&
                                ArmyLevel.ComputeArmyLevel(world, source) == ArmyLevel.ArmyLevelEnum.High)
                            world.moveArmy(source, destination, source.getArmyCount());
                    }
                }

                if (free_neighbors.isEmpty() && enemy_neighbors.isEmpty() && neighbours.length != 0) {
                    // select a random neighbour
                    Node destination = neighbours[(int) (neighbours.length * Math.random())];

                    // move half of the node's army to the neighbor node
                    world.moveArmy(source, destination, source.getArmyCount()/2);
                }
            }
        }

    }

}
