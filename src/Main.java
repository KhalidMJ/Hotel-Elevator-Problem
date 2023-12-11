import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Arrays;

public class Main extends Application {
    private static Hotel hotel;
    private static Elevator elevator1;
    private static Elevator elevator2;

    public static void main(String[] args) {
        // Creating the simulation main objects -----------------------
        hotel = new Hotel(8, 2);
        elevator1 = hotel.getElevators()[0];
        elevator2 = hotel.getElevators()[1];

        // generating and importing passengers
        String path = "src/Passengers.csv";


        try {
            Simulation.generateRandomPassengersToFile(70, 5, 150, path);
            String[][] passengersData = Simulation.getPassengersFromFile(path);
            Passenger[] passengers = Simulation.turnPassengersArrayIntoObjects(passengersData);
            passengers = Simulation.sortPassengersByArrivalTime(passengers);
            hotel.setPassengers(passengers);
            System.out.println(Arrays.toString(passengers));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        Simulation.setStartTime();
        launch(args);
    }

    @Override
    public void start(Stage mainStage) {

        // Creating elevator panes
        ElevatorPane elevator1Pane = new ElevatorPane(elevator1);
        ElevatorPane elevator2Pane = new ElevatorPane(elevator2);

        // Creating elevator information box
        ElevatorInformationBox elevator1Information = new ElevatorInformationBox(elevator1);
        ElevatorInformationBox elevator2Information = new ElevatorInformationBox(elevator2);

        // Creating floors panes
        FloorPane floor0Pane = new FloorPane(hotel.getFloors()[0], new Image("images/floor0.PNG"));
        FloorPane floor1Pane = new FloorPane(hotel.getFloors()[1], new Image("images/floor1.PNG"));
        FloorPane floor2Pane = new FloorPane(hotel.getFloors()[2], new Image("images/floor2.PNG"));
        FloorPane floor3Pane = new FloorPane(hotel.getFloors()[3], new Image("images/floor3.PNG"));
        FloorPane floor4Pane = new FloorPane(hotel.getFloors()[4], new Image("images/floor4.PNG"));
        FloorPane floor5Pane = new FloorPane(hotel.getFloors()[5], new Image("images/floor5.PNG"));
        FloorPane floor6Pane = new FloorPane(hotel.getFloors()[6], new Image("images/floor6.PNG"));
        FloorPane floor7Pane = new FloorPane(hotel.getFloors()[7], new Image("images/floor7.PNG"));

        // Adding the floors in one single VBox
        VBox floorsVBox = new VBox(floor7Pane, floor6Pane, floor5Pane, floor4Pane, floor3Pane, floor2Pane, floor1Pane, floor0Pane);

        // Running the main loop in a background thread to split it from the GUI Thread, hence we can update the GUI in real time.
        new Thread(() -> Main.elevatorRun(elevator1, "SCAN")).start();
        new Thread(() -> Main.elevatorRun(elevator2, "SCAN")).start();
        new Thread(Main::passengersUpdatingRun).start();

        // The main timeline for updates and animations
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(elevator1Pane.kfUpdateElevatorPicture(), elevator1Pane.kfUpdateElevatorY(), elevator1Pane.kfUpdateDisplayInformation());
        timeline.getKeyFrames().addAll(elevator2Pane.kfUpdateElevatorPicture(), elevator2Pane.kfUpdateElevatorY(), elevator2Pane.kfUpdateDisplayInformation());
        timeline.getKeyFrames().addAll(floor0Pane.kfUpdateFloor(), floor1Pane.kfUpdateFloor(), floor2Pane.kfUpdateFloor(),
                                        floor3Pane.kfUpdateFloor(), floor4Pane.kfUpdateFloor(), floor5Pane.kfUpdateFloor(),
                                        floor6Pane.kfUpdateFloor(), floor7Pane.kfUpdateFloor());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        HBox hbox = new HBox(elevator1Pane, elevator2Pane, floorsVBox);
        hbox.setSpacing(0);

        Scene scene = new Scene(hbox, 600, 880);
        mainStage.setResizable(true);
        mainStage.setScene(scene);
        mainStage.show();
    }



    private static void elevatorRun(Elevator elevator, String algorithm) {
        // start the elevator and run the simulation
        elevator.start("SCAN");
    }

    private static void passengersUpdatingRun() {
        // update the passengers on each floor
        hotel.updateFloorPassengers();
    }

}

// GUI Classes ----------------------------------------------------
    class ProgramInterface extends GridPane {
    private final StackPane Spane1;
    private final StackPane Spane2;
    private final StackPane Spane3;

