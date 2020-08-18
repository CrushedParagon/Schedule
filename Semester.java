public enum Semester{//basic enum with the String equivalent, and one with the number 7 for use later
   FALL("Fall",0),WINTER("Winter",7);
   
   private String season;
   private int factor;
   
   private Semester(String season, int factor){
      this.season=season;
      this.factor=factor;
   }
   
   public String getName(){
      return season;
   }
   
   public int getFactor(){
      return factor;
   }
   
 
}