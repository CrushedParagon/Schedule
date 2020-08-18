import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Day{
   private Days day;//stores an enum called Days, confusing name structure because I'm dumb
   private int[][] schedule;
   private Semester semester;//stores a Semester enum
   
   public Day(Days day,Semester semester){//constructor
      schedule=new int[7][2];//will always be 7 by 2 array since monday-Sunday and start-end times
      this.day=day;
      this.semester=semester;
      }
   
   //reads in the files
   public String readSchedule(String filename) throws FileNotFoundException,IOException{
      BufferedReader classes=new BufferedReader(new FileReader(filename));//creates a BufferedReader with the filename
      String line=classes.readLine();//reads in the first line
      int numClass;
      try{//tries to parse an int on line 1, if there is none line count is set to 0
         numClass=Integer.parseInt(line);
      } catch (Exception e){
         numClass=0;
      }
      
      String slot="";//the string that will be returned
      for(int i=0; i<numClass; i++){
         line=classes.readLine();
         String[] split=line.trim().split(";");//splits by semicolon
         slot+=parseArray(split);//adds the result from parseArray to slot
         if(i!=numClass-1){
            slot+="\n";//adds a linebreak unless it's the end of the loop
         }
      }
      
      return slot;
   }
   
   public String parseArray(String[] values){//parseArray returns the form name,scaledTime1,scaledTime2
      String name=values[0];//gets name from first index of array
      String[] beginning=values[1].split(":");
      String[] end=values[2].split(":");
      int scaleBeginning=Integer.parseInt(beginning[0])*60+Integer.parseInt(beginning[1]);//converts time into a number from 0 to 1440
      int scaleEnd=Integer.parseInt(end[0])*60+Integer.parseInt(end[1]);
      return name+","+scaleBeginning+","+scaleEnd;
   }
   
   
}