package kfuzzy.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import kfuzzy.Model;
import kfuzzy.algo.KFuzzyAlgorithm;
import kfuzzy.gui.ClustersPaintComponent;
import kfuzzy.io.KFuzzyInput;
import kfuzzy.io.ReaderInterface;
import kfuzzy.io.SimpleWriter;
import kfuzzy.io.TABReader;
import kfuzzy.io.WriterInterface;
import kfuzzy.math.Vector;


class State {
    public enum StateValues {
	Unknown, Unopened, Opened, Clusterized;
    }

    public interface StateChangedListener {
	void stateChanged(StateValues oldState, StateValues newState);
    }

    private StateValues state;
    private ArrayList<StateChangedListener> listeners;

    public State(StateValues state) {
	this.state = state;
	this.listeners = new ArrayList<StateChangedListener>();
    }

    public State() {
	this(StateValues.Unknown);
    }

    public void addListener(StateChangedListener listener) {
	listeners.add(listener);
    }

    public StateValues getState() {
	return state;
    }

    public void setState(StateValues newState) {
	for (StateChangedListener listener : listeners)
	    listener.stateChanged(state, newState);
	state = newState;
    }
}

public class MainFrame extends JFrame {
    private class DataFilesFilter extends FileFilter {
	public final static String DESCRIPTION = "Data files (*.tab)";

	public boolean accept(java.io.File file) {
	    if (file.isDirectory())
		return true;
	    return file.getName().endsWith("tab");
	}

	public String getDescription() {
	    return DESCRIPTION;
	}
    }

    private class OutputFilesFilter extends FileFilter {
	public final static String DESCRIPTION = "Text files (*.txt)";

