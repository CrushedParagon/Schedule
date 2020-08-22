import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class SchedulePanel extends JPanel{
   private ArrayList<JTextField> textFields=new ArrayList<JTextField>();
   private JTextArea area;
   
   public ArrayList<JTextField> getTextFields(){
      return textFields;
   }
   
   public JTextArea getArea(){
      return area;
   }
   
   public void addToList(JTextField tf){
      textFields.add(tf);
   }
   
   public void setArea(JTextArea ta){
      area=ta;
   }

}