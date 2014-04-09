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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

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
    double xmint,ymint,xmin,ymin;
    JLabel roadlabel = new JLabel();
    HashMap<Point, String> map = new HashMap<Point, String>();
    ArrayList<Double> distanceList = new ArrayList<Double>();
    HashMap<Double, String> distanceMap = new HashMap<Double, String>();
    private double zoomed = 1.0;
    private int zoomX;
    private int zoomY;

     public MapView()
    {
        frame = new JFrame("MapDenmark");
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        JMenu fileMenu = new JMenu("Zoom");
        menubar.add(fileMenu);
        JMenuItem zoomIn = new JMenuItem("Zoom in");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        fileMenu.add(zoomIn);
        fileMenu.add(zoomOut);
        frame.add(new DrawPanel());
        frame.add(roadlabel, "South");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                System.out.println("Window Resized: Frame");
            }
        });

        frame.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
            }

            @Override //Move and repaint map in window
            public void keyPressed(KeyEvent e)
            {
                System.out.println("Pressed " + e.getKeyCode());

                if (e.getKeyCode() == 37)
                {
                    MIN_X = MIN_X - 5000;
                    System.out.println(MIN_X);
                } else if (e.getKeyCode() == 39)
                {
                    MIN_X = MIN_X + 5000;
                    System.out.println(MIN_X);
                } else if (e.getKeyCode() == 38)
                {
                    MAX_Y = MAX_Y + 5000;
                    System.out.println(MAX_Y);
                } else if (e.getKeyCode() == 40)
                {
                    MAX_Y = MAX_Y - 5000;
                    System.out.println(MAX_Y);
                }
                frame.repaint();

            }
        });
    }


    class DrawPanel extends JPanel
    {

        private AffineTransform transform = new AffineTransform();
        private double scale = 1.0;

        public DrawPanel()
        {
            this.addMouseMotionListener(new mouseMotionHandler());
            this.addMouseWheelListener(new ZoomHandler());
            this.addMouseMotionListener(new StreetNameHandler());
            
        }

        @Override
        public void paintComponent(Graphics g)
        {

            super.paintComponent(g);
            try
            {
                doDrawing(g);
            } catch (IOException ex)
            {
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
                                    
            
            
            for (int i = 0; i < mm.edges.size(); i++)
            {
                EdgeData edge = mm.edges.get(i);

                int xnode = (int) ((mm.nodes.get(edge.TNODE).X_COORD) - MIN_X);
                int ynode = (int) (MAX_Y - (mm.nodes.get(edge.FNODE).Y_COORD));
                
                if (edge.VEJNAVN != null && edge.VEJNAVN.length() > 0)
                {
                    map.put(new Point(xnode, ynode), edge.VEJNAVN);
                    //map.put(new Point(xnode / xDivisor, ynode /yDivisor), edge.VEJNAVN);

                }
                
                for(int r = 0; r < mm.edges.size();r++){
                
                    ymint = mm.nodes.get(edge.TNODE).Y_COORD;
                        if(mm.nodes.get(edge.TNODE).Y_COORD < ymint){
                   ymint = mm.nodes.get(edge.TNODE).Y_COORD;
                }
                 xmint = mm.nodes.get(edge.TNODE).X_COORD;
                if(mm.nodes.get(edge.TNODE).X_COORD < xmint){
                   xmint = mm.nodes.get(edge.TNODE).X_COORD;
                }
                    
                 ymin = mm.nodes.get(edge.FNODE).Y_COORD;
                        if(mm.nodes.get(edge.FNODE).Y_COORD < ymin){
                   ymin = mm.nodes.get(edge.FNODE).Y_COORD;
                }
                 xmin = mm.nodes.get(edge.FNODE).X_COORD;
                if(mm.nodes.get(edge.FNODE).X_COORD < xmin){
                   xmin = mm.nodes.get(edge.FNODE).X_COORD;
                }
                        }
                    
                

                if (edge.TYP == 8)
                {
                    g2d.setColor(Color.GREEN);
                } else if (edge.TYP == 3)
                {
                    g2d.setColor(Color.BLUE);
                } else if (edge.TYP == 1)
                {
                    g2d.setColor(Color.RED);
                } else
                {
                    g2d.setColor(Color.BLACK);
                }

                Line2D.Double line = new Line2D.Double(((mm.nodes.get(edge.FNODE).X_COORD) - MIN_X) / xDivisor,
                        (MAX_Y - (mm.nodes.get(edge.FNODE).Y_COORD)) / yDivisor,
                        ((mm.nodes.get(edge.TNODE).X_COORD) - MIN_X) / xDivisor,
                        (MAX_Y - (mm.nodes.get(edge.TNODE).Y_COORD)) / yDivisor);

                Shape shape = transform.createTransformedShape(line);
                g2d.draw(shape);
            }
            g2d.setTransform(transform);

            transform.scale(scale, scale);
System.out.println(ymint + "," + xmint +","+ymin+","+xmin);
        }
        private class StreetNameHandler implements MouseMotionListener
        {
          @Override
            public void mouseMoved(MouseEvent e)
            {
                int mouseY = e.getY();
                int mouseX = e.getX();
                Point source = new Point(mouseX, mouseY);
                //roadlabel.setText("X = " + e.getX()*xDivisor + " Y = " + e.getY()*yDivisor);

                for (Map.Entry<Point, String> entry : map.entrySet())
                {
                    distanceMap.put(source.distanceSq(entry.getKey()), entry.getValue());
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
            public void mouseDragged(MouseEvent e)
            {
                //System.out.println("Mouse dragged");
            }

        }  
        

        
        private class mouseMotionHandler implements MouseMotionListener 
        {
            int mouseY;
            int mouseX;
            
            @Override
            public void mouseMoved(MouseEvent e)
            {
                
                if(zoomed == 1.0)
                {    
                mouseY = e.getY() * xDivisor;
                mouseX = e.getX() * yDivisor;
                }
                else
                {
                zoomY = (zoomY*yDivisor) - (int)((minHeight/zoomed)/2); 
                zoomX = (zoomX*xDivisor) - (int)((minWidth/zoomed)/2); 
                //System.out.println(zoomed);
                double convY = (e.getY() * yDivisor) /zoomed;
                double convX = (e.getX() * xDivisor) /zoomed;
                mouseY = zoomY + (int)convY;
                mouseX = zoomX + (int)convX;
                //System.out.println("X " + mouseX + " Y " + mouseY);

                }    
                
                Point source = new Point(mouseX, mouseY);
                //roadlabel.setText("X = " + e.getX()*xDivisor + " Y = " + e.getY()*yDivisor);

                for (Map.Entry<Point, String> entry : map.entrySet())
                {
                    distanceMap.put(source.distanceSq(entry.getKey()), entry.getValue());
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
            public void mouseDragged(MouseEvent e)
            {
                //System.out.println("Mouse dragged");
            }

        }
        
        private class ZoomHandler implements MouseWheelListener
        {
            double scale = 1.0;

            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
                {

                    Point2D p1 = e.getPoint();
                    Point2D p2 = null;
                    try
                    {
                        p2 = transform.inverseTransform(p1, null);
                    } catch (NoninvertibleTransformException ex)
                    {
                        // should not get here
                        ex.printStackTrace();
                        return;
                    }

                    scale -= (0.1 * e.getWheelRotation());
                    scale = Math.max(0.1, scale);
                    zoomed = scale;
                    zoomX = (int)p1.getX();
                    zoomY = (int)p1.getY();

                    transform.setToIdentity();
                    transform.translate(p1.getX(), p1.getY());
                    transform.scale(scale, scale);
                    transform.translate(-p2.getX(), -p2.getY());


                    DrawPanel.this.revalidate();
                    DrawPanel.this.repaint();
                }
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
