package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GUI {

	private JFrame frame = new JFrame("Filtro Anti-Spam");
	private JList list;
	private Dimension dimension;
	private String ham;
	private String rules;
	private String spam;
	private ReadFile rf = new ReadFile();
	private DefaultListModel<String> modelRules = new DefaultListModel<>();
	private DefaultListModel<String> modelPesos = new DefaultListModel<>();

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
		ArrayList<String> listRules = new ArrayList<String>();
		ArrayList<String> listPesos = new ArrayList<String>();
		Collections.addAll(listPesos, "a1", "str", "mystr", "a1", "str", "mystr", "a1", "str", "mystr", "a1", "str",
				"mystr", "a1", "str", "mystr", "a1", "str", "mystr", "a1", "str", "mystr", "a1", "str", "mystr", "a1",
				"str", "mystr");

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// Aqui são criados 3 os paineis principais.
		JPanel topPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel botPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		midPanel.setLayout(new BorderLayout());
		botPanel.setLayout(new BorderLayout());
		// topPanel.setPreferredSize( new Dimension( 300, 200 ) );
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
		JButton done = new JButton("Paths has been chosen");
		subpanel3.add(done, BorderLayout.SOUTH);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// verificação se os ficheiros estão escolhidos todos e bem.
				// Aqui será onde os caminhos serão dados para outro metodo para serem abertos e
				// lidos.
				// something like: readFiles(give a path);// use this method as much as i want,
				// or 3 different read files for each file, cause each one has a different stuff
				// to do in there
				setModelContent(rf.ReadRules(rules), modelRules);
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
		saveConfig.setPreferredSize(new Dimension(70, 50));
		saveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// escrever no ficheiro rules.cf
			}
		});
		JButton avaliateConfig = new JButton("Avaliate");
		avaliateConfig.setPreferredSize(new Dimension(70, 50));
		avaliateConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// chamar a classe para avaliar!
			}
		});

		JPanel results = new JPanel();
		results = setNumberOfFakes(2, 3);// devolver os valores de FP e FN

		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		buttons.add(saveConfig, BorderLayout.NORTH);
		buttons.add(avaliateConfig, BorderLayout.CENTER);
		buttons.add(results, BorderLayout.SOUTH);

		JPanel listBoxRules = createListBox(listRules, modelRules);
		JPanel listBoxPesos = createListBox(listPesos, modelPesos);
		midPanel.add(listBoxRules, BorderLayout.WEST);
		listBoxRules.add(listBoxPesos, BorderLayout.EAST);
		listBoxPesos.add(buttons, BorderLayout.EAST);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		frame.add(botPanel, BorderLayout.SOUTH);

	}

	private JPanel createListBox(ArrayList<String> listRules, DefaultListModel<String> model) {
		JList list;
		list = new JList<>(model);
		JScrollPane pane = new JScrollPane(list);
		JPanel panel = new JPanel();
		pane.setPreferredSize(new Dimension(100, 170));
		setModelContent(listRules, model);
		panel.add(pane);
		return panel;

	}

	private void setModelContent(ArrayList<String> listRules, DefaultListModel<String> model) {
		for (int i = 0; i != listRules.size(); i++) {
			model.addElement(listRules.get(i));
		}
	}

	// devolve o caminho do ficheiro que for selecionado
	private String getPath(JButton search1, JTextField titleText) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("C:/Users"));
		fc.setDialogTitle("File Browser.");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(search1) == JFileChooser.APPROVE_OPTION) {
			titleText.setText(fc.getSelectedFile().getAbsolutePath());
		}
		if (fc.getSelectedFile().getAbsolutePath() != null) {
			return fc.getSelectedFile().getAbsolutePath();
		} else {
			return null;
		}
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
