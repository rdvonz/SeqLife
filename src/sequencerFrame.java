import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class sequencerFrame {

    private JFrame frame;
    private static int[] mousePos;
    private static Sequencer seq;
    public static Conway conway;
    //TODO: create an interface for these scales
    private static int[] cmajscale = {72, 71, 69, 67, 65, 64, 62, 60};
    private static int[] scale = {61, 63, 66, 68, 70, 75, 78, 68};
    public static boolean[][] doa = {
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false}
    };
    private final int CELLWIDTH = 50;
    private final int CELLHEIGHT = 50;
    private final int ROWS = 8;
    private final int COLS = 8;
    private static Grid panel;
    public static boolean done = false;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    sequencerFrame window = new sequencerFrame();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public sequencerFrame() {
        initialize();
        mousePos = new int[2];
    }

    /**
     * Initialize the contents of the frame.
     */

    public static void refresh() {
        Sequencer.parseSequence(sequencerFrame.doa);
        sequencerFrame.doa = sequencerFrame.conway.nextStep();
        panel.repaint();


    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 630, 510);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        conway = new Conway(doa);

        panel = new Grid(ROWS, COLS);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().setLayout(null);
        //TODO: create a gui interface for the instrument number
        //TODO: create a gui interface for the bpm
        //TODO Create a gui interface for the damp pedal
        //refer to the sequencer class for these values, you may need to make getters and setters, Evan.
        Sequencer.initialize(scale, 0, 128, true);

        JButton btnExecute = new JButton("Execute");
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Sequencer.parseSequence(sequencerFrame.doa);
                Sequencer.playSequence();
            }
        });
        btnExecute.setBounds(525, 11, 89, 23);
        frame.getContentPane().add(btnExecute);

        JButton btnNewButton = new JButton("Stop");
        btnNewButton.setBounds(525, 36, 89, 23);
        frame.getContentPane().add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Step");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

            }
        });
        btnNewButton_1.setBounds(525, 59, 89, 23);
        frame.getContentPane().add(btnNewButton_1);

        final JSpinner spinner = new JSpinner();
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                Sequencer.setInstrument(Integer.parseInt(spinner.getValue().toString()));
            }
        });
        spinner.setModel(new SpinnerNumberModel(0, 0, 128, 1));
        spinner.setBounds(563, 93, 51, 20);
        frame.getContentPane().add(spinner);

        JLabel label = new JLabel("#:");
        label.setBounds(535, 96, 46, 14);
        frame.getContentPane().add(label);

        JSlider slider = new JSlider();
        slider.setBounds(402, 158, 200, 23);
        frame.getContentPane().add(slider);

        JLabel lblBpm = new JLabel("BPM");
        lblBpm.setBounds(489, 131, 46, 14);
        frame.getContentPane().add(lblBpm);

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Cb", "E#"}));
        comboBox.setBounds(568, 227, 46, 20);
        frame.getContentPane().add(comboBox);

        JLabel lblScale = new JLabel("Scale:");
        lblScale.setBounds(525, 230, 46, 14);
        frame.getContentPane().add(lblScale);
        panel.setBounds(0, 0, CELLWIDTH * ROWS, CELLHEIGHT * COLS);
        panel.setBackground(new java.awt.Color(255, 255, 255));

        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PointerInfo pi = MouseInfo.getPointerInfo();
                Point point = new Point(pi.getLocation());
                SwingUtilities.convertPointFromScreen(point, panel);
                int cellX = ((int) point.getX() / CELLWIDTH * COLS) / 8;
                int cellY = ((int) point.getY() / CELLHEIGHT * ROWS) / 8;
                doa[cellX][cellY] = !doa[cellX][cellY];
                panel.repaint();


            }
        });


    }

    private class Grid extends JPanel {
        int row;
        int col;
        int iBoundMax;
        int iBoundMin;
        int kBoundMax;
        int kBoundMin;

        Grid(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < col; i++) {
                for (int j = 0; j < row; j++) {
                    g.drawRect(((i * CELLWIDTH)), j * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                    if (doa[i][j]) {
                        g.fillRect(i * CELLWIDTH, j * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                    }

                }
            }
        }


    }
}


