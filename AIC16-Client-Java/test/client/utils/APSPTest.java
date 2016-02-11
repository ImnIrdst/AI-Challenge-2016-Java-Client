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
 *
 * Tests utils.APSP Class
 */
public class APSPTest extends TestCase {
	@Mock
	World world;


	@Override
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	public void testAPSP(){
		//                                        -> 3
		// build a world with a Graph like this 1 -> 2 -> 0
		Node[] nodes = new Node[4];
		nodes[0] = new Node(0);
		nodes[1] = new Node(1);
		nodes[2] = new Node(2);
		nodes[3] = new Node(3);

		nodes[0].setNeighbours(new Node[]{});
		nodes[1].setNeighbours(new Node[]{nodes[2], nodes[3]});
		nodes[2].setNeighbours(new Node[]{nodes[0]});
		nodes[3].setNeighbours(new Node[]{});
		Graph graph = new Graph(nodes);

		// Mockito stubbing
		when(world.getMap()).thenReturn(graph);

		APSP.initialize(world);
		assertEquals(APSP.getDist(0, 0), 0);
		assertEquals(APSP.getDist(0, 1), Consts.INF);
		assertEquals(APSP.getDist(0, 2), Consts.INF);
		assertEquals(APSP.getDist(0, 3), Consts.INF);
		assertEquals(APSP.getDist(1, 0), 2);
		assertEquals(APSP.getDist(1, 1), 0);
		assertEquals(APSP.getDist(1, 2), 1);
		assertEquals(APSP.getDist(1, 3), 1);
		assertEquals(APSP.getDist(2, 0), 1);
		assertEquals(APSP.getDist(2, 1), Consts.INF);
		assertEquals(APSP.getDist(2, 2), 0);
		assertEquals(APSP.getDist(2, 3), Consts.INF);
		assertEquals(APSP.getDist(3, 0), Consts.INF);
		assertEquals(APSP.getDist(3, 1), Consts.INF);
		assertEquals(APSP.getDist(3, 2), Consts.INF);
		assertEquals(APSP.getDist(3, 3), 0);
	}
}