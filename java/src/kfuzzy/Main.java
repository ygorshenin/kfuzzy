package kfuzzy;

import kfuzzy.math.*;


/**
 * Main is the main class of the whole project.
 * Takes responsibility of creating model and running form.
 *
 * @author Yuri Gorshenin
 * @version 2011.0418
 * @since 1.6
 */
public class Main implements Runnable {
    public void run() {
	try {
	    go();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(-1);
	}
    }

    public static void main(String[] args) {
	new Thread(new Main()).start();
    }

    /**
     * Main method of the project. Creates model and runs form.
     */
    public void go() {
    }
}
