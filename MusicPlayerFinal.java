import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MusicPlayerFinal {
	static JFrame frame = new JFrame("My first music video");
	static MyDrawingPanel myDrawing;
	
	public static void main(String [] args) {
		MusicPlayerFinal mini = new MusicPlayerFinal();
		mini.go();		
	}
	
	public void setUpGui() {
		myDrawing = new MyDrawingPanel();
		frame.setContentPane(myDrawing);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(30, 30, 400, 400);
		frame.setVisible(true);
	}
	
	public void go() {
		setUpGui();
		
		try {
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			int[] eventsIWant = {127};
			// The event registration method, takes the listener AND an int array
			// representing the list of ControllerEvents we want. We listen to only one event, #127
			sequencer.addControllerEventListener(myDrawing, eventsIWant);  
			
			Sequence seq = new Sequence(Sequence.PPQ, 4); // The tempo-based timing type(PPQ), for which the resolution(4) is expressed in pulses (ticks) per quarter note.
			Track track = seq.createTrack(); // The MIDI data, lives in track
			
			int r = 0;
			for (int i = 0; i < 80; i+= 4) {
				
				r = (int) (Math.random() * 75);				
				track.add(makeEvent(144, 1, r, 100, i));  // #144 means NoteOn				
				track.add(makeEvent(176, 1, 127, 0, i)); // Here's how we pick the beat, we instert our on ControllerEvent
				// (176 says the event type is ControllerEvent), with the argument #127. This event will do nothing
				// I put it in just so we can get an event each time a note is played. (We can't listen otherwise for NoteOn/NoteOff)				
				track.add(makeEvent(128, 1, r, 100, i + 2)); // #128 means NoteOff
			}
			
			sequencer.setSequence(seq); // Give the Sequencer to the Sequencer (like puting the DVD in DVD Player) 
			sequencer.setTempoInBPM(180);
			sequencer.start();
			
		} catch (Exception ex) {
			System.out.println("Nasty things still happen, like just now");
			ex.printStackTrace();
		}		
	}	
	
	public MidiEvent makeEvent(int command, int channel, int note, int velocity, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(command, channel, note, velocity);
			event = new MidiEvent(a, tick);
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
		return event;
	}
	
	class MyDrawingPanel extends JPanel implements ControllerEventListener {
		boolean flag = false;  // The flag will be used to set it to true only when we get an event. We have to use it, because other thing might trigger a repaint()
		
		public void controlChange(ShortMessage event) {
			flag = true;
			repaint();
		}

		public void paintComponent(Graphics g) {
			if(flag) {
				
				int red = (int) (Math.random() * 256);				// This code is to generate a random colo and paint a semi-random rectangle
				int green = (int) (Math.random() * 256);
				int blue = (int) (Math.random() * 256);		
				Color randomColor = new Color(red, green, blue);
				g.setColor(randomColor);
			
				int height = (int) ((Math.random() * 120) + 10);
				int width = (int) ((Math.random() * 120) + 10);
			
				int x = (int) (Math.random() * 250);
				int y = (int) (Math.random() * 200);
			
				g.fillRect(x, y, height, width);
				flag = false;
			}
		}
	}
	
}