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
        WEAK,
        MEDIOCRE,
        STRONG
    }

    public static void initialize(World world){
        ArmyLevel.world = world;
    }

    public static World getWorld(){ return world; }

    public static ArmyLevelEnum computeArmyLevel(Node node) {
        if (NodeUtils.isEmptyNode(node)) { // IF Empty Node
            return ArmyLevelEnum.FreeNode;

        } else if (NodeUtils.isAllyNode(node)){ // If Ally Node
            if (node.getArmyCount() < world.getLowArmyBound()) return ArmyLevelEnum.WEAK;
            else if (node.getArmyCount() < world.getMediumArmyBound()) return ArmyLevelEnum.MEDIOCRE;
            else return ArmyLevelEnum.STRONG;

        } else { // If Enemy Node
            if (node.getArmyCount() == 0) return ArmyLevelEnum.WEAK;
            else if (node.getArmyCount() == 1) return ArmyLevelEnum.MEDIOCRE;
            else return ArmyLevelEnum.STRONG;
        }
    }

    public static boolean isWeak(Node node){
        return computeArmyLevel(node) == ArmyLevelEnum.WEAK;
    }

    public static boolean isMediocre(Node node){
        return computeArmyLevel(node) == ArmyLevelEnum.MEDIOCRE;
    }

    public static boolean isStrong(Node node) {
        return computeArmyLevel(node) == ArmyLevelEnum.STRONG;
    }

    public static int getWeakLevelMid(){
        return world.getLowArmyBound() / 2;
    }

    public static int getMediocreLevelMid(){
        return (world.getLowArmyBound() + world.getLowArmyBound())/2;
    }

    public static int getStrongLevelMid(){
        return 2 * getMediocreLevelMid();
    }

    public static int getEnemyAndNeighboursApproxArmy(int weakCnt, int mediocreCnt, int strongCnt){
        return weakCnt*ArmyLevel.getWeakLevelMid()
                + mediocreCnt*ArmyLevel.getMediocreLevelMid()
                + strongCnt*ArmyLevel.getStrongLevelMid();
    }

    public static int getEnemyApproxArmy(Node node){
        if (isWeak(node)) return getWeakLevelMid();
        if (isMediocre(node)) return getMediocreLevelMid();
        if (isStrong(node)) return getStrongLevelMid();
        return getStrongLevelMid();
    }

    public static int getEnemyAndNeighboursApproxArmy(Node node){
        int weakCnt = 0;
        int mediocreCnt = 0;
        int strongCnt = 0;
        for (Node neighbour : node.getNeighbours()) {
            if (NodeUtils.isEnemyNode(neighbour)) {
                if (ArmyLevel.isWeak(neighbour)) weakCnt++;
                if (ArmyLevel.isMediocre(neighbour)) mediocreCnt++;
                if (ArmyLevel.isStrong(neighbour)) strongCnt++;
            }
        }

        if (NodeUtils.isEnemyNode(node)) {
            if (ArmyLevel.isWeak(node)) weakCnt++;
            if (ArmyLevel.isMediocre(node)) mediocreCnt++;
            if (ArmyLevel.isStrong(node)) strongCnt++;
        }

        return getEnemyAndNeighboursApproxArmy(weakCnt, mediocreCnt, strongCnt);
    }

    public static boolean isEnemyAndNeighboursApproxWeak(Node node){
        return getEnemyAndNeighboursApproxArmy(node) < world.getLowArmyBound();
    }

    public static boolean isEnemyAndNeighboursApproxMediocre(Node node){
        return getEnemyAndNeighboursApproxArmy(node) >= world.getLowArmyBound()
                && getEnemyAndNeighboursApproxArmy(node) < world.getMediumArmyBound();
    }

    public static boolean isEnemyAndNeighboursApproxStrong(Node node){
        return getEnemyAndNeighboursApproxArmy(node) >= getStrongLevelMid();
    }

    public static boolean isApproxWeak(int weakCnt, int mediocreCnt, int strongCnt){
        return getEnemyAndNeighboursApproxArmy(weakCnt, mediocreCnt, strongCnt) < world.getLowArmyBound();
    }

    public static boolean isApproxMediocre(int weakCnt, int mediocreCnt, int strongCnt){
        return getEnemyAndNeighboursApproxArmy(weakCnt, mediocreCnt, strongCnt) >= world.getLowArmyBound()
                && getEnemyAndNeighboursApproxArmy(weakCnt, mediocreCnt, strongCnt) < world.getMediumArmyBound();
    }

    public static boolean isApproxStrong(int weakCnt, int mediocreCnt, int strongCnt){
        return getEnemyAndNeighboursApproxArmy(weakCnt, mediocreCnt, strongCnt) >= world.getMediumArmyBound();
    }

    public static boolean canProduceStrongArmy(Node node){
        return node.getArmyCount() > world.getMediumArmyBound()*2;
    }

    public static int getExceedStrongArmy(Node node){
        return node.getArmyCount() - world.getMediumArmyBound();
    }


}