	public boolean accept(java.io.File file) {
	    if (file.isDirectory())
		return true;
	    return file.getName().endsWith("txt");
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
		    openDispatcher(path);
		else
		    JOptionPane.showMessageDialog(MainFrame.this, fileChooser.getSelectedFile() + " doesn't exists", "error",
						  JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private class SaveAction extends AbstractAction {
	private JFileChooser fileChooser;

	public SaveAction(JFileChooser fileChooser) {
	    putValue(NAME, "Save");
	    this.fileChooser = fileChooser;
	}

	@Override public void actionPerformed(ActionEvent e) {
	    if (lastFile == null)
		saveAsDispatcher(fileChooser);
	    else
		saveDispatcher(lastFile);
	}
    }

    private class SaveAsAction extends AbstractAction {
	private JFileChooser fileChooser;

	public SaveAsAction(JFileChooser fileChooser) {
	    putValue(NAME, "Save As...");
	    this.fileChooser = fileChooser;
	}

	@Override public void actionPerformed(ActionEvent e) {
	    saveAsDispatcher(fileChooser);
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
		state.setState(State.StateValues.Clusterized);

		paintComponent.setClusters(model.getPoints(), model.getClusters());
		paintComponent.repaint();
	    } catch (Exception e) {
		JOptionPane.showMessageDialog(MainFrame.this, "Can't clusterize data", "error", JOptionPane.ERROR_MESSAGE);
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

    private void saveAsDispatcher(JFileChooser fileChooser) {
	int result = fileChooser.showSaveDialog(this);
	if (result == JFileChooser.APPROVE_OPTION)
	    saveDispatcher(fileChooser.getSelectedFile().getPath());
    }

    private void saveDispatcher(String filename) {
	OutputStreamWriter out = null;
	try {
	    out = new FileWriter(filename);
	    if (!fileWriter.write(out, model.getOutput()))
		throw new Exception("Can't write output");

	    lastFile = filename;
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(MainFrame.this, String.format("Can't write output to file: %s", filename), "error",
					  JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	} finally {
	    if (out != null)
		try {
		    out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
    }

    private void openDispatcher(String filename) {
	InputStreamReader in = null;
	try {
	    in = new FileReader(filename);
	    KFuzzyInput kfuzzyInput = fileReader.read(in);
	    if (kfuzzyInput == null)
		throw new Exception("can't process input file");

	    numClustersField.setValue(kfuzzyInput.getNumClusters());

	    SetRange(firstIndex, 0, kfuzzyInput.getNumDimensions());
	    SetRange(secondIndex, 0, kfuzzyInput.getNumDimensions());

	    firstIndex.setSelectedIndex(0);
	    secondIndex.setSelectedIndex(0);

	    model.setPoints(kfuzzyInput.getVectors());

	    paintComponent.setClusters(model.getPoints(), model.getClusters());
	    paintComponent.setFirstIndex(0);
	    paintComponent.setSecondIndex(0);
	    paintComponent.repaint();

	    lastFile = null;

	    state.setState(State.StateValues.Opened);
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(MainFrame.this, String.format("Can't process file %s", filename), "error", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	} finally {
	    if (in != null)
		try {
		    in.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
    }

    private JMenu createFileMenu() {
	JMenu file = new JMenu("File");
	file.setMnemonic('F');

	JMenuItem open = new JMenuItem(openAction);
	open.setMnemonic('O');
	open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));

	JMenuItem save = new JMenuItem(saveAction);
	save.setMnemonic('S');
	save.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));

	JMenuItem saveAs = new JMenuItem(saveAsAction);

	JMenuItem quit = new JMenuItem(quitAction);
	quit.setMnemonic('Q');
	quit.setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.CTRL_MASK));

	file.add(open);
	file.add(save);
	file.add(saveAs);
	file.addSeparator();
	file.add(quit);

	return file;
    }

    private JMenu createAlgoMenu() {
	JMenu algo = new JMenu("Algo");
	algo.setMnemonic('A');

	JMenuItem clusterize = new JMenuItem(clusterizeAction);
	clusterize.setMnemonic('C');
	clusterize.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK));

	algo.add(clusterize);

	return algo;
    }

    private JToolBar createToolBar() {
	JToolBar toolBar = new JToolBar("Command bar");
	toolBar.add(new JButton(openAction));
	toolBar.add(new JButton(clusterizeAction));
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

    private JFileChooser createOpenFileChooser() {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Select data file");
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fileChooser.addChoosableFileFilter(new DataFilesFilter());

	return fileChooser;
    }

    private JFileChooser createSaveFileChooser() {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Select file for saving results");
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fileChooser.addChoosableFileFilter(new OutputFilesFilter());

	return fileChooser;
    }

    public MainFrame(Model model) {
	this.state = new State();
	this.model = model;

	openAction = new OpenAction(createOpenFileChooser());

	JFileChooser saveFileChooser = createSaveFileChooser();
	saveAction = new SaveAction(saveFileChooser);
	saveAsAction = new SaveAsAction(saveFileChooser);

	numClustersField.setValue(new Integer(DEFAULT_NUM_CLUSTERS));
	blendingField.setValue(new Double(DEFAULT_BLENDING));
	numIterationsField.setValue(new Integer(DEFAULT_MAX_ITERATIONS));

	firstIndex.addActionListener(new ChangeIndexActionListener(true));
	secondIndex.addActionListener(new ChangeIndexActionListener(false));

	setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	setLocationRelativeTo(null);
	setTitle(TITLE);

	JMenuBar menuBar = new JMenuBar();
	menuBar.add(createFileMenu());
	menuBar.add(createAlgoMenu());
	setJMenuBar(menuBar);

	add(createToolBar(), BorderLayout.NORTH);
	add(paintComponent, BorderLayout.CENTER);

	this.state.addListener(new State.StateChangedListener() {
		public void stateChanged(State.StateValues oldState, State.StateValues newState) {
		    boolean enabled = newState == State.StateValues.Opened || newState == State.StateValues.Clusterized;

		    clusterizeAction.setEnabled(enabled);

		    numClustersField.setEnabled(enabled);
		    blendingField.setEnabled(enabled);
		    numIterationsField.setEnabled(enabled);

		    firstIndex.setEnabled(enabled);
		    secondIndex.setEnabled(enabled);
		}
	    });
	this.state.addListener(new State.StateChangedListener() {
		public void stateChanged(State.StateValues oldState, State.StateValues newState) {
		    boolean enabled = newState == State.StateValues.Clusterized;

		    saveAction.setEnabled(enabled);
		    saveAsAction.setEnabled(enabled);
		}
	    });
	this.state.setState(State.StateValues.Unopened);
    }

    public final static int DEFAULT_WIDTH = 800;
    public final static int DEFAULT_HEIGHT = 600;
    public final static String TITLE = "KFuzzy Algorithm";

    public final static int DEFAULT_NUM_CLUSTERS = 1;
    public final static double DEFAULT_BLENDING = 1.5;
    public final static int DEFAULT_MAX_ITERATIONS = 100;


    private ClustersPaintComponent paintComponent = new ClustersPaintComponent();

    private AbstractAction openAction;
    private AbstractAction saveAction;
    private AbstractAction saveAsAction;
    private AbstractAction quitAction = new QuitAction();
    private AbstractAction clusterizeAction = new ClusterizeAction();

    private JComboBox firstIndex = new JComboBox();
    private JComboBox secondIndex = new JComboBox();

    private JFormattedTextField numClustersField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField blendingField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField numIterationsField = new JFormattedTextField(NumberFormat.getIntegerInstance());

    private ReaderInterface fileReader = new TABReader();
    private WriterInterface fileWriter = new SimpleWriter();

    private Model model;
    private State state;

    private String lastFile = null;
}
