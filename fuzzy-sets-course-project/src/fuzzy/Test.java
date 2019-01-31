package fuzzy;

import java.io.IOException;
import java.util.List;

public class Test {
	
	private final static String FILE_PATH = "data/FAscores_4x4skills.csv";
//	private final static String FILE_PATH = "data/test.csv";

	public static void main(String[] args) {
		List<List<Double>> data = null;
		try {
			data = CsvFileReader.readData(FILE_PATH);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		data.stream().forEach(System.out::println);
	}
}
