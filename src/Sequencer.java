import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

public class Sequencer{
    private static int[] scale;
    private int octave;
    private int instrument;
    private static Track track;
    private static javax.sound.midi.Sequencer sequencer;

    private int noteLength;
    private static int velocity;

    private Sequence sequence;


    public Sequencer(int[] scale, int instrument, int tempo, boolean sustain){
        //Set up initial settings for the sequencer
        this.instrument = instrument;
        Sequencer.scale = scale;
        boolean sustain1 = sustain;
        Synthesizer synth;
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
    private static void parseSequence(boolean[][] grid) throws InvalidMidiDataException{
        //Iterate over grid in order to parse into midi track
        ShortMessage on;
        ShortMessage off;

        int ticks=0;
        int tickLength=16;
       // String playing = "Should be playing: ";
        for (boolean[] aGrid : grid) {
            for (int col = 0; col < aGrid.length; col++) {
                if (aGrid[col]) {
                    //playing += scale[row]+" ";
                    off = new ShortMessage();
                    off.setMessage(ShortMessage.NOTE_OFF, 0, scale[col], velocity);
                    on = new ShortMessage();
                    on.setMessage(ShortMessage.NOTE_ON, 0, scale[col], velocity);
                    track.add(new MidiEvent(on, ticks));
                    track.add(new MidiEvent(off, ticks + tickLength));
                } else {
                    //on.setMessage(ShortMessage.NOTE_ON,  0, 0, velocity);
                    // off.setMessage(ShortMessage.NOTE_OFF, 0, 0, velocity);
                    //track.add(new MidiEvent(on, ticks));
                    //track.add(new MidiEvent(off, ticks+tickLength));
                }
            }
            // playing+="\nthen: ";
            ticks += tickLength;
        }


    }
    public void createTrack(boolean[][] grid, int instrument){
        //Change instrument
        sequence.deleteTrack(track);
        track = sequence.createTrack();
        ShortMessage sm = new ShortMessage( );
        try{
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
        track.add(new MidiEvent(sm, 0));
         //Parse the grid
         //Add notes to sequencer
         parseSequence(grid);
        } catch(InvalidMidiDataException e){
            e.printStackTrace();
        }
    }
    public void playSequence(){
        //Check if sequencer is stopped
        sequencer.addMetaEventListener(new MetaEventListener( ) {
            public void meta(MetaMessage m) {
                // A message of this type is automatically sent
                // when we reach the end of the track
                if (m.getType( ) == 47) sequencer.stop();
            }
        });
        // And start playing now.
        try{
        sequencer.setSequence(sequence);
        sequencer.setTickPosition(0);
        sequencer.start();
        } catch(InvalidMidiDataException e){
            e.printStackTrace();
        }
    }
}