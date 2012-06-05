import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class sequencerFrame
{

    private JFrame frame;
    private static int[] mousePos;
    private static Sequencer seq;
    private static int[] scale = {72, 71, 69, 67, 65, 64, 62, 60};
    private static boolean[][] doa={
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
    };


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
        frame.setBounds(100, 100, 630, 510);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final Grid panel = new Grid(8,8);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().setLayout(null);
        seq = new Sequencer(scale, 0, 128, true);
        JButton btnExecute = new JButton("execute");
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //This is the sequence brah
                seq.createTrack(doa, 0);
                seq.playSequence();
            }
        });
        btnExecute.setBounds(525, 11, 89, 23);
        frame.getContentPane().add(btnExecute);
        panel.setBounds(0,0,500,510);
        panel.setBackground(new java.awt.Color(255,255,255));

        frame.getContentPane().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                PointerInfo pi = MouseInfo.getPointerInfo();
                Point point = new Point(pi.getLocation());
                SwingUtilities.convertPointFromScreen(point, panel);
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
                        doa[i][k] = !doa[i][k];

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


        return mousePos[0] < xMax && mousePos[0] > xMin && mousePos[1] < yMax && mousePos[1] > yMin;
    }

}


