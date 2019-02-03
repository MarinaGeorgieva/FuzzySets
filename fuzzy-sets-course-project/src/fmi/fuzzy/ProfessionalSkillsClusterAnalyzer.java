package fmi.fuzzy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fmi.fuzzy.clustering.FuzzyCMeansClusterer;
import fmi.fuzzy.utils.CsvFileReader;

public class ProfessionalSkillsClusterAnalyzer {

	private static final String FILE_PATH = "data/FAscores_4x4skills.csv";
	
	private static final int MIN_CLUSTERS = 2;
	private static final int MAX_CLUSTERS = 20;
	
	public static void main(String[] args) {
		List<List<Double>> data = null;
		try {
			data = CsvFileReader.readData(FILE_PATH);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<List<Double>> soft = new ArrayList<>();
		List<List<Double>> middle = new ArrayList<>();
		List<List<Double>> hard = new ArrayList<>();
		for (List<Double> dataPoint : data) {
			soft.add(dataPoint.subList(8, 12));
			middle.add(dataPoint.subList(0, 4));
			hard.add(dataPoint.subList(4, 8));
		}
		
		analyzeClusters(soft);
		
	}
	
	private static void analyzeClusters(List<List<Double>> dataPoints) {
		for (int c = MIN_CLUSTERS; c <= MAX_CLUSTERS; c++) {
			FuzzyCMeansClusterer fuzzyCMeansClusterer = new FuzzyCMeansClusterer(c, 1000, 2);
			fuzzyCMeansClusterer.cluster(dataPoints);
	
		}
	}
}
