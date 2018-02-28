
import java.io.*;
import java.util.*;

public class Utility {

	/*
	public class FileContents {
		String algorithm;
		int sizeOfBoard;
		int numOfLiz;
		String[][] trees;
	}
	*/

	public FileContents readFile(String fileName) throws IOException {
		FileReader fr = new FileReader(fileName);
		BufferedReader bf = new BufferedReader(fr);
		String str = null;
		List<String> contents = new LinkedList<String>();
		while ((str = bf.readLine()) != null) {
			contents.add(str);
		}
		// System.out.println("read end");
		FileContents file = new FileContents();
		if (contents.size() >= 4) {
			file.algorithm = contents.get(0);
			file.sizeOfBoard = Integer.parseInt(contents.get(1));
			file.numOfLiz = Integer.parseInt(contents.get(2));
			int size = file.sizeOfBoard;
			String[][] board = new String[size][size];
			for (int i = 0, listLine = 3; i < size; i++, listLine++) {
				for (int j = 0; j < size; j++) {
					board[i][j] = String.valueOf(contents.get(listLine).charAt(
							j));
				}
			}
			file.trees = board;
		}
		fr.close();
		return file;
	}

	public void writeFile(String[][] map) throws IOException {
		FileWriter fw = new FileWriter("output.txt");
		if (map == null)
			fw.write("FAIL");
		else {
			fw.write("OK\r\n");
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map.length; j++) {
					fw.write(map[i][j]);
				}
				fw.write("\r\n");
			}
		}
		fw.close();

	}

	public void showMap(String[][] map) {
		int n = map.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	public String[][] copyMap(String[][] map) {
		int n = map.length;
		String[][] copy = new String[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				copy[i][j] = map[i][j];
			}
		}
		return copy;
	}

	public boolean isValid(String[][] map, int row, int col) {
		if (map[row][col].equals("0"))
			return true;
		return false;
	}

	public String[][] updateMap(String[][] map, int row, int col) {
		String[][] res = copyMap(map);
		res[row][col] = String.valueOf((Integer.valueOf(res[row][col]) ^ 1));
		int n = map.length;
		// column update
		for (int i = 1; col + i < n; i++) {
			if (res[row][col + i].equals("2"))
				break;
			else if (res[row][col + i].equals("0"))
				res[row][col + i] = "x";
		}
		for (int i = 1; col - i >= 0; i++) {
			if (res[row][col - i].equals("2"))
				break;
			else if (res[row][col - i].equals("0"))
				res[row][col - i] = "x";
		}
		// row update
		for (int i = 1; row + i < n; i++) {
			if (res[row + i][col].equals("2"))
				break;
			else if (res[row + i][col].equals("0"))
				res[row + i][col] = "x";
		}
		for (int i = 1; row - i >= 0; i++) {
			if (res[row - i][col].equals("2"))
				break;
			else if (res[row - i][col].equals("0"))
				res[row - i][col] = "x";
		}
		// oblique update
		for (int i = 1; row + i < n && col + i < n; i++) { // right-down
			if (res[row + i][col + i].equals("2"))
				break;
			else if (res[row + i][col + i].equals("0"))
				res[row + i][col + i] = "x";
		}
		for (int i = 1; row + i < n && col - i >= 0; i++) { // left-down
			if (res[row + i][col - i].equals("2"))
				break;
			else if (res[row + i][col - i].equals("0"))
				res[row + i][col - i] = "x";
		}
		for (int i = 1; row - i >= 0 && col + i < n; i++) { // right-up
			if (res[row - i][col + i].equals("2"))
				break;
			else if (res[row - i][col + i].equals("0"))
				res[row - i][col + i] = "x";
		}
		for (int i = 1; row - i >= 0 && col - i >= 0; i++) { // left-up
			if (res[row - i][col - i].equals("2"))
				break;
			else if (res[row - i][col - i].equals("0"))
				res[row - i][col - i] = "x";
		}
		return res;
	}

	public void transformMap(String[][] map) {
		int n = map.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j].equals("x"))
					map[i][j] = "0";
			}
		}
	}

	public boolean putLiz(String[][] map, int liz, List<String[][]> list) {
		Stack<Integer> colStack = new Stack<Integer>();
		Stack<Integer> rowStack = new Stack<Integer>();
		Stack<String[][]> mapStack = new Stack<String[][]>();

		int row = 0, col = 0, n = map.length;
		String[][] temp = copyMap(map);
		for (; row < n; row++) {
			for (; col < n; col++) {
				if (temp[row][col].equals("0")) {
					rowStack.push(row);
					colStack.push(col);
					mapStack.push(copyMap(temp));
					temp = updateMap(temp, row, col);
					liz--;
				}
			}
			col = 0;
			if (liz == 0) {
				list.add(temp);
				return true;
			} else if (row == n - 1 && liz != 0) {
				if (mapStack.isEmpty())
					return false;
				temp = mapStack.pop();
				row = rowStack.pop();
				col = colStack.pop() + 1;
				liz++;
			}
		}
		return false;
	}

}
