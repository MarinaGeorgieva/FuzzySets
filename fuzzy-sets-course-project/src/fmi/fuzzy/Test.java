package fmi.fuzzy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fmi.fuzzy.clustering.FuzzyCMeansClusterer;
import fmi.fuzzy.utils.CsvFileReader;

public class Test {
	
//	private final static String FILE_PATH = "data/FAscores_4x4skills.csv";
	private final static String FILE_PATH = "data/test.csv";

	public static void main(String[] args) {
		List<List<Double>> data = null;
		try {
			data = CsvFileReader.readData(FILE_PATH);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		data.stream().forEach(System.out::println);
		
		List<List<Double>> soft = new ArrayList<>();
		List<List<Double>> middle = new ArrayList<>();
		List<List<Double>> hard = new ArrayList<>();
		
		for (List<Double> dataPoint : data) {
			soft.add(dataPoint.subList(8, 12));
			middle.add(dataPoint.subList(0, 4));
			hard.add(dataPoint.subList(4, 8));
		}
		
//		System.out.println("Soft");
//		soft.stream().forEach(System.out::println);
//		System.out.println("Middle");
//		middle.stream().forEach(System.out::println);
//		System.out.println("Hard");
//		hard.stream().forEach(System.out::println);
		
		for (int c = 2; c <= 3; c++) {
			FuzzyCMeansClusterer fuzzyCMeansClusterer = new FuzzyCMeansClusterer(c, 1000, 2);
			fuzzyCMeansClusterer.cluster(soft);
			System.out.println("Results for " + c + " clusters");
			System.out.println("Membership matrix:");
			printMembershipMatrix(fuzzyCMeansClusterer.getMembershipMatrix());
		}
	}
	
	private static void printMembershipMatrix(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[i].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.println();
		}
	}
}
