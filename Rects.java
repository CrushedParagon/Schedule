import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.Line2D;

//by far the most complex class, and also a big mess because I'm inexperienced in GUI programming


public class Rects extends JPanel{
   private Integer[][] points;//a list of points to draw the rectangles
   
   //a whole bunch of constants used for arithmatic
   private static  final int WINDOW_WIDTH=1280;
   private static final int WINDOW_HEIGHT=720;
   private static final int SIDE_BUFFER=100;
   private static final int TOP_BUFFER=50;
   private static final int FONT_HEIGHT=7;
   private static final int FONT_WIDTH=11;
   private static final int MIN_TIME=510;
   private static final int MAX_TIME=1320;
   
   //an array containing the enum values of Days
   private static final Days[] DAYS = new Days[]{Days.MONDAY,Days.TUESDAY,Days.WEDNESDAY,Days.THURSDAY,Days.FRIDAY,Days.SATURDAY,Days.SUNDAY};
   
   private String[] names;//the list of names generated in Schedule.java
   private int semester;//either 0 or 1 indicated Winter or Fall
   
   public Rects(int semester){
      this.semester=semester;
      String[] toParse= Schedule.getSchedule();//gets the schedule array from Schedule.java
      points=Schedule.parseForGUI(toParse);//gets the 2D array from Schedule's pareseForGUI method
      names=Schedule.getNames();//initializes the names array
   }
   public static void drawGUI(){//draws the GUI by taking in an int for semester
      Rects panel=new Rects(0);//calls the constructor for this class
      Rects panel2=new Rects(1);
      
      panel.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
      panel2.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
      
      
      JTabbedPane screen=new JTabbedPane();

      
      JFrame frame=new JFrame("Schedule");//creates the frame
      
      screen.addTab("Fall",panel);
      screen.addTab("Winter",panel2);
      
      frame.getContentPane().add(screen);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
      
      saveImage(panel,0);
      saveImage(panel2,1);
   }
   
