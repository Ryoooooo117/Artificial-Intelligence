package hw3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
			data.add(str);
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
					kb.AtomicList.add(getAtomic(strs[0]));
				}
				else{
					List<Atomic> ats = new LinkedList<Atomic>();
					for( String s : strs ){
						ats.add(getAtomic(s));
					}
					kb.DNFList.add(ats);
				}
			}
			file.kb = kb;
		}
		fr.close();
		return file;	
	}
	
	public Atomic getAtomic(String s){
		Atomic atom = new Atomic();
		String predicate = "";
		List<String> var = new LinkedList<String>();
		List<Integer> varType = new LinkedList<Integer>();
		int connective = 1;
		if( s.charAt(0) == '~' ){
			connective = -1;
			s = s.substring(1);
		}
		int index = 0;
		for( ; index < s.length() ; index++ ){
			if( s.charAt(index)!='(' ){
				predicate += s.charAt(index);
			}
			else break;
		}
		String temp = s.substring(index+1, s.length()-1);
		String[] str = temp.split(",");
		for( String t : str ){
			var.add(t);
			if( t.length() == 1 ) varType.add(0);
			else varType.add(1);
		}
		atom.predicate = predicate;
		atom.var = var;
		atom.varType = varType;
		atom.connective = connective;
		return atom;
	}
}
