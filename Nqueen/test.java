
import java.io.IOException;
import java.util.*;

public class Test {

	public static void main(String[] args) throws IOException {
		Utility utility = new Utility();
		DFS dfs = new DFS();
		BFS bfs = new BFS();
		SA sa = new SA();

		String fileName = "input3.txt";
		FileContents info = utility.readFile(fileName);
		String[][] map = new String[info.sizeOfBoard][info.sizeOfBoard];
		if (info.algorithm.equals("DFS")) {
			map = dfs.search(info.trees, info.numOfLiz);
		} else if (info.algorithm.equals("BFS")) {
			map = bfs.search(info.trees, info.numOfLiz);
		} else if (info.algorithm.equals("SA")) {
			map = sa.search(info.trees, info.numOfLiz);
		}
		if (map == null)
			System.out.println("fail");
		else
			utility.showMap(map);
		utility.writeFile(map);

	}

}
