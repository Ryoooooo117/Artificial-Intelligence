
import java.io.IOException;
import java.util.*;

public class AlphaBeta5 {
	
	class Result{
		int[][] board;
		int number;
	}
	
	int cutCount=0;
	int sum=0;
	Utility util = new Utility();
	
	public Action alphaBetaSearch(State state, int maxLv) throws IOException{
		int alpha = Integer.MIN_VALUE ,beta = Integer.MAX_VALUE;
		Result nextStates = getAllNextState(state);
		Action res = maxValue(state,alpha,beta,maxLv);
		State resState = getCurState(nextStates,state,res.fruitNumber,1);
		//util.showBoard(resState.board);
		//System.out.println("v:"+res.value*2+" i:"+res.fruitNumber);
		//System.out.println("cutCount: "+cutCount+" sum:"+sum);
		util.writeFile("output.txt",resState.board,nextStates.board,res);
		return res;
	}
	
	public Action maxValue(State state, int alpha, int beta, int level){ 
		sum++;
		if( level == 1 ){
			Action res = getMax(state,1);
			return res;
		}
		int value = Integer.MIN_VALUE;
		Action res = new Action();
		Result nextStates = getAllNextState(state);
		/*
		if( nextStates.number == 0 ){
			return getMax(state,1);
		}*/
		for( int i = 1; i<=nextStates.number ; i++ ){
			State curState = getCurState(nextStates,state,i,1);
			Action next = minValue(curState,alpha,beta,level-1);
			if( next.value > value ){
				value = next.value ;
				res.value = value;
				res.fruitNumber = i;
			}
			if( value>=beta ){
				cutCount++;
				return res;
			}
			alpha = Math.max(alpha, value);
		}
		return res;
	}
	
	public Action minValue(State state, int alpha, int beta, int level){
		sum++;
		if( level == 1 ){
			Action res = getMax(state,2);
			return res;
		}
		int value = Integer.MAX_VALUE;
		Action res = new Action();
		Result nextStates = getAllNextState(state);
		/*
		if( nextStates.number == 0 ){
			return getMax(state,2);
		}
		*/
		for( int i = 1; i<=nextStates.number ; i++ ){
			State curState = getCurState(nextStates,state,i,2);
			Action next = maxValue(curState,alpha,beta,level-1);
			if( next.value < value){
				value = next.value;
				res.value = value;
				res.fruitNumber = i;
			}
			if( value<=alpha ){
				cutCount++;
				return res;
			}
			beta = Math.min(beta, value);
		}
		//System.out.println(" v:"+value+" p:"+res.point+" d:"+newDelta);
		return res;
	}
	
	public Action getMax(State state, int player){
		Result nextState = getAllNextState(state);
		Action res = new Action();
		int[][] board = util.copyBoard(nextState.board);
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for( int i = 0 ; nextState.number > 0 && i < board.length ; i++ ){
			for( int j = 0 ; j < board.length ; j++ ){
				if( board[i][j]!=0 ){
					if( map.containsKey(board[i][j]) ){
						map.replace(board[i][j], map.get(board[i][j])+1);
					}
					else map.put(board[i][j], 1);
				}
			}
		}
		int max = nextState.number>0? Integer.MIN_VALUE:0;
		int fruit = 0;
		for( int i = 1 ; i<=map.size() ; i++ ){
			if( max<map.get(i) ){
				max = map.get(i);
				fruit = i;
			}
		}
		if( player == 1 ) res.value = state.delta+max*2;
		else res.value = state.delta-max*2;
		res.fruitNumber = fruit;
		return res;
	}
	
	public Result getAllNextState(State state){
		int n = state.board.length, fruitNumber = 1;
		int[][] fruitBoard = new int[n][n];
		for( int i = 0 ; i<n ; i++ ){
			for( int j = 0 ; j<n ; j++ ){
				if( state.board[i][j] != '*' && fruitBoard[i][j]==0){
					util.checkBoard2(state.board, fruitBoard, state.board[i][j], i, j, fruitNumber);
					fruitNumber++;
				}
			}
		}
		Result res = new Result();
		res.board = fruitBoard;
		res.number = fruitNumber-1;
		return res;
	}
	
	public boolean allFilled(State state){
		int n = state.board.length;
		for( int i = 0 ; i<n ; i++ ){
			for( int j = 0 ; j<n ; j++ ){
				if( state.board[i][j] != '*' ) return false;
			}
		}
		return true;
	}
	
	public State getCurState(Result nextState, State state, int fruitNumber, int player) {
		char[][] board = util.copyBoard(state.board);
		int point = 0;
		for( int i = 0 ; i < nextState.board.length ; i++ ){
			for( int j = 0 ; j < nextState.board.length ; j++ ){
				if( nextState.board[i][j] == fruitNumber ){
					board[i][j] = '*';
					point+=2;
				}
			}
		}
		util.fallDown(board);
		State res = new State(board,point);
		if( player == 2 ) res.delta = state.delta-point;
		else res.delta = state.delta+point;
		return res;
	}
	
}