   @Override
   //the complex method with all the evil math, should've probably split it up with methods 
   protected void paintComponent(Graphics g){
      super.paintComponent(g);
      
      Graphics2D g2=(Graphics2D)g;//casts Graphics into Graphics2D just so I can adjust line size
      
      g2.setStroke(new BasicStroke(2));
      
      g.setFont(new Font("Helvetica",Font.BOLD,20));//the default font. Changing font would also require changing the FONT_WIDTH and FONT_HEIGHT constants
      
      
      //this loop draws all the rectangles 
      for(int i=0; i<points.length; i++){
         if(points[i][0]==semester){//checks that class is in this semester
         Integer start=scaleInt(points[i][2])+1;//calls scaleInt to turn numbers into y coordinates
         Integer end=scaleInt(points[i][3])+1;
         g.setColor(Color.lightGray);//sets color to gray
         
         //x coordinate of rectangle is 2nd element of the ith array in points (the number from 0 to 6
         //multiplied by the width of the window minus the buffer on both sides
         //divided by 7, since we need 7 columns, then we add SIDE_BUFFER to shift it to the right
         int rectx=points[i][1]*(WINDOW_WIDTH-2*SIDE_BUFFER)/7+SIDE_BUFFER;
         
         //the width is the weidth of the window minus the buffers divided by 7
         int width=(WINDOW_WIDTH-2*SIDE_BUFFER)/7;
         
         //creates a rectange at rectx,start with width 'width' and height 'end-start'
         g.fillRect(rectx,start,width,end-start);
         
         //sets color to black
         g.setColor(Color.BLACK);
         
         //draws the name of the class
         g.drawString(names[i],rectx+(width/2)-FONT_WIDTH*names[i].length()/2,start+(end-start)/2+FONT_HEIGHT);
         //text is names[i] with x value being rectx with width/2 added on to put it in the middle
         //but since we want the text centered we subtract a value proportional to half the
         //text's length, by a factor of FONT_WIDTH
         
         //the y value is start + (end-start)/2 to put it in the middle, then offset by FONT_HEIGHT to 
         //make the centering more accurate
         }
      }
      
      //here we draw the vertical lines of the grid
      //we start at the side buffer
      int begin=SIDE_BUFFER;
      
      for(int i=0; i<8; i++){//we always loop 8 times because it'll always be 7 days
      
         //draw a line from begin,TOP_BUFFER to begin,WINDOW_HEIGHT-TOP_BUFFER
         //where begin moves right as the loop progresses
         g.drawLine(begin,TOP_BUFFER,begin,WINDOW_HEIGHT-TOP_BUFFER);
         
         if(i<7){//if i<7 it draws the day names above the grid
         
            //stringX is defined as starting at begin, offset by (WINDOW_WIDTH-2*SIDE_BUFFER)/14
            //which is the width of the column, divided by 2
            //this is then offset by FONT_WIDTH*(half of length of String)
            int stringX=begin+(WINDOW_WIDTH-2*SIDE_BUFFER)/14-FONT_WIDTH*DAYS[i].getName().length()/2;
            
            //the stringY will always be TOP_BUFFER/2+FONT_HEIGHT
            int stringY=TOP_BUFFER/2+FONT_HEIGHT;
            
            //draws the text using these coordinates
            g.drawString(DAYS[i].getName(),stringX,stringY);
         }
         
         //moves begin right by the width of a column
         begin+=(WINDOW_WIDTH-2*SIDE_BUFFER)/7;
         
      }
      
      //this is for creating the horizontal lines of the grid
      
      //starts at the TOP_BUFFER
      int beginy=TOP_BUFFER;
      
      //factor is the length of a single hour in terms of scaled units
      //this was achieved by taking the the formula for scaling a range to another range
      //(m-rmin)/(rmax-rmin)*(tmax-tmin)+tmin
      //and creating an equation for the difference of an observed value 60 mins higher than the other
      //x=(m+60-rmin)/(rmax-rmin)*(tmax-tmin)+tmin-[(m-rmin)/(rmax-rmin)*(tmax-tmin)+tmin]
      //if you simplify the right side you'll get x=60*(tmax-tmin)/(rmax-rmin)
      int factor=60*(WINDOW_HEIGHT-2*TOP_BUFFER)/(MAX_TIME-MIN_TIME);
      
      //num lines is the height of the window divided by how many times factor goes in
      int numLines=(WINDOW_HEIGHT-2*TOP_BUFFER)/factor;
      
      //baseTime is essentially the "hour" part of the text
      int baseTime=MIN_TIME/60;
      //by taking the modulo we get the minute part of the text
      int baseTimeRem=MIN_TIME%60;
      
      //this creates a String, if the remainder is less than 10 an extra 0 is inserted partways through
      String baseTimeString=""+baseTime+":";
      if(baseTimeRem<10) baseTimeString+="0";
      baseTimeString+=baseTimeRem;
      
      //the loop that draws the lines and the text
      for(int i=0; i<=numLines; i++){
         if(beginy<=WINDOW_HEIGHT-TOP_BUFFER){//checks to make sure we're not past the bottom 
         //since leaving it as a strict less than sometimes made the line not go far enough I made it go an extra one to err on caution
         
            //draws the line from SIDE_BUFFER,beginy to WINDOW_WIDTH-SIDE_BUFFER-2,beginy
            //where beginy moves down as the loop progresses, and I added the -2 to properly align the grid
            g.drawLine(SIDE_BUFFER,beginy,WINDOW_WIDTH-SIDE_BUFFER-2,beginy);
         
            //increases the hour by one (but does not update the String
            baseTime++;
            
            //determines the x value as halfway into the side buffer,
            //adjusted with FONT_WIDTH and half the String length
            int stringx=SIDE_BUFFER/2-FONT_WIDTH*baseTimeString.length()/2;
            
            //determines the y value with beginy adjusted by FONT_HEIGHT
            int stringy=beginy+FONT_HEIGHT;
            
            //draws String using these coordinates
            g.drawString(baseTimeString,stringx,stringy);
            
            //updates the String using the new value of baseTime
            baseTimeString=""+baseTime+":";
            if(baseTimeRem<10) baseTimeString+="0";
            baseTimeString+=baseTimeRem;
            
            
            //adjusts the y value by the factor (one hour in scaled units) +1 for leniency
            beginy+=factor+1;
         }
      }
      
      //draws one extra line at the very bottom so the grid is closed
      g.drawLine(SIDE_BUFFER,WINDOW_HEIGHT-TOP_BUFFER,WINDOW_WIDTH-SIDE_BUFFER-2,WINDOW_HEIGHT-TOP_BUFFER);
      
      
   }
   
   //a function that scales an Integer from MIN_TIME to MAX_TIME into the range from
   //TOP_BUFFER to WINDOW_HEIGHT-TOP_BUFFER
   public static Integer scaleInt(Integer a){
      Integer scaled=(a-MIN_TIME)*(WINDOW_HEIGHT-2*TOP_BUFFER)/(MAX_TIME-MIN_TIME)+TOP_BUFFER;
      System.out.println(""+scaled);
  
      
      return scaled;
      
   }
   
   //the method that saves the schedules as png files
   public static void saveImage(JComponent panel, int semester){
      String term;//the semester the schedule is for
      //assigned using an integer that's passed through from the GUI
      //along with the panel
      if(semester==0){
         term="Fall";
      }  
      else term="Winter"; 
      
      //gets the size of the panel
      Dimension size = panel.getSize(); 
      BufferedImage image = new BufferedImage(
                    size.width, size.height 
                              , BufferedImage.TYPE_INT_RGB); //creates a BufferedImage with the size data
        Graphics2D g2 = image.createGraphics();//creates a Graphics2D object with the BufferedImage
        panel.paint(g2);//paints onto the 2D graphic with the panel contents
        try //tries to writes the image as fallSchedule.png or winterSchedule.png based on the semester
        {
            String file=""+term+"Schedule.png";
            ImageIO.write(image, "png", new File(file));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
   }
   
   
}