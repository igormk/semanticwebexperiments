import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;


// this experiment test how many Classes, Properties and Individuals
// can be placed in ontology which is read in memory

public class Experiment05 {
	
	static String NS;
	
	public static void main(String[] args) {
		int i,j,k;
		int N = 0;
		String SOURCE = "http://www.eswc2006.org/technologies/ontology";
		//String SOURCE = "http://www.eswc2006.org/qwertyuiopuoyitorieuwieorptoyitriuy/technologies/ontology";
		
		NS = SOURCE + "#";
		
		// we create the model
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		
		/*
		// we test how many classes can be placed
		OntClass oc;
		try {
			N = 0;
			while (true) {
				oc = model.createClass(NS+N);
				N++;
				System.out.println(N);
			}
		}
		catch (Exception exc) {
			System.out.println(exc.toString());
		}
		finally {
		}
		
		System.out.println(N+" classes");
		
		// we test how many properties can be placed
		
		
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		
		OntProperty op;
		try {
			N = 0;
			while (true) {
				op = model.createOntProperty(NS+N);
				N++;
				System.out.println(N);
			}
				
		}
		catch (Exception exc) {
			System.out.println(exc.toString());
		}
		finally {
			
		}
		
		System.out.println(N+" Properties");
		*/
		
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		OntClass oc = model.createClass(NS+"t");
		Individual ind;
		try {
			N = 0;
			while (true) {
				ind = model.createIndividual(NS+N, model.getOntClass(NS+"t"));
				N++;
				System.out.println(N);
			}
		}
		catch (Exception exc) {
			System.out.println(exc.toString());
		}
		finally {
			
		}
		System.out.println(N+" Individuals");
		
	}
	
}
