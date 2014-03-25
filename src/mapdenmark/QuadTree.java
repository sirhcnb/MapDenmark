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
    double x, y, length;
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

            double midX = x + length / 2;
            double midY = y + length / 2;

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
            NW.setCoords(x, y, length / 2);
            NW.split();

            NE = new QuadTree(ne, nodes, ID + "1");
            NE.setCoords(midX, y, length / 2);
            NE.split();

            SW = new QuadTree(sw, nodes, ID + "2");
            SW.setCoords(x, midY, length / 2);
            SW.split();

            SE = new QuadTree(se, nodes, ID + "3");
            SE.setCoords(midX, midY, length / 2);
            SE.split();
        }
    }

    public void setCoords(double x, double y, double length) {
        this.x = x;
        this.y = y;
        this.length = length;

    }

    public String getID(double x, double y) {
        if (NW == null) {
            return this.ID;
        }
        if (NW.nextLevel(x, y)) {
            return NW.getID(x, y);
        }
        if (NE.nextLevel(x, y)) {
            return NE.getID(x, y);
        }
        if (SW.nextLevel(x, y)) {
            return SW.getID(x, y);
        }
        if (SE.nextLevel(x, y)) {
            return SE.getID(x, y);
        }
        return ID;
    }

    private boolean nextLevel(double x1, double y1) {
        return (x1 >= x
                && y1 >= y
                && x1 <= x + length
                && y1 <= y + length);
    }
    
    private boolean nextLevel(double x1, double y1, double x2, double y2) {
        return (x1 >= x
                && y1 >= y
                && x2 <= x + length
                && y2 <= y + length);
    }
    
    public ArrayList<EdgeData> queryRange(double x1, double y1, double x2, double y2){
        if (NW.nextLevel(x1, y1, x2, y2)) return NW.queryRange(x1, y1, x2, y2);
        if (NE.nextLevel(x1, y1, x2, y2)) return NE.queryRange(x1, y1, x2, y2);
        if (SW.nextLevel(x1, y1, x2, y2)) return SW.queryRange(x1, y1, x2, y2);
        if (SE.nextLevel(x1, y1, x2, y2)) return SE.queryRange(x1, y1, x2, y2);
                
        return getEdges();
    
} 
    public ArrayList<EdgeData> getEdges()
    {
        if(NW == null) return edges; 
        ArrayList<EdgeData> e = new ArrayList<>();
        e.addAll(NW.getEdges());
        e.addAll(NE.getEdges());
        e.addAll(SW.getEdges());
        e.addAll(SE.getEdges());
        return e;        
    }
    
}
