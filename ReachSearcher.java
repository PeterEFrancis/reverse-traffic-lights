import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ReachSearcher {


	private int[] reachable;
	public static final int REACHABLE = 1, LOCAL_MAX_DEPTH = 2, GAME_OVER = 3, NOT_REACHABLE = 0;
	public int m, n;
	public int[][] lines;
	public int fullBoardID;
	
	ReachSearcher(int m, int n) {
		this.m = Math.min(m, n);
		this.n = Math.max(m, n);
		
		int[] fullBoard = new int[this.m * this.n];
		for (int i = 0; i < this.m * this.n; i++)
			fullBoard[i] = 3;
		fullBoardID = get_ID(fullBoard);
		
		if (new File("saved/" + this.m + "x" + this.n + ".dat").isFile())
			load_data();
		
		// generate lines
		int numLines = Math.max(0, n - 2) * this.m + Math.max(0, m - 2) * this.n + 2 * Math.max(0, m - 2) * Math.max(0, n - 2);
		lines = new int[numLines][3];
		int i = 0;
		for (int r = 0; r < this.m; r++)
			for (int c = 0; c < this.n; c++) {
				int pivot = r * this.n + c;
				if (c < this.n - 2) {
					lines[i++] = new int[] {pivot, pivot + 1, pivot + 2}; // horizontal
					if (r < this.m - 2) {
						lines[i++] = new int[] {pivot, pivot + 1 + this.n, pivot + 2 + 2 * this.n}; // down right diagonal
					}
				}
                if (r < this.m - 2) {
                	lines[i++] = new int[] {pivot, pivot + this.n, pivot + 2 * this.n}; // vertical
                	if (c > 1) {
                    	lines[i++] = new int[] {pivot, pivot - 1 + this.n, pivot - 2 + 2 * this.n}; // down left diagonal
                	}
                }
			}	
	}
	
	private void load_data() {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("saved/" + this.m + "x" + this.n + ".dat"));) {
			reachable = (int[]) input.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("No data file found, try running full_tree_search() first");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void save_data() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("saved/" + this.m + "x" + this.n + ".dat"));) {
			output.writeObject(reachable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean reached_traffic_lights_condition(int[] node) {
		for (int[] line : lines) {
			int a = line[0];
			int b = line[1];
			int c = line[2];
			if ((node[a] != 0) && (node[a] == node[b]) && (node[b] == node[c]))
                return true;
		}
		return false;
	}

	private ArrayList<int[]> get_children(int[] node) {
		ArrayList<int[]> children = new ArrayList<>();
        for (int i = 0; i < m * n; i++) {
        	if (node[i] < 3) {
        		int[] child = node.clone();
        		child[i]++;
        		children.add(child);
        	}
        }
        return children;
	}
	
	public int get_ID(int[] node) {
		int ID = 0;
		for (int i : node) {
			ID *= 4;
			ID += i;
		}
		return ID;
	}
	
	public int[] get_node_from_ID(int ID) {
		int[] out = new int[n * m];
		int remaining = ID;
		for (int i = n * m - 1; i >= 0; i--) {
			out[i] = remaining % 4;
			remaining /= 4;
		}
		return out;
	}
	
	public int get_depth(int nodeID) {
		int sum = 0;
		for (int el : get_node_from_ID(nodeID))
			sum += el;
		return sum;
	}
	
	public void full_tree_search() {
		reachable = new int[fullBoardID + 1];
		eval(new int[n * m]);
		save_data();
	}
	
	private void eval(int[] node) {
		if (reachable[get_ID(node)] == 0) {
			reachable[get_ID(node)] = REACHABLE;
			for (int[] child : get_children(node)) {
				if (reached_traffic_lights_condition(child)) {
					reachable[get_ID(node)] = LOCAL_MAX_DEPTH;
					reachable[get_ID(child)] = GAME_OVER; // game over, so do not look further
				}
				else
					eval(child);
			}
		}
	}
	
	// not an efficient function to iterate
	public int is_reachable(int nodeID) {
		load_data();
		return reachable[nodeID];
	}
	
	public int[] find_deepest() {
		load_data();
		int deepestDepth = 0;
		int deepestNodeID = 0;		
		for (int ID = 0; ID <= fullBoardID; ID++) {
			int depth = get_depth(ID);
			if (reachable[ID] == LOCAL_MAX_DEPTH && depth > deepestDepth) {
				deepestDepth = depth;
				deepestNodeID = ID;
			}
		}
		return get_node_from_ID(deepestNodeID);
	}

	public String nodeToString(int[] node) {
		final char[] stateChars = {'-', 'G', 'Y', 'R'};
		StringBuilder sb = new StringBuilder("  ");
		for (int c = 0; c < n; c++)
			sb.append(c);
		sb.append("\n");
		for (int r = 0; r < m; r++) {
			sb.append(r + " ");
			for (int c = 0; c < n; c++) 
				sb.append(stateChars[node[r * n + c]]);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws IOException {
		
		int[][] tests = new int[][] {{1,3}, {1,4}, {1,5}, {1,6}, {1,7}, {1,8},
		                             {2,3}, {2,4}, {2,5}, {2,6}, {2,7},
		                             {3,3}, {3,4}};

	    for (int[] test : tests) {
			ReachSearcher S = new ReachSearcher(test[0], test[1]);
			System.out.println("Starting full tree search on " + test[0]+ "x" + test[1] + " board ...");
			S.full_tree_search();
			int[] deepestNode = S.find_deepest();
			System.out.println("Finished: (" + S.get_depth(S.get_ID(deepestNode)) + " clicks) The most possible clicked board is \n" + S.nodeToString(deepestNode) + "\n");
		}		
		
	}
}
