import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class LineChartView extends GridPane {
    private Button openChartBtn;
    private Chart chart;
    private ObservableList<XYChart.Series<Number, Number>> chartData = FXCollections.observableArrayList();
    private LineChartModel model = new LineChartModel();
    private ObjectProperty<ChartType> chartTypeProperty = new SimpleObjectProperty<>(ChartType.SCATTER);


    public LineChartView(Stage stage) {

        openChartBtn = new Button("Open");
        openChartBtn.setMinSize(100, 50);
        openChartBtn.setOnAction(e -> openButtonAction(stage));

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        chart = new ScatterChart<>(xAxis, yAxis, chartData);

        initControlPanel();

        // Rewriting chart on radiobutton click
        chartTypeProperty.addListener(((observable, oldValue, newValue) -> {
            switch (newValue) {
                case SCATTER:
                    chart = new ScatterChart<>(xAxis, yAxis, chartData);
                    break;
                case LINE:
                    chart = new LineChart<>(xAxis, yAxis, chartData);
                    break;
                case PIE:
                    chart = new PieChart(
                    FXCollections
                    .observableArrayList(
                    chartData
                    .get(0)
                    .getData()
                    .stream()
                    .map(data -> new PieChart.Data(
                    data.getXValue().toString(), data.getYValue().doubleValue()))
                    .collect(Collectors.toList())));
                    break;
            }
            add(new StackPane(chart), 0, 0);
            for (XYChart.Series<Number, Number> ser : chartData) {
                for (XYChart.Data<Number, Number> dat : ser.getData()) {
                    System.out.println(dat);
                }
            }

        }));
    }


    private void initControlPanel() {

        // Creating radio buttons
        RadioButton pieChartSelector = new RadioButton("Pie");
        RadioButton lineChartSelector = new RadioButton("Line");
        RadioButton scatterChartSelector = new RadioButton("Scatter");

        //Forming a toggle group
        ToggleGroup radioButtonsGroup = new ToggleGroup();
        pieChartSelector.setToggleGroup(radioButtonsGroup);
        lineChartSelector.setToggleGroup(radioButtonsGroup);
        scatterChartSelector.setToggleGroup(radioButtonsGroup);

        //Adding reactions
        pieChartSelector.setOnAction(event -> chartTypeProperty.setValue(ChartType.PIE));
        lineChartSelector.setOnAction(event -> chartTypeProperty.setValue(ChartType.LINE));
        scatterChartSelector.setOnAction(event -> chartTypeProperty.setValue(ChartType.SCATTER));

        //Constructing buttons layout
        FlowPane buttonsLayout = new FlowPane(openChartBtn, pieChartSelector, lineChartSelector, scatterChartSelector);
        add(new StackPane(chart), 0, 0);
        add(buttonsLayout, 0, 1);

    }


    void openButtonAction(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("./src/main/resources/data"));
        File file = fc.showOpenDialog(stage);

        if (file != null) {
            try {
                chartData
                .add(
                new XYChart.Series<>(
                FXCollections.observableArrayList(
                model
                .readData(file)
                .stream()
                .map(pair ->
                new XYChart.Data<Number, Number>(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList()))));
            } catch (NumberFormatException ex) {
                throwError("Некорректные данные", ex.getMessage());
            } catch (IOException ex) {
                throwError("Файл не прочитан", ex.getMessage());
            }
        }
    }
    void throwError(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
