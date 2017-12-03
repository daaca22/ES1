package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GUI {

	private JFrame frame = new JFrame("Filtro Anti-Spam");
	private JList list;
	private JList list2;
	private Dimension dimension;
	private String ham;
	private String rules;
	private String spam;
	private ReadFile rf = new ReadFile();
 

	private DefaultTableModel model1 = new DefaultTableModel();

	private ArrayList<String> listRules = new ArrayList<String>();
	private ArrayList<Double> listPesos = new ArrayList<Double>();

	private JScrollPane pane;
	private JScrollPane pane2;

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
					setModelContent(rf.readRules(rules), model1);
				} else {
					titleText1.setText("No File Selected");
				}
				if (ham != null) {
					rf.readHam(ham);
					FPFN();
				} else {
					titleText2.setText("No File Selected");
				}
				if (spam != null) {
					rf.readHam(spam);
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
				// escrever no ficheiro rules.cf
			}
		});
		JButton avaliateConfig = new JButton("Avaliate");
		avaliateConfig.setPreferredSize(new Dimension(20, 30));
		avaliateConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// chamar a classe para avaliar!
			}
		});

		JPanel results = new JPanel();
		//results = setNumberOfFakes(2, 3);// devolver os valores de FP e FN
		
		
		

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 3));
		//buttons.setPreferredSize(new Dimension(100, 200));
		buttons.add(saveConfig );
		buttons.add(avaliateConfig );
		buttons.add(results );
		
		
		
		
		//JTABLE STUFF
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
		listRules = list;
		for (int i = 0; i != list.size(); i++) {
			model.addRow(new Object[] { list.get(i), setPesos() });

		}
	}

	private Double setPesos() {// apos carregar os caminhos
 
		// DecimalFormat df = new DecimalFormat("#.0000");
		double randomValue = 0.0;
		double d = 0.0;
		Random r = new Random();
		randomValue = -5 + (5 - (-5)) * r.nextDouble();
		d = round(randomValue,3);
		
		listPesos.add(d);
		return d;
	}
	
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

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
		//System.out.println(fc.getSelectedFile().getAbsolutePath());

		return fc.getSelectedFile().getAbsolutePath();

	}
	
	
	private void FPFN(){
		
		double pesos=0;
		int fn=0; //contador dos falsos negativos
		int
		fp=0; //contador dos falsos positivos

		for (int i=0; i != listRules.size(); i++) {
			for (int j=0; j != listPesos.size(); j++) {

				//if(listRules.get(i).equals("") ){
				pesos+= listPesos.get(j);
				System.out.println(pesos);
				
				if(pesos>=5){ //spam
					//System.out.println("FP");
					fp=fp+1; 			
					
				}else{
					//System.out.println("FN");
					fn=fn+1;
				}		
			}
		}
	
	JPanel resultado = new JPanel();
	resultado = setNumberOfFakes(fp, fn); //colocar no painel os resultados dos fp e fn (?)
	System.out.println("fn: "+ fn + "fp: "+ fp);
	
	
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

	
	public JPanel setNumberOfFakes(int fpResult, int fnResult) {
		String fpString = Integer.toString(fpResult);
		String fnString = Integer.toString(fnResult);
		JPanel pane = new JPanel();
		JPanel subpanelFN = new JPanel();
		JPanel subpanelFP = new JPanel();
		pane.setLayout(new BorderLayout());
		subpanelFP.setLayout(new BorderLayout());
		subpanelFN.setLayout(new BorderLayout());
		JLabel fp = new JLabel("FP:");
		JLabel fn = new JLabel("FN:");
		JTextField fpText = new JTextField(fpString);
		JTextField fnText = new JTextField(fnString);
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
