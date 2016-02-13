package client.model;

import client.scoring.NodeScorePair;
import client.scoring.NodePriority;

/**
 * Graph Node
 * Please do not change this class.
 */
public class Node implements Comparable<Node> {
    private int index;
    private int owner;
    private int armyCount;
    private Node[] neighbours;

	// Defined by me.
	public long selfScore;
	public NodePriority priority;
	public NodeScorePair[] neighborScores;

    public Node(int index) {
        this.index = index;
        this.owner = -1;
    }

    public NodePriority getPriority() {
        return priority;
    }

    public NodeScorePair[] getNeighborScores() {
        return neighborScores;
    }

    /**
     * get neighbours of this node.
     * @return array of neighbours
     */
    public Node[] getNeighbours() {
        return neighbours;
    }

    /**
     * get index of the node. useful when you want to move and you need index of a node.
     *
     * @return index of the node
     */
    public int getIndex() {
        return index;
    }

    /**
     * get owner of the node. owner will be an integer. if you want to check if
     * this node is yours you can check owner is equal to your team ID. also if
     * you want to check if this node is for you opponent, you can check owner
     * is equal to (1 - your team ID). otherwise this node is empty.
     *
     * @return owner of the node
     * @see client.World#getMyNodes
     * @see client.World#getOpponentNodes
     * @see client.World#getFreeNodes
     */
    public int getOwner() {
        return owner;
    }

    /**
     * get number of armies in this node. note that if this node is for your
     * opponent, then the result of the function is an approximation of the
     * army count in this node. read contest manual for more details.
     *
     * @return number of armies in this node
     */
    public int getArmyCount() {
        return armyCount;
    }

    public void setNeighbours(Node[] neighbours) {
        this.neighbours = neighbours;
	    this.neighborScores = new NodeScorePair[neighbours.length];
	    for (int i=0 ; i<neighbours.length ; i++)
		    this.neighborScores[i].node = neighbours[i];
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void setArmyCount(int armyCount) {
        this.armyCount = armyCount;
    }

    void setIndex(int index) {
        this.index = index;
    }

	@Override
	public int compareTo(Node o) {
		return priority.compareTo(o.priority);
	}
}
