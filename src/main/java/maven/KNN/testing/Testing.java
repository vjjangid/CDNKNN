package maven.KNN.testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Testing {
	
	public static void main(String[] args) {
		
		List<Float> l = new ArrayList<Float>();
		l.add(1.23f);
		l.add(4.53f);
		l.add(43.56f);
		l.add(0.1f);
		Collections.sort(l);
		
		for(int i=0; i<l.size(); i++)
			System.out.println(l.get(i));
		
		
	}

}
