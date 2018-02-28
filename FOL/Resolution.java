
import java.util.*;

public class Resolution {
	
	public boolean resolution( Atomic query, KB kb ){
		Atomic reQuery = reverseQuery(query);
		List<List<Atomic>> tDNFs = findAllReverseDNF(reQuery,kb);
		List<Atomic> tempA = new LinkedList<Atomic>();
		tempA.add(reQuery);
		List<List<Atomic>> resoDNF = resolve(reQuery,tempA,tDNFs);
		for( int i = 0 ; i < resoDNF.size() ; i++ ){
			List<Atomic> clause = resoDNF.get(i);
			
			System.out.println("  resoDNF");
			showDNF(resoDNF);
			System.out.println("  clause");
			showAtoms(clause);
			
			for( Atomic q: clause ){ 
				List<List<Atomic>> t = findAllReverseDNF(q,kb);
				if( !t.isEmpty() ){
					List<List<Atomic>> temp = resolve(q,clause,t);
					if( !temp.isEmpty() && temp.get(0).isEmpty() ) return true;
					int loopFlag = 0;
					for( List<Atomic> l : temp ){
						if( containsDNF(resoDNF,l) ){
							loopFlag = 1; break;
						}
					}
					if( loopFlag == 1 ) continue;	// if duplicate, continue
					if( temp.isEmpty() ) break;
					else if( !temp.isEmpty() ){
						resoDNF.addAll(temp);
						break;
					}
				}
			}
		}
		return false;
	}
	
	public Atomic reverseQuery (Atomic query ){
		Atomic res = new Atomic();
		res.connective = -query.connective;
		res.predicate = query.predicate;
		List<String> var = new LinkedList<String>(query.var);
		List<Integer> varType = new LinkedList<Integer>(query.varType);
		res.var = var;
		res.varType = varType;
		return res;
	}
	
	
	public List<List<Atomic>> findAllReverseDNF( Atomic query, KB kb ){
		//List<List<Atomic>> clauses = new LinkedList<List<Atomic>>(kb.clauses);
		List<List<Atomic>> res = new LinkedList<List<Atomic>>();
		for( List<Atomic> atoms : kb.clauses ){
			int flag = 0;
			List<Atomic> a = new LinkedList<Atomic>(atoms);
			for( Atomic atom : atoms ){
				if( atom.predicate.equals(query.predicate) && atom.connective*query.connective == -1 ){
					// modify original DNF, put the same predicate target in first
					int index = a.indexOf(atom);
					a.remove(index);
					a.add(0, atom);
					flag = 1;
					//res.add(atoms);
				}
			}
			if( flag == 1 ) res.add(a);
		}
		return res;
	}
	
	
	
	public List<List<Atomic>> resolve(Atomic query, List<Atomic> qDNF, List<List<Atomic>> tDNFs){
		List<List<Atomic>> res = new LinkedList<List<Atomic>>();
		List<List<Atomic>> clauses = copyClauses(tDNFs);
		int falseFlag = 0;
		for( List<Atomic> ts : clauses ){
			// if cannot unify, next dnf
			//List<Atomic> qTemp = (qDNF),tTemp = (ts);
			List<Atomic> qTemp = copyList(qDNF),tTemp = copyList(ts);
			boolean u1 = unify(query, qTemp,tTemp), u2 = unify(query,tTemp,qTemp);
			if( u1 == false && u2 == false ){
				falseFlag++;
				continue;
			}
			
			System.out.println("t:");
			showAtoms(tTemp);
			System.out.println("q:");
			showAtoms(qTemp);
			System.out.println("new:");
			
			List<Atomic> newDNF = combine(qTemp,tTemp,query);
			showAtoms(newDNF);
			res.add(newDNF);
		}
		//System.out.println("reso:");
		//showDNF(res);
		return res;
	}
	
	// return new t
	
