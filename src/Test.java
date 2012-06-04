import javax.sound.midi.MidiUnavailableException;

public class Test {

    public static void main(String[] args) throws MidiUnavailableException{
        boolean[][] grid = {{false, false, false, false, true},
                            {false, false, false, true, false},
                            {false, false, true, false, false},
                            {false, true, false, false, false},
                            {true, false, false, false, false}};
        boolean [][] grid3 = {{true, true, true, true, true},
                             {true, true, true, true, true},
                             {true, true, true, true, true},
                             {true, true, true, true, true},
                             {true, true, true, true, true}};

        boolean[][] grid2 = {{true, false, true, false, true},
                            {true, false, true, true, false},
                            {true, false, true, false, false},
                            {true, true, true, false, false},
                             {true, false, false, false, false}};

        int[] scale = {48, 50, 52, 53, 55, 57, 59};
        Sequencer seq = new Sequencer(grid, 5, scale, 5, 128, true);
        seq.playSequence();
    }



}