package mapdenmark;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
import java.io.IOException;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

 
/**
 *
 * @author nith14
 */
public class MapView
{
    private JFrame frame;
    //min x-coordinate and max y-coordinate, subtracted from to make map origin in 0,0
    private final int MAX_Y = 6403000;
    private final int MIN_X = 441000; 
    public int minWidth = 800;
    public int minHeight = 600;
    public int xDivisor = 600; //initial x-dividant value
    public int yDivisor = 600; //initial y-dividant value
    
 
    public MapView()
    {
        frame = new JFrame("MapDenmark");
        frame.add(new DrawPanel());
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("Window Resized: Frame");
            }
        });
    }
 
    class DrawPanel extends JPanel
    {
 
        @Override
        public void paintComponent(Graphics g)
        {
 
            super.paintComponent(g);
            try {
                doDrawing(g);
            } catch (IOException ex) {
                System.out.println("Cannot find specified file");
            }
        }
        
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(minWidth, minHeight);
        }
 
        private void doDrawing(Graphics g) throws IOException
        {
            MapModel mm = new MapModel(); //object created to load data from model
            mm.loadData(); //loads data
 
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (int i = 0; i < mm.edges.size(); i++) {
                EdgeData edge = mm.edges.get(i);
                
                if(edge.TYP == 8) {
                    g2d.setColor(Color.GREEN);
                }
                
                else if(edge.TYP == 3){
                    g2d.setColor(Color.BLUE);
                }
                else if(edge.TYP == 1){
                    g2d.setColor(Color.RED);
                }
                else{
                    g2d.setColor(Color.BLACK);
                }
                
                //
                g2d.draw(new Line2D.Double(((mm.nodes.get(edge.FNODE).X_COORD) - MIN_X) / xDivisor,
                        (MAX_Y - (mm.nodes.get(edge.FNODE).Y_COORD)) / yDivisor,
                        ((mm.nodes.get(edge.TNODE).X_COORD) - MIN_X) / xDivisor,
                        (MAX_Y - (mm.nodes.get(edge.TNODE).Y_COORD)) / yDivisor));
            }
 
        }
    }
 
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new MapView();
            }
        });
    }
}