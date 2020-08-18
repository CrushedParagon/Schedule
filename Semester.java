public enum Semester{
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