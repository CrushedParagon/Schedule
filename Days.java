public enum Days{//basic enum where each value also holds it's String equivalent and an index
   MONDAY("Monday",0),TUESDAY("Tuesday",1),WEDNESDAY("Wednesday",2),THURSDAY("Thursday",3),FRIDAY("Friday",4),SATURDAY("Saturday",5),SUNDAY("Sunday",6);
   
   private String dayName;
   private int index;
   
   private Days(String dayName, int index){
      this.dayName=dayName;
      this.index=index;
   }
   
   public String getName(){
      return dayName;
   }
   
   public int getIndex(){
      return index;
   }
}