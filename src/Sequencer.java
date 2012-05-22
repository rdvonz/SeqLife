import javax.sound.midi.*;
public class Sequencer {
	int[][] grid;
	
	Synthesizer synth;
	Receiver recvr;
	
	
	public Sequencer(int numRows, int numColumns){
		try{
			synth = MidiSystem.getSynthesizer();
			synth.open();
			recvr = synth.getReceiver();
		}
		catch(MidiUnavailableException e){
			System.out.print("Midi ain't workin");
		}
		grid = new int[numRows][numColumns];
		
		MidiMessage noteOn = getNoteOnMessage(62);
		MidiMessage noteOff = getNoteOffMessage(62);
		recvr.send(noteOn,0);
		try{
			Thread.sleep(2000);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		recvr.send(noteOff, 0);
		
		
	}
	private MidiMessage getMessage(int cmd, int note){
		try{
			ShortMessage msg = new ShortMessage();
			int channel = 0;
			int velocity = 60;
			msg.setMessage(cmd, channel, note, velocity);
			return (MidiMessage) msg;
		}
		catch(InvalidMidiDataException e){
			e.printStackTrace();
			
		}
		return null;
	}
	private MidiMessage getNoteOnMessage(int note){
		return getMessage(ShortMessage.NOTE_ON, note);
	}
	
	private MidiMessage getNoteOffMessage(int note){
		return getMessage(ShortMessage.NOTE_OFF, note);
	}
	

}
