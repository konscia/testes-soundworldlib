import isdraw.*;
import tunes.*;


public class VeryBasicExample extends World {
    MusicBox mbox = new MusicBox();
    int ticks = 0;

  /**
   * On each tick add to the <code>tickTunes TuneBucket</code>
   * the current note to be played, on the currently chosen instrument.
   */
    public void onTick(){}

    @Override
    public void onKeyEvent(String s) {
        if(s.equals("c")){
            Tune tom = new Tune(PIANO, new Note(noteC));
            mbox.playTune(tom);           
        } else if(s.equals("d")){
            Tune tom = new Tune(PIANO, new Note(noteD));
            mbox.playTune(tom);
        } else if(s.equals("a")){
            Tune tom = new Tune(PIANO, new Note(noteA));
            mbox.playTune(tom);
        } else if(s.equals("f")){
            Tune tom = new Tune(PIANO, new Note(noteF));
            mbox.playTune(tom);
        } else if(s.equals("e")){
            Tune tom = new Tune(PIANO, new Note(noteE));
            mbox.playTune(tom);
        }   
    }
    @Override
    public void draw() {  }
}

/** Examples and tests for the SoundLibDemoWorld */
class DemoVeryBasicExample implements SoundConstants{

  /** Run the world as an application:
   *  first delete the SoundLibDemoApplet class */
  public static void main(String[] argv){
    // construct an instance of a SoundLibDemoWorld
    VeryBasicExample w = new VeryBasicExample();
    // and run the SoundLibDemoWorld
    w.bigBang(300, 200, 0.2);
  }
}