    private final Label labelINF;
    private final GridPane GRIDPANE_S2;
    private final Label PassengerNo;
    private final Label PassengerTime;
    private final Label LastPassenger;
    private final TextField TextField1;
    private final TextField TextField2;
    private final TextField TextField3;
    private final Button BtnSimulation;
    private final GridPane GRIDPANE_S3;
    private final Button Phase1;
    private final Button Phase2;
    private final Label Sellectphase;





    public ProgramInterface(){
        setStyle("-fx-background-color: Gray");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(100));
        setHgap(10);
        setVgap(10);

        Spane1 = new StackPane();
        Spane2 = new StackPane();
        Spane3 = new StackPane();


        Spane1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Spane2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Spane3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setVgrow(Spane1, Priority.ALWAYS);
        GridPane.setVgrow(Spane2, Priority.ALWAYS);
        GridPane.setVgrow(Spane3, Priority.ALWAYS);

        GridPane.setHgrow(Spane1, Priority.ALWAYS);
        GridPane.setHgrow(Spane2, Priority.ALWAYS);
        GridPane.setHgrow(Spane3, Priority.ALWAYS);

        add(Spane1, 0, 0);

        GridPane.setRowSpan(Spane1, 2);

        add(Spane2, 1, 0);
        GridPane.setColumnSpan(Spane2, 2);

        add(Spane3, 1, 1);
        Scene scene = new Scene(this, 350, 250);

        // StackPane 1 Data
        labelINF= new Label("Hotel Elevator Problem\n\n" +
                "Stusent Name   ,    ID\n" +
                "Khaled Jaafari , 2036103\n"+
                "Ammar Alwesabi , 2035083\n"+
                "Fares Alahmadi , 2035993\n"+
                "Younes Alhazmi , 2047592\n\n"+
                "Instructor :khaled Al-Khalifi ");
        StackPane.setAlignment(labelINF, Pos.TOP_CENTER);
        labelINF.setFont(new Font("Cambria", 46));
        Spane1.getChildren().add(labelINF);

        // StackPane 2 Data
        GRIDPANE_S2= new GridPane();
        PassengerNo= new Label(" Number Of Passenger ");
        PassengerTime= new Label("Time of passenger coming");
        LastPassenger= new Label("The Time of the last person");
        TextField1 = new TextField();
        TextField2 = new TextField();
        TextField3 = new TextField();
        BtnSimulation =new Button("Simulation");
        GridPane.setHalignment(BtnSimulation, HPos.LEFT);


        GRIDPANE_S2.setAlignment(Pos.TOP_CENTER);



        GRIDPANE_S2.add(PassengerNo, 0, 0);
        GRIDPANE_S2.add(TextField1, 1, 0);
        GRIDPANE_S2.add(PassengerTime, 0, 1);
        GRIDPANE_S2.add(TextField2, 1, 1);
        GRIDPANE_S2.add(LastPassenger, 0, 2);
        GRIDPANE_S2.add(TextField3, 1, 2);
        GRIDPANE_S2.add(BtnSimulation, 1, 3);

        GRIDPANE_S2.setHgap(10);
        GRIDPANE_S2.setVgap(10);


        Spane2.getChildren().add(GRIDPANE_S2);



        // StackPane 3 Data
        GRIDPANE_S3= new GridPane();
        Phase1 = new Button("Phase1");
        Phase2 = new Button("phase2");
        Sellectphase= new Label("select the phase you want ");
        Sellectphase.setFont(new Font("Cambria", 46));
        GRIDPANE_S3.setAlignment(Pos.TOP_CENTER);

        GRIDPANE_S3.add(Sellectphase, 0, 0);
        GRIDPANE_S3.add(Phase1, 0, 1);
        GRIDPANE_S3.add(Phase2, 0, 2);

        GRIDPANE_S3.setVgap(50);
        Phase1.setAlignment(Pos.CENTER_RIGHT);
        Phase2.setAlignment(Pos.CENTER_LEFT);



