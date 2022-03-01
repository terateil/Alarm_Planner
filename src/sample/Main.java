package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

class PST{
    static Stage pst;
    public static void setpst(Stage p)
    {
        pst=p;
    }

}


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root=loader.load();

        Controller controller=loader.getController();
        controller.setStage(primaryStage);


        primaryStage.setTitle("Alarm Planner");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        PST.setpst(primaryStage);



    }


    public static void main(String[] args) {
        launch(args);
    }
}
