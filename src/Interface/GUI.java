package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * This Class handles everything about the interface, and user interaction.
 * 
 * @author Daniel Coimbra and Goncalo Meireles
 *
 */

public class GUI {

	private JFrame frame = new JFrame("Filtro Anti-Spam");
	private Dimension dimension;
	private String ham;
	static String rules;
	private String spam;
	private ReadFile rf = new ReadFile();

	private DefaultTableModel modelManual = new DefaultTableModel();
	private DefaultTableModel modelAutomatic = new DefaultTableModel();

	public ArrayList<Rule> listRules = new ArrayList<Rule>();

	static public ArrayList<Rule> staticRulesList = new ArrayList<Rule>();

	static ArrayList<Email> listHam = new ArrayList<Email>();
	static ArrayList<Email> listSpam = new ArrayList<Email>();

	public GUI() {
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		addFrameContent();
		frame.pack();
	}

	public void open(int width, int height) {
		frame.setSize(width, height);
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int) dimension.getWidth() / 3, (int) dimension.getHeight() / 4);
		frame.setVisible(true);
	}

	/**
	 * This method is the main method in this class and it handles all the buttons,
	 * TextFields, Tables and etc.
	 * 
	 * @param nothing
	 * 
	 * @return nothing.
	 */

	public void addFrameContent() {

		// Aqui são criados 3 os paineis principais.
		JPanel topPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel botPanel = new JPanel();
		topPanel.setLayout(new GridLayout(3, 1));
		midPanel.setLayout(new FlowLayout());
		botPanel.setLayout(new FlowLayout());
		topPanel.setPreferredSize(new Dimension(400, 100));
		midPanel.setPreferredSize(new Dimension(400, 300));
		botPanel.setPreferredSize(new Dimension(400, 300));

		// aqui são criados os botoes para procurar o caminho dos ficheiros
		JButton search1 = new JButton("Search");
		JButton search2 = new JButton("Search");
		JButton search3 = new JButton("Search");
		// aqui sao criados as textfields para procurar o caminho dos ficheiros
		JTextField titleText1 = new JTextField("rules.cf");
		JTextField titleText2 = new JTextField("Ham.log.txt");
		JTextField titleText3 = new JTextField("Spam.log.txt");

		// aqui são criados os subpaineis da parte dos caminhos, cada textfield e botao
		// é um painel
		JPanel subpanel = getPanel(search1, titleText1);
		JPanel subpanel2 = getPanel(search2, titleText2);
		JPanel subpanel3 = getPanel(search3, titleText3);

		// Label de interação com o utilizador

		JLabel mensageLabel = new JLabel("");
		mensageLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		mensageLabel.setForeground(Color.GREEN);

		// aqui são colocados por ordem os subpaineis, no primeiro painel, dos caminhos.
		topPanel.add(subpanel);
		topPanel.add(subpanel2);
		topPanel.add(subpanel3);

		// aqui vai ser criado o botão para informar que estão os caminhos Escolhidos

		search1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rules = getPath(search1, titleText1);
				if (rules.endsWith("rules.cf")) {
					if (rules != null) {
						setModelContentManual(rf.readRules(rules), modelManual);// vai carregar as regras na GUI
						// setModelContentAutoRules(rf.readRules(rules), modelAutomatic);
						listRules = getRulesList(modelManual);
						staticRulesList = listRules;
					} else {
						titleText1.setText("No File Selected");
					}
				} else {
					titleText1.setText("Wrong File");
					// rules = null;
				}
			}
		});
		// botão para adicionar o fiheiro ham.log
		search2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ham = getPath(search2, titleText2);
				if (ham.endsWith("ham.log.txt")) {
					if (ham != null) {
						listHam = rf.readHamOrSpam(ham);
						mensageLabel.setText("Ficheiro foi Carregado com Sucesso!");
					} else {
						titleText2.setText("No File Selected");
					}
				} else {
					titleText2.setText("Wrong File");
					ham = null;
				}
			}
		});
		// botão para adicionar o fiheiro spam.log
		search3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spam = getPath(search3, titleText3);
				if (spam.endsWith("spam.log.txt")) {
					if (spam != null) {
						listSpam = rf.readHamOrSpam(spam);
						System.out.println(listSpam.size());
						mensageLabel.setText("Ficheiro foi Carregado com Sucesso!");
					} else {
						titleText3.setText("No File Selected");
					}
				} else {
					titleText3.setText("Wrong File");
					spam = null;
				}
			}
		});
		// botão para guardar os pesos e os seus valores no ficheiro rules.cf
		JButton saveConfig = new JButton("Save");
		saveConfig.setPreferredSize(new Dimension(20, 30));
		saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rules != null && spam != null && ham != null) {
					listRules = getRulesList(modelManual);
					rf.writeRules(rules, listRules);
					mensageLabel.setText("Ficheiro foi Guardado com Sucesso!");
				} else {
					mensageLabel.setText("Não Foram Carregados Ficheiros!");
				}
			}
		});

		JPanel results = new JPanel();
		JTextField fpText = new JTextField("             ");
		JTextField fnText = new JTextField("             ");
		results = numberOfFakesTextFields(fpText, fnText);
		// butão avaliar da configuração manual
		JButton avaliateConfig = new JButton("Avaliate");
		avaliateConfig.setPreferredSize(new Dimension(20, 30));
		avaliateConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (listHam.isEmpty() || listSpam.isEmpty()) {// se o ficheiro ham.log ou spam.log não estiverem
																// carregados manda esta msg abaixo.
					mensageLabel.setText("Não Foram Carregados Ficheiros!");
				} else {
					listRules = getRulesList(modelManual); // lista de Regras que têm o nome e o peso
					String fpString = Integer.toString(calculateFP(listHam, listRules));
					String fnString = Integer.toString(calculateFN(listSpam, listRules));
					fpText.setText(fpString);
					fnText.setText(fnString);
					mensageLabel.setText("Ficheiro foi Avaliado com Sucesso!");
				}
			}
		});

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 3));
		buttons.add(saveConfig);
		buttons.add(avaliateConfig);
		buttons.add(results);

		// JTABLE STUFF
		String[] header = { "Rules", "Pesos" };
		String[][] data = {};

		modelManual = new DefaultTableModel(data, header);

		JTable table = new JTable(modelManual);

		table.setPreferredScrollableViewportSize(new Dimension(180, 150));
		table.setFillsViewportHeight(true);

		JScrollPane js = new JScrollPane(table);
		js.setVisible(true);
		js.setPreferredSize(new Dimension(400, 200));
		buttons.setPreferredSize(new Dimension(400, 50));

		midPanel.add(js, BorderLayout.NORTH);
		midPanel.add(buttons, BorderLayout.SOUTH);
		midPanel.add(mensageLabel, BorderLayout.SOUTH);

		// JTABLE STUFF

		modelAutomatic = new DefaultTableModel(data, header);
		JTable tableAuto = new JTable(modelAutomatic);
		tableAuto.setPreferredScrollableViewportSize(new Dimension(180, 150));
		tableAuto.setFillsViewportHeight(true);
		tableAuto.setEnabled(false);

		JScrollPane js1 = new JScrollPane(tableAuto);
		js1.setVisible(true);

		JButton saveConfigAuto = new JButton("Save");
		saveConfigAuto.setPreferredSize(new Dimension(20, 30));
		saveConfigAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rules != null && spam != null && ham != null) {
					ArrayList<Rule> list = getRulesList(modelAutomatic);
					rf.writeRules(rules, list);
					mensageLabel.setText("Ficheiro foi Guardado com Sucesso!");
				} else {
					mensageLabel.setText("Não Foram Carregados Ficheiros!");
				}
			}
		});
		JPanel resultsAuto = new JPanel();
		JTextField fpTextAuto = new JTextField("             ");
		JTextField fnTextAuto = new JTextField("             ");
		resultsAuto = numberOfFakesTextFields(fpTextAuto, fnTextAuto);

		JButton avaliateConfigAuto = new JButton("Avaliate");
		avaliateConfigAuto.setPreferredSize(new Dimension(20, 30));
		avaliateConfigAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rules != null && spam != null && ham != null) {
					AntiSpamFilterAutomaticConfiguration anti = new AntiSpamFilterAutomaticConfiguration();
					try {
						anti.main(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Double d[] = new Double[3];
					d = rf.readVectorPesos();
					ArrayList<Double> pesos = rf.getPesosFromFileAuto(d[2].intValue());
					setModelContentAuto(rf.readRules(rules), modelAutomatic, pesos);
					String fpString = Double.toString(d[0]);
					String fnString = Double.toString(d[1]);
					fpTextAuto.setText(fpString);
					fnTextAuto.setText(fnString);
					mensageLabel.setText("Ficheiro foi Avaliado Automáticamente com Sucesso!");

					// generate R and latex.
					generateR();
					generateLatexReport();
				} else {
					mensageLabel.setText("Não Foram Carregados Ficheiros!");
				}
			}
		});

		JPanel buttons1 = new JPanel();
		buttons1.setLayout(new GridLayout(1, 3));
		buttons1.add(saveConfigAuto);
		buttons1.add(avaliateConfigAuto);
		buttons1.add(resultsAuto);

		js1.setPreferredSize(new Dimension(400, 200));
		buttons1.setPreferredSize(new Dimension(400, 50));

		botPanel.add(js1);
		botPanel.add(buttons1);

		frame.setLayout(new FlowLayout());
		frame.add(topPanel);
		frame.add(midPanel);
		frame.add(botPanel);

	}

	/**
	 * This method is responsible for generating the R graphic using the Rscript
	 * command in a given directory and a given file R.
	 * 
	 * @param nothing
	 * 
	 * @return nothing.
	 */
	private void generateR() {
		String[] params = new String[2];

		params[0] = "/Library/Frameworks/R.framework/Versions/3.4/Resources/Rscript";
		params[1] = "/Users/danielcoimbra/git/ES1-2017-EIC2-4/experimentBaseDirectory/AntiSpamStudy/R/HV.Boxplot.R";

		String[] envp = new String[1];
		envp[0] = "Path=/Library/Frameworks/R.framework/Versions/3.4/Resources";

		try {
			Process p = Runtime.getRuntime().exec(params, envp,
					new File("/Users/danielcoimbra/git/ES1-2017-EIC2-4/experimentBaseDirectory/AntiSpamStudy/R"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method is responsible for generating the Latex pdf using the pdflatex
	 * command in a given directory and a given file tex.
	 * 
	 * @param nothing
	 * 
	 * @return nothing.
	 */
	public void generateLatexReport() {
		String[] params = new String[2];
		params[0] = "/usr/local/texlive/2017/bin/x86_64-darwin/pdflatex";
		params[1] = "/Users/danielcoimbra/git/ES1-2017-EIC2-4/experimentBaseDirectory/AntiSpamStudy/latex/AntiSpamStudy.tex";
		String[] envp = new String[1];
		envp[0] = "Path=/usr/local/texlive/2017/bin/x86_64-darwin";
		try {
			Process p = Runtime.getRuntime().exec(params, envp,
					new File("/Users/danielcoimbra/git/ES1-2017-EIC2-4/experimentBaseDirectory/AntiSpamStudy/latex"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method is responsible for setting the Rules and his values in the manual
	 * table.
	 * 
	 * @param list
	 *            ArrayList of Rules
	 * @param model
	 *            DefaultTableModel of the desired table, in this case the manual
	 *            model.
	 * 
	 * @return nothing.
	 */
	private void setModelContentManual(ArrayList<Rule> list, DefaultTableModel model) {// passo aqui uma lista de Rules
		clearTable(model);
		for (int i = 0; i != list.size(); i++) {
			model.addRow(new Object[] { list.get(i).getRule(), list.get(i).getValue() });
		}
	}

	/**
	 * This method is responsible for setting the Rules and his values in the
	 * automatic configuration table.
	 * 
	 * @param list
	 *            ArrayList of Rules, that will get the name of the rules
	 * @param model
	 *            DefaultTableModel of the desired table, in this case the automatic
	 *            model.
	 * @param listD
	 *            ArrayList of Doubles, that comes from the automatic algorithm, to
	 *            get the value of the rules
	 * 
	 * @return nothing.
	 */

	private void setModelContentAuto(ArrayList<Rule> list, DefaultTableModel model, ArrayList<Double> listD) {
		clearTable(model);
		for (int i = 0; i != list.size(); i++) {
			model.addRow(new Object[] { list.get(i).getRule(), listD.get(i) });
		}
	}

	/**
	 * This method is responsible for cleaning the JTabel values of every row
	 * 
	 * @param model
	 *            DefaultTableModel that the values will be deleted from.
	 * 
	 * @return nothing.
	 */
	private void clearTable(DefaultTableModel model) {
		if (model.getRowCount() > 0) {
			for (int i = model.getRowCount() - 1; i > -1; i--) {
				model.removeRow(i);
			}
		}
	}

	// vai a tabela da interface grafica buscar a lista para avaliar ou gravar num
	// ficheiro

	/**
	 * This method is responsible for checking the DefaultTableModel and get the
	 * values from the table.
	 * 
	 * @param model
	 *            DefaultTableModel that the values will be from.
	 * 
	 * @return ArrayList of Rules.
	 */

	private ArrayList<Rule> getRulesList(DefaultTableModel model) {

		ArrayList<Rule> rulesList = new ArrayList<Rule>();
		for (int count = 0; count < model.getRowCount(); count++) {
			String name = model.getValueAt(count, 0).toString();
			Double p = (Double.parseDouble(model.getValueAt(count, 1).toString()));
			Rule r = new Rule(name, p);
			rulesList.add(r);
		}

		return rulesList;
	}

	/**
	 * This method is responsible for letting the user choose the directory to the
	 * file.
	 * 
	 * @param search1
	 *            is the button responsible for the search
	 * 
	 * @param titleText
	 *            is the TextField responsible for the showing the path.
	 * 
	 * @return String with the path to the file.
	 */
	private String getPath(JButton search1, JTextField titleText) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("C:/Users"));
		fc.setDialogTitle("File Browser.");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(search1) == JFileChooser.APPROVE_OPTION) {
			titleText.setText(fc.getSelectedFile().getAbsolutePath());
		} else {
			return " ";
		}
		// System.out.println(fc.getSelectedFile().getAbsolutePath());

		return fc.getSelectedFile().getAbsolutePath();

	}

	/**
	 * This method is responsible for calculating the FP in the ham.log.txt file,
	 * iterates over the listRules given and the hamList
	 * 
	 * @param hamList
	 *            list of Email from the file ham.log.txt
	 * 
	 * @param listRules
	 *            list of Rules from the file rules.cf
	 * 
	 * @return integer with the number of FP.
	 */
	public int calculateFP(ArrayList<Email> hamList, ArrayList<Rule> listRules) {
		int fp = 0;
		for (Email email : hamList) {
			if (isSpam(email.getValues(), listRules))// se for maior que 5 é spam logo não é ham o que torna falso
														// positivo
				fp++;
		}
		return fp;
	}

	/**
	 * This method is responsible for calculating the FN in the spam.log.txt file,
	 * iterates over the listRules given and the spamList
	 * 
	 * @param hamList
	 *            list of Email from the file spam.log.txt
	 * 
	 * @param listRules
	 *            list of Rules from the file rules.cf
	 * 
	 * @return integer with the number of FN.
	 */
	public int calculateFN(ArrayList<Email> spamList, ArrayList<Rule> listRules) {
		int fn = 0;
		for (Email email : spamList) {
			if (!isSpam(email.getValues(), listRules))
				fn++;
		}
		return fn;
	}

	/**
	 * This method is responsible for checking if an Email is Spam or not, checking
	 * the values of the Rules from each Email from the spam.log or ham.log
	 * 
	 * @param rules
	 *            Array of rules
	 * 
	 * @param listRules
	 *            list of Rules from the file rules.cf
	 * 
	 * @return Boolean true is is Spam, false if is not Spam.
	 */
	private Boolean isSpam(String[] rules, ArrayList<Rule> listRules) {// começar por melhorar este
		Double d = 0.0;
		for (int i = 0; i != rules.length; i++) {
			d = d + getPeso(rules[i], listRules);
		}
		if (d > 5) {
			return true;
		}
		return false;
	}

	public Double getPeso(String rule, ArrayList<Rule> listRules) {
		Double d = 0.0;
		for (Rule r : listRules) {
			if (r.getRule().equals(rule)) {
				d = r.getValue();
			}
		}

		return d;
	}

	/**
	 * This method is responsible for creating a JPanel with a button and a
	 * TextField for the Paths
	 * 
	 * @param search
	 *            Button Search
	 * 
	 * @param titleText
	 *            TextField to show the paths
	 * 
	 * @return JPanel
	 */
	private JPanel getPanel(JButton search, JTextField titleText) {
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(1, 2));
		titleText.setPreferredSize(new Dimension(210, 40));
		titleText.setEditable(false);
		subpanel.add(titleText);
		subpanel.add(search);
		return subpanel;
	}

	/**
	 * This method is responsible for creating a JPanel with the TextFields of the
	 * FN and FP and the Labels.
	 * 
	 * @param fpText
	 *            TextField that will have the number of FP
	 * @param fnText
	 *            TextField that will have the number of FP
	 * 
	 * @return JPanel with the FN and FP TextField
	 */
	public JPanel numberOfFakesTextFields(JTextField fpText, JTextField fnText) {
		JPanel pane = new JPanel();
		JPanel subpanelFN = new JPanel();
		JPanel subpanelFP = new JPanel();
		pane.setLayout(new BorderLayout());
		subpanelFP.setLayout(new BorderLayout());
		subpanelFN.setLayout(new BorderLayout());
		JLabel fp = new JLabel("FP:");
		JLabel fn = new JLabel("FN:");
		subpanelFN.add(fn, BorderLayout.WEST);
		subpanelFN.add(fnText, BorderLayout.EAST);
		subpanelFP.add(fp, BorderLayout.WEST);
		subpanelFP.add(fpText, BorderLayout.EAST);
		pane.add(subpanelFN, BorderLayout.WEST);
		subpanelFN.add(subpanelFP, BorderLayout.NORTH);
		return pane;
	}

	public static void main(String[] args) {
		GUI window = new GUI();
		window.open(400, 700);
	}

}
