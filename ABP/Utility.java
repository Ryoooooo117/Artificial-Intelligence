
import java.io.*;
import java.util.*;

public class Utility {

	public FileInfo readFile(String fileName) throws IOException {
		FileReader fr = new FileReader(fileName);
		BufferedReader bf = new BufferedReader(fr);
		String str = null;
		List<String> data = new LinkedList<String>();
		while ((str = bf.readLine()) != null) {
			data.add(str);
		}
		// System.out.println("read end");
		FileInfo file = new FileInfo();
		if (data.size() >= 4) {
			file.boardSize = Integer.parseInt(data.get(0));
			file.fruitType = Integer.parseInt(data.get(1));
			file.time = Float.parseFloat(data.get(2));
			int n = file.boardSize;
			char[][] board = new char[n][n];
			for (int i = 0, listLine = 3; i < n; i++, listLine++) {
				for (int j = 0; j < n; j++) {
					board[i][j] = data.get(listLine).charAt(j);
				}
			}
			file.board = board;
		}
		fr.close();
		return file;
	}
	
	public char[][] copyBoard(char[][] board) {
		int n = board.length;
		char[][] copy = new char[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				copy[i][j] = board[i][j];
			}
		}
		return copy;
	}
	
	public int[][] copyBoard(int[][] board) {
		int n = board.length;
		int[][] copy = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				copy[i][j] = board[i][j];
			}
		}
		return copy;
	}
	
	public void checkBoard(char[][] board, char fruit, int row, int col, State state){
		if( board[row][col]!=fruit || board[row][col]==('*') ) return;
		board[row][col]='*';
		state.point++;
		//up: row-1, col
		if( row-1>=0 ) checkBoard(board,fruit,row-1,col,state);
		//down: row+1, col
		if( row+1<board.length ) checkBoard(board,fruit,row+1,col,state);
		//left: row, col-1
		if( col-1>=0 ) checkBoard(board,fruit,row,col-1,state);
		//right: row, col+1
		if( col+1<board.length ) checkBoard(board,fruit,row,col+1,state);
		
		state.board = board;
	}
	
	public void checkBoard2(char[][] board, int[][] fruitBoard, char fruit, int row, int col, int fruitNumber){
		if( board[row][col]!=fruit || board[row][col]==('*') || fruitBoard[row][col]!=0) return;
		fruitBoard[row][col]=fruitNumber;
		//up: row-1, col
		if( row-1>=0 ) checkBoard2(board,fruitBoard,fruit,row-1,col,fruitNumber);
		//down: row+1, col
		if( row+1<board.length ) checkBoard2(board,fruitBoard,fruit,row+1,col,fruitNumber);
		//left: row, col-1
		if( col-1>=0 ) checkBoard2(board,fruitBoard,fruit,row,col-1,fruitNumber);
		//right: row, col+1
		if( col+1<board.length ) checkBoard2(board,fruitBoard,fruit,row,col+1,fruitNumber);
	}
	
	public void modifyBoard(char[][] boardM,char[][] boardO){
		char[][] board = copyBoard(boardO);
		for( int i = 0 ; i < boardO.length ; i++ ){
			for( int j = 0 ; j < boardO.length ; j++ ){
				if( board[i][j]==('*') ) boardM[i][j]='*';
			}
		}
		
	}
	
	public void fallDown(char[][] board){
		int n = board.length;
		for( int col=0 ; col<n ; col++ ){
			List<Integer> stars = new LinkedList<Integer>();
			List<Character> fruits = new LinkedList<Character>();
			for( int row=0 ; row<n ; row++ ){
				if( board[row][col] == '*' ) stars.add(row);
				else fruits.add(board[row][col]);
			}
			if( stars.isEmpty() ) continue;
			for( int row=0 ; row<stars.size() ; row++ ){
				board[row][col] = '*';
			}
			for( int row=stars.size(), idx=0 ; row<n ; row++ ){
				board[row][col] = fruits.get(idx++);
			}
		}
		
	}

	public void showBoard(char[][] board){
		for( int i = 0 ; i<board.length ; i++ ){
			for( int j = 0 ; j<board.length ; j++ ){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void showBoard(int[][] board){
		for( int i = 0 ; i<board.length ; i++ ){
			for( int j = 0 ; j<board.length ; j++ ){
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void writeFile(String fileName, char[][] board, int[][] fruits, Action res) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		if ( board == null )
			fw.write("FAIL");
		else{
			int row = -1, col = -1;
			for( int i = 0 ; i < fruits.length ; i++ ){
				for( int j = 0 ; j < fruits.length ; j++ ){
					if( fruits[i][j]==res.fruitNumber && res.fruitNumber != 0 ){
						row = i; col = j; break;
					}
				}
				if( row != -1 ) break; 
			}
			String loc = "";
			if( row!=-1 ){
				loc += (char) ('A'+col) + String.valueOf(row+1);
				fw.write(loc+"\r\n");
			}
			else fw.write("empty\r\n");
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					fw.write(board[i][j]);
				}
				fw.write("\r\n");
			}
		}
		fw.close();

	}
}
