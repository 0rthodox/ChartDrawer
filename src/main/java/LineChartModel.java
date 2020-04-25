import javafx.util.Pair;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LineChartModel {
    public List<Pair<? extends Number, ? extends Number>> readData(File file) throws IOException {
        final List<String> lines = FileUtils.readAll(file);
        return lines.stream().map(line -> {
            String[] splitted = line.split(" ");
            return new Pair<>(Double.valueOf(splitted[0]), Double.valueOf(splitted[1]));
        }).collect(Collectors.toList());
    }
}
