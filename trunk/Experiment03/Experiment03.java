import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;


public class Experiment03 {
	
	public static void main(String[] args) {
		int i,j,k;
		long t1,t2,t3,t4,t5,t6;
		
		File directory = new File("owl library");
		String filename[] = directory.list();
		int N = filename.length;
		String fullpath[] = new String[N];
		
		for (i = 0; i < filename.length; i++) {
			fullpath[i] = "owl library//"+filename[i];
			//System.out.println(fullpath[i]);
		}
		
		for (i=0;i<N;i++) {
			// we read every file
			
			
			Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open(fullpath[i]);
	        if (in == null) {
	            throw new IllegalArgumentException( "File: " + fullpath[i] + " not found");
	        }
	        
	        System.out.println("START OF READING "+filename[i]);
			
	        t1 = System.currentTimeMillis();
	        
	        // read the RDF/XML file
	        model.read(in, "");
			
	        t2 = System.currentTimeMillis();
	        
	        System.out.println("The file was read in "+(t2-t1)+" miliseconds");
	        
	        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		    InfModel inf = ModelFactory.createInfModel(reasoner, model);
	        
		    t3 = System.currentTimeMillis();
		    
		    System.out.println("The inference model was created in "+(t3-t2)+" miliseconds");
		    int counter = 0;
		    
		    StmtIterator pom = inf.listStatements();
		    
		    t4 = System.currentTimeMillis();
		    
		    Statement test[] = new Statement[5000];
		    
		    for (; pom.hasNext(); ) {
		        
		    	Statement s = pom.nextStatement(); 
		        //System.out.println("Statement is " + s);
		    	
		    	test[counter] = s;
		    	
		    	//System.out.println(counter);
		    	counter++;
		    	
		    	if (counter == 3000)
		    		break;
		    	
		    }
		    
		    t5 = System.currentTimeMillis();
		    double averagetime = (double)(t5-t4)/(double)counter;
		    DecimalFormat df = new DecimalFormat("0.000");
		    
		    System.out.println("Average time for finding a statement is "+df.format(averagetime)+" miliseconds");
		    
		    for (i=0;i<counter;i++) {
		    	//System.out.println(i);
		    	inf.contains(test[i]);
		    }
		    
		    t6 = System.currentTimeMillis();
		    
		    averagetime = (double)(t6-t5)/(double)counter;
		    
		    System.out.println("Average time for executing a query is "+df.format(averagetime)+" miliseconds");
		    
	        //model.write(System.out);
		}
		
		// System.out.print(listFilenames);
		/*
		fr = new FileReader (listFilenames);
		br = new BufferedReader(fr);
		Scanner scan = new Scanner(br);
		*/
		
	}
	
}
