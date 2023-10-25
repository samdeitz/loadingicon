package loading;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/*
 * Sam Deitz
 * 5/23/2023
 * loading icon
 */

public class LoadingIcon extends JFrame implements ActionListener{
    
    public static void main(String[] args) {
        new LoadingIcon();
    }

    DrawingPanel panel = new DrawingPanel();
    
    final static int numBalls = 7;
    final static int PANW = 500, PANH = 500;

    Timer loadingTimer;
    int time;
    Timer circleTimer;

    final static int maxTime = 90;
    final static double angle = Math.PI/135;



    ArrayList<Ball> balls = new ArrayList<>();


    // corners to rotate at
    int corner = 0;
    Point[] corners = {
        new Point(325, PANH/4), new Point(175, PANH/4), new Point(100, PANH/2), 
        new Point(175, PANH/2+PANH/4), new Point(325, PANH/2+PANH/4), new Point(400, PANH/2)};

    // colors for balls
    Color[] colors = {Color.RED, Color.BLUE, new Color(95, 189, 199), Color.PINK, new Color(255,0,255)};


    boolean ani = false;
    Circle exp = new Circle(0, 0, 0);

    LoadingIcon() {
        this.setTitle("Loading...");
        panel = new DrawingPanel();
        this.add(panel);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Create balls

        // to choose color from array
        int colorChoose = 0;

        // distance between each ball(x and y) --> -1 to fit all
        double disX = 75/(numBalls-1);
        double disY = 130/(numBalls-1);

        // make each ball
        for(int i = 0; i < numBalls; i++) {

            // create ball starting at first point(top right) --> add distance based on which ball
            balls.add(new Ball(i*disX+325, i*disY+PANH/4, 15, colors[colorChoose]));

            // send to next color
            colorChoose++;
            if(colorChoose % colors.length == 0) colorChoose=0;
        }
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        loadingTimer = new Timer(10, this);
        loadingTimer.start();
        circleTimer = new Timer(1, new CT());
        circleTimer.start();
        
    }


    PointD rotatePoint(double angle, double x, double y, double centrex, double centrey) {
		double newx = (x-centrex) * Math.cos(angle) + (y-centrey) * Math.sin(angle);
		double newy = -(x-centrex) * Math.sin(angle) + (y-centrey) * Math.cos(angle);
		PointD pd = new PointD(); 
		pd.x = newx+centrex;
		pd.y = newy+centrey;
		return pd;
	}


    class DrawingPanel extends JPanel {
        DrawingPanel() {
            this.setPreferredSize(new Dimension(PANW, PANH));

            //semi transparent
            this.setBackground(new Color(0,0,0, 25));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // make 2D, and anti alias
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(4));
             
 
            g2.setColor(new Color(95, 189, 199));

            // hexagon code
            Polygon hex = new Polygon();

            hex.addPoint(175, PANH/4);
            hex.addPoint(325, PANH/4);

            hex.addPoint(400, PANH/2);

            hex.addPoint(325, PANH/4+PANH/2);
            hex.addPoint(175, PANH/4+PANH/2);

            hex.addPoint(100, PANH/2);
            g2.drawPolygon(hex);

            //balls
            for(Ball b : balls) {

                // inner circle
                g2.setColor(b.color);
                g2.fillOval((int)b.x-b.size/2, (int)b.y-b.size/2, b.size, b.size);
                
                // outer circle
                g2.setColor(Color.WHITE);
                g2.drawOval((int)b.x-b.size/2, (int)b.y-b.size/2, b.size, b.size); 
            }
            
            
            if(ani) {
                g2.setColor(new Color(95, 189, 199));
                g2.drawOval(exp.x-exp.size/2, exp.y-exp.size/2, exp.size, exp.size);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // rotate balls
        time++;

        // if it has run 120 times since last corner, move to next corner
        if(time == maxTime) {
            if(corner < 5) corner++; 
            else corner = 0;
            ani = true;
            time = 0;
        }
        
        // move 1 degree each time

        // rotate bsalls
        for(Ball b : balls) {
            PointD pd = rotatePoint(-angle,b.x, b.y, corners[corner].x, corners[corner].y);

            // set new points
            b.x =  pd.x;
            b.y =  pd.y;
        }
        panel.repaint();
    }

    class CT implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // if at corner
            if(ani) {

                //set start of circle
                Point start = corners[corner];
                exp.x = start.x;
                exp.y = start.y;

                // increment size to circle
                exp.size += 4;

                //if it makes its whole growth, end and dissappear
                if(exp.size == maxTime*2) {
                    ani = false;
                    exp.size = 0;
                }
                panel.repaint();
            }
        }
    
    }


}


