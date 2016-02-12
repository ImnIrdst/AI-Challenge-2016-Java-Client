package client.utils;

import client.World;
import client.model.Node;

/**
 * Created by mahdi on 2/11/16.
 *
 * Compute ArmyLevel
 */
public class ArmyLevel {

    /**
     * Enum for all types of army level
     */
    public enum ArmyLevelEnum {
        FreeNode,
        Low,
        Medium,
        High
    }


    /**
     *
     * @param world World we are playing in
     * @param node node to compute the army level for
     * @return army level for the given node
     */
    public static ArmyLevelEnum ComputeArmyLevel(World world, Node node) {
        if (node.getOwner() == -1) { // Check if node dose not have owner
            return ArmyLevelEnum.FreeNode;
        } else {
            int armyCount = node.getArmyCount();
            // The output will be exact when the node is ours
            // and bounds if node is for enemy

            if (armyCount < world.getLowArmyBound()) {
                return ArmyLevelEnum.Low;
            } else if (armyCount < world.getMediumArmyBound()) {
                return ArmyLevelEnum.Medium;
            } else {
                return ArmyLevelEnum.High;
            }
        }
    }
}
