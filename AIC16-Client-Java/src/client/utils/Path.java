package client.utils;

import client.World;
import client.model.Graph;
import client.model.Node;
import client.score.ScoreSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mahdi on 2/12/16.
 * Class to find path
 */
public class Path {

//    for debug
    private static boolean debug = true;
    private static final String TAG = "Path";

    public static ArrayList<Node> FindNearestEnemyNode(World world, Node from, Node to) {
        Queue<Node> Q = new LinkedList<>();
        HashMap<Integer, Integer> Parents = new HashMap<>();
        HashMap<Integer, Integer> Distance = new HashMap<>();
        ArrayList<Node> Path = new ArrayList<>();
        final int INF = (int)1e6;
        Node _to = new Node(from.getIndex());

        Distance.put(from.getIndex(), 0);
        Q.add(from);

        while (!Q.isEmpty()) {
            Node node = Q.remove();
            if (node.getIndex() == to.getIndex()) {
                _to = node;
                break;
            }

            int nodeDistance = Distance.get(node.getIndex());
            Parents.put(from.getIndex(), -1);

            for (Node neighbor :
                    node.getNeighbours()) {
                int neighborDistance = Distance.getOrDefault(neighbor.getIndex(), INF);
                if (neighborDistance > nodeDistance + 1) {
                    Distance.put(neighbor.getIndex(), nodeDistance + 1);
                    Q.add(neighbor);
                    Parents.put(neighbor.getIndex(), node.getIndex());
                }
            }
        }

        Q.clear();
        if (_to.getIndex() == from.getIndex())
            return null;

        Path.add(_to);

        while (true) {
            Node node = Path.get(Path.size() - 1);
            int parentIndex = Parents.get(node.getIndex());
            if (parentIndex == -1) {
                break;
            }
            Path.add(world.getMap().getNode(parentIndex));
        }

        return Path;
    }


    /**
     * this method finds the fist path to go if it want to go the best node possible
     * in other words make path , but does not need to save it :D
     * @param srcNode should be mine , otherwise safety is not guaranteed
     */
    public static void bfsToTheBestNode(World world, Node srcNode){
        Node to = world.getMap().getNode(ScoreSystem.getScoresListFromNode(srcNode).get(0).getDstIndex());
        ArrayList<Node> list = Path.FindNearestEnemyNode(world, srcNode, to);
        Node dest = list.get(list.size() - 2);
        world.moveArmy(srcNode, dest, srcNode.getArmyCount());

        Logger.log(TAG, "created a path from " + srcNode.getIndex() +
                " to " + to.getIndex() + " by neighbour " + dest.getIndex(), debug);
    }
}
