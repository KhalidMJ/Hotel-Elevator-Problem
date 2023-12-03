import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static Hotel hotel;
    private static Elevator elevator1;
    private static Elevator elevator2;

    public static void main(String[] args) {
        // Creating the simulation main objects -----------------------
        hotel = new Hotel(8, 2);
        elevator1 = hotel.getElevators()[0];
        elevator2 = hotel.getElevators()[1];

        launch(args);
    }

    @Override
    public void start(Stage mainStage) {

        // Creating elevator panes
        ElevatorPane elevator1Pane = new ElevatorPane(elevator1);
        ElevatorPane elevator2Pane = new ElevatorPane(elevator2);

        // Creating elevator information box
        ElevatorInformationBox elevator1Information = new ElevatorInformationBox(elevator1, "Elevator 1");
        ElevatorInformationBox elevator2Information = new ElevatorInformationBox(elevator2, "Elevator 2");
        // Running the main loop in a background thread to split it from the GUI Thread, hence we can update the GUI in real time.
        new Thread(() -> Main.elevatorRun(elevator1)).start();
        new Thread(() -> Main.elevatorRun(elevator2)).start();

        // The main timeline for updates and animations
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(elevator1Pane.kfUpdateElevatorPicture(), elevator1Pane.kfUpdateElevatorY());
        timeline.getKeyFrames().addAll(elevator2Pane.kfUpdateElevatorPicture(), elevator2Pane.kfUpdateElevatorY());
        timeline.getKeyFrames().addAll(elevator1Information.kfUpdateInformaiton(), elevator2Information.kfUpdateInformaiton());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setRate(1);
        timeline.play();

        HBox hbox = new HBox(elevator1Pane, elevator2Pane, elevator1Information, elevator2Information);
        hbox.setSpacing(0);

        Scene scene = new Scene(hbox, 100 * 2, 880);
        mainStage.setResizable(true);
        mainStage.setScene(scene);
        mainStage.show();
    }



    private static void elevatorRun(Elevator elevator) {
        while (true) {
            elevator.moveTo(5);
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
        private final SimpleStringProperty currentFloorProperty;
        private final SimpleStringProperty elevatorStatusProperty;
        private final SimpleStringProperty doorsStatusProperty;

        private final Label title;
        private final Label currentFloorText = new Label("Current Floor:");
        private final Label elevatorStatusText = new Label("Elevator Status:");
        private final Label doorsStatusText = new Label("Doors Status:");

        Elevator elevator;

        public ElevatorInformationBox(Elevator elev, String title){
            this.elevator = elev;
            this.title = new Label(title);

            currentFloorProperty = new SimpleStringProperty(""+elev.getCurrentFloor());
            elevatorStatusProperty = new SimpleStringProperty(""+elev.getElevatorStatus());
            doorsStatusProperty = new SimpleStringProperty(""+elev.getDoorsStatus());

            // Creating information labels and binding them to the properties
            Label elevatorCurrentFloor = new Label();
            Label elevatorStatus = new Label();
            Label elevatorDoorsStatus = new Label();
            elevatorCurrentFloor.textProperty().bind(currentFloorProperty);
            elevatorStatus.textProperty().bind(elevatorStatusProperty);
            elevatorDoorsStatus.textProperty().bind(doorsStatusProperty);

            // Label Styling
            this.title.setFont(Font.font(25));
            this.title.setAlignment(Pos.CENTER);
            this.title.setTextAlignment(TextAlignment.CENTER);

            currentFloorText.setFont(Font.font(20));
            elevatorStatusText.setFont(Font.font(20));
            doorsStatusText.setFont(Font.font(20));
            elevatorCurrentFloor.setFont(Font.font(20));
            elevatorStatus.setFont(Font.font(20));
            elevatorDoorsStatus.setFont(Font.font(20));

            currentFloorText.setPrefSize(200, 30);
            elevatorStatusText.setPrefSize(200, 30);
            doorsStatusText.setPrefSize(200, 30);
            elevatorCurrentFloor.setPrefSize(200, 30);
            elevatorStatus.setPrefSize(200, 30);
            elevatorDoorsStatus.setPrefSize(200, 30);

            elevatorCurrentFloor.setAlignment(Pos.CENTER_RIGHT);
            elevatorStatus.setAlignment(Pos.CENTER_RIGHT);
            elevatorDoorsStatus.setAlignment(Pos.CENTER_RIGHT);

            // Pane layout and style
            StackPane titlePane = new StackPane(this.title);
            titlePane.setBackground(Background.fill(Color.LIGHTSKYBLUE));


            add(titlePane,           0, 0, 2, 1);
            add(currentFloorText,     0, 1, 1, 1);
            add(elevatorStatusText,   0, 2, 1, 1);
            add(doorsStatusText,      0, 3, 1, 1);
            add(elevatorCurrentFloor, 1, 1, 1, 1);
            add(elevatorStatus,       1, 2, 1, 1);
            add(elevatorDoorsStatus,  1, 3, 1, 1);

            setVgap(20);
            setHgap(200);
            setBorder(Border.stroke(Color.LIGHTSKYBLUE));
            setMaxSize(450, 200);
            setMinSize(450, 200);
            setPrefSize(450, 200);

        }

        public KeyFrame kfUpdateInformaiton(){
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.2), event -> {
                currentFloorProperty.set(""+this.elevator.getCurrentFloor());
                elevatorStatusProperty.set(""+this.elevator.getElevatorStatus());
                doorsStatusProperty.set(""+this.elevator.getDoorsStatus());
            });
            return keyframe;
        }
    }