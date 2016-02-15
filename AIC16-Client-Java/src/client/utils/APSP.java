package client.utils;

import client.AI;
import client.World;
import client.model.Node;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by iman on 2/11/16.
 *
 * Computes All Pair Shortest Path (APSP) for the given world.
 */
public class APSP {
	private static World world = null;             // game world
	private static int[][] dist = null;     // distance between node i, j

	private static int n;                   // map size

	/**
	 * initialize the static class
	 * @param world: game World
	 */
	public static void initialize(World world){
		if (dist != null) return;

		APSP.world = world;
		APSP.n = world.getMap().getNodes().length;
		computeAPSP();
	}

	/**
	 * computes distance between two nodes
	 * @param u: first node id
	 * @param v: second node id
	 * @return distance between nodes
	 */
	public static int getDist(Node u, Node v){
		return dist[u.getIndex()][v.getIndex()];
	}

	/**
	 * Get First Neighbor on the shortest path of source -> destination
	 * @param source: source node
	 * @param target: target node
	 * @return first neighbor on the shortest path of source -> destination
	 */
	public static Node getNeighborOnThePath(Node source, Node target){
		for (Node neighbor : source.getNeighbours()){
			if (getDist(source, target) == getDist(source, neighbor) + getDist(neighbor, target))
				return neighbor;
		}

		return source; // returns a dummy value (never get to this)
	}

	/**
	 * computes APSP based on world map and fills dist array using BFS algorithm
	 * // TODO: Maybe this needs to be implemented using dijkstra algorithm.
	 */
	private static void computeAPSP(){
		// initialize dist array
		dist = new int[n][n];
		for (int i=0 ; i<n ; i++) {
			Arrays.fill(dist[i], Consts.INF); dist[i][i] = 0;
		}

		// run BFS from each node to fill the APSP array
		Queue<Node> queue = new LinkedList<>();
		for (Node s : world.getMap().getNodes()){
			queue.clear(); queue.add(s);

			int[] vis = new int[n];
			vis[s.getIndex()] = 1;

			while (!queue.isEmpty()) {
				Node u = queue.poll();
				for (Node v : u.getNeighbours()) { // v.getNeighbours() can't return null
					if (vis[v.getIndex()] == 0){
						queue.add(v); vis[v.getIndex()] = 1;
						dist[s.getIndex()][v.getIndex()] = dist[s.getIndex()][u.getIndex()] + 1;
					}
				}
			}
		}
	}
}
