package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AlarmSceneController implements Initializable {
    @FXML Text AlarmTime;
    @FXML Text AlarmName;
    @FXML Text AlarmDetail;
    @FXML Button OKButton;

    Stage st;
    MediaPlayer alarm_music;
    public void setMP(MediaPlayer m)
    {
        alarm_music=m;
    }

    public void setStage(Stage thisstage)
    {
        st=thisstage;
    }

    public void settexts(TaskData alarmdata)
    {
        AlarmTime.setText(alarmdata.hour+" : "+alarmdata.min);
        AlarmName.setText(alarmdata.name);
        AlarmName.setFill(Color.hsb(0.0f,(float)(1-0.25*alarmdata.importance),1.0f));
        AlarmDetail.setText(alarmdata.detail);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OKButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                st.close();
                alarm_music.stop();
            }
        });

    }
}
