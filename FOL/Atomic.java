package hw3;

import java.util.*;

public class Atomic {
	String predicate;
	List<String> var;
	List<Integer> varType;	// -1-variable; 1-constant
	int connective;			// -1-not;
	
	Atomic(){}
	
	Atomic(String p,List<String> var,List<Integer> varType,int connective){
		this.predicate = p;
		this.var = new LinkedList<String>(var);
		this.varType = new LinkedList<Integer>(varType);
		this.connective = connective;
	}
	
	boolean equal(Atomic a){
		if( !this.predicate.equals(a.predicate) ) return false;
		if( this.connective != a.connective ) return false;
		for( int i = 0 ; i < a.var.size() ; i++ ){
			if( !this.var.get(i).equals(a.var.get(i)) ) return false;
		}
		for( int i = 0 ; i < a.varType.size() ; i++ ){
			if( !this.varType.get(i).equals(a.varType.get(i)) ) return false;
		}
		return true;
	}
	
	boolean converse(Atomic a){
		if( !this.predicate.equals(a.predicate) ) return false;
		if( this.connective == a.connective ) return false;
		for( int i = 0 ; i < a.var.size() ; i++ ){
			if( !this.var.get(i).equals(a.var.get(i)) ) return false;
		}
		for( int i = 0 ; i < a.varType.size() ; i++ ){
			if( !this.varType.get(i).equals(a.varType.get(i)) ) return false;
		}
		return true;
	}
}
