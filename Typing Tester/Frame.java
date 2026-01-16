import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.ArrayList;//import java.util.Timer; This is being imported here for timer ,which is a problem cuz we dont want this so instead of importing entire package we will import its class separately
import java.util.Random;
public class Frame extends JFrame implements ActionListener,KeyListener {
  String passage="";//Passage we get
  String typedPass="";//Passage the user types
  String message="";//Message to display at the end of the typing test
  int typed=0;//typed stores till which character the user the user has typed
  int count=0;
  int WPM;
  double start;//start time
  double end;//end time
  double elapsed;//end-start=elapsed time
  double seconds;//elapsed time in seconds
  boolean running;//If the person is typing
  boolean ended;//Whether the typin test has ended or not
  final int SCREEN_WIDTH;
  final int SCREEN_HEIGHT;
  final int DELAY=100;//milliseconds
  JButton button;
  Timer timer;
  JLabel label;
  public Frame(){
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//This is important ,this closes window when cross button is pressed
    SCREEN_WIDTH=720;
    SCREEN_HEIGHT=400;
    this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
    this.setVisible(true);//To make the frame visible
    this.setLocationRelativeTo(null);//Renders the frame at the center
    button = new JButton("Start");
    button.setFont(new Font("MV Boli",Font.BOLD,30));
    button.setForeground(Color.BLACK);
    button.setVisible(true);
    button.addActionListener(this);
    button.setFocusable(false);//This removes the line of focus from the button which is a box made of dashes

    label = new JLabel();
    label.setText("Typing Test");
    label.setFont(new Font("MV Boli",Font.BOLD,30));
    label.setVisible(true);
    label.setOpaque(true);
    label.setHorizontalAlignment(JLabel.CENTER);//Aligns the text in center
    label.setBackground(Color.yellow);
  
    this.add(button,BorderLayout.SOUTH);//adding button to frame at the south of it means at the bottom of the frame
    this.add(label,BorderLayout.NORTH);
    this.getContentPane().setBackground(new Color(173, 216, 230)); // Light Blue Color
    this.addKeyListener(this);
    this.setFocusable(true);//Important cuz if frame not focused then it won't catch the keys pressed
    this.setResizable(false);
    this.setTitle("Typing Test");
    this.revalidate();//this refreshes all components to load the frame again

  }
  @Override// It basically means that this function is active all the time and can perform what it has to do above other functions too
  public void paint(Graphics g){
    super.paint(g);
    draw(g);
  }
  public void draw(Graphics g){
    g.setFont(new Font("MV Boli",Font.BOLD,25));//Font for graphics which is goin to be our text
    if(running){
      //This will put the package on the screen
      if(passage.length()>1){// We dont want the program trying to display substrings when the passage is empty it will give error
        g.drawString(passage.substring(0, 50),g.getFont().getSize(),g.getFont().getSize()*5);//First argument is String ,Second is x-coordiante ,Third is y-coordinate
        g.drawString(passage.substring(50, 100),g.getFont().getSize(),g.getFont().getSize()*7);
        g.drawString(passage.substring(100, 150),g.getFont().getSize(),g.getFont().getSize()*9);
        g.drawString(passage.substring(150, 200),g.getFont().getSize(),g.getFont().getSize()*11);
      }
      g.setColor(Color.GREEN);
      if(typedPass.length()>0){
        if(typed<50)
          g.drawString(typedPass.substring(0,typed),g.getFont().getSize(),g.getFont().getSize()*5);//From the first letter to the currently typed one in green
        else
          g.drawString(typedPass.substring(0,50),g.getFont().getSize(),g.getFont().getSize()*5);//If the typed character exceeds 50 we can directly show the whole line in green  
      }
      if(typedPass.length()>50){
        if(typed<100)
          g.drawString(typedPass.substring(50,typed),g.getFont().getSize(),g.getFont().getSize()*7);
        else
          g.drawString(typedPass.substring(50,100),g.getFont().getSize(),g.getFont().getSize()*7);
      }
      if(typedPass.length()>100){
        if(typed<150)
          g.drawString(typedPass.substring(100,typed),g.getFont().getSize(),g.getFont().getSize()*9);
        else
          g.drawString(typedPass.substring(100,150),g.getFont().getSize(),g.getFont().getSize()*9);
      }
      if(typedPass.length()>150){
          g.drawString(typedPass.substring(150,typed),g.getFont().getSize(),g.getFont().getSize()*11);
      }
      running=false;//Once text made green make it false so it doesnt repaint
      //So when typed again running becomes true and it will substring from start with green color font
    }
    if(ended){
      if(WPM<=40)
        message="You are an Average Typist";
      else if(WPM>40&&WPM<=70)
        message="You are a Good Typist";
      else if(WPM>70 && WPM<=100)
        message="Impressive! You are Faster than Average";
      else
        message="You are a literal sweat";
      FontMetrics metrics=getFontMetrics(g.getFont());
      g.setColor(Color.BLUE);
      g.drawString("Typing Test Completed! SIUUUU",(SCREEN_WIDTH-metrics.stringWidth("Typing Test Completed! SIUUUU"))/2 ,g.getFont().getSize()*6);
      g.setColor(Color.BLACK);
      g.drawString("Typing Speed :"+WPM+"Words Per Minute",(SCREEN_WIDTH-metrics.stringWidth("Typing Speed :"+WPM+"Words Per Minute"))/2 ,g.getFont().getSize()*9);
      g.drawString(message,(SCREEN_WIDTH-metrics.stringWidth(message))/2 ,g.getFont().getSize()*11);
      timer.stop();
      ended=false;
    } 
  }

