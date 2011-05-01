package kfuzzy.gui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import kfuzzy.math.Vector;


public class ClustersPaintComponent extends JComponent {
    public final static int POINT_RADIUS = 6;
    public final static int X_OFFSET = 15;
    public final static int Y_OFFSET = 15;

    private Vector[] points = new Vector[] {};
    private int[] clusters = new int[] {};

    private Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.GRAY, Color.CYAN, Color.ORANGE, Color.PINK,
			       Color.MAGENTA, Color.DARK_GRAY, Color.LIGHT_GRAY };

    private int firstIndex = 0, secondIndex = 0;

    public void setFirstIndex(int index) {
	if (index >= 0 && index < getNumDimensions())
	    this.firstIndex = index;
    }

    public void setSecondIndex(int index) {
	if (index >= 0 && index < getNumDimensions())
	    this.secondIndex = index;
    }

    private double getMinCoord(int index) {
	double result = Double.POSITIVE_INFINITY;
	for (Vector p : points)
	    result = Math.min(result, p.get(index));
	return result;
    }

    private double getMaxCoord(int index) {
	double result = Double.NEGATIVE_INFINITY;
	for (Vector p : points)
	    result = Math.max(result, p.get(index));
	return result;
    }

    private Color getClusterColor(int cluster) {
	if (cluster < 0 || cluster >= colors.length)
	    return Color.BLACK;
	else
	    return colors[cluster];
    }

    private int getNumDimensions() {
	if (points.length == 0 || points[0].getSize() == 0)
	    return 0;
	else
	    return points[0].getSize();
    }

    public void setClusters(Vector[] points, int[] clusters) {
	this.points = points;
	this.clusters = clusters;
    }

    private void paintPoints(Graphics2D g2, int xoffset, int yoffset, int width, int height) {
	if (getNumDimensions() == 0)
	    return;

	double minX = getMinCoord(firstIndex), maxX = getMaxCoord(firstIndex);
	double minY = getMinCoord(secondIndex), maxY = getMaxCoord(secondIndex);

	double logicalWidth = maxX - minX, logicalHeight = maxY - minY;
	double scaleX = width / logicalWidth, scaleY = height / logicalHeight;


	for (int i = 0; i < points.length; ++i) {
	    double x = (points[i].get(firstIndex) - minX) * scaleX;
	    double y = (points[i].get(secondIndex) - minY) * scaleY;
	    double r = POINT_RADIUS;

	    Ellipse2D shape = new Ellipse2D.Double(xoffset + x - r / 2, yoffset + y - r / 2, r, r);

	    g2.setPaint(getClusterColor(clusters[i]));
	    g2.fill(shape);
	    g2.setPaint(Color.BLACK);
	    g2.draw(shape);
	}

    }

    @Override public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	int width = getWidth(), height = getHeight();

	Rectangle2D border = new Rectangle2D.Double(X_OFFSET, Y_OFFSET, width - 2 * X_OFFSET, height - 2 * Y_OFFSET);
	g2.setPaint(Color.BLACK);
	g2.draw(border);

	paintPoints(g2, X_OFFSET, Y_OFFSET, width - 2 * X_OFFSET, height - 2 * Y_OFFSET);
    }
}
