package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML BorderPane calenderscene;
    @FXML GridPane calender;
    @FXML Text YM_text;
    @FXML ImageView left_button;
    @FXML ImageView right_button;
    @FXML ChoiceBox Yearpicker;
    @FXML ChoiceBox Monthpicker;
    @FXML Button[][] dates=new Button[7][7];

    Stage par_stage;

    Popup plan_editor=new Popup();
    Stage PlanStage=new Stage();

    /////////////////////////
    int historic_year=1970;
    LocalDate current_date= LocalDate.now();
    int current_year= current_date.getYear();
    Month current_month=current_date.getMonth();
    int showing_month=current_month.getValue();
    int showing_year=current_year;
    ///////////////////////////
    int[][] days=new int[2400][20];
    int[][] startdays=new int[2400][20];

    public String putzero(int i)
    {
        if(i<10)
            return new String("0"+Integer.toString(i));
        else
            return Integer.toString(i);
    }


    public void set_Days()//sunday is 0
    {
        days[1970][1]=31;
        startdays[1970][1]=4;
        int prev_days=31;
        int prev_firstday=4;
        for(int i=1970;i<=2100;i++)
        {
            for(int j=1;j<=12;j++)
            {
                if(i==1970 && j==1)
                    continue;
                startdays[i][j]=(prev_firstday+prev_days)%7;
                if(j==2)
                {
                    if(i%4==0 && (i%100!=0 || i%400==0))
                    {
                        days[i][2]=29;
                    }
                    else
                        days[i][2]=28;

                }
                else
                {
                    if(j<=7)
                    {
                        if(j%2==1)
                            days[i][j]=31;
                        else
                            days[i][j]=30;
                    }
                    else
                    {
                        if(j%2==0)
                            days[i][j]=31;
                        else
                            days[i][j]=30;
                    }
                }
                prev_days=days[i][j];
                prev_firstday=startdays[i][j];
            }
        }

    }

    ObservableList month_list=FXCollections.observableArrayList();
    public void set_monthlist()
    {
        for(int i=1;i<=12;i++)
        {
            month_list.add(Month.values()[i-1]);
        }
    }


    public void setStage(Stage stage)
    {
        this.par_stage=stage;
    }

    public void clear_calender()
    {
        calender.getChildren().removeIf(node -> (node instanceof Button));
    }

    public void set_calender()
    {
        boolean counting=false;
        int cnt=0;

        for(int i=1;i<=6;i++)
        {
            for(int j=0;j<7;j++)
            {
                if(counting==true)
                    cnt++;
                if(cnt==days[showing_year][showing_month]+1)
                {
                    counting=false;
                }
                if(i==1 && j==startdays[showing_year][showing_month])
                {
                    counting=true;
                    cnt++;
                }
                if(cnt==0||counting==false)
                {
                    continue;
                }
                dates[i][j]=new Button();
                dates[i][j].setAlignment(Pos.TOP_LEFT);
                dates[i][j].setText(Integer.toString(cnt));
                int finalCnt = cnt;
                dates[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            FXMLLoader loader=new FXMLLoader(getClass().getResource("PlanEditor.fxml"));
                            PlanStage.setScene(new Scene(loader.load(),1000,800));
                            //PlanEditorController PEC=loader.getController();
                            //PEC.setThisstage(PlanStage);
                            PlanStage.setTitle(String.format("%d.%s.%s",showing_year,putzero(showing_month), putzero(finalCnt)));
                            PlanStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                calender.add(dates[i][j],j,i,1,1);//행. 열이 아니라 열. 행
                dates[i][j].setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            }
        }

    }
    public void set_yearpicker()
    {
        ObservableList year_list=FXCollections.observableArrayList();
        for(int i=1970;i<=2100;i++)
        {
            year_list.add(i);
        }
        Yearpicker.setItems(year_list);
        Yearpicker.setValue(current_year);
    }
    public void set_monthpicker()
    {

        Monthpicker.setItems(month_list);
        Monthpicker.setValue(current_month.name());

    }
    public int get_dates(){
        int starting=current_date.getDayOfMonth();
        DayOfWeek dayname=current_date.getDayOfWeek();
        //System.out.println(dayname.getValue());
        int first_day=dayname.getValue()-(starting-1);
        first_day%=7;
        if(first_day<0)
            first_day+=7;
        //System.out.println(first_day);
        return first_day;

    }

    public void update_calender()
    {
        YM_text.setText(Month.of(showing_month).name()+","+showing_year);
        clear_calender();
        set_calender();

    }
    /////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //calenderscene.prefHeightProperty().bind(par_stage.maxHeightProperty());?? WHY NPE
        //calenderscene.prefWidthProperty().bind(par_stage.maxWidthProperty());
        //System.out.println(showing_month);
        set_monthlist();
        set_Days();
        set_yearpicker();
        set_monthpicker();
        set_calender();
        YM_text.setText(current_month.name()+','+current_year);

        Monthpicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                showing_month=newValue.intValue()+1;
                update_calender();
            }
        });

        Yearpicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                showing_year=newValue.intValue()+historic_year;
                update_calender();
            }
        });

        left_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                showing_month-=1;
                if(showing_month==0)
                {
                    showing_month=12;
                    showing_year-=1;
                }

                //System.out.println(showing_month);
                Monthpicker.setValue(Monthpicker.getItems().get(showing_month-1));
                Yearpicker.setValue(Yearpicker.getItems().get(showing_year-historic_year));

            }
        });

        right_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                showing_month+=1;
                if(showing_month==13)
                {
                    showing_month=1;
                    showing_year+=1;
                }

                //System.out.println(showing_month);
                Monthpicker.setValue(Monthpicker.getItems().get(showing_month-1));
                Yearpicker.setValue(Yearpicker.getItems().get(showing_year-historic_year));
            }
        });
    }
}
