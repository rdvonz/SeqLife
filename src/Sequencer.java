import javax.sound.midi.*;

public class Sequencer {
    private static int instrument;
    private Track track;
    boolean[][] grid;
    private static int ticks;
    private static int tempo;

    //private static int[] cmajscale = {72, 71, 69, 67, 65, 64, 62, 60};
    private int[] scale = {61, 63, 66, 68, 70, 75, 78, 68};

    private static javax.sound.midi.Sequencer sequencer;

    private static int velocity;

    private Sequence sequence;

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
                        off.setMessage(ShortMessage.NOTE_OFF, 0, scale[col], velocity);
                        on = new ShortMessage();
                        on.setMessage(ShortMessage.NOTE_ON, 0, scale[col], velocity);
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
}