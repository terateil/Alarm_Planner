package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class PlanDisplayController implements Initializable {

    @FXML BorderPane PlanDisplay;

    @FXML Text TaskTitle;
    @FXML Rectangle TaskImportance;
    @FXML Text TaskDetail;
    @FXML Text TaskTime;

    @FXML Button EditButton;
    @FXML Button DeleteButton;



    Stage EditingStage=new Stage();
    Stage parstage;


    public void setParstage(Stage par)//called by PEC
    {
        parstage=par;
    }

    VBox PDc;
    public void setContainer(VBox ctn)
    {
        PDc=ctn;
    }

    public void match_parsize(VBox par)
    {
        PlanDisplay.prefWidthProperty().bind(par.widthProperty());
    }

    public void setnewplan()
    {

    }

    public void callFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {

            }
        }
    }

    public void loadAllPlans(File folder) throws IOException {
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
                PDC.setParstage(parstage);
                PDc.getChildren().add(plan);
                PDC.setContainer(PDc);
                plan.prefWidthProperty().bind(PDc.widthProperty());

                FileReader r=new FileReader(f);
                BufferedReader reader=new BufferedReader(r);

                PDC.TaskTitle.setText(reader.readLine());
                PDC.TaskTime.setText(reader.readLine()+" : "+reader.readLine());
                PDC.TaskImportance.setFill(Color.hsb(0.0f,(float)(1-0.25*Integer.parseInt(reader.readLine())),1.0f));
                String detail="";
                String line="";
                while((line = reader.readLine()) != null)
                {
                    detail+=line;
                }
                PDC.TaskDetail.setText(detail);

                reader.close();

            }
        }
    }

    Saver myplansaver;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override

            public void handle(ActionEvent event) {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("TaskEditor.fxml"));
                try {
                    EditingStage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TaskEditorController TEC=loader.getController();
                TEC.setParstage((Stage)PlanDisplay.getScene().getWindow());
                TEC.calledby(PlanDisplay);
                TEC.setcontainer(PDc);

                myplansaver=TEC.saver;

                EditingStage.setTitle(parstage.getTitle());
                EditingStage.show();
            }
        });


        DeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File targetpath=new File("./src/savefile/"+parstage.getTitle());
                try {
                    myplansaver.canceltimer();
                } catch (Exception e) {

                }
                File deletedfile=new File("./src/savefile/"+parstage.getTitle()+"/"+TaskTime.getText().substring(0,2)+"-"+TaskTime.getText().substring(5,7)+".txt");
                System.out.println(deletedfile.getName());
                System.out.println(deletedfile.exists());
                deletedfile.delete();
                PDc.getChildren().clear();
                try {
                    loadAllPlans(targetpath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