        Spane3.getChildren().add(GRIDPANE_S3);



    }

}

    class ElevatorPane extends Pane{
        private static final Image imgOpenDoors = new Image("images/elevatorOpen.jpg");
        private static final Image imgChangingDoors = new Image("images/elevatorChanging.jpg");
        private static final Image imgClosedDoors = new Image("images/elevatorClosed.jpg");

        private final ObjectProperty<Image> elevatorDoorsPictureProperty = new SimpleObjectProperty<>(imgClosedDoors);
        private final SimpleIntegerProperty currentFloorYProperty;
        private final SimpleIntegerProperty informationCurrentFloorYProperty;
        private final ImageView imageView;
        ElevatorInformationBox elevatorInformation;
        private final Elevator elevator;

        public ElevatorPane(Elevator elevator) {
            this.elevator = elevator;
            currentFloorYProperty = new SimpleIntegerProperty(Math.abs((this.elevator.getCurrentFloor() * 110) - 770));
            informationCurrentFloorYProperty = new SimpleIntegerProperty(currentFloorYProperty.getValue() + 7);
            imageView = new ImageView();
            imageView.setFitHeight(110);
            imageView.setFitWidth(100);
            imageView.yProperty().bindBidirectional(currentFloorYProperty);
            imageView.imageProperty().bindBidirectional(elevatorDoorsPictureProperty);

            Line line = new Line();
            line.setStartX(50);
            line.setEndX(50);
            line.setStartY(0);
            line.setEndY(875);
            line.setStroke(Color.grayRgb(70));
            line.setStrokeWidth(5);

            getChildren().add(line);
            getChildren().add(imageView);
            setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            setMaxSize(100, 880);
            setPrefSize(100, 880);
            setMinSize(100, 880);
            setBackground(Background.fill(Color.grayRgb(25)));

            elevatorInformation = new ElevatorInformationBox(this.elevator);
            elevatorInformation.layoutYProperty().bindBidirectional(informationCurrentFloorYProperty);
            elevatorInformation.setLayoutX(12);
            getChildren().add(elevatorInformation);

        }

        public KeyFrame kfUpdateElevatorPicture() {
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.2), event -> {
                if (this.elevator.getDoorsStatus() == DoorsStatus.CLOSED){
                    elevatorDoorsPictureProperty.set(imgClosedDoors);
                } else if (this.elevator.getDoorsStatus() == DoorsStatus.OPEN){
                    elevatorDoorsPictureProperty.set(imgOpenDoors);
                } else {
                    elevatorDoorsPictureProperty.set(imgChangingDoors);
                }
            });
            return keyframe;
        }
        public KeyFrame kfUpdateElevatorY() {
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.2), event -> {
                currentFloorYProperty.set(Math.abs((elevator.getCurrentFloor() * 110) - 770)); // TODO: find a way to make the animation smoother
                informationCurrentFloorYProperty.set(currentFloorYProperty.getValue() + 7);
            });
            return keyframe;
            }

        public KeyFrame kfUpdateDisplayInformation() {
            KeyFrame keyframe = elevatorInformation.kfUpdateInformation();
            return keyframe;
        }
    }


    class ElevatorInformationBox extends HBox {
        private static final Image imgOpenSign = new Image("images/OpenSign.png");
        private static final Image imgNotOpenSign = new Image("images/notOpen.png");
        private static final Image imgUpSign = new Image("images/up.png");
        private static final Image imgIdleSign = new Image("images/IDLE.png");
        private static final Image imgDownSign = new Image("images/down.png");
        private static final Image imgLvl0 = new Image("images/0.png");
        private static final Image imgLvl1 = new Image("images/1.png");
        private static final Image imgLvl2 = new Image("images/2.png");
        private static final Image imgLvl3 = new Image("images/3.png");
        private static final Image imgLvl4 = new Image("images/4.png");
        private static final Image imgLvl5 = new Image("images/5.png");
        private static final Image imgLvl6 = new Image("images/6.png");
        private static final Image imgLvl7 = new Image("images/7.png");

        private final ObjectProperty<Image> DoorsStatusPictureProperty = new SimpleObjectProperty<>(imgNotOpenSign);
        private final ObjectProperty<Image> elevatorStatusPictureProperty = new SimpleObjectProperty<>(imgIdleSign);
        private final ObjectProperty<Image> elevatorLevelPictureProperty = new SimpleObjectProperty<>(imgLvl0);

        private final ImageView imageViewDoorsStatus = new ImageView();
        private final ImageView imageViewElevatorStatus = new ImageView();
        private final ImageView imageViewElevatorLevel = new ImageView();
        Elevator elevator;

        public ElevatorInformationBox(Elevator elev){
            this.elevator = elev;

            // Binding the Image View properties to the images
            imageViewDoorsStatus.imageProperty().bindBidirectional(DoorsStatusPictureProperty);
            imageViewElevatorStatus.imageProperty().bindBidirectional(elevatorStatusPictureProperty);
            imageViewElevatorLevel.imageProperty().bindBidirectional(elevatorLevelPictureProperty);

            // Setting the size of the image views
            imageViewDoorsStatus.setFitHeight(16);
            imageViewDoorsStatus.setFitWidth(41);
            imageViewElevatorStatus.setFitHeight(17);
            imageViewElevatorStatus.setFitWidth(12);
            imageViewElevatorLevel.setFitHeight(17);
            imageViewElevatorLevel.setFitWidth(12);

            // Adding the images to the grid pane
            getChildren().add(imageViewDoorsStatus);
            getChildren().add(imageViewElevatorStatus);
            getChildren().add(imageViewElevatorLevel);

            // Modifying the settings of the HBox
            setSpacing(6);
            setMaxSize(65, 17);
            setMinSize(65, 17);
            setPrefSize(65, 17);
            setBackground(Background.fill(Color.BLACK));

        }

        public KeyFrame kfUpdateInformation(){
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.2), event -> {
                if (elevator.getDoorsStatus() == DoorsStatus.OPEN){
                    DoorsStatusPictureProperty.set(imgOpenSign);
                } else {
                    DoorsStatusPictureProperty.set(imgNotOpenSign);
                }

                if (elevator.getElevatorStatus() == ElevatorStatus.IDLE){
                    elevatorStatusPictureProperty.set(imgIdleSign);
                } else if (elevator.getElevatorStatus() == ElevatorStatus.MOVING_UP){
                    elevatorStatusPictureProperty.set(imgUpSign);
                } else {
                    elevatorStatusPictureProperty.set(imgDownSign);
                }

                switch (elevator.getCurrentFloor()) {
                    case 0 -> elevatorLevelPictureProperty.set(imgLvl0);
                    case 1 -> elevatorLevelPictureProperty.set(imgLvl1);
                    case 2 -> elevatorLevelPictureProperty.set(imgLvl2);
                    case 3 -> elevatorLevelPictureProperty.set(imgLvl3);
                    case 4 -> elevatorLevelPictureProperty.set(imgLvl4);
                    case 5 -> elevatorLevelPictureProperty.set(imgLvl5);
                    case 6 -> elevatorLevelPictureProperty.set(imgLvl6);
                    case 7 -> elevatorLevelPictureProperty.set(imgLvl7);
                }
            });
            return keyframe;
        }
    }

    class FloorPane extends Pane{
        private final SimpleIntegerProperty waitingPassengersProperty;
        private final ObjectProperty<Image> CallButtonsPectureProperty;
        private final ImageView imgvFloor;
        private final ImageView imgvPerson;
        private final ImageView imgvCallButtons;
        private final Floor floor;
        private final Label waitingPassengersCount;

        private final Image imgCallButtonOFF = new Image("images/elevatorCallOFF.png");
        private final Image imgCallButtonUP = new Image("images/elevatorCallUP.png");
        private final Image imgCallButtonDOWN = new Image("images/elevatorCallDOWN.png");
        private final Image imgCallButtonBOTH = new Image("images/elevatorCallBOTH.png");

        public FloorPane(Floor floor, Image image) {
            this.floor = floor;

            waitingPassengersProperty = new SimpleIntegerProperty(floor.getWaitingPassengersCount());
            waitingPassengersCount = new Label();
            waitingPassengersCount.textProperty().bind(waitingPassengersProperty.asString());
            waitingPassengersCount.setFont(Font.font(30));

            CallButtonsPectureProperty = new SimpleObjectProperty<>(new Image("images/elevatorCallOFF.png"));

            imgvFloor = new ImageView(image);
            imgvFloor.setFitWidth(400);
            imgvFloor.setFitHeight(110);

            imgvPerson = new ImageView(new Image("images/personSign.png"));
            imgvPerson.setFitWidth(20);
            imgvPerson.setFitHeight(20);

            imgvCallButtons = new ImageView();
            imgvCallButtons.imageProperty().bindBidirectional(CallButtonsPectureProperty);
            imgvCallButtons.setFitWidth(23);
            imgvCallButtons.setFitHeight(35);
            imgvCallButtons.setLayoutX(18);
            imgvCallButtons.setLayoutY(27);


            // Adding imgvFloor and waitingPassengersCount to a single HBox
            VBox vbox = new VBox(imgvPerson, waitingPassengersCount);
            vbox.setSpacing(1);
            vbox.setLayoutX(60);
            vbox.setLayoutY(10);

            getChildren().addAll(imgvFloor, imgvCallButtons, vbox);
            setMaxSize(400, 110);
            setPrefSize(400, 110);
            setMinSize(400, 110);
            setBackground(Background.fill(Color.GRAY));
        }

        public KeyFrame kfUpdateFloor() {
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.2), event -> {
                waitingPassengersProperty.set(floor.getWaitingPassengersCount());
                if (!floor.getCallButton().isPressed()){
                    CallButtonsPectureProperty.set(imgCallButtonOFF);
                }
                else if (floor.getCallButton().getButtonsStatus()[0] && !floor.getCallButton().getButtonsStatus()[1]){
                    CallButtonsPectureProperty.set(imgCallButtonUP);
                }
                else if (!floor.getCallButton().getButtonsStatus()[0] && floor.getCallButton().getButtonsStatus()[1]){
                    CallButtonsPectureProperty.set(imgCallButtonDOWN);
                } else {
                    CallButtonsPectureProperty.set(imgCallButtonBOTH);
                }
            });
            return keyframe;
        }
    }