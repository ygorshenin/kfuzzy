package kfuzzy.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.text.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import kfuzzy.algo.KFuzzyAlgorithm;
import kfuzzy.Model;
import kfuzzy.gui.ClustersPaintComponent;
import kfuzzy.io.KFuzzyInput;
import kfuzzy.io.ReaderInterface;
import kfuzzy.io.SimpleReader;
import kfuzzy.math.Vector;


public class MainFrame extends JFrame {
    public final static int DEFAULT_WIDTH = 800;
    public final static int DEFAULT_HEIGHT = 600;

    private class DataFilesFilter extends FileFilter {
	public final static String DESCRIPTION = "Data files (*.dat|*.data)";

	public boolean accept(java.io.File file) {
	    if (file.isDirectory())
		return true;
	    return file.getName().endsWith("dat") || file.getName().endsWith("data");
	}

	public String getDescription() {
	    return DESCRIPTION;
	}
    }

    private class ChangeIndexActionListener implements ActionListener {
	private boolean first;

	public ChangeIndexActionListener(boolean first) {
	    this.first = first;
	}

	public void actionPerformed(ActionEvent event) {
	    try {
		JComboBox comboBox = (JComboBox) event.getSource();
		int index = (Integer) comboBox.getSelectedItem();

		if (first)
		    paintComponent.setFirstIndex(index);
		else
		    paintComponent.setSecondIndex(index);
		paintComponent.repaint();
	    } catch (Exception e) {
		System.err.println(e);
	    }
	}
    }

    private class OpenAction extends AbstractAction {
	private JFileChooser fileChooser;

	private boolean exists(String path) {
	    return new java.io.File(path).exists();
	}

	public OpenAction(JFileChooser fileChooser) {
	    putValue(NAME, "Open");
	    this.fileChooser = fileChooser;
	}

