package hw3;

import java.io.IOException;
import java.util.*;

public class homework {

	
	public static void main(String[] args) throws IOException {
		String s = "~F(x,Alice,y)";
		String fileName = "HW3tc/9.txt";
		Utility util = new Utility();
		Resolution reso = new Resolution();
		FileInfo file = util.readFile(fileName);
		//util.showFile(file);
		List<Boolean> outPut = new LinkedList<Boolean>();
		for( Atomic q : file.query ){
			if( reso.resolution(q, file.kb) ){
				outPut.add(true);
			}
			else{
				outPut.add(false);
			}
		}
		for( boolean b : outPut ) System.out.println(b);
		util.writeFile(outPut);
	}
}
