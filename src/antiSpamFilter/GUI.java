package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GUI {

	private JFrame frame = new JFrame("ISCTE Search");
	private JList list;
	private Dimension dimension;
	private String ham;
	private String rules;
	private String spam;

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

		// aqui são criados os botoes para procurar o caminho dos ficheiros
		JButton search1 = new JButton("Search");
		JButton search2 = new JButton("Search");
		JButton search3 = new JButton("Search");
		// aqui sao criados as textfields para procurar o caminho dos ficheiros
		JTextField titleText1 = new JTextField("rules.cf");
		JTextField titleText2 = new JTextField("Ham.log.txt");
		JTextField titleText3 = new JTextField("Samp.log.txt");

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
				// Aqui será onde os caminhos serão dados para outro metodo para serem abertos e
				// lidos.
				// something like: readFiles(give a path);// use this method as much as i want,
				// or 3 different read files for each file, cause each one has a different stuff
				// to do in there
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

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(40, 40));

		midPanel.add(listScroller);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		frame.add(botPanel, BorderLayout.SOUTH);

	}

	private String getPath(JButton search1, JTextField titleText) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("C:/Users"));
		fc.setDialogTitle("File Browser.");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(search1) == JFileChooser.APPROVE_OPTION) {
			titleText.setText(fc.getSelectedFile().getAbsolutePath());
		}
		return fc.getSelectedFile().getAbsolutePath();
	}

	// este método cria um painel dando um botão e um textfield
	private JPanel getPanel(JButton search, JTextField titleText) {
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		titleText.setPreferredSize(new Dimension(200, 24));
		titleText.setEditable(false);
		subpanel.add(titleText, BorderLayout.WEST);
		subpanel.add(search, BorderLayout.EAST);
		return subpanel;
	}

	public static void main(String[] args) {

		GUI window = new GUI();
		window.open(300, 600);

	}

}