	public boolean unify(Atomic query, List<Atomic> q, List<Atomic> t){
		Map<String,String> map = new HashMap<String,String>();
		Atomic a = new Atomic();
		Set<String> duplicateList = new HashSet<String>();
		for( Atomic temp : q){
			if( temp.predicate.equals(query.predicate) ){
				a = temp;
				break;
			}
		}
		for( Atomic temp : t){ //q.c -> t.v || q.v -> t.v  ==> map(q,t)
			if( temp.predicate.equals(a.predicate ) && !duplicateList.contains(temp.predicate) ){
				duplicateList.add(temp.predicate);
				for( int i = 0 ; i < a.var.size() ; i++ ){
					if( (temp.varType.get(i)==-1 && a.varType.get(i)==1) 
							|| temp.var.get(i).equals(a.var.get(i))
							|| (temp.varType.get(i)==-1 && a.varType.get(i)==-1)){
						if( map.containsKey(a.var.get(i)) && !map.get(a.var.get(i)).equals(temp.var.get(i))) continue;
						map.put(a.var.get(i),temp.var.get(i));
					}
					else if( temp.varType.get(i)==1 && a.varType.get(i)==1 && !temp.var.get(i).equals(a.var.get(i)) ){
						map.clear();
						break;
					}
				}
			}
		}
		for( Atomic temp : t){
			if( !map.isEmpty() ){
				for( int i = 0 ; i < temp.var.size() ; i++ ){
					if( map.containsValue(temp.var.get(i)) ){
						String value = "";
						for( String s : map.keySet() ){
							if( map.get(s).equals(temp.var.get(i)) ) value = s;
						}
					 	temp.var.set(i,value);
					 	int type = 1;
					 	if( (int)temp.var.get(i).charAt(0) <= 122 && (int)temp.var.get(i).charAt(0) >= 97 ) type=-1;
						temp.varType.set(i, type);
					}
				}
			}
		}
		if( map.isEmpty() ) return false;
		else return true;
	}
	
	
	public List<Atomic> combine(List<Atomic> q, List<Atomic> t, Atomic query){
		List<Atomic> res = new LinkedList<Atomic>();
		List<Integer> dt = new LinkedList<Integer>();
		List<Integer> dq = new LinkedList<Integer>();
		for( int i = 0 ; i < t.size() ; i++ ){
			for( int j = 0 ; j < q.size() ; j++ ){
				if( t.get(i).converse(q.get(j)) ){
					dt.add(i);
					dq.add(j);
				}
			}
		}
		for( int i : dt ) t.remove(i);
		for( int i : dq ) q.remove(i);
		for( int i = 0 ; i < q.size() ; i++ ){
			Atomic aq = q.get(i);
			if( aq.equal(query) ) q.remove(i);
		}
		res.addAll(t);
		res.addAll(q);
		return res;
	}

	public boolean containsDNF(List<List<Atomic>> a , List<Atomic> b ){
		int count = 0;
		for( List<Atomic> clause : a ){
			if( clause.size() == b.size() ){
				for( int i = 0 ; i < clause.size() ; i++ ){
					for( int j = 0 ; j < clause.size() ; j++ ){
						if( clause.get(i).equal(b.get(j))) count++;
						//if( clause.get(i).equal(b.get(j))) return true;
					}
				}
			}
		}
		if( count == b.size() ) return true;
		return false;
	}
	
	public List<List<Atomic>> copyClauses(List<List<Atomic>> DNFs){
		List<List<Atomic>> res = new LinkedList<List<Atomic>>();
		for( int i = 0 ; i < DNFs.size() ; i++ ){
			List<Atomic> clause = new LinkedList<Atomic>(DNFs.get(i));
			List<Atomic> temp = copyList(clause);
			res.add(new LinkedList<Atomic>(temp));
		}
		return res;
	}
	
	public List<Atomic> copyList(List<Atomic> list){
		List<Atomic> res = new LinkedList<Atomic>();
		for( Atomic a : list ){
			res.add(new Atomic(a.predicate,a.var,a.varType,a.connective));
		}
		return res;
	}
	
 	public void showDNF(List<List<Atomic>> DNF){
		for( List<Atomic> atoms : DNF ){
			for( Atomic a : atoms ){
				String con = "";
				String var = "";
				if( a.connective==-1) con+="~";
				for( int j = 0 ; j < a.var.size() ; j++ ){
					var += a.var.get(j)+" ";
				}
				System.out.print(con+a.predicate+" "+var+" ");
				//System.out.println("        loc:"+atoms);
			}
			System.out.println();
		}
	}
	
	public void showAtoms(List<Atomic> atoms){
		if( atoms.isEmpty() || atoms == null ) return;
		for( Atomic a : atoms ){
			String con = "";
			String var = "";
			if( a.connective==-1) con+="~";
			for( int j = 0 ; j < a.var.size() ; j++ ){
				var += a.var.get(j)+" ";
			}
			System.out.print(con+a.predicate+" "+var+" ");
		}
		System.out.println();
	}
	
}
