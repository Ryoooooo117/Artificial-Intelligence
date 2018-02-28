
import java.io.*;
import java.util.*;

public class test {

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		Utility util = new Utility();
		AlphaBeta4 ab = new AlphaBeta4();
		//AlphaBeta5 ab = new AlphaBeta5();
		//backup1 ab = new backup1();
		String fileName = "input.txt";
		FileInfo info = util.readFile(fileName);
		
		int maxLevel = 3;
		//if( info.time > 220 || info.boardSize<=15 ) maxLevel = 4;
		char[][] board = info.board;
		State state = new State(board,0);
		System.out.println("maxLevel:"+maxLevel);
		Action res = ab.alphaBetaSearch(state,maxLevel);
		long endTime = System.currentTimeMillis();
		//util.showBoard(res.board);
		System.out.println("time:"+(endTime-startTime)+" point:"+res.value);
		//System.out.println("p:"+res.point+" d:"+res.delta+" t:"+(endTime-startTime));
	}
}
