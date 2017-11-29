package antiSpamFilter;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	  public AntiSpamFilterProblem() {
	    // 10 variables (anti-spam filter rules) by default 
	    this(10);
	  }
	  //ler o ficheiro rules para ver quantos pesos temos(n� de linhas)
	private ReadFile rf = new ReadFile();

	public AntiSpamFilterProblem() {
		// 10 variables (anti-spam filter rules) by default
		// abrir p ficheiro rules.cf e por o numeoro de regras!! ex 300 this(300)

		this(10);// 335
		

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
		double aux, xi, xj;
		double[] fx = new double[getNumberOfObjectives()];
		double[] x = new double[getNumberOfVariables()];
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			x[i] = solution.getVariableValue(i);
		}

		// tem que dar numeros inteiros
		fx[0] = 0.0;
		for (int var = 0; var < solution.getNumberOfVariables() - 1; var++) {
			fx[0] += Math.abs(x[0]); // Example for testing
		}
		// fazer o somatorio aqui e usar a mesma função para o manual!
		// apagar este codigo teste e faer um que some as regras todas
		fx[1] = 0.0;
		for (int var = 0; var < solution.getNumberOfVariables(); var++) {
			fx[1] += Math.abs(x[1]); // Example for testing
		}

		solution.setObjective(0, fx[0]);// FP
		solution.setObjective(1, fx[1]);// FN
	}
}
