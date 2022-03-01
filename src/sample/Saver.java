package sample;



import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Saver {
    String path="./src/sounds/Over_the_Horizon.mp3";
    Media m=new Media(new File(path).toURI().toString());
    MediaPlayer music=new MediaPlayer(m);

    Stage thisstage;

    VBox PDc;
    public void setcontainer(VBox ctn)
    {
        PDc=ctn;
    }

    public void delete_all()
    {
        PDc.getChildren().clear();
    }

    Timer tm=new Timer();
    public void savetask(TaskData taskdata,String savedate,String current_date) throws ParseException {
        try {
            File datefolder=new File("./src/savefile/"+savedate);
            datefolder.mkdir();
            System.out.println(taskdata.hour+taskdata.min);
            String saveTaskpath=datefolder+"/"+taskdata.hour+"-"+taskdata.min+".txt";
            System.out.println(saveTaskpath);
            PrintWriter pw=new PrintWriter(saveTaskpath);
            pw.println(taskdata.name);
            System.out.println("ok");
            pw.println(taskdata.hour);
            System.out.println("ok");
            pw.println(taskdata.min);
            System.out.println("ok");
            pw.println(taskdata.importance);
            System.out.println("ok");
            pw.println(taskdata.detail);
            System.out.println("ok");
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void setAlarm(TaskData taskdata,String current_date) throws ParseException {
        Date date = new Date();
        long current_time = date.getTime();
        System.out.println(current_date);
        String myDate = String.format(current_date+String.format(" %s:%s:00",taskdata.hour,taskdata.min));//ex)"2014/10/29 18:10:45";
        System.out.println(myDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date taskdate = sdf.parse(myDate);
        long task_time = taskdate.getTime();



        Stage alarmer=new Stage();

        alarmer.setAlwaysOnTop(true);

        FXMLLoader loader=new FXMLLoader(getClass().getResource("AlarmScene.fxml"));
        try {
            alarmer.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AlarmSceneController ALC=loader.getController();
        ALC.setStage(alarmer);
        ALC.settexts(taskdata);
        ALC.setMP(music);


        TimerTask alarm=new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    alarmer.show();
                    music.play();
                });
            }
        };
        tm.schedule(alarm,task_time-current_time);


    }

    public void canceltimer()
    {
        tm.cancel();
    }
}
