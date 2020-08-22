import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class autoSchedule extends JTabbedPane{


   
   public static void main(String[] args){
      new File("./img").mkdirs();
      makeGUI();
   }
   
   public autoSchedule(){
   
   }
   
   public static void makeGUI(){
      JFrame frame=new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JTabbedPane semesterPane=new JTabbedPane();
      semesterPane.setPreferredSize(new Dimension(700,150));
      
      for(Semester s:Semester.values()){
         
         autoSchedule panel=new autoSchedule();
      
         for(Days d:Days.values()){
            SchedulePanel toAdd=panel.makePanel();
            panel.addTab(d.getName(),toAdd);
         }
         
         semesterPane.addTab(s.getName(),panel);
      }
      
      JPanel bottom=new JPanel();
      JButton reset=new JButton("Reset");
      
      reset.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            autoSchedule panel=(autoSchedule)semesterPane.getSelectedComponent();
            Component pane=panel.getSelectedComponent();
            SchedulePanel full=(SchedulePanel) pane;
            full.getArea().setText("");
         }
      
      });
      
      JButton add=new JButton("Add");
      
      add.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            autoSchedule panel=(autoSchedule)semesterPane.getSelectedComponent();
            Component pane=panel.getSelectedComponent();
            SchedulePanel full=(SchedulePanel) pane;
            
            ArrayList<JTextField> fields=full.getTextFields();
            

            
            String className=fields.get(0).getText();
            String classStart=fields.get(1).getText();
            String classEnd=fields.get(2).getText();
            
            for(int i=0; i<3; i++){
               fields.get(i).setText("");
            }
            
            String toSet=className+"; "+classStart+" ; "+classEnd;
           
            if(checkValidity(classStart) && checkValidity(classEnd)){
               full.getArea().append(toSet+"\n");
            }
         }
      });
      
      JButton save=new JButton("Save");
      
      save.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            for(int i=0; i<2; i++){
               for(int j=0; j<7; j++){
                  String semester=semesterPane.getTitleAt(i);
                  
                  autoSchedule first=(autoSchedule)semesterPane.getComponentAt(i);
                  
                  String day=first.getTitleAt(j);
                  SchedulePanel second=(SchedulePanel)first.getComponentAt(j);
                  String text=second.getArea().getText();
                  try{
                     printFile(semester,day,text);
                  } catch(IOException io){
                     System.out.println(io);
                  }
                  System.out.println(semester+day+text);
               }
            }
         }
      });
      
      JButton view=new JButton("View Schedule");
      view.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            String[] args={};
            Schedule.main(args);
         }
      });
      
      bottom.add(reset,BorderLayout.WEST);
      bottom.add(save,BorderLayout.WEST);
      bottom.add(view,BorderLayout.EAST);
      bottom.add(add,BorderLayout.EAST);
      
      frame.getContentPane().add(bottom,BorderLayout.SOUTH);
      frame.getContentPane().add(semesterPane,BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
   }
   
   public SchedulePanel makePanel(){
      SchedulePanel toReturn=new SchedulePanel();
      GridLayout layout=new GridLayout(1,2);
      toReturn.setLayout(layout);
      
      JPanel subPanel=new JPanel();
      GridLayout layout2=new GridLayout(3,2);
      subPanel.setLayout(layout2);
      
      JLabel name=new JLabel("Class Name");
      JLabel start=new JLabel("Start Time(24 Hour Time)");
      JLabel end=new JLabel("End Time(24 Hour Time)");
      
      JLabel[] labels={name,start,end};
      
      JTextField className=new JTextField();
      JTextField classStart=new JTextField();
      JTextField classEnd=new JTextField();
      
      
      toReturn.addToList(className);
      toReturn.addToList(classStart);
      toReturn.addToList(classEnd);
      
      ArrayList<JTextField> textFields=toReturn.getTextFields();
      
      for(int i=0; i<3; i++){
         subPanel.add(labels[i]);
         subPanel.add(textFields.get(i));
      }
      
      JTextArea area=new JTextArea();
      area.setEditable(false);
      toReturn.setArea(area);
      
      toReturn.add(subPanel);
      toReturn.add(area);
      
      return toReturn;
   }
   
   public static boolean checkValidity(String s){
      boolean toReturn=true;
      String[] times=s.trim().split(":");
      if(times.length!=2){
         toReturn=false;
      }
      try{
         Integer.parseInt(times[0]);
         Integer.parseInt(times[1]);
      }catch (Exception e){
         toReturn=false;
      }
      return toReturn;
      
   }
   
   public static void printFile(String semester, String day, String text) throws IOException{
      String filename="./img/"+semester+day+".txt";
      FileWriter writer=new FileWriter(filename);
      
      if(!text.equals("")){
         String[] lines=text.split("\\n");
         int numLines=lines.length;

         String newText="";
      
         for(String s:lines){
         String[] part=s.split(";");
         String revisedLine=part[0].trim()+";"+part[1].trim()+";"+part[2].trim()+"\n";
         newText+=revisedLine;
         }
      
         String finalText=""+numLines+"\n"+newText;
         
         writer.write(finalText);
         writer.close();
         System.out.println(finalText);
      }
      
      else{
         writer.write(""+0);
         writer.close();
      }
   }
}