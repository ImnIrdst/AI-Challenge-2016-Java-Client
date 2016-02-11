package client.utils;

import client.World;
import client.model.Node;

/**
 * Created by mahdi on 2/11/16.
 *
 * Compute ArmyLevel
 */
public class ArmyLevel {

    public enum ArmyLevelEnum {
        FreeNode,
        Low,
        Medium,
        High
    }

    public static ArmyLevelEnum ComputeArmyLevel(World world, Node node) {
        if (node.getOwner() == -1) {
            return ArmyLevelEnum.FreeNode;
        } else {
            int armyCount = node.getArmyCount();
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
