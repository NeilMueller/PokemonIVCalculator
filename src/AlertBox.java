import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String errorMsg){
        Stage window = new Stage();

        //Have to deal with this window
        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("INVALID INPUT");
        window.setMinWidth(200);

        Label label = new Label(errorMsg);
        Button exit = new Button("OK");
        exit.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, exit);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        //Have to close window
        window.showAndWait();
    }
}
