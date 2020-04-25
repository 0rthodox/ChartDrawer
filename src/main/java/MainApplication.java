import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("ChartDrawer");
        Scene scene = new Scene(new LineChartView(stage));

        stage.setScene(scene);
        stage.show();
    }
}
