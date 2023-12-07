import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
            Simulation.generateRandomPassengersToFile(80, 5, 700, path);
            String[][] passengersData = Simulation.getPassengersFromFile(path);
            Passenger[] passengers = Simulation.turnPassengersArrayIntoObjects(passengersData);
            System.out.println(Arrays.toString(passengers));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
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
        // Running the main loop in a background thread to split it from the GUI Thread, hence we can update the GUI in real time.
        new Thread(() -> Main.elevatorRun(elevator1)).start();
        new Thread(() -> Main.elevatorRun(elevator2)).start();

        // The main timeline for updates and animations
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(elevator1Pane.kfUpdateElevatorPicture(), elevator1Pane.kfUpdateElevatorY());
        timeline.getKeyFrames().addAll(elevator2Pane.kfUpdateElevatorPicture(), elevator2Pane.kfUpdateElevatorY());
        timeline.getKeyFrames().addAll(elevator1Information.kfUpdateInformation(), elevator2Information.kfUpdateInformation());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        HBox hbox = new HBox(elevator1Pane, elevator2Pane, elevator1Information, elevator2Information);
        hbox.setSpacing(0);

        Scene scene = new Scene(hbox, 800, 880);
        mainStage.setResizable(true);
        mainStage.setScene(scene);
        mainStage.show();
    }



    private static void elevatorRun(Elevator elevator) {
        while (true) {
            elevator.moveTo(Simulation.returnRandomIndexFromRange(0, 7));
            Simulation.delay(3);
            //elevator.checkRequests();
            //elevator.moveTo(elevator.getPath[0]);
            //elevator.unloadPassengers();
            //elevator.loadPassengers();
        }
    }

    }

    class ElevatorPane extends Pane{
        private static final Image imgOpenDoors = new Image("images/elevatorOpen.png");
        private static final Image imgChangingDoors = new Image("images/elevatorChanging.png");
        private static final Image imgClosedDoors = new Image("images/elevatorClosed.png");

        private final ObjectProperty<Image> elevatorDoorsPictureProperty = new SimpleObjectProperty<>(imgClosedDoors);
        private final SimpleIntegerProperty currentFloorYProperty;
        private final ImageView imageView;
        private final Elevator elevator;

        public ElevatorPane(Elevator elevator) {
            this.elevator = elevator;
            currentFloorYProperty = new SimpleIntegerProperty(Math.abs((this.elevator.getCurrentFloor() * 110) - 770));

            imageView = new ImageView();
            imageView.setFitHeight(110);
            imageView.setFitWidth(100);
            imageView.yProperty().bindBidirectional(currentFloorYProperty);
            imageView.imageProperty().bindBidirectional(elevatorDoorsPictureProperty);

            getChildren().add(imageView);
            setMaxSize(100, 880);
            setPrefSize(100, 880);
            setMinSize(100, 880);
            setBackground(Background.fill(Color.LIGHTSKYBLUE));

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
            });
            return keyframe;
            }
    }


    class ElevatorInformationBox extends GridPane {
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
            imageViewDoorsStatus.setFitHeight(30);
            imageViewDoorsStatus.setFitWidth(100);
            imageViewElevatorStatus.setFitHeight(40);
            imageViewElevatorStatus.setFitWidth(28);
            imageViewElevatorLevel.setFitHeight(40);
            imageViewElevatorLevel.setFitWidth(28);


            add(imageViewDoorsStatus, 0, 0,2, 1);
            add(imageViewElevatorStatus, 0, 1, 1, 1);
            add(imageViewElevatorLevel, 1, 1, 1, 1);

            setHalignment(imageViewElevatorStatus, HPos.RIGHT);
            setHalignment(imageViewElevatorLevel, HPos.LEFT);

            // Modifying the settings of the grid and adding the signs
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPrefWidth(45);
            ColumnConstraints column2 = new ColumnConstraints();
            column2.setPrefWidth(45);
            getColumnConstraints().addAll(column1, column2);

            setVgap(10);
            setHgap(10);

            setMaxSize(100, 80);
            setMinSize(100, 80);
            setPrefSize(100, 80);
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