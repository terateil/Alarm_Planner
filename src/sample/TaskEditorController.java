package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.util.ResourceBundle;


class TaskData
{

    String name;
    String hour;
    String min;
    int importance;
    String detail;
    TaskData(String name,String hour,String min,int importance,String detail)
    {
        this.name=name;
        this.hour=hour;
        this.min=min;
        this.importance=importance;
        this.detail=detail;
    }
}


public class TaskEditorController implements Initializable {
    @FXML GridPane TaskEditor;

    @FXML TextField ScheduleName;
    @FXML ChoiceBox HourPicker;
    @FXML ChoiceBox MinPicker;
    @FXML Group ImpChecks;
    @FXML TextArea Details;
    @FXML Button SaveButton;


    //XSSFWorkbook wb=new XSSFWorkbook();
    //XSSFSheet sht=wb.createSheet("Saves");

    VBox PDc;
    public void setcontainer(VBox ctn)
    {
        PDc=ctn;
    }

    Stage parstage;
    public void setParstage(Stage par)//called by PDC
    {
        parstage=par;
    }
    BorderPane caller;
    public void calledby(BorderPane caller)
    {
        this.caller=caller;
    }


    public void setImpChecks()
    {
        for(int i=0;i<ImpChecks.getChildren().size();i++)
        {
            CheckBox chk=(CheckBox)ImpChecks.getChildren().get(i);
            int finalI = i;
            chk.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue)
                    {
                        for(int j=0;j<ImpChecks.getChildren().size();j++)
                        {
                            if(finalI ==j)
                                continue;
                            else
                            {
                                CheckBox others=(CheckBox)ImpChecks.getChildren().get(j);
                                others.setSelected(false);
                            }

                        }
                    }
                }
            });

        }
    }

    public void setHourPicker()
    {
        for(int i=0;i<=9;i++)
        {
            HourPicker.getItems().add("0"+ i);
        }
        for(int i=10;i<=23;i++)
        {
            HourPicker.getItems().add(Integer.toString(i));
        }
    }
    public void setMinPicker()
    {
        for(int i=0;i<=9;i++)
        {
            MinPicker.getItems().add("0"+ i);
        }
        for(int i=10;i<=59;i++)
        {
            MinPicker.getItems().add(Integer.toString(i));
        }
    }

    public void set_callerdata(TaskData taskdata)
    {
        Text TaskTitle=(Text)caller.getTop();
        TaskTitle.setText(taskdata.name);

        Rectangle TaskImportance=(Rectangle)caller.getLeft();
        TaskImportance.setFill(Color.hsb(0.0f,(float)(1-0.25*taskdata.importance),1.0f));

        ScrollPane scp= (ScrollPane) caller.getCenter();
        Text TaskDetail=(Text) scp.getContent();
        TaskDetail.setText(taskdata.detail);

        Text TaskTime=(Text)caller.getRight();
        TaskTime.setText(taskdata.hour+" : "+taskdata.min);



    }


    Saver saver=new Saver();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setHourPicker();
        setMinPicker();
        setImpChecks();


        SaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int importance = 0;
                ObservableList imps=ImpChecks.getChildren();
                for(int i=0;i<imps.size();i++)
                {
                    CheckBox imp=(CheckBox) imps.get(i);
                    if(imp.isSelected())
                        importance=i;
                }
                TaskData temp_saver=new TaskData(ScheduleName.getText(),HourPicker.getValue().toString(),MinPicker.getValue().toString(),importance,Details.getText());
                saver.setcontainer(PDc);
                Stage thisStage=(Stage)TaskEditor.getScene().getWindow();
                try {
                    saver.savetask(temp_saver,thisStage.getTitle(),thisStage.getTitle());
                    saver.setAlarm(temp_saver,thisStage.getTitle());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                set_callerdata(temp_saver);

            }
        });



    }
}
