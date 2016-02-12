package client.utils;

import client.World;
import client.model.Graph;
import client.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mahdi on 2/12/16.
 * Class to find path
 */
public class Path {

    public static ArrayList<Node> FindNearestEnemyNode(World world, Node from) {
        Queue<Node> Q = new LinkedList<>();
        HashMap<Integer, Integer> Parents = new HashMap<>();
        HashMap<Integer, Integer> Distance = new HashMap<>();
        ArrayList<Node> Path = new ArrayList<>();
        final int INF = (int)1e6;
        Node to = new Node(from.getIndex());

        Distance.put(from.getIndex(), 0);
        Q.add(from);

        while (!Q.isEmpty()) {
            Node node = Q.remove();
            if (node.getOwner() != world.getMyID()) {
                to = node;
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
        if (to.getIndex() == from.getIndex())
            return null;

        Path.add(to);

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
}
