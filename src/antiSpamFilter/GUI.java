package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

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

public class GUI {

	private JFrame frame = new JFrame("Filtro Anti-Spam");
	private Dimension dimension;
	private String ham;
	private String rules;
	private String spam;
	private ReadFile rf = new ReadFile();

	private DefaultTableModel model1 = new DefaultTableModel();

	private ArrayList<String> listRulesName = new ArrayList<String>();
	private ArrayList<Rule> listRules = new ArrayList<Rule>();

	private ArrayList<Email> listHam = new ArrayList<Email>();
	private ArrayList<Email> listSpam = new ArrayList<Email>();

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

	public void addFrameContent() {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// Aqui são criados 3 os paineis principais.
		JPanel topPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel botPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		midPanel.setLayout(new BorderLayout());
		botPanel.setLayout(new BorderLayout());
		midPanel.setPreferredSize(new Dimension(300, 200));
		botPanel.setPreferredSize(new Dimension(300, 200));

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

		// aqui são colocados por ordem os subpaineis, no primeiro painel, dos caminhos.
		topPanel.add(subpanel, BorderLayout.NORTH);
		topPanel.add(subpanel2, BorderLayout.CENTER);
		topPanel.add(subpanel3, BorderLayout.SOUTH);

		// aqui vai ser criado o botão para informar que estão os caminhos Escolhidos
		JButton done = new JButton("Paths have been chosen");
		subpanel3.add(done, BorderLayout.SOUTH);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// verificação se os ficheiros estão escolhidos todos e bem.
				// Aqui será onde os caminhos serão dados para outro metodo para serem abertos e
				// lidos.
				if (rules != null) {
					setModelContent(rf.readRules(rules), model1);// vai carregar as regras na GUI
				} else {
					titleText1.setText("No File Selected");
				}
				if (ham != null) {
					listHam = rf.readHam(ham);
				} else {
					titleText2.setText("No File Selected");
				}
				if (spam != null) {
					listSpam = rf.readHam(spam);
					System.out.println(listSpam.size());
				} else {
					titleText3.setText("No File Selected");
				}
			}
		});

		search1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rules = getPath(search1, titleText1);
			}
		});

		search2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ham = getPath(search2, titleText2);
			}
		});
		search3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spam = getPath(search3, titleText3);
			}
		});

		JButton saveConfig = new JButton("Save");
		saveConfig.setPreferredSize(new Dimension(20, 30));
		saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rf.writeRules("lol", listRules);
			}
		});
		JPanel results = new JPanel();
		JTextField fpText = new JTextField("             ");
		JTextField fnText = new JTextField("             ");
		results = numberOfFakesTextFields(fpText, fnText);

		JButton avaliateConfig = new JButton("Avaliate");
		avaliateConfig.setPreferredSize(new Dimension(20, 30));
		avaliateConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// chamar a classe para avaliar!
				listRules = getRulesList(model1); // lista de Regras que têm o nome e o peso

				String fpString = Integer.toString(calculateFP(listHam));
				String fnString = Integer.toString(calculateFN(listSpam));
				fpText.setText(fpString);
				fnText.setText(fnString);
			}
		});

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 3));
		// buttons.setPreferredSize(new Dimension(100, 200));
		buttons.add(saveConfig);
		buttons.add(avaliateConfig);
		buttons.add(results);

		// JTABLE STUFF
		String[] header = { "Rules", "Pesos" };
		String[][] data = {};

		model1 = new DefaultTableModel(data, header);

		JTable table = new JTable(model1);

		table.setPreferredScrollableViewportSize(new Dimension(180, 150));
		table.setFillsViewportHeight(true);

		JScrollPane js = new JScrollPane(table);
		js.setVisible(true);

		midPanel.add(js, BorderLayout.NORTH);
		midPanel.add(buttons, BorderLayout.SOUTH);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		frame.add(botPanel, BorderLayout.SOUTH);

	}

	private void setModelContent(ArrayList<String> list, DefaultTableModel model) {
		listRulesName = list;
		for (int i = 0; i != list.size(); i++) {
			model.addRow(new Object[] { list.get(i), setPesos() });
		}
	}

	public Double setPesos() {// apos carregar os caminhos carrega pesos aleatorios
		double randomValue = 0.0;
		double d = 0.0;
		Random r = new Random();
		randomValue = -5 + (5 - (-5)) * r.nextDouble();
		d = round(randomValue, 3);
		return d;
	}

	public ArrayList<Rule> createRules(ArrayList<Double> pesos) {
		ArrayList<Rule> rulesList = new ArrayList<Rule>();
		for (int i = 0; i != pesos.size(); i++) {
			Rule rule = new Rule(listRulesName.get(i), pesos.get(i));
			rulesList.add(rule);
			System.out.println(rule.getRule() + " " + rule.getValue());
		}

		return rulesList;
	}

	private ArrayList<Rule> getRulesList(DefaultTableModel model) {

		ArrayList<Rule> rulesList = new ArrayList<Rule>();
		ArrayList<Double> pesos = new ArrayList<Double>();
		for (int count = 0; count < model.getRowCount(); count++) {
			pesos.add(Double.parseDouble(model.getValueAt(count, 1).toString()));
		}
		rulesList = createRules(pesos);
		return rulesList;
	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	// devolve o caminho do ficheiro que for selecionado
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

	// calcular o numero de FP ao percorrer a lista com os emails de ham.log
	public int calculateFP(ArrayList<Email> hamList) {
		int fp = 0;
		for (Email email : hamList) {
			if (isSpam(email.getValues()))
				fp++;
		}
		return fp;
	}

	// calcular o numero de FN ao percorrer a lista com os emails de spam.log
	public int calculateFN(ArrayList<Email> spamList) {
		int fn = 0;
		for (Email email : spamList) {
			if (!isSpam(email.getValues()))
				fn++;
		}
		return fn;
	}

	// este metodo vê se está acima ou abaixo de 5
	private Boolean isSpam(String[] rules) {
		Double d = 0.0;
		for (int i = 0; i != rules.length; i++) {
			d = d + getPeso(rules[i]);
		}
		if (d > 5) {
			return true;
		}
		return false;
	}

	// este metodo percorre a lista de Regras(classe Rule) e vai buscar o peso de
	// cada Regra para somar no metodo anterior
	public Double getPeso(String rule) {
		Double d = 0.0;
		for (Rule r : listRules) {
			if (r.getRule().equals(rule)) {
				d = r.getValue();
			}
		}

		return d;
	}

	// este método cria um painel dando um botão e um textfield
	private JPanel getPanel(JButton search, JTextField titleText) {
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		titleText.setPreferredSize(new Dimension(210, 40));
		titleText.setEditable(false);
		subpanel.add(titleText, BorderLayout.WEST);
		subpanel.add(search, BorderLayout.EAST);
		return subpanel;
	}

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
		window.open(300, 600);
	}

}
