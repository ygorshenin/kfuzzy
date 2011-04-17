package kfuzzy;

import kfuzzy.math.*;


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

    public void go() {
    }
}
