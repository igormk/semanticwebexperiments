import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;


public class Experiment04 {

	static String NS;
	
	public static void generateOntology(int N, OntModel model) {
		// we have N starting nodes and height of N+1
		int i,j,k;
		int left, right;
		OntClass oc;
		
		for (i=0;i<N;i++) {
			model.createClass(NS+"C_0_"+i);
		}
		
		for (i=1;i<=N+1;i++) {
			
			if (i%2==0) {
				
				// we have N nodes in this row
				for (j=0;j<N;j++) {
					oc = model.createClass(NS+"C_"+i+"_"+j);
					left = j;
					right = j+1;
					
					model.getOntClass(NS+"C_"+(i-1)+"_"+left).addProperty(RDFS.subClassOf, oc);
					model.getOntClass(NS+"C_"+(i-1)+"_"+right).addProperty(RDFS.subClassOf, oc);
					
				}
				
				
				
			} else {
				
				// we have N+1 nodes in this row
				for (j=0;j<=N;j++) {
					oc = model.createClass(NS+"C_"+i+"_"+j);
					left = j-1;
					right = j;
					
					if (left >= 0) {
						model.getOntClass(NS+"C_"+(i-1)+"_"+left).addProperty(RDFS.subClassOf, oc);
						
						
					}
					
					if (right < N) {
						model.getOntClass(NS+"C_"+(i-1)+"_"+right).addProperty(RDFS.subClassOf, oc);
					}
				}
				
			}
			
		}
		/*
		 * 
		 * for (i=0;i<N;i++) {
			model.createClass(NS+"C_0_"+i);
		}
		 * 
		 */
		// now we populate the ontology with individuals
		
		for (i=0;i<N;i++) {
			
			Individual ind = model.createIndividual(NS + "I_"+i, model.getOntClass(NS+"C_0_"+i));
		}
		
	}
	
	public static void calculate(int N) {
		long t1,t2,t3,t4,t5;
		// create the base model
		String SOURCE = "http://www.eswc2006.org/technologies/ontology";
		NS = SOURCE + "#";
		// this number should be odd
		
		System.out.println("BEGIN OF TEST");
		System.out.println("N = "+N);
		
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
		
		t1 = System.currentTimeMillis();
		
		generateOntology(N, model);
		
		t2 = System.currentTimeMillis();
		
		System.out.println("Ontology was created in "+(t2-t1)+" miliseconds");
		
		//OntClass oc = model.getOntClass(NS+"C_"+(N+1)+"_0");
		
		OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, model);
		/*
		for (ExtendedIterator ei = inf.lis;ei.hasNext();) {
			
			//ei.next();
			OntClass pom = (OntClass) ei.next();
			System.out.println(pom.toString());
			//System.out.println(pom.getURI());
		}
		*/
		
		t3 = System.currentTimeMillis();
		
		System.out.println("Ontology model was created in "+(t3-t2)+" miliseconds");
		
		int counter = 0;
		
		for (ExtendedIterator ei = inf.listIndividuals();ei.hasNext();) {
			
			//ei.next();
			Individual pom = (Individual) ei.next();
			//System.out.println(pom.toString());
			//System.out.println(pom.getURI());
			counter++;
			
			System.out.println(counter);
			
			if (counter == N)
				break;
			
		}
		
		t4 = System.currentTimeMillis();
		
		System.out.println("The individuals were found in "+(t4-t3)+" miliseconds");
		
		//model.write(System.out);

	}
	
	public static void main(String[] args) {
		// 33 is the maximum
		int i,j,k;
		
		calculate(33);
		
		if (true)
			return;
		
		for (i=3;i<=33;i+=2) {
			calculate(i);
		}
		
	}

}
