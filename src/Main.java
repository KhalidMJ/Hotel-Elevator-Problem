import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

        // Running the main loop in a background thread to split it from the GUI Thread, hence we can update the GUI in real time.
        new Thread(() -> Main.elevatorRun(elevator1)).start();
        new Thread(() -> Main.elevatorRun(elevator2)).start();

        HBox hbox = new HBox(elevator1Pane, elevator2Pane);
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

            Timeline timeline = startTimeline(elevator);
            timeline.play();
        }

        private Timeline startTimeline(Elevator elevator) {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.2), event -> {
                        if (elevator.getDoorsStatus() == DoorsStatus.CLOSED){
                            elevatorDoorsPictureProperty.set(imgClosedDoors);
                        } else if (elevator.getDoorsStatus() == DoorsStatus.OPEN){
                            elevatorDoorsPictureProperty.set(imgOpenDoors);
                        } else {
                            elevatorDoorsPictureProperty.set(imgChangingDoors);
                        }

                    }),
                    new KeyFrame(Duration.seconds(0.2), event -> {
                        currentFloorYProperty.set(Math.abs((elevator.getCurrentFloor() * 110) - 770)); // TODO: find a way to make the animation smoother
                    })
            );

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setRate(1);
            return timeline;
        }
    }