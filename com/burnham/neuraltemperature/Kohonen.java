package com.burnham.neuraltemperature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class Kohonen {
  private Node[][] map = new Node[Constants.MAP_SIZE][Constants.MAP_SIZE];

  public static void main(String args[]) {
    evaluateAverageQuality();
    // trainMapAndPrintAndEvaluate();
    // Kohonen net = new Kohonen();
    // trainMapAndPrint(net);
    // runInteractive(net);
  }

  private static void evaluateAverageQuality() {
    double count = 100;
    double qualitySum = 0;

    for(int i = 0; i < count; i++) {
      Kohonen net = Kohonen.trainNet();
      qualitySum += net.evaluateMap();
    }

    double averageQuality = qualitySum / count;
    System.out.println("average quality: " + averageQuality);
  }

  private static Kohonen trainNet() {
    Kohonen net = new Kohonen();
    for (int i = 0; i < Constants.TRAINING_ITERATIONS; i++) {
      net.train();
    }
    return net;
  }

  private static Kohonen trainMapAndPrintAndEvaluate() {
    Kohonen net = new Kohonen();
    trainMapAndPrint(net);

    double mapQuality = net.evaluateMap();
    System.out.format("map accuracy: %3.2f\n", mapQuality);
    return net;
  }

  private static void trainMapAndPrint(Kohonen net) {
    for (int i = 0; i < Constants.TRAINING_ITERATIONS; i++) {
      net.train();
    }
    System.out.println("done with training.");
    net.print();
  }

  private static void runInteractive(Kohonen net) {
    while (true) {
      System.out.println("\nenter temperature:");
      double temperatureInput = getUserInput();
      Node bestfit = net.findBestFit(temperatureInput);
      System.out.format("bestfit: %d,%d\nweight: %f\nfitness: %f\nlabel: %s\n", bestfit.getX(), bestfit.getY(),
          bestfit.getWeight(), bestfit.getFitness(temperatureInput), bestfit.getLabel());
    }
  }

  private static double getUserInput() {
    String input = "";
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      input = br.readLine();
    } catch (IOException e) {
      System.out.println(e);
      System.exit(1);
    }

    return Double.parseDouble(input);
  }

  public Kohonen() {
    for (int i = 0; i < Constants.MAP_SIZE; i++)
      for (int j = 0; j < Constants.MAP_SIZE; j++)
        map[i][j] = new Node(i, j);
  }

  public void train() {
    double randomTemperature = Util.getRandomTemperature();
    Node bestfit = findBestFit(randomTemperature);
    bestfit.setLabel(Util.getWaterState(randomTemperature));
    adjustMapFitness(bestfit, randomTemperature);
  }

  public Node findBestFit(double temperatureInput) {
    Node bestfit = null;
    double bestsofar = Double.MAX_VALUE;
    for (int i = 0; i < Constants.MAP_SIZE; i++)
      for (int j = 0; j < Constants.MAP_SIZE; j++) {
        double fitness = map[i][j].getFitness(temperatureInput);
        if (fitness < bestsofar) {
          bestsofar = fitness;
          bestfit = map[i][j];
        }
      }
    return bestfit;
  }

  public double evaluateMap() {
    double testCount = 0;
    double correctCount = 0;
    for(double temperature = Constants.MIN_TEMP; temperature <= Constants.MAX_TEMP; temperature++) {
      Node bestfit = findBestFit(temperature);
      testCount++;
      if(bestfit.getLabel() == Util.getWaterState(temperature)) {
        correctCount++;
      }
    }
    return correctCount / testCount;
  }

  public void print() {
    for (int i = 0; i < Constants.MAP_SIZE; i++) {
      for (int j = 0; j < Constants.MAP_SIZE; j++)
        System.out.format("%7s", map[i][j].getLabel());
      System.out.println();
      for (int j = 0; j < Constants.MAP_SIZE; j++)
        System.out.format("%7.2f", map[i][j].getWeight());
      System.out.println();
      for (int j = 0; j < Constants.MAP_SIZE; j++)
        System.out.format("%7d", map[i][j].getAdjustmentCount());
      System.out.println();
      System.out.println();
    }
    System.out.println();
  }

  private Optional<Node> getNodeAt(int x, int y) {
    Node node = null;
    if (x >= 0 && x < Constants.MAP_SIZE && y >= 0 && y < Constants.MAP_SIZE) {
      node = map[x][y];
    }
    return Optional.ofNullable(node);
  }

  private void adjustMapFitness(Node bestFit, double temperature) {
    int x = bestFit.getX();
    int y = bestFit.getY();

    for (int distance = 0; distance <= Constants.ADJUSTMENT_DISTANCE; distance++) {
      for (int distX = -distance; distX <= distance; distX++) {
        int distY = distance - Math.abs(distX);
        adjustNodeFitness(x + distX, y + distY, temperature, 1 / Math.pow(2, distance));
        if (distY != 0) {
          adjustNodeFitness(x + distX, y - distY, temperature, 1 / Math.pow(2, distance));
        }
      }
    }
  }

  private void adjustNodeFitness(int x, int y, double temperature, double learningScale) {
    Optional<Node> opNode = getNodeAt(x, y);
    if (opNode.isEmpty()) {
      return;
    }
    Node node = opNode.get();
    double diff = temperature - node.getWeight();
    // node.setWeight(node.getWeight() + diff * Constants.LEARNING_FACTOR);
    node.setWeight(node.getWeight() + diff * Constants.LEARNING_FACTOR * learningScale);
  }
}