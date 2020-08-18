import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Day{
   private Days day;
   private int[][] schedule;
   private Semester semester;
   
   public Day(Days day,Semester semester){
      schedule=new int[7][2];
      this.day=day;
      this.semester=semester;
      String file=semester.getName()+day.getName()+".txt";
      }
   
   public String readSchedule(String filename) throws FileNotFoundException,IOException{
      BufferedReader classes=new BufferedReader(new FileReader(filename));
      String line=classes.readLine();
      int numClass;
      try{
         numClass=Integer.parseInt(line);
      } catch (Exception e){
         numClass=0;
      }
      String slot="";
      for(int i=0; i<numClass; i++){
         line=classes.readLine();
         String[] split=line.trim().split(";");
         slot+=parseArray(split);
         if(i!=numClass-1){
            slot+="\n";
         }
      }
      
      return slot;
   }
   
   public String parseArray(String[] values){
      String name=values[0];
      String[] beginning=values[1].split(":");
      String[] end=values[2].split(":");
      int scaleBeginning=Integer.parseInt(beginning[0])*60+Integer.parseInt(beginning[1]);
      int scaleEnd=Integer.parseInt(end[0])*60+Integer.parseInt(end[1]);
      return name+","+scaleBeginning+","+scaleEnd;
   }
   
   
}