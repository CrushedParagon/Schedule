import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.Line2D;

public class Rects extends JPanel{
   private Integer[][] points;
   
   private static  final int WINDOW_WIDTH=1280;
   private static final int WINDOW_HEIGHT=720;
   private static final int SIDE_BUFFER=100;
   private static final int TOP_BUFFER=50;
   private static final int FONT_HEIGHT=7;
   private static final int FONT_WIDTH=11;
   private static final int MIN_TIME=510;
   private static final int MAX_TIME=1320;
   
   private static final Days[] DAYS = new Days[]{Days.MONDAY,Days.TUESDAY,Days.WEDNESDAY,Days.THURSDAY,Days.FRIDAY,Days.SATURDAY,Days.SUNDAY};
   
   private String[] names;
   private int semester;
   
   public Rects(int semester){
      this.semester=semester;
      String[] toParse= Schedule.getSchedule();
      points=Schedule.parseForGUI(toParse);
      System.out.println("Created");
      names=Schedule.getNames();
   }
   public static void drawGUI(int semester){
      Rects panel=new Rects(semester);
      panel.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
      
      JFrame frame=new JFrame("Schedule");
      
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(panel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
      saveImage(panel,semester);
   }
   
   @Override
   protected void paintComponent(Graphics g){
      super.paintComponent(g);
      
      Graphics2D g2=(Graphics2D)g;
      
      g2.setStroke(new BasicStroke(2));
      
      g.setFont(new Font("Helvetica",Font.BOLD,20));
      
      for(int i=0; i<points.length; i++){
         if(points[i][0]==semester){
         Integer start=scaleInt(points[i][2])+1;
         Integer end=scaleInt(points[i][3])+1;
         g.setColor(Color.WHITE);
         int rectx=points[i][1]*(WINDOW_WIDTH-2*SIDE_BUFFER)/7+SIDE_BUFFER;
         int width=(WINDOW_WIDTH-2*SIDE_BUFFER)/7;

         g.fillRect(rectx,start,width,end-start);
         g.setColor(Color.BLACK);
         g.drawString(names[i],rectx+(width/2)-FONT_WIDTH*names[i].length()/2,start+(end-start)/2+FONT_HEIGHT);
         }
      }
      
      int begin=SIDE_BUFFER;
      
      for(int i=0; i<8; i++){
         g.drawLine(begin,TOP_BUFFER,begin,WINDOW_HEIGHT-TOP_BUFFER);
         
         if(i<7){
            int stringX=begin+(WINDOW_WIDTH-2*SIDE_BUFFER)/14-FONT_WIDTH*DAYS[i].getName().length()/2;
            int stringY=TOP_BUFFER/2+FONT_HEIGHT;
            g.drawString(DAYS[i].getName(),stringX,stringY);
         }
         
         begin+=(WINDOW_WIDTH-2*SIDE_BUFFER)/7;
         
      }
      
      int beginy=TOP_BUFFER;
      
      int factor=60*(WINDOW_HEIGHT-2*TOP_BUFFER)/(MAX_TIME-MIN_TIME);
      int numLines=(WINDOW_HEIGHT-2*TOP_BUFFER)/factor;
      
      int baseTime=MIN_TIME/60;
      int baseTimeRem=MIN_TIME%60;
      
      String baseTimeString=""+baseTime+":";
      if(baseTimeRem<10) baseTimeString+="0";
      baseTimeString+=baseTimeRem;
      
      for(int i=0; i<=numLines; i++){
         if(beginy<=WINDOW_HEIGHT-TOP_BUFFER){
            g.drawLine(SIDE_BUFFER,beginy,WINDOW_WIDTH-SIDE_BUFFER-2,beginy);
         
         
            baseTime++;
            int stringx=SIDE_BUFFER/2-FONT_WIDTH*baseTimeString.length()/2;
            int stringy=beginy+FONT_HEIGHT;
         
            g.drawString(baseTimeString,stringx,stringy);
         
            baseTimeString=""+baseTime+":";
            if(baseTimeRem<10) baseTimeString+="0";
            baseTimeString+=baseTimeRem;
         
            beginy+=factor+1;
         }
      }
      g.drawLine(SIDE_BUFFER,WINDOW_HEIGHT-TOP_BUFFER,WINDOW_WIDTH-SIDE_BUFFER-2,WINDOW_HEIGHT-TOP_BUFFER);
      
      
   }
   
   
   public static Integer scaleInt(Integer a){
      Integer scaled=(a-MIN_TIME)*(WINDOW_HEIGHT-2*TOP_BUFFER)/(MAX_TIME-MIN_TIME)+TOP_BUFFER;
      System.out.println(""+scaled);
  
      
      return scaled;
      
   }
   
   public static void saveImage(JComponent panel, int semester){
      String term;
      if(semester==0){
         term="Fall";
      }  
      else term="Winter"; 
      
      Dimension size = panel.getSize(); //gets the size of the panel
      BufferedImage image = new BufferedImage(
                    size.width, size.height 
                              , BufferedImage.TYPE_INT_RGB); //creates a BufferedImage with the size data
        Graphics2D g2 = image.createGraphics();
        panel.paint(g2);//paints onto the 2D graphic with the panel contents
        try
        {
            String file=""+term+"Schedule.png";
            ImageIO.write(image, "png", new File(file));//writes the image as bingo(x).png
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
   }
   
   
}