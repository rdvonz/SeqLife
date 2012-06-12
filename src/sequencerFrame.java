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
    // private static int[] mousePos;
    public static Conway conway;
    public static boolean[][] doa;
    private final int CELLWIDTH = 20;
    private final int CELLHEIGHT = 20;
    private final int ROWS = 25;
    private final int COLS = 10;
    private static Grid panel;

    //Create a sequencer
    Sequencer seq;

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
    }

    public void initGrid() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                doa[i][j] = false;
            }
        }
    }

    /**
     * Initialize the contents of the frame.
     */

    public static void refresh() {
        panel.repaint();


    }

    private void initialize() {
        //Initialize sequencer
        seq = new Sequencer(0, 128);
        seq.setScale(Sequencer.MIXOLYDIAN);
        doa = new boolean[ROWS][COLS];
        initGrid();
        frame = new JFrame();
        frame.setBounds(100, 100, 630, 510);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        conway = new Conway(doa);


        panel = new Grid(ROWS, COLS);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().setLayout(null);

        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        JButton btnExecute = new JButton("Execute");
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                seq.parseSequence(sequencerFrame.doa);
                seq.playSequence();
            }
        });
        btnExecute.setBounds(20, COLS * CELLHEIGHT + 20, 89, 23);
        frame.getContentPane().add(btnExecute);

        JButton btnNewButton = new JButton("Stop");
        btnNewButton.setBounds(20, COLS * CELLHEIGHT + 23 + 20, 89, 23);
        frame.getContentPane().add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Step");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                seq.setTempo(0);
            }
        });
        btnNewButton_1.setBounds(20, COLS * CELLHEIGHT + 46 + 20, 89, 23);
        frame.getContentPane().add(btnNewButton_1);

        final JSpinner spinner = new JSpinner();
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                seq.setInstrument(Integer.parseInt(spinner.getValue().toString()));
            }
        });
        spinner.setModel(new SpinnerNumberModel(0, 0, 128, 1));
        spinner.setBounds(0, COLS * CELLHEIGHT + 23 * 3 + 20, 51, 20);
        frame.getContentPane().add(spinner);

        JLabel label = new JLabel("#:");
        label.setBounds(0, COLS * CELLHEIGHT + 23 * 4 + 20, 46, 14);
        frame.getContentPane().add(label);

        final JSlider slider = new JSlider();
        slider.setMaximum(256);
        slider.setMinimum(64);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                seq.setTempo(slider.getValue());
            }
        });
        slider.setBounds(402, 158, 200, 23);
        frame.getContentPane().add(slider);

        JLabel lblBpm = new JLabel("BPM");
        lblBpm.setBounds(489, 131, 46, 14);
        frame.getContentPane().add(lblBpm);

        final JComboBox comboBox = new JComboBox();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (comboBox.getSelectedIndex() == 0) {
                    seq.setScale(Sequencer.BIYU);
                }
                if (comboBox.getSelectedIndex() == 1) {
                    seq.setScale(Sequencer.BLUES);
                }
                if (comboBox.getSelectedIndex() == 2) {
                    seq.setScale(Sequencer.BLUESDIMINISHED);
                }
                if (comboBox.getSelectedIndex() == 3) {
                    seq.setScale(Sequencer.DORIAN);
                }
                if (comboBox.getSelectedIndex() == 4) {
                    seq.setScale(Sequencer.FULLMINOR);
                }
                if (comboBox.getSelectedIndex() == 5) {
                    seq.setScale(Sequencer.HARMONICMAJOR);
                }
                if (comboBox.getSelectedIndex() == 6) {
                    seq.setScale(Sequencer.HAWAIIAN);
                }
                if (comboBox.getSelectedIndex() == 7) {
                    seq.setScale(Sequencer.IONIANSHARP);
                }
                if (comboBox.getSelectedIndex() == 8) {
                    seq.setScale(Sequencer.JAZZMINOR);
                }
                if (comboBox.getSelectedIndex() == 9) {
                    seq.setScale(Sequencer.LYDIAN);
                }
                if (comboBox.getSelectedIndex() == 10) {
                    seq.setScale(Sequencer.MAJOR);
                }
                if (comboBox.getSelectedIndex() == 11) {
                    seq.setScale(Sequencer.MIXOLYDIAN);
                }
                if (comboBox.getSelectedIndex() == 12) {
                    seq.setScale(Sequencer.ORIENTAL);
                }
                if (comboBox.getSelectedIndex() == 13) {
                    seq.setScale(Sequencer.SUPERLOCRIAN);
                }
                if (comboBox.getSelectedIndex() == 14) {
                    seq.setScale(Sequencer.VERDIENIGMATICASCENDING);
                }
                if (comboBox.getSelectedIndex() == 15) {
                    seq.setScale(Sequencer.ZIRAFKEND);
                }
            }
        });
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Biyu", "Blues", "BluesDiminished", "Dorian", "FullMinor", "HarmonicMajor", "Hawaiian", "IonianSharp", "JazzMinor", "Lydian", "Major", "MixoLydian", "Oriental", "Superlocrian", "VerdienigmaticAscending", "Zirafkend"}));
        comboBox.setBounds(444, 227, 170, 20);
        frame.getContentPane().add(comboBox);

        JLabel lblScale = new JLabel("Scale:");
        lblScale.setBounds(402, 230, 46, 14);
        frame.getContentPane().add(lblScale);
        panel.setBounds(0, 0, CELLWIDTH * ROWS, CELLHEIGHT * COLS);
        panel.setBackground(new java.awt.Color(255, 255, 255));

        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PointerInfo pi = MouseInfo.getPointerInfo();
                Point point = new Point(pi.getLocation());
                SwingUtilities.convertPointFromScreen(point, panel);
                int cellX = ((int) point.getX() / CELLWIDTH * COLS) / COLS;
                int cellY = ((int) point.getY() / CELLHEIGHT * ROWS) / ROWS;
                doa[cellX][cellY] = !doa[cellX][cellY];
                panel.repaint();


            }
        });


    }

    private class Grid extends JPanel {
        int row;
        int col;

        Grid(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    g.drawRect(((i * CELLWIDTH)), j * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                    if (doa[i][j]) {
                        g.fillRect(i * CELLWIDTH, j * CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
                    }

                }
            }
        }


    }
}


