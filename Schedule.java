import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Schedule{
   private static String[] schedule;
   private static String[] classNames;//stores the name of every class
   public static void main(String[] args){
      schedule=new String[14];//will always be 14 (Fall+Winter Monday-Friday)
      for(Semester term:Semester.values()){ //nested for loops that read in all 14 txt files
         for(Days d:Days.values()){
            Day day=new Day(d,term);//creates a Day with a d Days (I hate this naming too) and term
            String filename="./img/"+term.getName()+d.getName()+".txt";
            String toParse="";
            try{//tries to read in the files
            toParse=day.readSchedule(filename);
            } catch (FileNotFoundException e){
               System.out.println("oof");//if file is missing program can contine
            }catch (Exception e){
               System.out.println("Big oof");//if something else goes wrong I've fucked up somewhere
            }
            schedule[d.getIndex()+term.getFactor()]=toParse;//puts the Strings into the array, adds 7 if it's winter
            
         }
      }
      Rects.drawGUI();//draws the GUI in Rects twice, once for Fall, one for Winter
   }
   
   //returns a 2D array for use by the GUI
   public static Integer[][] parseForGUI(String[] toParse){
      ArrayList<Integer[]> toReturn= new ArrayList<Integer[]>();//ArrayList of Integer arrays since I didn't define a limit to number of classes
      ArrayList<String> names=new ArrayList<String>();//Same thing for class names
      for(int i=0; i<14; i++){
         if(!toParse[i].equals("")){//checks that String isn't empty
         String[] line=toParse[i].split("\\n");//splits by new lines
         
         
         for(int j=0; j<line.length; j++){
            String[] teach=line[j].split(",");//splits again by comma
            names.add(teach[0]);//name is in index 0
            Integer[] add={i/7,i%7,Integer.parseInt(teach[1]),Integer.parseInt(teach[2])};//adds an array containing
           // {Either 0 or 1(indicates semester),Number from 0 to 6 (Indicates Day),begin time,end time}
            toReturn.add(add);
         }}
      }
      classNames=names.toArray(new String[names.size()]);//converts the String ArrayList to a String array
      return toReturn.toArray(new Integer[toReturn.size()][]);//converts the Integer[] Arraylist to a 2D Integer array
   }
   
   public static String[] getSchedule(){//two basic accessor methods
      return schedule;
   }
   
   public static String[] getNames(){
      return classNames;
   }
}