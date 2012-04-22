import isdraw.*;
import geometry.*;
import colors.*;
import tunes.*;

import java.util.*;
import tester.*;

/**
 * Copyright 2010 Viera K. Proulx, Matthias Felleisen
 * This program is distributed under the terms of the 
 * GNU Lesser General Public License (LGPL)
 */

//------------------------------------------------------------------------------
/**
 * <p>Represent the world that plays simple scales.</p>
 * <p>Up and down arrows define the direction.</p>
 * <p>Space bar plays a C major chord on organ instrument.</p>
 * <p>Mouse click in the white region switches the instrument
 * between piano and tuba.</p>
 * 
 * @author Viera K. Proulx
 * @since 25 October 2010
 */
public class SoundLibDemoWorld extends World {
  
  /** The greeting*/
  public String hello;
  
  /**
   * Initialize the greeting and initialize the list of scale notes
   * 
   * @param hello the first line of instructions
   */
  SoundLibDemoWorld(String hello){
    this.hello = hello;
    initScales();
  }
  
  /** are we playing piano or tuba? */
  public boolean pianoChoice = true;
  
  /** the direction for the scale */
  public int updownChoice = 1;
  
  /** index of the currently playing note in the scale */
  public int currentNote = 7;
  
  /** the notes in the C-major scale */
  public ArrayList<Note> upScale = new ArrayList<Note>();
  
  /**
   * Initialize the C-major scale
   */
  public void initScales(){
    this.upScale.add(new Note(noteC));
    this.upScale.add(new Note(noteD));
    this.upScale.add(new Note(noteE));
    this.upScale.add(new Note(noteF));
    this.upScale.add(new Note(noteG));
    this.upScale.add(new Note(noteA));
    this.upScale.add(new Note(noteB));
    this.upScale.add(new Note(noteUpC));
  }
  
  /**
   * Produce the next note to be played in the current direction
   * 
   * @return the note to be played on the next tick
   */
  public Note nextNote(){
    this.currentNote = (this.currentNote + this.updownChoice + 8) % 8;
    return this.upScale.get(this.currentNote);
  }
  
  /**
   * Get the instruments we are now playing
   * 
   * @return the instrument we are to play 
   * (SoundConstants name for the current program)
   */
  public int getInstrument(){
    if (this.pianoChoice)
      return PIANO;
    else
      return TUBA;
  }
  
  /**
   * On each tick add to the <code>tickTunes TuneBucket</code>
   * the current note to be played, on the currently chosen instrument.
   */
  public void onTick(){
    this.tickTunes.addNote(this.getInstrument(), this.nextNote());
  }
  
  /**
   * <p>Use up and down keys to select the direction for the scale.</p>
   * <p>Press and hold the space bar to play the C-major chord on organ.</p>
   */
  public void onKeyEvent(String ke){
    if (ke.equals("up"))
      this.updownChoice = +1;
    if (ke.equals("down"))
      this.updownChoice = -1;
    if (ke.equals(" "))
      // build the chord from the pitch values only
      this.keyTunes.addChord(ORGAN, new Chord(60, 64, 67));
  }
  
  /**
   * Mouse click in the upper 100 pixels changes the instrument selection
   */
  public void onMouseClicked(Posn pos){
    if (pos.y < 100)
      this.pianoChoice = !this.pianoChoice;
  }
  
  /**
   * Draw the title line and the three instruction lines 
   * in the white upper half of the <code>Canvas</code>.
   * Show the note that is played in the lower half.
   */
  public void draw(){
    // upper half
    this.theCanvas.drawRect(new Posn(0, 0), 300, 100, new White());
    this.theCanvas.drawString(new Posn(10, 35), 
        this.hello, new Red());
    this.theCanvas.drawString(new Posn(10, 55), 
        "Click in white to change the instrument", new Blue());
    this.theCanvas.drawString(new Posn(10, 70), 
        "Use arrow up-down to reverse the scale", new Blue());
    this.theCanvas.drawString(new Posn(10, 85), 
        "Hold space bar to play a chord", new Blue());
    
    // lower half
    this.theCanvas.drawRect(new Posn(0, 100), 300, 100, new Blue());
    this.theCanvas.drawString(new Posn(20, 140), "Playing the note: " + 
                   this.upScale.get(this.currentNote).snote, new Red());
    this.theCanvas.drawDisk(new Posn(20 + this.currentNote * 20, 160), 5, 
                   new Black());
  }
}

/** Examples and tests for the SoundLibDemoWorld */
class ExamplesSoundLibDemo implements SoundConstants{
  ExamplesSoundLibDemo(){}
  
  SoundLibDemoWorld w = new SoundLibDemoWorld("Play the scales");
  
