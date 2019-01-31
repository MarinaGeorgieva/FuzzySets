package fuzzy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvFileReader {
	
	public static List<List<Double>> readData(String filePath) throws IOException {
		List<List<Double>> data = new ArrayList<List<Double>>();
		
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {		 
			data = stream.skip(1).map(line -> Arrays.asList(line.split(","))
					.stream().map(Double::valueOf).collect(Collectors.toList()))
			.collect(Collectors.toList());
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}