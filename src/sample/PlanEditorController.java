package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class PlanEditorController implements Initializable {
    @FXML BorderPane PlanEditor;
    @FXML ScrollPane PlanList;
    @FXML VBox InnerContainer;
    @FXML Button AddButton;
    @FXML Button LoadButton;



    public void MatchContainer()
    {

        PlanList.prefViewportWidthProperty().bind(PlanEditor.widthProperty());
        InnerContainer.prefWidthProperty().bind(PlanList.widthProperty());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources){
        MatchContainer();

        AddButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FXMLLoader load_plan=new FXMLLoader(getClass().getResource("PlanDisplay.fxml"));

                BorderPane plan= null;
                try {
                    plan = load_plan.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PlanDisplayController PDC=load_plan.getController();
                PDC.setParstage((Stage)PlanEditor.getScene().getWindow());
                InnerContainer.getChildren().add(plan);
                PDC.setContainer(InnerContainer);
                plan.prefWidthProperty().bind(InnerContainer.widthProperty());

            }
        });
        final int[] a = new int[1];
        LoadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage mystage=(Stage)PlanEditor.getScene().getWindow();
                File folder=new File("./src/savefile/"+mystage.getTitle());
                File[] files=folder.listFiles();
                if(files!=null)
                {
                    for(File f:files)
                    {
                        FXMLLoader load_plan=new FXMLLoader(getClass().getResource("PlanDisplay.fxml"));

                        BorderPane plan= null;
                        try {
                            plan = load_plan.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PlanDisplayController PDC=load_plan.getController();
                        PDC.setParstage(mystage);
                        InnerContainer.getChildren().add(plan);
                        PDC.setContainer(InnerContainer);
                        plan.prefWidthProperty().bind(InnerContainer.widthProperty());

                        FileReader r= null;
                        try {
                            r = new FileReader(f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        BufferedReader reader=new BufferedReader(r);

                        try {
                            PDC.TaskTitle.setText(reader.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            PDC.TaskTime.setText(reader.readLine()+" : "+reader.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            a[0] =Integer.parseInt(reader.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PDC.TaskImportance.setFill(Color.hsb(0.0f,(float)(1-0.25*(float)a[0]),1.0f));
                        String detail="";
                        String line="";
                        while(true)
                        {
                            try {
                                if (!((line = reader.readLine()) != null)) break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            detail+=line;
                        }
                        PDC.TaskDetail.setText(detail);

                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /*
                        try {
                            PDC.myplansaver.setAlarm(new TaskData(PDC.TaskTitle.getText(),PDC.TaskTime.getText().substring(0,2),PDC.TaskTime.getText().substring(5,7),a[0],PDC.TaskDetail.getText()),mystage.getTitle());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        */


                    }
                }
            }
        });
    }

}
