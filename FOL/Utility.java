package hw3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Utility {
	
	public FileInfo readFile(String fileName) throws IOException{
		FileReader fr = new FileReader(fileName);
		BufferedReader bf = new BufferedReader(fr);
		String str = null;
		List<String> data = new LinkedList<String>();
		while ((str = bf.readLine()) != null) {
			str = str.trim();
			data.add(str);
			//System.out.println(str);
		}
		// System.out.println("read end");
		FileInfo file = new FileInfo();
		if (data.size() >= 1) {
			int index = 0;
			file.querySize = Integer.parseInt(data.get(index++));
			List<Atomic> query = new LinkedList<Atomic>();
			for( int i = 0; i < file.querySize ; i++ ){
				query.add(getAtomic(data.get(index++)));
			}
			file.query = query;
			file.KBSize = Integer.parseInt(data.get(index++));
			KB kb = new KB();
			for( int i = 0 ; i < file.KBSize ; i++ ){
				String line = data.get(index++);
				String[] strs = line.split("\\|");
				if( strs.length == 1 ){
					List<Atomic> temp = new LinkedList<Atomic>();
					temp.add(getAtomic(strs[0]));
					kb.clauses.add(temp);
				}
				else{
					List<Atomic> ats = new LinkedList<Atomic>();
					for( String s : strs ){
						ats.add(getAtomic(s));
					}
					kb.clauses.add(ats);
				}
			}
			file.kb = kb;
		}
		fr.close();
		return file;	
	}
	
	public Atomic getAtomic(String s){
		s = s.trim();
		Atomic atom = new Atomic();
		String predicate = "";
		List<String> var = new LinkedList<String>();
		List<Integer> varType = new LinkedList<Integer>();
		int connective = 1;
		if( s.charAt(0) == '~' ){	// judge the positive and negative sign first
			connective = -1;		// connective == -1 means negative
			s = s.substring(1);
		}
		int index = 0;
		for( ; index < s.length() ; index++ ){	// get the predicate before '('
			if( s.charAt(index)!='(' ){
				predicate += s.charAt(index);
			}
			else break;
		}
		String temp = s.substring(index+1, s.length()-1);	// get the vars in atomic
		String[] str = temp.split(",");
		for( String t : str ){
			var.add(t);
			if( (int)t.charAt(0) <= 122 && (int)t.charAt(0) >= 97 ) varType.add(-1); // if it is variable
			else varType.add(1);
		}
		atom.predicate = predicate;
		atom.var = var;
		atom.varType = varType;
		atom.connective = connective;
		return atom;
	}
	
	public void writeFile(List<Boolean> list) throws IOException{
		FileWriter fw = new FileWriter("output.txt");
		/*
		for( int i = 0 ; i < list.size() ; i++ ){
			Boolean b = list.get(i);
			if( i == list.size()-1 ){
				fw.write(b.toString());
			}
			else fw.write(b.toString()+"\r\n");
		}
		*/
		for( boolean b : list ){
			if( b==true ) fw.write("TRUE\r\n");
			else fw.write("FALSE\r\n");
		}
		fw.close();
	}
	
	public void showFile(FileInfo file){
		for( int i = 0 ; i < file.KBSize ; i++ ){
			List<Atomic> temp = file.kb.clauses.get(i);
			if( temp.size() == 1 ){
				System.out.println("atomic:");
				showAtomic(temp.get(0));
			}
			else{
				System.out.println("DNF:");
				for( int j = 0 ; j < temp.size() ; j++ ){
					showAtomic(temp.get(j));
				}
			}
		}
	}
	
	public void showAtomic(Atomic a){
		String con = "";
		String var = "";
		if( a.connective==-1) con+="~";
		for( int j = 0 ; j < a.var.size() ; j++ ){
			var += a.var.get(j)+" ";
		}
		System.out.println("  "+con+a.predicate+" "+var+" ");
	}
}
