package fmi.fuzzy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fmi.fuzzy.clustering.FuzzyCMeansClusterer;
import fmi.fuzzy.utils.CsvFileReader;

public class ProfessionalSkillsClusterAnalyzer {

	private static final String FILE_PATH = "data/FAscores_4x4skills.csv";
	
	private static final int MIN_CLUSTERS = 2;
	private static final int MAX_CLUSTERS = 30;
	
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
		
		// Normalize datasets
		System.out.println("---------- Results for soft skills ----------");
		soft = normalizeData(soft);
		analyzeClusters(soft);
		System.out.println();
		
		System.out.println("---------- Results for middle skills ----------");
		middle = normalizeData(middle);
		analyzeClusters(middle);
		System.out.println();
		
		System.out.println("---------- Results for hard skills ----------");
		hard = normalizeData(hard);
		analyzeClusters(hard);
		System.out.println();
	}
	
	private static void analyzeClusters(List<List<Double>> dataPoints) {
		for (int c = MIN_CLUSTERS; c <= MAX_CLUSTERS; c++) {
			FuzzyCMeansClusterer fuzzyCMeansClusterer = new FuzzyCMeansClusterer(c, 1000, 2);
			fuzzyCMeansClusterer.cluster(dataPoints);
			double awcd = fuzzyCMeansClusterer.getAverageWithinClusterDistance();
//			System.out.println("AWCD for number of clusters = " + c + " is " + awcd);
			System.out.println(awcd);
		}
	}
	
	private static List<List<Double>> normalizeData(List<List<Double>> data) {
		List<List<Double>> normalizedData = new ArrayList<>();
		
		int size = data.size();
		double[] min = new double[size];
		double[] max = new double[size];
		for (int i = 0; i < data.size(); i++) {
			min[i] = data.get(i).get(0);
			max[i] = data.get(i).get(0);
			for (int j = 0; j < data.get(i).size(); j++) {
				if (data.get(i).get(j) < min[i]) {
					min[i] = data.get(i).get(j);
				}
				
				if (data.get(i).get(j) > max[i]) {
					max[i] = data.get(i).get(j);
				}
			}
			
			List<Double> normalizedPoint = new ArrayList<>();			
			for (int j = 0; j < data.get(i).size(); j++) {
				double normalizedVal = (data.get(i).get(j) - min[i]) / (max[i] - min[i]);
				normalizedPoint.add(normalizedVal);
			}
			normalizedData.add(normalizedPoint);
		}
		return normalizedData;
	}
}