  /**
   * Test the method initScales in the class SoundLibDemoWorld
   * @param t the instance of Tester that runs these tests
   */
  void testInitScales(Tester t){
    t.checkExpect(this.w.upScale.size(), 8);
    this.w.initScales();
    t.checkExpect(this.w.upScale.size(), 16);
    t.checkExpect(this.w.upScale.get(5), this.w.upScale.get(13));
    
    // tear down
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    this.w.upScale.remove(0);
    
    // check that it was done correctly
    t.checkExpect(this.w.upScale.size(), 8);
    t.checkExpect(this.w.upScale.get(4), new Note("G4n1"));   
    t.checkExpect(this.w.upScale.get(5), new Note("A5n1"));    
  }
  

  /**
   * Test the method nextNote in the class SoundLibDemoWorld
   * 
   * @param t the instance of Tester that runs these tests
   */
  void testNextNote(Tester t){
    t.checkExpect(this.w.nextNote(), new Note("C4n1"));
    t.checkExpect(this.w.nextNote(), new Note("D4n1"));
    
    this.w.updownChoice = -1;
    t.checkExpect(this.w.nextNote(), new Note("C4n1"));
    t.checkExpect(this.w.nextNote(), new Note("C5n1"));
    
    this.w.updownChoice = +1;
    t.checkExpect(this.w.nextNote(), new Note("C4n1"));
    
    // tear down
    this.w.currentNote = 7;
  }
  
  /**
   * Test the method getInstrument in the class SoundLibDemoWorld
   * 
   * @param t the instance of Tester that runs these tests
   */
  void testGetInstrument(Tester t){
    t.checkExpect(this.w.getInstrument(), PIANO);
    
    this.w.pianoChoice = false;
    t.checkExpect(this.w.getInstrument(), TUBA);

    this.w.pianoChoice = true;
    t.checkExpect(this.w.getInstrument(), PIANO);
  }
  

  /**
   * Test the method onTick in the class SoundLibDemoWorld
   * 
   * @param t the instance of Tester that runs these tests
   */
  void testOnTick(Tester t){
    this.w.onTick();
    t.checkExpect(this.w.tickTunes.bucketSize(), 1);
    t.checkExpect(this.w.tickTunes.contains(PIANO, new Note("C4n1")), true);
    
    // because tick events are not processed, tickTunes bucket is not emptied
    this.w.pianoChoice = false;
    this.w.onTick();
    t.checkExpect(this.w.tickTunes.bucketSize(), 2);
    t.checkExpect(this.w.tickTunes.contains(TUBA, new Note("D4n1")), true);
    
    // tear down
    this.w.pianoChoice = true;
    this.w.tickTunes.clearBucket();
  }
  
  /**
   * Test the method onKeyEvent in the class SoundLibDemoWorld
   * 
   * @param t the instance of Tester that runs these tests
   */
  void testOnKeyEvent(Tester t){
    this.w.onKeyEvent("up");
    t.checkExpect(this.w.updownChoice, 1);

    this.w.onKeyEvent("down");
    t.checkExpect(this.w.updownChoice, -1);

    this.w.onKeyEvent("down");
    t.checkExpect(this.w.updownChoice, -1);
    
    this.w.onKeyEvent("up");
    t.checkExpect(this.w.updownChoice, 1);
    
    this.w.onKeyEvent(" ");
    t.checkExpect(this.w.keyTunes.contains(ORGAN, 60), true);
    t.checkExpect(this.w.keyTunes.contains(ORGAN, 64), true);
    t.checkExpect(this.w.keyTunes.contains(ORGAN, 67), true);
    
    // tear down
    this.w.keyTunes.clearBucket();
  }
  
  /**
   * Test the method onMouseClicked in the class SoundLibDemoWorld
   * 
   * @param t the instance of Tester that runs these tests
   */
  void testOnMouseClicked(Tester t){
    // click outside - no change
    this.w.onMouseClicked(new Posn(100, 120));
    t.checkExpect(this.w.pianoChoice, true);

    // click inside - change
    this.w.onMouseClicked(new Posn(100, 80));
    t.checkExpect(this.w.pianoChoice, false);

    // click outside - no change
    this.w.onMouseClicked(new Posn(100, 120));
    t.checkExpect(this.w.pianoChoice, false);

    // click inside - change
    this.w.onMouseClicked(new Posn(100, 20));
    t.checkExpect(this.w.pianoChoice, true);
  }
  
  /** Run the world as an application:
   *  first delete the SoundLibDemoApplet class */
  public static void main(String[] argv){
    // construct an instance of a SoundLibDemoWorld
    SoundLibDemoWorld w = new SoundLibDemoWorld("Play the scales");
    // and run the SoundLibDemoWorld
    w.bigBang(300, 200, 0.2);
  }  
}