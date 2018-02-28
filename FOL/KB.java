
import java.util.*;

public class KB {
	List<Atomic> AtomicList;
	List<List<Atomic>> DNFList;
	KB(){
		this.AtomicList = new LinkedList<Atomic>();
		this.DNFList = new LinkedList<List<Atomic>>();
	}
}
