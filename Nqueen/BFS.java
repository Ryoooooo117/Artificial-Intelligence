
import java.util.*;

public class BFS {

	Utility util = new Utility();

	public String[][] search(String[][] map, int liz) {
		List<List<String[][]>> queue = new LinkedList<List<String[][]>>();
		List<String[][]> temp = new LinkedList<String[][]>();
		int row = 0, col = 0, n = map.length;
		temp.add(util.copyMap(map));
		queue.add(temp);
		int count = 0;
		while (row < n) {
			List<String[][]> tempState = new LinkedList<String[][]>();
			for (String[][] state : queue.get(0)) {
				while (col < n) {
					if (util.isValid(state, row, col)) {
						tempState.add(util.copyMap(util.updateMap(state, row, col)));
					}
					col++;
				}
				col = 0;
			}
			if( !tempState.isEmpty() ){
				queue.add(tempState);
				queue.remove(0);
				liz--;
			}
			row++;
			count++;
		}
		if( liz==0 ){
			String[][] resMap = queue.get(0).get(0);
			util.transformMap(resMap);
			return resMap;
		}
		for (List<String[][]> list : queue) {
			for (String[][] tempMap : list) {
				List<String[][]> res = new LinkedList<String[][]>();
				if (util.putLiz(tempMap, liz, res)){
					String[][] resMap = res.get(0);
					util.transformMap(resMap);
					return resMap;
				}
			}
		}
		return null;
	}

	public String[][] checkMap(String[][] map, int liz) {
		int n = map.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j].equals("1"))
					liz--;
			}
		}
		if (liz == 0)
			return util.copyMap(map);
		else
			return null;
	}

}
