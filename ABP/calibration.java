
import AlphaBeta5.*;

import java.io.FileWriter;
import java.io.IOException;

public class calibration {
	
	static Utility util = new Utility();
	
	public static char[][] generateBoard(int n, int fruitNumber){
		char[] fruits = new char[fruitNumber];
		for( int i = 0 ; i < fruitNumber ; i++ ){
			fruits[i] = (char)('0'+i);
		}
		char[][] res =  new char[n][n];
		for( int i = 0 ; i < n ; i++ ){
			for( int j = 0 ; j < n ; j++ ){
				int random = (int) (Math.random()*(fruitNumber));
				res[i][j] = fruits[random];
			}
		}
		return res;
	}
	
	public static void writeFile(char[][] board) throws IOException {
		FileWriter fw = new FileWriter("cases.txt",true);
		int size = board.length;
		fw.write(size+"\r\n");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				fw.write(board[i][j]);
			}
			fw.write("\r\n");
		}
		fw.write("\r\n");
		fw.close();
	}
	
	static int loseCount = 0;
	
	public static void battle(int size, int fruitNumber, int p1L, int p2L) throws IOException{
		char[][] board = generateBoard(size,fruitNumber);
		String fileName = "input.txt";
		FileInfo info = util.readFile(fileName);
		//char[][] board = info.board;
		int p1 = 0, p2 = 0;
		AlphaBeta5 ab = new AlphaBeta5();
		State state = new State(util.copyBoard(board),0);
		Result nextStates = ab.getAllNextState(state);
		//for( int i = 0 ; i < 10 ; i++ ){
		int i = 0, time=0, count = 1;
		while( nextStates.number>0 ){
			Action res = new Action();
			if( i%2 == 0 ){
				long startTime = System.currentTimeMillis();
				//if( count>0 ) p1L = 4;
				res = ab.alphaBetaSearch(state,p1L);
				count--;
				long endTime = System.currentTimeMillis();
				time += (endTime-startTime);
				//System.out.println("t:"+(endTime-startTime));
				state = ab.getCurState(nextStates,state,res.fruitNumber,1);
				p1 += state.point;
			}
			else{
				res = ab.alphaBetaSearch(state,p2L);
				state = ab.getCurState(nextStates,state,res.fruitNumber,1);
				p2 += state.point;
			}
			state.delta = 0 ; state.point = 0;
			i++;
			nextStates = ab.getAllNextState(state);
		}
		System.out.println("p1:"+p1+" p2:"+p2+" time:"+time);
		if( p1<=p2 ){
			loseCount++;
			System.out.println("         lose");
			writeFile(board);
		}
	}
	
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		
		
		int size = 26, fruitNumber = 9, p1 = 3, p2 = 3;
		//for( int s = 18 ; s<=26 ; s++ ){
		//	size=s;
			//System.out.println("size:"+size);
			for( int i = 1 ; i <= 30 ; i++ ) {
				System.out.print(i+"   ");
				battle(size,fruitNumber,p1,p2);
			}
			System.out.println();
		//}
		System.out.println("lose times:"+loseCount);
		/*
		Result nextStates = ab.getAllNextState(state);
		State resState = ab.getCurState(nextStates,state,res.fruitNumber,1);
		util.showBoard(resState.board);
		*/
		//System.out.println("time:"+(endTime-startTime));
		System.out.println("end");
		
	}
}
