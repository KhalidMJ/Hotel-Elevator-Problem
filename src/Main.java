import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
    private static Hotel hotel;
    private static Elevator elevator1;
    private static Elevator elevator2;
    private static final String passengersFilePath = "src/Passengers.csv";

    public static void main(String[] args) {
        // Creating the simulation main objects
        hotel = new Hotel(8, 2);
        elevator1 = hotel.getElevators()[0];
        elevator2 = hotel.getElevators()[1];

        Simulation.setStartTime();
        launch(args);
    }

    @Override
    public void start(Stage mainStage) {

        // Creating the home page interface
        ProgramInterface programInterface = new ProgramInterface(passengersFilePath);

        // Importing the CSS file
        String css = this.getClass().getResource("styles.css").toExternalForm();

        // Creating the home page scene
        Scene homeScene = new Scene(programInterface, programInterface.getPrefWidth(), programInterface.getPrefHeight());
        homeScene.getStylesheets().add(css);

        // Setting the stage and showing the scene
        mainStage.setScene(homeScene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static Scene createSimulationScene(){
        // Creating elevator panes
        ElevatorPane elevator1Pane = new ElevatorPane(elevator1);
        ElevatorPane elevator2Pane = new ElevatorPane(elevator2);

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

        // Creating the simulation statistics box
        SimulationStatistics simulationStatistics = new SimulationStatistics();

        // Creating the clock
        ClockPane clockPane = new ClockPane(12, 0, 0);

        ResultsTablePane resultsTablePane = new ResultsTablePane();

        // The main timeline for updates and animations
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(elevator1Pane.kfUpdateElevatorPicture(), elevator1Pane.kfUpdateElevatorY(), elevator1Pane.kfUpdateDisplayInformation());
        timeline.getKeyFrames().addAll(elevator2Pane.kfUpdateElevatorPicture(), elevator2Pane.kfUpdateElevatorY(), elevator2Pane.kfUpdateDisplayInformation());
        timeline.getKeyFrames().addAll(floor0Pane.kfUpdateFloor(), floor1Pane.kfUpdateFloor(), floor2Pane.kfUpdateFloor(),
                floor3Pane.kfUpdateFloor(), floor4Pane.kfUpdateFloor(), floor5Pane.kfUpdateFloor(),
                floor6Pane.kfUpdateFloor(), floor7Pane.kfUpdateFloor());
        timeline.getKeyFrames().addAll(simulationStatistics.kfUpdateAverageWaitingTime());
        timeline.getKeyFrames().add(clockPane.kfUpdateTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        HBox infoHBox = new HBox(clockPane, simulationStatistics);
        VBox infoVbox = new VBox(infoHBox, resultsTablePane);
        HBox hbox = new HBox(infoVbox, elevator1Pane, elevator2Pane, floorsVBox);
        hbox.setSpacing(0);
        Scene simulationScene = new Scene(hbox, 1200, 880);
        String css = Main.class.getResource("styles.css").toExternalForm();
        simulationScene.getStylesheets().add(css);
        return simulationScene;
    }

    public static void startSimulation(String algorithm) {
        // Running the main loop in a background thread to split it from the GUI Thread, hence we can update the GUI in real time.
        new Thread(() -> Main.elevatorRun(elevator1, algorithm)).start();
        new Thread(() -> Main.elevatorRun(elevator2, algorithm)).start();
        new Thread(Main::mainLoop).start();
    }

    protected static void elevatorRun(Elevator elevator, String algorithm) {
        // start the elevator and run the simulation
        elevator.start(algorithm);
    }

    private static void mainLoop() {
        // Exporting the passengers data from the csv file, processing the data and storing it in the hotel object.
        try {
        String[][] passengersData = Simulation.getPassengersFromFile(passengersFilePath);
        Passenger[] passengers = Simulation.turnPassengersArrayIntoObjects(passengersData, hotel);
        passengers = Simulation.sortPassengersByArrivalTime(passengers);
        hotel.setPassengers(passengers);
        } catch (Exception e){
        System.out.println(e.getMessage());
        }
        Simulation.setStartTime();
        // update the passengers on each floor
        hotel.updateFloorPassengers();
    }
}

// GUI Classes ----------------------------------------------------
    class ProgramInterface extends GridPane {
    private final StackPane titlePane;
    private final StackPane generatingPassengersPane;
    private final StackPane simulationStartPane;

    private final Label titleLabel;
    private final Label generatePassengersLabel;
    private final GridPane gpGridPane;
    private final Label passengersCountLabel;
    private final Label firstPassengerArrivelTimeLabel;
    private final Label lastPassengerArrivelTimeLabel;
    private final TextField passengersCountTextField;
    private final TextField firstArrivalTimeTextField;
    private final TextField lastArrivalTextField;
    private final Button generatePassengersButton;
    private final Label GRPerrorLabel;

    private final GridPane sspGridPane;
    private final Label selectAlgorithmLabel;
    private final ToggleGroup algorithmToggleGroup;
    private final RadioButton algorithm1RadioButton;
    private final RadioButton algorithm2RadioButton;
    private final Button startSimulationButton;
    private Label SSPerrorLabel;

    public ProgramInterface(String passengersFilePath){
        super();
        // Styling the main GridPane
        getStyleClass().add("initial-scene");
        setAlignment(Pos.CENTER);
        setPadding(new Insets(60));
        setHgap(20);
        setVgap(30);
        setPrefSize(900, 450);
        setMinSize(900, 450);
        setMaxSize(900, 450);

        // Title pane
        titlePane = new StackPane();
        titlePane.getStyleClass().add("title-pane");
        titlePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setVgrow(titlePane, Priority.ALWAYS);
        setHgrow(titlePane, Priority.ALWAYS);

        titleLabel= new Label("Hotel Elevator Problem");
        titlePane.getChildren().add(titleLabel);

        // Generating Passengers Pane
        generatingPassengersPane = new StackPane();
        generatingPassengersPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setVgrow(generatingPassengersPane, Priority.ALWAYS);
        setHgrow(generatingPassengersPane, Priority.ALWAYS);
        generatingPassengersPane.getStyleClass().add("settings-box");

        gpGridPane = new GridPane();
        gpGridPane.getStyleClass().add("settings-box");
        generatePassengersLabel = new Label("Generate Random Passengers");
        generatePassengersLabel.getStyleClass().add("settings-title");
        passengersCountLabel = new Label("Total Number of Passengers");
        firstPassengerArrivelTimeLabel = new Label("Arrival Time of First Passenger");
        lastPassengerArrivelTimeLabel = new Label("Arrival Time of Last Passenger");
        passengersCountLabel.getStyleClass().add("settings-label");
        firstPassengerArrivelTimeLabel.getStyleClass().add("settings-label");
        lastPassengerArrivelTimeLabel.getStyleClass().add("settings-label");
        passengersCountTextField = new TextField();
        firstArrivalTimeTextField = new TextField();
        lastArrivalTextField = new TextField();
        generatePassengersButton = new Button("Generate Passengers");
        GRPerrorLabel = new Label();
        GRPerrorLabel.setFont(Font.font(15));

        passengersCountTextField.setPromptText("Enter a positive integer");
        firstArrivalTimeTextField.setPromptText("In seconds");
        lastArrivalTextField.setPromptText("in seconds");

        gpGridPane.setAlignment(Pos.TOP_CENTER);
        setHalignment(generatePassengersLabel, HPos.CENTER);
        gpGridPane.add(generatePassengersLabel, 0, 0, 2, 1);
        gpGridPane.add(passengersCountLabel, 0, 1);
        gpGridPane.add(passengersCountTextField, 1, 1);
        gpGridPane.add(firstPassengerArrivelTimeLabel, 0, 2);
        gpGridPane.add(firstArrivalTimeTextField, 1, 2);
        gpGridPane.add(lastPassengerArrivelTimeLabel, 0, 3);
        gpGridPane.add(lastArrivalTextField, 1, 3);
        gpGridPane.add(generatePassengersButton, 1, 4);
        gpGridPane.add(GRPerrorLabel, 0, 5, 2, 1);
        gpGridPane.setHgap(10);
        gpGridPane.setVgap(10);

        generatePassengersButton.setOnAction(event -> {
            try {
                int passengersCount = Integer.parseInt(passengersCountTextField.getText());
                long firstArrivalTime = Long.parseLong(firstArrivalTimeTextField.getText());
                long lastArrivalTime = Long.parseLong(lastArrivalTextField.getText());
                if (passengersCount > 0 && firstArrivalTime >= 0 && lastArrivalTime >= 0 && firstArrivalTime < lastArrivalTime){
                    Simulation.generateRandomPassengersToFile(passengersCount, firstArrivalTime, lastArrivalTime, passengersFilePath);
                    GRPerrorLabel.setText("Passengers Generated Successfully");
                    GRPerrorLabel.setTextFill(Color.GREEN);
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e3 -> {
                                GRPerrorLabel.setText("");
                            })
                    );
                    timeline.setCycleCount(1);
                    timeline.play();
                } else {
                    // showing a label under the button to inform the user that the input is not valid, then removing it after 2 seconds
                    GRPerrorLabel.setText("Please enter valid inputs");
                    GRPerrorLabel.setTextFill(Color.RED);
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(3), e4 -> {
                                GRPerrorLabel.setText("");
                            })
                    );
                    timeline.setCycleCount(1);
                    timeline.play();
                }
            } catch (Exception e){
                // showing a label under the button to inform the user that the input is not valid, then removing it after 2 seconds
                GRPerrorLabel.setText("Please enter valid inputs");
                GRPerrorLabel.setTextFill(Color.RED);
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(3), e5 -> {
                            GRPerrorLabel.setText("");
                        })
                );
                timeline.setCycleCount(1);
                timeline.play();
            }
        });

        generatingPassengersPane.getChildren().add(gpGridPane);

        // Simulation Start Pane
        simulationStartPane = new StackPane();
        simulationStartPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setVgrow(simulationStartPane, Priority.ALWAYS);
        setHgrow(simulationStartPane, Priority.ALWAYS);

        sspGridPane = new GridPane();
        sspGridPane.getStyleClass().add("settings-box");
        selectAlgorithmLabel = new Label("Select Elevator Algorithm");
        selectAlgorithmLabel.getStyleClass().add("settings-title");

        algorithmToggleGroup = new ToggleGroup();
        algorithm1RadioButton = new RadioButton("SCAN (phase 1)");
        algorithm1RadioButton.getStyleClass().add("settings-label");
        algorithm1RadioButton.setToggleGroup(algorithmToggleGroup);
        algorithm2RadioButton = new RadioButton("C-LOOK (phase 2)");
        algorithm2RadioButton.getStyleClass().add("settings-label");
        algorithm2RadioButton.setToggleGroup(algorithmToggleGroup);
        SSPerrorLabel = new Label();
        SSPerrorLabel.setTextFill(Color.RED);
        SSPerrorLabel.setFont(Font.font(13));


        startSimulationButton = new Button("Start Simulation");
        startSimulationButton.setOnAction(event -> {
            if (algorithmToggleGroup.getSelectedToggle() == algorithm1RadioButton){
                Main.startSimulation("SCAN");
                Stage mainStage = (Stage) startSimulationButton.getScene().getWindow();
                mainStage.setScene(Main.createSimulationScene());
            } else if (algorithmToggleGroup.getSelectedToggle() == algorithm2RadioButton){
                Main.startSimulation("C-LOOK");
                Stage mainStage = (Stage) startSimulationButton.getScene().getWindow();
                mainStage.setScene(Main.createSimulationScene());

            } else {
                // showing a label under the button to inform the user that no algorithm is selected, then removing it after 2 seconds
                SSPerrorLabel.setText("Please select an algorithm");
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(3), e7 -> {
                            SSPerrorLabel.setText("");
                        })
                );
                timeline.setCycleCount(1);
                timeline.play();
            }
        });

        sspGridPane.setAlignment(Pos.TOP_CENTER);
        setHalignment(selectAlgorithmLabel, HPos.CENTER);
        setHalignment(startSimulationButton, HPos.CENTER);
        sspGridPane.add(selectAlgorithmLabel, 0, 0);
        sspGridPane.add(algorithm1RadioButton, 0, 1);
        sspGridPane.add(algorithm2RadioButton, 0, 2);
        sspGridPane.add(startSimulationButton, 0, 3, 2, 1);
        sspGridPane.add(SSPerrorLabel, 0, 4, 2, 1);
        sspGridPane.setVgap(20);

        simulationStartPane.getChildren().add(sspGridPane);

        // Adding the StackPanes to the GridPane
        add(titlePane, 0, 0, 2, 1);
        add(generatingPassengersPane, 0, 1);
        add(simulationStartPane, 1, 1);
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

    class SimulationStatistics extends GridPane {
        private final SimpleLongProperty averageWaitingTimeProperty;
        private final Label titleLabel;
        private final Label averageWaitingTimeLabel;
        public SimulationStatistics() {
            super();
            // setting the properties of the grid pane
            setAlignment(Pos.CENTER);
            setPadding(new Insets(10));
            setVgap(5);
            setMaxSize(300, 150);
            setPrefSize(300, 150);
            setMinSize(300, 150);

            getStyleClass().add("statistics-box");
            // Creating the property and initializing it
            averageWaitingTimeProperty = new SimpleLongProperty(Simulation.getAverageWaitingTime());
            // Creating the label and binging it to the property
            averageWaitingTimeLabel = new Label();
            averageWaitingTimeLabel.textProperty().bind((averageWaitingTimeProperty).asString().concat(" seconds"));
            titleLabel = new Label("Average Waiting Time");


            // Styling the labels
            averageWaitingTimeLabel.getStyleClass().add("label-waitingTime");
            titleLabel.getStyleClass().add("label-waitingTime");
            setHalignment(averageWaitingTimeLabel, HPos.CENTER);

            // Adding the label to the stack pane
            add(titleLabel, 0, 0, 2, 1);
            add(averageWaitingTimeLabel, 0, 1,2, 1 );
        }
        // Method to update the average waiting time
        public KeyFrame kfUpdateAverageWaitingTime() {
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.3), event -> {
                averageWaitingTimeProperty.set(Simulation.getAverageWaitingTime());
                if (Simulation.isSimEnded()){
                    // change its color
                    averageWaitingTimeLabel.setTextFill(Color.GREEN);
                    titleLabel.setTextFill(Color.GREEN);
                }
            });
            return keyframe;
        }
    }

    class ClockPane extends StackPane{
        private final int startHour;
        private final int startMinute;
        private final int startSecond;
        private long currentTime;

        private Label lblHour;
        private Label lblMinute;
        private Label lblSecond;

        private SimpleLongProperty timeSecondsProperty;
        private SimpleLongProperty timeMinutesProperty;
        private SimpleLongProperty timeHoursProperty;

        public ClockPane(int startHour, int startMinute, int startSecond) {
            super();
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.startSecond = startSecond;

            getStyleClass().add("clock-box");
            // Set current time
            currentTime = Simulation.getElapsedTime();
            timeSecondsProperty = new SimpleLongProperty(currentTime + startSecond);
            timeMinutesProperty = new SimpleLongProperty((currentTime / 60) + startMinute);
            timeHoursProperty = new SimpleLongProperty((currentTime / 3600) + startHour);

            // Create labels for displaying time
            lblHour = new Label();
            lblMinute = new Label();
            lblSecond = new Label();
            Label colon1 = new Label(":");
            Label colon2 = new Label(":");
            Label pm = new Label("PM");

            // Set labels to display initial time
            lblHour.textProperty().bind(timeHoursProperty.asString());
            lblMinute.textProperty().bind(timeMinutesProperty.asString());
            lblSecond.textProperty().bind(timeSecondsProperty.asString());


            // Styling the labels
            lblHour.getStyleClass().add("label-clock");
            lblMinute.getStyleClass().add("label-clock");
            lblSecond.getStyleClass().add("label-clock");
            colon1.getStyleClass().add("label-clock");
            colon2.getStyleClass().add("label-clock");
            pm.getStyleClass().add("label-clock");


            // Display time in a digital clock format in a HBox
            HBox hbox = new HBox();
            hbox.getChildren().addAll(lblHour, colon1, lblMinute, colon2, lblSecond, pm);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(1);

            setMaxSize(300, 150);
            setPrefSize(300, 150);
            setMinSize(300, 150);
            getChildren().add(hbox);

        }

        public KeyFrame kfUpdateTime() {
            KeyFrame keyframe = new KeyFrame(Duration.seconds(0.5), event -> {
                currentTime = Simulation.getElapsedTime();
                timeSecondsProperty.set((currentTime) % 60);
                timeMinutesProperty.set(((currentTime) / 60) % 60 + + this.startMinute);
                timeHoursProperty.set(((currentTime) / 3600) % 24 + this.startHour);
            });
            return keyframe;
        }
    }

    class ResultsTablePane extends StackPane {
        private final TableView<Passenger> table;
        private final TableColumn<Passenger, String> nameColumn;
        private final TableColumn<Passenger, String> idColumn;
        private final TableColumn<Passenger, String> currentFloorColumn;
        private final TableColumn<Passenger, String> destinationFloorColumn;
        private final TableColumn<Passenger, String> waitingTimeColumn;
        private static ObservableList<Passenger> data;



        public ResultsTablePane(){
            super();
            data = FXCollections.observableArrayList();
            table = new TableView<>();
            table.setItems(data);
            table.setPrefSize(500, 650);
            table.setMaxSize(500, 650);
            table.setMinSize(500, 650);

            // Creating the columns
            nameColumn = new TableColumn<>("Name");
            idColumn = new TableColumn<>("ID");
            currentFloorColumn = new TableColumn<>("Departure Floor");
            destinationFloorColumn = new TableColumn<>("Destination Floor");
            waitingTimeColumn = new TableColumn<>("Waiting Time");

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            currentFloorColumn.setCellValueFactory(new PropertyValueFactory<>("currentFloor"));
            destinationFloorColumn.setCellValueFactory(new PropertyValueFactory<>("destinationFloor"));
            waitingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

            table.getColumns().addAll(nameColumn, idColumn, currentFloorColumn, destinationFloorColumn, waitingTimeColumn);
            table.getStyleClass().add("label-table");

            // Setting the columns size
            nameColumn.setPrefWidth(100);
            idColumn.setPrefWidth(60);
            currentFloorColumn.setPrefWidth(120);
            destinationFloorColumn.setPrefWidth(120);
            waitingTimeColumn.setPrefWidth(100);

            setMaxSize(600, 730);
            setPrefSize(600, 730);
            setMinSize(600, 730);
            getStyleClass().add("table-box");
            getChildren().add(table);
        }

        public static void updateTable(Passenger passenger){
            data.add(passenger);
        }
    }