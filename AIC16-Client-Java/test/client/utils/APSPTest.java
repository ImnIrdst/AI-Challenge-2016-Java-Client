package client.utils;

import client.World;
import client.model.Game;
import client.model.Graph;
import client.model.Node;
import junit.framework.TestCase;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Created by iman on 2/11/16.
 * <p>
 * Tests utils.APSP Class
 */
public class APSPTest extends TestCase {
	@Mock
	World world;


	@Override
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	public void testAPSP() {
		//                                        -> 3 -> 4 ->
		// build a world with a Graph like this 1 -> 2 ------> 0
		Node[] nodes = new Node[5];
		nodes[0] = new Node(0);
		nodes[1] = new Node(1);
		nodes[2] = new Node(2);
		nodes[3] = new Node(3);
		nodes[4] = new Node(4);

		nodes[0].setNeighbours(new Node[]{});
		nodes[1].setNeighbours(new Node[]{nodes[2], nodes[3]});
		nodes[2].setNeighbours(new Node[]{nodes[0]});
		nodes[3].setNeighbours(new Node[]{nodes[4]});
		nodes[4].setNeighbours(new Node[]{nodes[0]});
		Graph graph = new Graph(nodes);

		// Mockito stubbing
		when(world.getMap()).thenReturn(graph);

		// Initialize APSP
		APSP.initialize(world);

		// Test getDist
		assertEquals(APSP.getDist(nodes[0], nodes[0]), 0);
		assertEquals(APSP.getDist(nodes[0], nodes[1]), Consts.INF);
		assertEquals(APSP.getDist(nodes[0], nodes[2]), Consts.INF);
		assertEquals(APSP.getDist(nodes[0], nodes[3]), Consts.INF);
		assertEquals(APSP.getDist(nodes[1], nodes[0]), 2);
		assertEquals(APSP.getDist(nodes[1], nodes[1]), 0);
		assertEquals(APSP.getDist(nodes[1], nodes[2]), 1);
		assertEquals(APSP.getDist(nodes[1], nodes[3]), 1);
		assertEquals(APSP.getDist(nodes[2], nodes[0]), 1);
		assertEquals(APSP.getDist(nodes[2], nodes[1]), Consts.INF);
		assertEquals(APSP.getDist(nodes[2], nodes[2]), 0);
		assertEquals(APSP.getDist(nodes[2], nodes[3]), Consts.INF);
		assertEquals(APSP.getDist(nodes[3], nodes[0]), 2);
		assertEquals(APSP.getDist(nodes[3], nodes[1]), Consts.INF);
		assertEquals(APSP.getDist(nodes[3], nodes[2]), Consts.INF);
		assertEquals(APSP.getDist(nodes[3], nodes[3]), 0);

		// Test getNeighborsOnThePath
		assertEquals(APSP.getNeighborOnThePath(nodes[1], nodes[0]).getIndex(), nodes[2].getIndex());
		assertEquals(APSP.getNeighborOnThePath(nodes[1], nodes[2]).getIndex(), nodes[2].getIndex());
		assertEquals(APSP.getNeighborOnThePath(nodes[1], nodes[2]).getIndex(), nodes[2].getIndex());
		assertEquals(APSP.getNeighborOnThePath(nodes[1], nodes[3]).getIndex(), nodes[3].getIndex());
		assertEquals(APSP.getNeighborOnThePath(nodes[3], nodes[0]).getIndex(), nodes[4].getIndex());

		// False Scenario: if source and destination are equal (re
		assertEquals(APSP.getNeighborOnThePath(nodes[0], nodes[0]).getIndex(), nodes[0].getIndex());
		assertEquals(APSP.getNeighborOnThePath(nodes[2], nodes[2]).getIndex(), nodes[2].getIndex());
	}
}