  @Override
  public void keyTyped(KeyEvent e) {//keyTyped uses the key character which can identify capital and lowercase difference in keyPressed(Note : it takes Unicode so it also considers shift which is problem)
    if(passage.length()>1){
      if(count==0)//Used to calculate time which is used for calculating WPM(Words Per Minute)
        start=LocalTime.now().toNanoOfDay();
      else if(count==200){//Timer ends when all 200 characters are typed
        end=LocalTime.now().toNanoOfDay();
        elapsed=end-start;
        seconds=elapsed/1000000000.0;//nanosecond/1000000000=second
        WPM=(int)(((200.0/5)/seconds)*60);//Formula for calculating WPM
        ended=true;
        running=false;
        count++;//So that it doesnt again stop time
      }
      char[] pass = passage.toCharArray();
      if(typed<200){
        running=true;
        if(e.getKeyChar()==pass[typed]){//Typed is an int maintaining the index of what is currently typed
          typedPass=typedPass+pass[typed];//To the typed passage we are adding what is currently typed
          typed++;
          count++;//count will be used for the time taken for typing the test
        }//if the typed character is not equal to the current position it will not add it to the typedpassage ,so the user needs to type the right text
      }

    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {  
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource()==button){// if the button is clicked
      passage=getPassage();
      timer=new Timer(DELAY,this);
      timer.start();
      running=true;
      ended=false;
      // When start button is pressed it resets all the values
      typedPass="";
      message="";
      typed=0;
      count=0;
    }
    if(running)
      repaint();//Repaint erases everything on screen and draws again at interval of timer which is important at animation effects
    if(ended)
      repaint();
  }
  public static String getPassage(){
    ArrayList<String> Passages=new ArrayList<String>();
    String pas1 = "The quick brown fox jumps over the lazy dog. This is a commonly used sentence in typing tests. It is designed to include every letter of the alphabet at least once. It helps users practice typing speeds and accuracy in a fun way.";
    String pas2 = "She sells seashells by the seashore every morning, and she has been doing so for many years. Her collection of shells is vast and varied. Some are smooth, some are jagged, and others have interesting patterns. She enjoys sharing her knowledge of the sea with others.";
    String pas3 = "A bird in the hand is worth two in the bush, as the saying goes. This piece of wisdom reminds us that what we have right now is often more valuable than the uncertain possibilities of the future. It's a lesson in appreciating the present moment and being cautious with our desires.";
    String pas4 = "To be or not to be, that is the question. This famous line from William Shakespeare's Hamlet has been analyzed and interpreted for centuries. It raises profound existential questions about life, death, and the human condition, making it one of the most iconic phrases in English literature.";

    Passages.add(pas1);
    Passages.add(pas2);
    Passages.add(pas3);
    Passages.add(pas4);
    Random rand= new Random();
    int place=(rand.nextInt(4));
    String toReturn =Passages.get(place).substring(0,200);//We are using 200 characters in the typing test
    if(toReturn.charAt(199)==32)//If last character is black space(Unicode=32)
    {
      toReturn=toReturn.strip();//Removing the blank spaces before and after the substring we have taken
      toReturn=toReturn+".";//Adding a full stop at the last instead of space
    }
    return(toReturn);//We have got our Passage!
  }
  
  public static void main(String[] args) {
    // Create an instance of the Frame class
    new Frame();
} 
}
