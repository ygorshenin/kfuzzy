package kfuzzy.gui;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;


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
		    JOptionPane.showMessageDialog(MainFrame.this, fileChooser.getSelectedFile(), "info",
						  JOptionPane.INFORMATION_MESSAGE);
		else
		    JOptionPane.showMessageDialog(MainFrame.this, fileChooser.getSelectedFile() + " doesn't exists", "error",
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

    private JMenu createFileMenu() {
	JMenu file = new JMenu("File");
	file.setMnemonic('F');

	JMenuItem open = new JMenuItem(new OpenAction(createFileChooser()));
	open.setMnemonic('O');
	open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));

	JMenuItem quit = new JMenuItem(new QuitAction());
	quit.setMnemonic('Q');
	quit.setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.CTRL_MASK));

	file.add(open);
	file.addSeparator();
	file.add(quit);

	return file;
    }

    private JFileChooser createFileChooser() {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Select data file");
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fileChooser.addChoosableFileFilter(new DataFilesFilter());

	return fileChooser;
    }

    public MainFrame() {
	setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	setLocationRelativeTo(null);

	JMenuBar menuBar = new JMenuBar();
	menuBar.add(createFileMenu());
	setJMenuBar(menuBar);
    }
}
