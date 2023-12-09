import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
            //Simulation.generateRandomPassengersToFile(80, 5, 700, path);
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

        // Combining each elevator pane with its information box into a single VBox
        VBox elevator1Vbox = new VBox(elevator1Pane, elevator1Information);
        VBox elevator2Vbox = new VBox(elevator2Pane, elevator2Information);

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
        timeline.getKeyFrames().addAll(elevator1Pane.kfUpdateElevatorPicture(), elevator1Pane.kfUpdateElevatorY());
        timeline.getKeyFrames().addAll(elevator2Pane.kfUpdateElevatorPicture(), elevator2Pane.kfUpdateElevatorY());
        timeline.getKeyFrames().addAll(elevator1Information.kfUpdateInformation(), elevator2Information.kfUpdateInformation());
        timeline.getKeyFrames().addAll(floor0Pane.kfUpdateFloor(), floor1Pane.kfUpdateFloor(), floor2Pane.kfUpdateFloor(),
                                        floor3Pane.kfUpdateFloor(), floor4Pane.kfUpdateFloor(), floor5Pane.kfUpdateFloor(),
                                        floor6Pane.kfUpdateFloor(), floor7Pane.kfUpdateFloor());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        HBox hbox = new HBox(elevator1Vbox, elevator2Vbox, floorsVBox);
        hbox.setSpacing(0);

        Scene scene = new Scene(hbox, 800, 960);
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
            imageView.setFitHeight(90);
            imageView.setFitWidth(60);
            imageView.yProperty().bindBidirectional(currentFloorYProperty);
            imageView.imageProperty().bindBidirectional(elevatorDoorsPictureProperty);

            Line line = new Line();
            line.setStartX(50);
            line.setEndX(50);
            line.setStartY(0);
            line.setEndY(880);
            line.setStroke(Color.grayRgb(30));
            line.setFill(Color.GREENYELLOW);
            line.setStrokeWidth(5);

            getChildren().add(line);
            getChildren().add(imageView);
            setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            setMaxSize(100, 880);
            setPrefSize(100, 880);
            setMinSize(100, 880);
            setBackground(Background.fill(Color.GRAY));

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

    class FloorPane extends Pane{
        private final SimpleIntegerProperty waitingPassengersProperty;
        private final ImageView imageView;
        private final Floor floor;
        private final Label waitingPassengersCount;

        public FloorPane(Floor floor, Image image) {
            this.floor = floor;

            waitingPassengersProperty = new SimpleIntegerProperty(floor.getWaitingPassengersCount());
            waitingPassengersCount = new Label();
            waitingPassengersCount.textProperty().bind(waitingPassengersProperty.asString());
            waitingPassengersCount.setLayoutX(30);
            waitingPassengersCount.setLayoutY(30);
            waitingPassengersCount.setFont(Font.font(30));


            imageView = new ImageView(image);
            imageView.setFitWidth(400);
            imageView.setFitHeight(110);


            getChildren().addAll(imageView, waitingPassengersCount);
            setMaxSize(400, 110);
            setPrefSize(400, 110);
            setMinSize(400, 110);
            setBackground(Background.fill(Color.GRAY));
        }

        public KeyFrame kfUpdateFloor() {
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.2), event -> {
                waitingPassengersProperty.set(floor.getWaitingPassengersCount());
            });
            return keyframe;
        }
    }