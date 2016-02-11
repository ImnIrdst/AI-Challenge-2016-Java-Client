package majortests;

import junit.framework.TestCase;

import javax.swing.plaf.synth.SynthUI;
import java.util.Random;

/**
 * Created by iman on 2/11/16.
 *
 */
public class TestFloydWarshalPerformance extends TestCase {
	int n = 10;
	int[][] adj;

	@Override
	protected void setUp() throws Exception {
		adj = new int[n][n];

		Random random = new Random();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				adj[i][j] = random.nextInt(10);
				System.out.print(adj[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void testFloydWarshal() {
		long start = System.currentTimeMillis();

		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					adj[i][j] = Math.min(adj[i][j], adj[i][k] + adj[k][j]);
				}
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(adj[i][j] + " ");
			}
			System.out.println();
		}
		long finish = System.currentTimeMillis();
		System.out.println(finish - start);
	}
}
