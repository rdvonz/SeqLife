/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 6/6/12
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Series implements Runnable {
    boolean[][] grid;

    public Series(int[] scale, int instrument, int tempo, boolean sustain) {
        Sequencer.playSequence();
    }

    public void run() {
        Sequencer.playSequence();


    }
}
