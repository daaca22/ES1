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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	static String rules;
	private String spam;
	private ReadFile rf = new ReadFile();
 

	private DefaultTableModel modelManual = new DefaultTableModel();
	private DefaultTableModel modelAutomatic = new DefaultTableModel();

	public ArrayList<Rule> listRules = new ArrayList<Rule>();
	
	static public ArrayList<Rule> staticRulesList = new ArrayList<Rule>();

	static   ArrayList<Email> listHam = new ArrayList<Email>();
	static   ArrayList<Email> listSpam = new ArrayList<Email>();

	public ArrayList<Email> getListHam() {
		return listHam;
	}
	public ArrayList<Email> getListSpam() {
		return listSpam;
	}

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
						setModelContent(rf.readRules(rules), modelManual);// vai carregar as regras na GUI
						setModelContent(rf.readRules(rules), modelAutomatic);
						listRules = getRulesList(modelManual);
						staticRulesList = listRules;
					} else {
						titleText1.setText("No File Selected");
					}
				} else {
					titleText1.setText("Wrong File");
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
				}
			}
		});
		// botão para guardar os pesos e os seus valores no ficheiro rules.cf
		JButton saveConfig = new JButton("Save");
		saveConfig.setPreferredSize(new Dimension(20, 30));
		saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listRules = getRulesList(modelManual);
				rf.writeRules(rules, listRules);
				mensageLabel.setText("Ficheiro foi Guardado com Sucesso!");
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

				if (listHam.isEmpty() || listSpam.isEmpty()) {// se o ficheiro ham.log ou spam.log não estiverem carregados manda esta msg abaixo.
					mensageLabel.setText("Tem de carregar os ficheiros spam.log e ham.log");
				} else {
					listRules = getRulesList(modelManual); // lista de Regras que têm o nome e o peso
					String fpString = Integer.toString(calculateFP(listHam,listRules));
					String fnString = Integer.toString(calculateFN(listSpam,listRules));
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
		JTable table1 = new JTable(modelAutomatic);
		table.setPreferredScrollableViewportSize(new Dimension(180, 150));
		table.setFillsViewportHeight(true);

		JScrollPane js1 = new JScrollPane(table1);
		js1.setVisible(true);

		JButton saveConfig1 = new JButton("Save");
		saveConfig.setPreferredSize(new Dimension(20, 30));
		saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		JPanel results1 = new JPanel();
		JTextField fpText1 = new JTextField("             ");
		JTextField fnText1 = new JTextField("             ");
		results1 = numberOfFakesTextFields(fpText1, fnText1);

		JButton avaliateConfig1 = new JButton("Avaliate");
		avaliateConfig1.setPreferredSize(new Dimension(20, 30));
		avaliateConfig1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// listRules = getRulesList(modelManual); // lista de Regras que têm o nome e o
				// peso
 
				 
				 AntiSpamFilterAutomaticConfiguration anti =  new AntiSpamFilterAutomaticConfiguration();
				 try {
					anti.main(null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				 
				 
				 
			}
		});

		JPanel buttons1 = new JPanel();
		buttons1.setLayout(new GridLayout(1, 3));
		buttons1.add(saveConfig1);
		buttons1.add(avaliateConfig1);
		buttons1.add(results1);

		js1.setPreferredSize(new Dimension(400, 200));
		buttons1.setPreferredSize(new Dimension(400, 50));

		botPanel.add(js1);
		botPanel.add(buttons1);

		frame.setLayout(new FlowLayout());
		frame.add(topPanel);
		frame.add(midPanel);
		frame.add(botPanel);

	}
	// este método para colocar na Tabela os pesos e o seus valores.
	private void setModelContent(ArrayList<Rule> list, DefaultTableModel model) {// passo aqui uma lista de Rules
		for (int i = 0; i != list.size(); i++) {
			model.addRow(new Object[] { list.get(i).getRule(), list.get(i).getValue() });
		}
	}

	// vai a tabela da interface grafica buscar a lista para avaliar ou gravar num
	// ficheiro
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
	public int calculateFP(ArrayList<Email> hamList,ArrayList<Rule> listRules) {
		int fp = 0;
		for (Email email : hamList) {
			if (isSpam(email.getValues(),listRules))// se for maior que 5 é spam logo não é ham o que torna falso positivo
				fp++;
		}
		return fp;
	}

	// calcular o numero de FN ao percorrer a lista com os emails de spam.log
	public int calculateFN(ArrayList<Email> spamList,ArrayList<Rule> listRules) {
		int fn = 0;
		for (Email email : spamList) {
			if (!isSpam(email.getValues(),listRules))
				fn++;
		}
		return fn;
	}

	// este metodo vê se está acima ou abaixo de 5
	private Boolean isSpam(String[] rules,ArrayList<Rule> listRules) {// começar por melhorar este
		Double d = 0.0;
		for (int i = 0; i != rules.length; i++) {
			d = d + getPeso(rules[i],listRules);
		}
		if (d > 5) {
			return true;
		}
		return false;
	}
	// este metodo percorre a lista de Regras(classe Rule) e vai buscar o peso de
	// cada Regra para somar no metodo anterior
	public Double getPeso(String rule, ArrayList<Rule> listRules) {
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
		subpanel.setLayout(new GridLayout(1, 2));
		titleText.setPreferredSize(new Dimension(210, 40));
		titleText.setEditable(false);
		subpanel.add(titleText);
		subpanel.add(search);
		return subpanel;
	}
	
//	public ArrayList<Rule> getRegrasList() {
//		System.out.println("teste2 "+staticRulesList.size());
//		return staticRulesList;
//	}

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
