import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Derivation;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.VCARD;


public class Experiment01 {
	
	static long t1;
	static long t2;
	static long t3;
	static long t4;
	static long t5;
	
	// testing shows that the upper limit of N is around 2000
	// java heap space
	
	static void calculate(int N) {
		// doesn't work if you don't put NS in front of the name
		
		String NS = "urn:x-hp-jena:eg/";
		Model model = ModelFactory.createDefaultModel();
		
		int j;
		
		Resource r[] = new Resource[N];
		
		t1 = System.currentTimeMillis();
		
		r[N-1] = model.createResource(NS+"r"+Integer.toString(N-1)).addProperty(VCARD.N, "r"+Integer.toString(N-1));
		
		for (j=N-2;j>=0;j--) {
			
			//System.out.println(j);
			
			r[j] = model.createResource(NS+"r"+Integer.toString(j)).addProperty(VCARD.N, "r"+Integer.toString(j))
				.addProperty(RDFS.subClassOf, model.getResource(NS+"r"+Integer.toString(j+1)));
		}
		
		t2 = System.currentTimeMillis();
		
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
	    reasoner.setDerivationLogging(true);
	    InfModel inf = ModelFactory.createInfModel(reasoner, model);
        
	    t3 = System.currentTimeMillis();
	    
	    
		PrintWriter out = new PrintWriter(System.out);
	    for (StmtIterator i = inf.listStatements(model.getResource(NS+"r"+Integer.toString(0)), RDFS.subClassOf, model.getResource(NS+"r"+Integer.toString(N-1))); i.hasNext(); ) {
	        
	    	Statement s = i.nextStatement(); 
	        //System.out.println("Statement is " + s);
	        
	    	// the for can be excluded from the experiment
	    	// the program never goes into the for
	        
	        for (Iterator id = inf.getDerivation(s); id.hasNext(); ) {
	            Derivation deriv = (Derivation) id.next();
	            deriv.printTrace(out, true);
	        }
	        
	    }
	    out.flush();
		
	    t4 = System.currentTimeMillis();
	}
	
	public static void main(String[] args) throws Exception {
		int i;
		
		// because we want to be more accurate we make more precisious calculations
		
		int sum[] = new int[201];
		
		for (int k = 1;k<=10;k++) {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test"+k+".txt")));
			
			System.out.println("Experiment "+k);
			
			for (i=10;i<=2000;i+=10) {
				
				System.out.println(i);
			
				calculate(i);
			
				out.print(i+" ");
				out.print((t2-t1)+" ");
				out.print((t3-t2)+" ");
				out.println((t4-t3));
				
				sum[i/10] += (t4-t3);
				
			}
			
			out.close();
		}
		
		PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("test_average.txt")));
		PrintWriter fout1 = new PrintWriter(new BufferedWriter(new FileWriter("test_average_only_data.txt")));
		
		
		for (i=10;i<=2000;i+=10) {
			fout.println(i+" "+(sum[i/10]/10));
			fout1.println(sum[i/10]/10);
		}
		
		fout.close();
		fout1.close();
		
	}

}
