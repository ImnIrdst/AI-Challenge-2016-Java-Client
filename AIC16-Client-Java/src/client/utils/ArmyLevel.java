package client.utils;

import client.World;
import client.model.Node;

/**
 * Created by mahdi on 2/11/16.
 *
 * Compute ArmyLevel
 */
public class ArmyLevel {

    private static World world;

    public enum ArmyLevelEnum {
        FreeNode,
        Low,
        Medium,
        High
    }

    public static void initialize(World world){
        ArmyLevel.world = world;
    }

    public static ArmyLevelEnum ComputeArmyLevel(Node node) {
        if (NodeUtils.isEmptyNode(node)) { // IF Empty Node
            return ArmyLevelEnum.FreeNode;

        } else if (NodeUtils.isAllyNode(node)){ // If Ally Node
            if (node.getArmyCount() < world.getLowArmyBound()) return ArmyLevelEnum.Low;
            else if (node.getArmyCount() < world.getMediumArmyBound()) return ArmyLevelEnum.Medium;
            else return ArmyLevelEnum.High;

        } else { // If Enemy Node
            if (node.getArmyCount() == 0) return ArmyLevelEnum.Low;
            else if (node.getArmyCount() == 1) return ArmyLevelEnum.Medium;
            else return ArmyLevelEnum.High;
        }
    }
}
