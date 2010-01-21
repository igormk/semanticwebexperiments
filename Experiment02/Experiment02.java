import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
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


public class Experiment02 {
	
	// the max value of MAX_DEPTH
	// can be 16, 131071 nodes are created at this max depth
	
	static int MAX_DEPTH;
	static Model model;
	static String NS;
	static int counter;
	
	// a resource r is created, we need to put children and
	// to continue recursively in depth, or to leave the node
	
	static void createNodeTree(Resource r, int depth) {
		if (depth == MAX_DEPTH) {
			return;
		}
		
		Resource c1 = model.createResource(NS+counter);
		counter++;
		
		Resource c2 = model.createResource(NS+counter);
		counter++;
		
		c1.addProperty(RDFS.subClassOf, r);
		c2.addProperty(RDFS.subClassOf, r);
		
		createNodeTree(c1, depth+1);
		createNodeTree(c2, depth+1);
		
		
	}
	
	public static void main(String[] args) throws Exception {
		
		// we create the model
		NS = "urn:x-hp-jena:eg/";
		model = ModelFactory.createDefaultModel();
		counter = 0;
		// the value ised for MAX_DEPTH was 10
		MAX_DEPTH = 10;
		
		Resource root = model.createResource(NS+counter);
		counter++;
		createNodeTree(root, 0);
		
		//System.out.println(counter);
		
		//model.write(System.out);
		
		// we make 10 tests
		int testnum;
		int N = counter;
		
		int sum[][] = new int[N][N];
		
		for (testnum=1;testnum<=10;testnum++) {
			
			System.out.println("Trial "+testnum);
			
			PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("test"+testnum+".txt")));
			
			int i,j;
			
			long t1,t2,t3,t4;
			int totalrelations = 0;
			
			Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		    reasoner.setDerivationLogging(true);
		    InfModel inf = ModelFactory.createInfModel(reasoner, model);
			
			PrintWriter out = new PrintWriter(System.out);
			
			for (i=0;i<N;i++) {
				
				System.out.println(testnum+" "+i);
				
				for (j=0;j<N;j++) {
					
					t1 = System.currentTimeMillis();
					
					for (StmtIterator k = inf.listStatements(model.getResource(NS+i), RDFS.subClassOf, model.getResource(NS+j)); k.hasNext(); ) {
				        
				    	Statement s = k.nextStatement(); 
				        //System.out.println("Statement is " + s);
				        
				        totalrelations++;
				    	
				        // the for can be excluded from the experiment
				    	// the program never goes into the for
				        
				        //for (Iterator id = inf.getDerivation(s); id.hasNext(); ) {
				        //	Derivation deriv = (Derivation) id.next();
				        //    deriv.printTrace(out, true);
				        //}
				        
				    }
					
					t2 = System.currentTimeMillis();
					
					sum[i][j] += (t2-t1);
					
					fout.println(i+" "+j+" "+(t2-t1));
					
				}
			}
			
			out.flush();
			
			//System.out.println(totalrelations);
		}
		
		System.out.println("START OF SECOND PHASE");
		
		// we average the results
		int i,j;
		
		for (i=0;i<N;i++) {
			for (j=0;j<N;j++) {
				sum[i][j] /= 10;
			}
		}
		
		// we print the results
		PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("test_average.txt")));
		
		// we print the results in sorted order
		
		int S[] = new int[N*N];
		int pom = 0;
		
		for (i=0;i<N;i++) {
			for (j=0;j<N;j++) {
				S[pom] = sum[i][j];
				pom++;
			}
		}
		
		Arrays.sort(S);
		
		PrintWriter fout1 = new PrintWriter(new BufferedWriter(new FileWriter("test_average_sorted.txt")));
		
		for (i=0;i<N*N;i++) {
			fout1.println(S[i]);
		}
		
		fout.flush();
		fout1.flush();
		
	}

}
