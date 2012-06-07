import javax.sound.midi.*;

public class Sequencer {
    private static int[] scale;
    private static int octave;
    private static int instrument;
    private static Track track;
    private static int curTick;
    private static int newTick;
    static boolean[][] grid;
    private static int ticks;

    private static javax.sound.midi.Sequencer sequencer;

    private static int noteLength;
    private static int velocity;

    private static Sequence sequence;

    public static void initialize(int[] scale, int instrument, int tempo, boolean sustain) {
        //Set up initial settings for the sequencer
        Sequencer.instrument = instrument;
        Sequencer.scale = scale;
        boolean sustain1 = sustain;
        Synthesizer synth;
        curTick = 0;
        newTick = 0;
        ticks = 0;
        noteLength = 16; //Quarter notes
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

    public static void parseSequence(boolean[][] grid) {
        ShortMessage on;
        Sequencer.grid = grid;
        ShortMessage off;
        int tickLength = 16;
        //Change instrument
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
            track.add(new MidiEvent(sm, 0));

            //Parse grid for notes
            for (boolean[] aGrid : Sequencer.grid) {
                for (int col = 0; col < aGrid.length; col++) {
                    if (aGrid[col]) {
                        //playing += scale[row]+" ";
                        off = new ShortMessage();
                        off.setMessage(ShortMessage.NOTE_OFF, instrument, scale[col], velocity);
                        on = new ShortMessage();
                        on.setMessage(ShortMessage.NOTE_ON, instrument, scale[col], velocity);
                        track.add(new MidiEvent(on, ticks));
                        track.add(new MidiEvent(off, ticks + tickLength));
                    }

                }
                ticks += tickLength;


            }

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        curTick = ticks - 128;


    }

    public static void playSequence() {
        //Check if sequencer is stopped

        sequencer.addMetaEventListener(new MetaEventListener() {
            public void meta(MetaMessage m) {
                // A message of this type is automatically sent
                // when we reach the end of the track
                if (m.getType() == 47) {
                    // Restart the song
                    sequencer.setTickPosition(curTick);
                    sequencerFrame.refresh();
                    sequencer.start();


                }
            }
        });
        // And start playing now.
        try {
            sequencer.setSequence(sequence);
            sequencer.start();

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public static int[] getScale() {
        return scale;
    }

    public static void setScale(int[] scale) {
        Sequencer.scale = scale;
    }

    public static int getOctave() {
        return octave;
    }

    public static int getInstrument() {
        return instrument;
    }

    public static void setInstrument(int instrument) {
        Sequencer.instrument = instrument;
    }

    public static int getVelocity() {
        return velocity;
    }

    public static void setVelocity(int velocity) {
        Sequencer.velocity = velocity;
    }
}