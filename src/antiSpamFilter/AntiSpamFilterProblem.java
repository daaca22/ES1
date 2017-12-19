package antiSpamFilter;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	private static ReadFile rf = new ReadFile();
	private static int number = rf.numberOfRules(GUI.rules);
	private GUI gui = new GUI();

	public AntiSpamFilterProblem() {
		// 10 variables (anti-spam filter rules) by default
		// abrir p ficheiro rules.cf e por o numeoro de regras!! ex 300 this(300)
		this(number);// 335
		System.out.println(number);

	}
	// calcular aqui FP e FN nesta classe

	public AntiSpamFilterProblem(Integer numberOfVariables) {
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(2);
		setName("AntiSpamFilterProblem");

		List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(-5.0);// entre -5 e 5
			upperLimit.add(5.0);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	public void evaluate(DoubleSolution solution) {// tem de ser executada para cada vector!
		ArrayList<Rule> oldListRegras = new ArrayList<Rule>();
		ArrayList<Rule> newListRegras = new ArrayList<Rule>();
		double[] x = new double[getNumberOfVariables()];
		double fp = 0;
		double fn = 0;
		oldListRegras = GUI.staticRulesList; 
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {// x é o vector com os pesos
			x[i] = solution.getVariableValue(i);
			Rule r = new Rule(oldListRegras.get(i).getRule(), x[i]);
			newListRegras.add(r);
		}
		
		fp = gui.calculateFP(GUI.listHam, newListRegras);
		fn = gui.calculateFN(GUI.listSpam, newListRegras);
 
		
		
		// fazer sem hashmap agr e com hashmap dps se tiver tempo.

		solution.setObjective(0, fp);// FP
		solution.setObjective(1, fn);// FN
	}
	// criar metodo no ReadFile que leia os outros 2 ficheiros para descobrir o com
	// FP mais baixos e passar esses valores para o GUI
}
