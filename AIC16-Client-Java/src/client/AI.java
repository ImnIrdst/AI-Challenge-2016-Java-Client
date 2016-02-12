package client;
import client.model.Node;
import client.utils.ArmyLevel;
import java.util.*;

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
    Random random = new Random();

    public void doTurn(World world) {
        // fill this method, we've presented a stupid AI for example!

        Node[] myNodes = world.getMyNodes();
        for (Node source : myNodes) {

            // get neighbours
            Node[] neighbours = source.getNeighbours();
            if (neighbours.length > 0) {

                ArrayList<Node> free_neighbors= new ArrayList<>();
                ArrayList<Node> enemy_neighbors= new ArrayList<>();

                for (Node neighbor : neighbours) {
                    if (neighbor.getOwner() == -1) // Check if neighbor if free
                        free_neighbors.add(neighbor);
                    else if (neighbor.getOwner() != world.getMyID()) // Check if neighbor is for enemy
                        enemy_neighbors.add(neighbor);
                }


                if (!free_neighbors.isEmpty()) {
                    int count = free_neighbors.size();
                    int rnd = random.nextInt(count);
                    world.moveArmy(source, free_neighbors.get(rnd), source.getArmyCount()/2);
                }

                Collections.shuffle(enemy_neighbors, random);

                if (free_neighbors.isEmpty()) {
                    for (Node destination: enemy_neighbors) {
                        if (ArmyLevel.ComputeArmyLevel(world, destination) == ArmyLevel.ArmyLevelEnum.Low &&
                                ArmyLevel.ComputeArmyLevel(world, source) != ArmyLevel.ArmyLevelEnum.Low) {
                            world.moveArmy(source, destination, source.getArmyCount());
                            break;
                        }
                        else if (ArmyLevel.ComputeArmyLevel(world, destination) == ArmyLevel.ArmyLevelEnum.Medium &&
                                ArmyLevel.ComputeArmyLevel(world, source) == ArmyLevel.ArmyLevelEnum.High) {
                            world.moveArmy(source, destination, source.getArmyCount());
                            break;
                        }
                    }
                }

                if (free_neighbors.isEmpty() && enemy_neighbors.isEmpty()) {
                    // select a random neighbour
                    Node destination = neighbours[(int) (neighbours.length * Math.random())];

                    // move half of the node's army to the neighbor node
                    world.moveArmy(source, destination, source.getArmyCount()/2);
                }
            }
        }

    }

}
