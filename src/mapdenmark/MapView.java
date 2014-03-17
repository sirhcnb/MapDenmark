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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.io.IOException;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;
import java.util.Collections;
import java.util.ArrayList;


 
/**
 *
 * @author nith14
 */
public class MapView
{
    private JFrame frame;
    //min x-coordinate and max y-coordinate, subtracted from to make map origin in 0,0
    private int MAX_Y = 6403000;
    private int MIN_X = 441000; 
    public int minWidth = 800;
    public int minHeight = 600;
    public int xDivisor = 600; //initial x-dividant value
    public int yDivisor = 600; //initial y-dividant value
    JLabel roadlabel = new JLabel(); 
    HashMap<Point, String> map = new HashMap<Point, String>();
    ArrayList<Double> distanceList = new ArrayList<Double>();
    HashMap<Double, String> distanceMap = new HashMap<Double, String>();
    
 
    public MapView()
    {
        frame = new JFrame("MapDenmark");
        frame.add(new DrawPanel());
        frame.add(roadlabel, "South");
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
        
         frame.addMouseMotionListener(new MouseMotionListener()
        {          
                 
        @Override
        public void mouseMoved(MouseEvent e) {
        int mouseY = e.getY();    
        int mouseX = e.getX();
        Point source = new Point(mouseX,mouseY);
        //roadlabel.setText("X = " + e.getX()*xDivisor + " Y = " + e.getY()*yDivisor);
        
        for (Map.Entry<Point, String> entry : map.entrySet()) {
            distanceMap.put(source.distanceSq(entry.getKey()),entry.getValue()); 
            distanceList.add(source.distanceSq(entry.getKey()));
        }
        
        Collections.sort(distanceList); 

            Double closest = distanceList.get(0);
            String name = distanceMap.get(closest);
            roadlabel.setText(name);
        
        distanceList.clear();
        distanceMap.clear();
        }    
        
        @Override
        public void mouseDragged(MouseEvent e) {
        //System.out.println("Mouse dragged");
        }  
        
        });        
        
        frame.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {}

                    @Override
                    public void keyReleased(KeyEvent e) {}

                    @Override //Move and repaint map in window
                    public void keyPressed(KeyEvent e) {
                        System.out.println("Pressed " + e.getKeyCode());
                        		
                        if (e.getKeyCode() == 37){
                           MIN_X = MIN_X+5000; System.out.println(MIN_X);
                        }   
                        else if (e.getKeyCode() == 39){
                           MIN_X = MIN_X-5000;  System.out.println(MIN_X);
                        }   
                        else if (e.getKeyCode() == 38){
                           MAX_Y = MAX_Y-5000;  System.out.println(MAX_Y);
                        }
                        else if (e.getKeyCode()== 40){
                           MAX_Y = MAX_Y+5000;  System.out.println(MAX_Y);
                        }
                        frame.repaint();
                        
                    
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
                
                int xnode = (int) ((mm.nodes.get(edge.TNODE).X_COORD) - MIN_X);
                int ynode = (int)(MAX_Y -(mm.nodes.get(edge.FNODE).Y_COORD));

                if(edge.VEJNAVN != "")
                {
                    map.put(new Point(xnode/xDivisor,ynode/yDivisor), edge.VEJNAVN);
                    
                }
                
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
