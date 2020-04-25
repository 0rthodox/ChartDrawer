import javafx.util.Pair;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LineChartModel {
    private final static String SEPARATOR = " ";

    public List<Pair<? extends Number, ? extends Number>> readData(File file) throws IOException {
        final List<String> lines = FileUtils.readAll(file);
        return lines.stream().map(line -> {
            String[] split = line.split(SEPARATOR);
            if (split.length != 2) {
                throw new IllegalArgumentException("Wrong amount of arguments");
            }
            return new Pair<>(Double.valueOf(split[0]), Double.valueOf(split[1]));
        }).collect(Collectors.toList());
    }
}
