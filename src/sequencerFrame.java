import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class sequencerFrame
{

    private JFrame frame;
    private static int[] mousePos;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    sequencerFrame window = new sequencerFrame();
                    window.frame.setVisible(true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public sequencerFrame()
    {
        initialize();
        mousePos = new int[2];
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frame = new JFrame();
        frame.setBounds(100, 100, 535, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Grid panel = new Grid(8,8);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setBounds(100,40,300,200);
        panel.setBackground(new java.awt.Color(255,255,255));

        frame.getContentPane().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                PointerInfo pi = MouseInfo.getPointerInfo();
                Point point = new Point(pi.getLocation());
                SwingUtilities.convertPointFromScreen(point, e.getComponent());
                mousePos[0] = (int)point.getX();
                mousePos[1] = (int)point.getY();
                panel.repaint();

            }
        });
    }

    private class Grid extends JPanel
    {
        int row;
        int col;
        int iBoundMax;
        int iBoundMin;
        int kBoundMax;
        int kBoundMin;
        boolean[][] doa={
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false}
        };
        Grid(int row, int col)
        {
            this.row = row;
            this.col = col;
        }
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for(int i=0;i<col;i++)
            {
                for(int k=0;k<row;k++)
                {
                    g.drawRect(((i*50)+25),k*50,50,50);
                    iBoundMin = ((i*50)+25);
                    iBoundMax = ((i*50)+25)+50;
                    kBoundMin = (k*50);
                    kBoundMax = (k*50)+50;

                    if(isInBounds(iBoundMin, iBoundMax, kBoundMin, kBoundMax))
                    {
                        if(doa[i][k] ==false)
                        {
                            doa[i][k] = true;
                        }
                        else
                        {
                            doa[i][k] = false;
                        }

                    }
                    if(doa[i][k]){
                        g.fillRect(i*50+25, k*50, 50, 50);
                    }

                }
            }
            g.drawString("C", 11, 380);
            g.drawString("D", 11, 330);
            g.drawString("E", 11, 280);
            g.drawString("F", 11, 230);
            g.drawString("G", 11, 180);
            g.drawString("A", 11, 130);
            g.drawString("B", 11, 80);
            g.drawString("C", 11, 30);

        }


    }
    private static boolean isInBounds(int xMin, int xMax, int yMin, int yMax)
    {


        if(mousePos[0] < xMax && mousePos[0] > xMin && mousePos[1] < yMax && mousePos[1] > yMin )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}


