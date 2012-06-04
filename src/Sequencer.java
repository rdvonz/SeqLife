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
    static int[] scale;
    int octave;
    int instrument;
    boolean sustain;
    static Track track;
    static javax.sound.midi.Sequencer sequencer;

    int noteLength;
    static int velocity;

    Sequence sequence;


    public Sequencer(boolean[][] grid, int octave, int[] scale, int instrument, int tempo, boolean sustain){
        //Set up initial settings for the sequencer
        this.octave = octave;
        this.instrument = instrument;
        Sequencer.scale = scale;
        this.sustain = sustain;
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
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(tempo);
            createTrack(grid, instrument);

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
    private static void parseSequence(boolean[][] grid) throws InvalidMidiDataException{
        //Iterate over grid in order to parse into midi track
        ShortMessage on = new ShortMessage( );
        ShortMessage off = new ShortMessage( );

        int ticks=0;
        int tickLength=16;
        String playing = "Should be playing: ";
        for(int row = 0; row<grid.length; row++){
            for(int col = 0; col<grid[row].length; col++){
                if(grid[row][col]){
                    playing += scale[row]+" ";
                    on.setMessage(ShortMessage.NOTE_ON,  0, scale[row], velocity);
                    off.setMessage(ShortMessage.NOTE_OFF, 0, scale[row], velocity);
                    track.add(new MidiEvent(on, ticks));
                    track.add(new MidiEvent(off, ticks+tickLength));
                    ticks++;
                } else{
                   //on.setMessage(ShortMessage.NOTE_ON,  0, 0, velocity);
                   // off.setMessage(ShortMessage.NOTE_OFF, 0, 0, velocity);
                    //track.add(new MidiEvent(on, ticks));
                    //track.add(new MidiEvent(off, ticks+tickLength));
                }
            }
            playing+="\nthen: ";
            ticks+=tickLength;
        }
        System.out.print(playing);


    }
    public void createTrack(boolean[][] grid, int instrument){
        //Create a new track
        track = sequence.createTrack();

        //Change instrument
        ShortMessage sm = new ShortMessage( );
        try{
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
        track.add(new MidiEvent(sm, 0));
         //Parse the grid
         //Add notes to sequencer
         parseSequence(grid);
         sequencer.setSequence(sequence);
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
                // if (m.getType( ) == 47) System.exit(0);
            }
        });
        // And start playing now.
        sequencer.start();
    }
}