package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Main extends Application { 
    
    private Axes axes;
    private Plot plot;
    private StackPane cartPlane;
    private BorderPane root;
    private Scene scene;
    private FlowPane south;
    private Button btnPlot;
    private Label labFun;
    private TextField tfFunction;
    
    public final String djvfont = "DejaVu Sans";
    public final String flabl = "f(x) = ";
    public final String butTex = "Draw Graph";
    public final String title = "Function Graph";
    public final String cssFile = "application.css";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {  
        axes = new Axes(800, 600, -10, 10, 1, -10, 10, 1);
        plot = new Plot(x -> x, 0, 0, 0.1, axes);
        
        cartPlane = new StackPane(plot);
        south = new FlowPane(Orientation.HORIZONTAL, 15, 15);
        root = new BorderPane();
        
        btnPlot = new Button(butTex);
        setPlotFunctionAction();
        
        labFun = new Label(flabl);
        labFun.setFont(new Font(djvfont, 14));
        
        tfFunction = new TextField();
        tfFunction.setFont(new Font(djvfont, 14));

        south.getChildren()
             .addAll(labFun, tfFunction, btnPlot);
        south.setPadding(new Insets(5));
        //south.setStyle("-fx-background-color: #F4F4F4;");
        
        root.setTop(cartPlane);
        root.setBottom(south);
        root.setPadding(new Insets(20));
        
        scene = new Scene(root, Color.rgb(35, 39, 50));
        scene.getStylesheets().add(getClass()
                              .getResource(cssFile)
                              .toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    private void setPlotFunctionAction() {
        btnPlot.setOnAction(e -> {
            if (tfFunction.getText().trim().length() != 0) {
                try {
                    Plot nplot = new Plot(x -> 
                         new ExpressionBuilder(tfFunction.getText())
                            .variables("x")
                            .build()
                            .setVariable("x", x)
                            .evaluate(), -10, 10, 0.1, axes);
                    cartPlane.getChildren().remove(0);
                    cartPlane.getChildren().add(nplot);
                } catch (IllegalArgumentException ex) {
                    displayError(ex);
                }
            }
        });         
    }
    
    private void displayError(IllegalArgumentException e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error has occurred!");
        alert.setContentText(e.getMessage());
        alert.showAndWait();    
    }
}
