import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class sequencerFrame {

    private JFrame frame;
    // private static int[] mousePos;
    public static Conway conway;
    public static boolean[][] doa;
    private final int CELLWIDTH = 50;
    private final int CELLHEIGHT = 50;
    private final int ROWS = 8;
    private final int COLS = 8;
    private static Grid panel;

    private JButton btnStart;
    private Timer timer;
    private JComboBox comboBox_1;
    private String[] ports = new String[]{""};
    //Create a sequencer
    Sequencer seq;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
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

        comboBox_1 = new JComboBox();
        comboBox_1.setBounds(547, 258, 67, 20);
        frame.getContentPane().add(comboBox_1);

        timer = new Timer();
        timer.scheduleAtFixedRate(new pollPorts(), 0, 500);

        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (Visualizer.isRunning()) {
                    Visualizer.stop();
                    btnStart.setText("Start");
                } else {
                    if (comboBox_1.getModel().getSize() > 0) {
                        Visualizer.start((String) comboBox_1.getSelectedItem());
                        btnStart.setText("Stop");
                    }
                }
            }
        });

        btnStart.setBounds(525, 283, 89, 23);
        frame.getContentPane().add(btnStart);
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

    public class pollPorts extends TimerTask {
        public void run() {
            String[] temp = Visualizer.getPorts();
            boolean notMatched = false;
            if (temp.length != ports.length) {
                notMatched = true;
            } else {
                for (int i = 0; i < temp.length; i++) {
                    if (ports[i].compareTo(temp[i]) != 0) {
                        notMatched = true;
                    }
                }
            }
            if (notMatched) {
                //System.out.println("change");
                ports = temp;
                DefaultComboBoxModel model = new DefaultComboBoxModel(ports);
                comboBox_1.setModel(model);
            }
        }

    }


}


