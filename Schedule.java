import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Schedule{
   private static String[] schedule;
   private static String[] classNames;
   public static void main(String[] args){
      schedule=new String[14];
      for(Semester term:Semester.values()){ 
         for(Days d:Days.values()){
            Day day=new Day(d,term);
            String filename="./img/"+term.getName()+d.getName()+".txt";
            String toParse="";
            try{
            toParse=day.readSchedule(filename);
            } catch (FileNotFoundException e){
               System.out.println("oof");
            }catch (Exception e){
               System.out.println("Big oof");
            }
            schedule[d.getIndex()+term.getFactor()]=toParse;
            
         }
      }
      Rects.drawGUI(0);
      Rects.drawGUI(1);
   }
   
   public static Integer[][] parseForGUI(String[] toParse){
      ArrayList<Integer[]> toReturn= new ArrayList<Integer[]>();
      ArrayList<String> names=new ArrayList<String>();
      for(int i=0; i<14; i++){
         if(!toParse[i].equals("")){
         String[] line=toParse[i].split("\\n");
         
         
         for(int j=0; j<line.length; j++){
            String[] teach=line[j].split(",");
            names.add(teach[0]);
            Integer[] add={i/7,i%7,Integer.parseInt(teach[1]),Integer.parseInt(teach[2])};
            toReturn.add(add);
         }}
      }
      classNames=names.toArray(new String[names.size()]);
      return toReturn.toArray(new Integer[toReturn.size()][]);
   }
   
   public static String[] getSchedule(){
      return schedule;
   }
   
   public static String[] getNames(){
      return classNames;
   }
}