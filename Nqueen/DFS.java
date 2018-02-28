
import java.util.*;

public class DFS {

	Utility util = new Utility();

	public String[][] search(String[][] map, int liz) {

		Stack<Integer> prevCol = new Stack<Integer>();
		Stack<Integer> prevRow = new Stack<Integer>();
		Stack<String[][]> mapStack = new Stack<String[][]>();

		int row = 0, col = 0, n = map.length;
		while (row >= 0) {
			if (liz == 0)
				break;
			else if (row == n && liz != 0) {
				List<String[][]> temp = new LinkedList<String[][]>();
				if (util.putLiz(map, liz, temp)) {
					map = temp.get(0);
					break;
				} else
					col = n;
			}
			if (col < n) {
				if (util.isValid(map, row, col)) {
					mapStack.push(util.copyMap(map));
					prevRow.push(row);
					prevCol.push(col);
					map = util.updateMap(map, row, col);
					liz--;
					row++;
					col = 0;
				} else
					col++;
			} else {
				if (prevCol.isEmpty())
					return null;
				col = prevCol.pop() + 1;
				row = prevRow.pop();
				map = mapStack.pop();
				liz++;
			}
		}
		util.transformMap(map);
		return map;
	}

}
