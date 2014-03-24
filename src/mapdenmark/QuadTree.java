/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapdenmark;

/**
 *
 * @author Nicolai
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class QuadTree {

    QuadTree NW, NE, SW, SE;
    MapModel mm = new MapModel(); //object created to load data from model
    private final ArrayList edges;
    private final HashMap nodes;
    double x, y, height, width;
    public final String ID;
    //private final double width = 800;
    //private final double height = 590;
    

    public QuadTree(ArrayList edges, HashMap nodes, String ID) {
        this.edges = mm.edges;
        this.nodes = mm.nodes;
        this.ID = ID;
    }

    public void split() throws IOException {

        if (edges.size() > 200) {

            double midX = x + width / 2;
            double midY = y + height / 2;

            ArrayList<EdgeData> nw = new ArrayList<>();
            ArrayList<EdgeData> ne = new ArrayList<>();
            ArrayList<EdgeData> sw = new ArrayList<>();
            ArrayList<EdgeData> se = new ArrayList<>();

            for (EdgeData e : mm.edges) {
                NodeData fn = mm.nodes.get(e.FNODE);
                NodeData tn = mm.nodes.get(e.TNODE);

                if (fn.X_COORD <= midX && fn.Y_COORD <= midY) {
                    nw.add(e);
                } else if (fn.X_COORD > midX && fn.Y_COORD <= midY) {
                    ne.add(e);
                } else if (fn.X_COORD <= midX && fn.Y_COORD > midY) {
                    sw.add(e);
                } else {
                    se.add(e);
                }

                if (tn.X_COORD <= midX && tn.Y_COORD <= midY
                        && !(fn.X_COORD <= midX && fn.Y_COORD <= midY)) {
                    nw.add(e);
                } else if (tn.X_COORD > midX && tn.Y_COORD <= midY
                        && !(fn.X_COORD > midX && fn.Y_COORD <= midY)) {
                    ne.add(e);
                } else if (tn.X_COORD <= midX && tn.Y_COORD > midY
                        && !(fn.X_COORD <= midX && fn.Y_COORD > midY)) {
                    sw.add(e);
                } else if (tn.X_COORD > midX && tn.Y_COORD > midY
                        && !(fn.X_COORD > midX && fn.Y_COORD > midY)) {
                    se.add(e);
                }
            }

            NW = new QuadTree(nw, nodes, ID + "0");
            NW.setCoords(x, y, width / 2, height / 2);
            NW.split();

            NE = new QuadTree(ne, nodes, ID + "1");
            NE.setCoords(midX, y, width / 2, height / 2);
            NE.split();

            SW = new QuadTree(sw, nodes, ID + "2");
            SW.setCoords(x, midY, width / 2, height / 2);
            SW.split();

            SE = new QuadTree(se, nodes, ID + "3");
            SE.setCoords(midX, midY, width / 2, height / 2);
            SE.split();
        }
    }

    public void setCoords(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }
}

