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

    private Conway conway;
    private final int CELLWIDTH = 50;
    private final int CELLHEIGHT = 50;
    private final int ROWS = 8;
    private final int COLS = 8;


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
        final Grid panel = new Grid(ROWS,COLS);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().setLayout(null);
        seq = new Sequencer(scale, 0, 128, true);
        conway = new Conway(doa);
        JButton btnExecute = new JButton("execute");
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //This is the sequence brah
                seq.createTrack(doa, 0);
                seq.playSequence();
                conway.setGrid(doa);
                doa = conway.nextStep();
                panel.repaint();
            }
        });
        btnExecute.setBounds(525, 11, 89, 23);
        frame.getContentPane().add(btnExecute);
        panel.setBounds(0,0,CELLWIDTH*ROWS,CELLHEIGHT*COLS);
        panel.setBackground(new java.awt.Color(255,255,255));

        frame.getContentPane().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                PointerInfo pi = MouseInfo.getPointerInfo();
                Point point = new Point(pi.getLocation());
                SwingUtilities.convertPointFromScreen(point, panel);
                int cellX = ((int)point.getX()/CELLWIDTH*COLS)/8;
                int cellY = ((int)point.getY()/CELLHEIGHT*ROWS)/8;
                System.out.println(cellX+","+cellY);
                doa[cellX][cellY] = !doa[cellX][cellY];
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
                for(int j=0; j<row;j++)
                {
                    g.drawRect(((i*CELLWIDTH)),j*CELLHEIGHT,CELLWIDTH, CELLHEIGHT);
                    if(doa[i][j]){
                        g.fillRect(i*CELLWIDTH, j*CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                    }

                }
            }
        }


    }

}


