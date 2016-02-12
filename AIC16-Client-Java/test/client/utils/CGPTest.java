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
 * Created by al on 2/12/16.
 */
public class CGPTest extends TestCase {

    @Mock
    World world;


    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    public void testCGP() {

//        initialize nodes of graph
        Node[] nodes = new Node[15];
        for (int i = 0; i < 15; i++) {
            nodes[i] = new Node(i);
        }

        int[][] neighbours = new int[15][];
        neighbours[0] = new int[]{2, 1};
        neighbours[1] = new int[]{0, 3};
        neighbours[2] = new int[]{0};
        neighbours[3] = new int[]{1, 4, 8, 9};
        neighbours[4] = new int[]{3};
        neighbours[5] = new int[]{7};
        neighbours[6] = new int[]{7};
        neighbours[7] = new int[]{5, 6, 11, 8};
        neighbours[8] = new int[]{3, 7};
        neighbours[9] = new int[]{3, 13};
        neighbours[10] = new int[]{11};
        neighbours[11] = new int[]{7, 10, 14};
        neighbours[12] = new int[]{13};
        neighbours[13] = new int[]{12, 9};
        neighbours[14] = new int[]{11};

        for (int i = 0; i < 15; i++) {
            Node[] neighbour = new Node[neighbours[i].length];
            for (int j = 0; j < neighbours[i].length; j++) {
                neighbour[j] = nodes[neighbours[i][j]];
            }
            nodes[i].setNeighbours(neighbour);
        }

        int[] myNodesIds = new int[]{0, 1, 2, 5};
        int[] enemyNodesIds = new int[]{9, 10, 11 , 12 , 13};

        Node[] myNodes = new Node[myNodesIds.length];
        Node[] enemyNodes = new Node[enemyNodesIds.length];

        for (int i = 0; i < myNodesIds.length; i++) {
            myNodes[i] = nodes[myNodesIds[i]];
            myNodes[i].setOwner(1);
            myNodes[i].setArmyCount(10);
        }
        for (int i = 0; i < enemyNodesIds.length; i++) {
            enemyNodes[i] = nodes[enemyNodesIds[i]];
            enemyNodes[i].setOwner(2);
            enemyNodes[i].setArmyCount(10);
        }

        Graph fullGraph = new Graph(nodes);
        when(world.getMap()).thenReturn(fullGraph);
        when(world.getMyID()).thenReturn(1);
        when(world.getMyNodes()).thenReturn(myNodes);
        when(world.getOpponentNodes()).thenReturn(enemyNodes);

        CGP.initialize(world);

        assertEquals(CGP.isNodeBorder(nodes[0]),false);
        assertEquals(CGP.isNodeBorder(nodes[1]),true);
        assertEquals(CGP.isNodeBorder(nodes[10]),false);

        assertEquals(CGP.getEnemySectionsCount() , 2);
        assertEquals(CGP.getMySectionsCount() , 2);

        assertEquals(CGP.getPartNumberOfNode(nodes[0]),0);
        assertEquals(CGP.getPartNumberOfNode(nodes[1]),0);
        assertEquals(CGP.getPartNumberOfNode(nodes[5]),1);

        assertEquals(CGP.getMyCGParts().get(0).contains(nodes[0]) , true);
        assertEquals(CGP.getMyCGParts().get(0).contains(nodes[1]) , true);
        assertEquals(CGP.getMyCGParts().get(0).contains(nodes[2]) , true);
        assertEquals(CGP.getMyCGParts().get(0).contains(nodes[5]) , false);
        assertEquals(CGP.getMyCGParts().get(0).contains(nodes[3]) , false);
        assertEquals(CGP.getMyCGParts().get(1).contains(nodes[5]) , true);

        assertEquals(CGP.getMyCGPBorders().get(0).contains(nodes[0]),false);
        assertEquals(CGP.getMyCGPBorders().get(0).contains(nodes[1]),true);
        assertEquals(CGP.getMyCGPBorders().get(0).contains(nodes[2]),false);
        assertEquals(CGP.getMyCGPBorders().get(1).contains(nodes[5]),true);


//        i do not check getEnemy* because enemy and getMy* are basically same

    }
}