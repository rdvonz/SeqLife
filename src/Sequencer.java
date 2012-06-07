import javax.sound.midi.*;

public class Sequencer {
    private static int instrument;
    private Track track;
    boolean[][] grid;
    private static int ticks;
    private static int tempo;
    private int[] scale = {61, 63, 66, 68, 70, 75, 78, 68};

    private static javax.sound.midi.Sequencer sequencer;

    private static int velocity;

    private Sequence sequence;

    //Scale constants
    public static final int BIYU = 0;
    public static final int BLUES = 1;
    public static final int BLUESDIMINISHED = 3;
    public static final int DORIAN = 4;
    public static final int FULLMINOR = 5;
    public static final int HARMONICMAJOR = 6;
    public static final int HAWAIIAN = 7;
    public static final int IONIANSHARP = 9;
    public static final int JAZZMINOR = 10;
    public static final int LYDIAN = 11;
    public static final int MAJOR = 12;
    public static final int MIXOLYDIAN = 13;
    public static final int ORIENTAL = 14;
    public static final int SUPERLOCRIAN = 15;
    public static final int VERDIENIGMATICASCENDING = 16;
    public static final int ZIRAFKEND = 17;

    public Sequencer(int instrument, int tempo) {
        //Set up initial settings for the sequencer
        this.instrument = instrument;
        this.tempo = tempo;
        Synthesizer synth;
        ticks = 0;
        velocity = 64; //Mid volume

        try {
            //Setup values to create sequencer
            sequence = new Sequence(Sequence.PPQ, 16);
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            synth = MidiSystem.getSynthesizer();
            synth.open();
            sequencer.getTransmitter().setReceiver(synth.getReceiver());
            sequencer.setTempoInBPM(tempo);
            track = sequence.createTrack();

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void parseSequence(boolean[][] grid) {
        ShortMessage on;
        this.grid = grid;
        ShortMessage off;
        int tickLength = 16;
        try {
            ShortMessage sm = new ShortMessage();
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
            track.add(new MidiEvent(sm, 0));
            for (boolean[] aGrid : grid) {
                for (int col = 0; col < aGrid.length; col++) {
                    if (aGrid[col]) {
                        off = new ShortMessage();
                        off.setMessage(ShortMessage.NOTE_OFF, 0, scale[grid[0].length - col - 1], velocity);
                        on = new ShortMessage();
                        on.setMessage(ShortMessage.NOTE_ON, 0, scale[grid[0].length - col - 1], velocity);
                        track.add(new MidiEvent(on, ticks));
                        track.add(new MidiEvent(off, ticks + tickLength));
                    }

                }
                ticks += tickLength;


            }

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }


    }

    public void playSequence() {

        //Check if sequencer is stopped
        sequencer.addMetaEventListener(new MetaEventListener() {
            public void meta(MetaMessage m) {
                // A message of this type is automatically sent
                // when we reach the end of the track
                if (m.getType() == 47) {
                    sequencer.setTempoInBPM(tempo);
                    // start  the song at the last position
                    sequencerFrame.doa = sequencerFrame.conway.nextStep();
                    parseSequence(sequencerFrame.doa);
                    sequencerFrame.refresh();

                    sequencer.start();


                }

            }
        });
        // first start
        try {
            sequencer.setSequence(sequence);
            sequencer.start();

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void setInstrument(int instrument) {
        //Change instrument
        this.instrument = instrument;

    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void setScale(int scale) {
        //AAAAAAAHHHHHHHHHH
        if (scale == Sequencer.BIYU) {
            this.scale = new int[]{24, 27, 31, 34, 36, 39, 43, 46, 48, 51, 55, 58, 60, 63, 67, 70, 72, 75, 79, 82, 84, 87, 91, 94, 96, 99, 103, 106, 108, 111, 115, 118};
        } else if (scale == Sequencer.BLUES) {
            this.scale = new int[]{41, 42, 43, 46, 48, 51, 53, 54, 55, 58, 60, 63, 65, 66, 67, 70, 72, 75, 77, 78, 79, 82, 84, 87, 89, 90, 91, 94, 96, 99, 101, 102};
        } else if (scale == Sequencer.BLUESDIMINISHED) {
            this.scale = new int[]{48, 49, 51, 52, 54, 55, 56, 58, 60, 61, 63, 64, 66, 67, 68, 70, 72, 73, 75, 76, 78, 79, 80, 82, 84, 85, 87, 88, 90, 91, 92, 94};
        } else if (scale == Sequencer.DORIAN) {
            this.scale = new int[]{25, 27, 30, 32, 34, 37, 39, 42, 44, 46, 49, 51, 54, 56, 58, 61, 63, 66, 68, 70, 73, 75, 78, 80, 82, 85, 87, 90, 92, 94, 97, 99};
        } else if (scale == Sequencer.FULLMINOR) {
            this.scale = new int[]{51, 53, 55, 56, 57, 58, 59, 60, 62, 63, 65, 67, 68, 69, 70, 71, 72, 74, 75, 77, 79, 80, 81, 82, 83, 84, 86, 87, 89, 91, 92, 93};
        } else if (scale == Sequencer.HARMONICMAJOR) {
            this.scale = new int[]{44, 47, 48, 50, 52, 53, 55, 56, 59, 60, 62, 64, 65, 67, 68, 71, 72, 74, 76, 77, 79, 80, 83, 84, 86, 88, 89, 91, 92, 95, 96, 98};
        } else if (scale == Sequencer.HAWAIIAN) {
            this.scale = new int[]{39, 43, 45, 47, 48, 50, 51, 55, 57, 59, 60, 62, 63, 67, 69, 71, 72, 74, 75, 79, 81, 83, 84, 86, 87, 91, 93, 95, 96, 98, 99, 103};
        } else if (scale == Sequencer.IONIANSHARP) {
            this.scale = new int[]{45, 47, 48, 50, 52, 53, 56, 57, 59, 60, 62, 64, 65, 68, 69, 71, 72, 74, 76, 77, 80, 81, 83, 84, 86, 88, 89, 92, 93, 95, 96, 98};
        } else if (scale == Sequencer.JAZZMINOR) {
            this.scale = new int[]{45, 47, 48, 50, 51, 53, 55, 57, 59, 60, 62, 63, 65, 67, 69, 71, 72, 74, 75, 77, 79, 81, 83, 84, 86, 87, 89, 91, 93, 95, 96, 98};
        } else if (scale == Sequencer.LYDIAN) {
            this.scale = new int[]{43, 45, 48, 50, 51, 52, 54, 55, 57, 60, 62, 63, 64, 66, 67, 69, 72, 74, 75, 76, 78, 79, 81, 84, 86, 87, 88, 90, 91, 93, 96, 98};
        } else if (scale == Sequencer.MIXOLYDIAN) {
            this.scale = new int[]{45, 46, 48, 50, 52, 53, 55, 57, 58, 60, 62, 64, 65, 67, 69, 70, 72, 74, 76, 77, 79, 81, 82, 84, 86, 88, 89, 91, 93, 94, 96, 98};
        } else if (scale == Sequencer.ORIENTAL) {
            this.scale = new int[]{45, 46, 48, 49, 52, 53, 54, 57, 58, 60, 61, 64, 65, 66, 69, 70, 72, 73, 76, 77, 78, 81, 82, 84, 85, 88, 89, 90, 93, 94, 96, 97};
        } else if (scale == Sequencer.SUPERLOCRIAN) {
            this.scale = new int[]{44, 46, 48, 49, 51, 52, 54, 56, 58, 60, 61, 63, 64, 66, 68, 70, 72, 73, 75, 76, 78, 80, 82, 84, 85, 87, 88, 90, 92, 94, 96, 97};
        } else if (scale == Sequencer.VERDIENIGMATICASCENDING) {
            this.scale = new int[]{46, 47, 48, 49, 52, 54, 56, 58, 59, 60, 61, 64, 66, 68, 70, 71, 72, 73, 76, 78, 80, 82, 83, 84, 85, 88, 90, 92, 94, 95, 96, 97};
        } else if (scale == Sequencer.ZIRAFKEND) {
            this.scale = new int[]{48, 50, 51, 53, 55, 56, 57, 59, 60, 62, 63, 65, 67, 68, 69, 71, 72, 74, 75, 77, 79, 80, 81, 83, 84, 86, 87, 89, 91, 92, 93, 95};
        }
    }
}