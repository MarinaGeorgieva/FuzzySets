package fuzzy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FuzzyCMeans { 

	// The number of clusters 
	private final int c;
	
	// The convergence criteria
	private final double epsilon;
	
	private final int maxIterations;
	
	// The fuzziness factor
	private final double m;
	
	private final Random random;
	
	private double[][] membershipMatrix;
	
	private List<List<Double>> dataPoints;
	
	private List<List<Double>> clusters;

	public FuzzyCMeans(int c, double epsilon, int maxIterations, double m) {
		super();
		this.c = c;
		this.epsilon = epsilon;
		this.maxIterations = maxIterations;
		this.m = m;
		this.random = new Random();
		
		this.membershipMatrix = null;
		this.dataPoints = new ArrayList<>();
		this.clusters = new ArrayList<>();
	}
	
	public List<List<Double>> cluster(List<List<Double>> data) {
		int size = data.size();
		
		dataPoints = data;
		membershipMatrix = new double[size][c];
		
		initializeClusterCenters();
		double oldObjectiveFunction = 0.0; // calculateObjectiveFunction();
		
		int iteration = 0;
		double difference = 0.0;
		do {
			updateMembershipMatrix();
			updateClusterCenters();
            double newObjectiveFunction = calculateObjectiveFunction();
            difference = Math.abs(newObjectiveFunction - oldObjectiveFunction);
            oldObjectiveFunction = newObjectiveFunction;      
		} while (difference > epsilon && ++iteration < maxIterations);
		
		return clusters;
	}
	
	private void initializeClusterCenters() {
		for (int i = 0; i < c; i++) {
			List<Double> center = dataPoints.get(random.nextInt(dataPoints.size()));
			clusters.add(center);
		}
	}
	
	private void updateMembershipMatrix() {
		for (int i = 0; i < dataPoints.size(); i++) {
			for (int j = 0; j < clusters.size(); j++) {
				double sum = 0.0;
				double distanceA = calculateEuclideanDistance(dataPoints.get(i), clusters.get(j));
				for (List<Double> cluster : clusters) {
					double distanceB = calculateEuclideanDistance(dataPoints.get(i), cluster);
					if (distanceB == 0.0) {
						sum = Double.POSITIVE_INFINITY;
						break;
					}
					sum += Math.pow(distanceA / distanceB, 2.0 / (m - 1));
				}
				
				double membership;
                if (sum == 0.0) {
                    membership = 1.0;
                } else if (sum == Double.POSITIVE_INFINITY) {
                    membership = 0.0;
                } else {
                    membership = 1.0 / sum;
                }
                membershipMatrix[i][j] = membership;
			}
		}
	}
	
	private void updateClusterCenters() {
		List<List<Double>> newClusters = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			double[] newClusterCenter = new double[clusters.get(i).size()];
			double sum = 0.0;
			for (int j = 0; j < dataPoints.size(); j++) {
				double u = Math.pow(membershipMatrix[j][i], m);
				for (int idx = 0; idx < newClusterCenter.length; idx++) {
					newClusterCenter[idx] += u * dataPoints.get(j).get(idx);
				}
				sum += u;
			}
			List<Double> clusterCenterCoords = new ArrayList<>();
			for (int idx = 0; idx < newClusterCenter.length; idx++) {
				clusterCenterCoords.add(newClusterCenter[idx] / sum);
			}
			newClusters.add(clusterCenterCoords);
		}
		clusters.clear();
        clusters = newClusters;
	}
	
	private double calculateObjectiveFunction() {
		double objectiveFunction = 0.0;
		for (int i = 0; i < dataPoints.size(); i++) {
			for (int j = 0; j < clusters.size(); j++) {
				double distance = calculateEuclideanDistance(dataPoints.get(i), clusters.get(j));
				objectiveFunction += Math.pow(membershipMatrix[i][j], m) * distance * distance;
			}
		}
		return objectiveFunction;
	}
	
	private double calculateEuclideanDistance(List<Double> x, List<Double> y) {
		double distance = 0.0;
		for (int i = 0; i < x.size(); i++) {
			double xCoord = x.get(i);
			double yCoord = y.get(i);
			distance += (yCoord - xCoord) * (yCoord - xCoord);
		}	
		return Math.sqrt(distance);
	}	
}