	@Override public void actionPerformed(ActionEvent e) {
	    int result = fileChooser.showOpenDialog(MainFrame.this);
	    if (result == JFileChooser.APPROVE_OPTION) {
		String path = fileChooser.getSelectedFile().getPath();
		if (exists(path))
		    openFile(path);
		else
		    JOptionPane.showMessageDialog(MainFrame.this, fileChooser.getSelectedFile() + " doesn't exists", "error",
						  JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private class ClusterizeAction extends AbstractAction {
	public ClusterizeAction() {
	    putValue(NAME, "Clusterize");
	}

	@Override public void actionPerformed(ActionEvent event) {
	    try {
		int numClusters = ((Number) numClustersField.getValue()).intValue();

		double blending = ((Number) blendingField.getValue()).doubleValue();
		int maxIterations = ((Number) numIterationsField.getValue()).intValue();

		model.clusterize(numClusters, new KFuzzyAlgorithm.Options(blending, maxIterations));

		paintComponent.setClusters(model.getPoints(), model.getClusters());
		paintComponent.repaint();
	    } catch (Exception e) {
		JOptionPane.showMessageDialog(MainFrame.this, String.format("Can't clusterize: %s", e.getMessage()), "error",
					      JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private class QuitAction extends AbstractAction {
	public QuitAction() {
	    putValue(NAME, "Quit");
	}

	@Override public void actionPerformed(ActionEvent e) {
	    System.exit(0);
	}
    }

    private void SetRange(JComboBox comboBox, int from, int to) {
	comboBox.removeAllItems();
	for (int i = from; i < to; ++i)
	    comboBox.addItem(new Integer(i));
    }

    private void openFile(String filename) {
	try {
	    KFuzzyInput kfuzzyInput = fileReader.read(new FileReader(filename));
	    numClustersField.setValue(kfuzzyInput.getNumClusters());
	    numClustersField.setEnabled(true);

	    blendingField.setEnabled(true);
	    numIterationsField.setEnabled(true);

	    SetRange(firstIndex, 0, kfuzzyInput.getNumDimensions());
	    firstIndex.setEnabled(true);
	    SetRange(secondIndex, 0, kfuzzyInput.getNumDimensions());
	    secondIndex.setEnabled(true);

	    firstIndex.setSelectedIndex(0);
	    secondIndex.setSelectedIndex(0);

	    clusterizeButton.setEnabled(true);

	    model.setPoints(kfuzzyInput.getVectors());

	    paintComponent.setClusters(model.getPoints(), model.getClusters());
	    paintComponent.setFirstIndex(0);
	    paintComponent.setSecondIndex(0);
	    paintComponent.repaint();
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(MainFrame.this, String.format("Can't process file: %s", filename), "error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private JMenu createFileMenu() {
	JMenu file = new JMenu("File");
	file.setMnemonic('F');

	JMenuItem open = new JMenuItem(openAction);
	open.setMnemonic('O');
	open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));

	JMenuItem quit = new JMenuItem(quitAction);
	quit.setMnemonic('Q');
	quit.setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.CTRL_MASK));

	file.add(open);
	file.addSeparator();
	file.add(quit);

	return file;
    }

    private JToolBar createToolBar() {
	JToolBar toolBar = new JToolBar("Command bar");
	toolBar.add(new JButton(openAction));
	toolBar.add(clusterizeButton);
	toolBar.addSeparator();

	toolBar.add(new JLabel("Num clusters: "));
	toolBar.add(numClustersField);
	toolBar.add(new JLabel("Blending: "));
	toolBar.add(blendingField);
	toolBar.add(new JLabel("Max iterations: "));
	toolBar.add(numIterationsField);
	toolBar.addSeparator();

	toolBar.add(new JLabel("X axis: "));
	toolBar.add(firstIndex);
	toolBar.add(new JLabel("Y axis: "));
	toolBar.add(secondIndex);

	return toolBar;
    }

    private JFileChooser createFileChooser() {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Select data file");
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fileChooser.addChoosableFileFilter(new DataFilesFilter());

	return fileChooser;
    }

    public MainFrame(Model model) {
	this.model = model;

	fileReader = new SimpleReader();

	openAction = new OpenAction(createFileChooser());
	quitAction = new QuitAction();


	clusterizeButton = new JButton(new ClusterizeAction());
	clusterizeButton.setEnabled(false);

	numClustersField = new JFormattedTextField(NumberFormat.getIntegerInstance());
	numClustersField.setValue(new Integer(1));
	numClustersField.setEnabled(false);

	blendingField = new JFormattedTextField(NumberFormat.getNumberInstance());
	blendingField.setValue(new Double(1.5));
	blendingField.setEnabled(false);

	numIterationsField = new JFormattedTextField(NumberFormat.getIntegerInstance());
	numIterationsField.setValue(new Integer(1000));
	numIterationsField.setEnabled(false);

	firstIndex = new JComboBox();
	firstIndex.addActionListener(new ChangeIndexActionListener(true));
	secondIndex = new JComboBox();
	secondIndex.addActionListener(new ChangeIndexActionListener(false));

	firstIndex.setEnabled(false);
	secondIndex.setEnabled(false);

	setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	setLocationRelativeTo(null);

	JMenuBar menuBar = new JMenuBar();
	menuBar.add(createFileMenu());
	setJMenuBar(menuBar);

	add(createToolBar(), BorderLayout.NORTH);

	paintComponent = new ClustersPaintComponent();
	add(paintComponent, BorderLayout.CENTER);
    }

    private ClustersPaintComponent paintComponent;
    private AbstractAction openAction;
    private AbstractAction quitAction;
    private JComboBox firstIndex;
    private JComboBox secondIndex;

    private JFormattedTextField numClustersField;
    private JFormattedTextField blendingField;
    private JFormattedTextField numIterationsField;

    private JButton clusterizeButton;

    private ReaderInterface fileReader;

    private Model model;
}
