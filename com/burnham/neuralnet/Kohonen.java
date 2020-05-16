package com.burnham.neuralnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Kohonen {
  private Node[][] map = new Node[Constants.MAP_SIZE][Constants.MAP_SIZE];

  public static void main(String args[]) {
    Kohonen net = new Kohonen();

    for (int i = 0; i < Constants.TRAINING_ITERATIONS; i++) {
      net.train();
    }
    net.print();
    double mapQuality = net.testMapQuality();
    System.out.format("map accuracy: %3.2f\n", mapQuality);
    System.out.println("done with training data.");

    while (true) {
      System.out.println("\nenter temperature:");
      double temperatureInput = getUserInput();
      Node bestfit = net.findBestFit(temperatureInput);
      System.out.format("bestfit: %d,%d\nweight: %f\nfitness: %f\nlabel: %s\n", bestfit.getX(), bestfit.getY(),
          bestfit.getWeight(), bestfit.getFitness(temperatureInput), bestfit.getLabel());
    }
  }

  static double getUserInput() {
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
    // System.out.println("train " + randomTemperature + " bestfit " + bestfit.getX() + "," + bestfit.getY() + " fitness "
    //     + bestfit.getFitness(randomTemperature));
    bestfit.setLabel(Util.getWaterState(randomTemperature));
    adjustMapFitness(bestfit, randomTemperature);
  }

  public double testMapQuality() {
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

  public Node getNodeAt(int x, int y) {
    if (x >= 0 && x < Constants.MAP_SIZE && y >= 0 && y < Constants.MAP_SIZE) {
      return map[x][y];
    }
    return null;
  }

  public void adjustMapFitness(Node bestFit, double temperature) {
    int x = bestFit.getX();
    int y = bestFit.getY();

    for (int distance = 0; distance <= Constants.ADJUSTMENT_DISTANCE; distance++) {
      for (int distX = -distance; distX <= distance; distX++) {
        int distY = distance - Math.abs(distX);
        adjustNodeFitness(getNodeAt(x + distX, y + distY), temperature, 1 / Math.pow(2, distance));
        if (distY != 0) {
          adjustNodeFitness(getNodeAt(x + distX, y - distY), temperature, 1 / Math.pow(2, distance));
        }
      }
    }

    // adjustNodeFitness(getNodeAt(x + 1, y + 0), temperature, .5);
    // adjustNodeFitness(getNodeAt(x - 1, y + 0), temperature, .5);
    // adjustNodeFitness(getNodeAt(x + 0, y + 1), temperature, .5);
    // adjustNodeFitness(getNodeAt(x + 0, y - 1), temperature, .5);

    // adjustNodeFitness(getNodeAt(x + 2, y + 0), temperature, .25);
    // adjustNodeFitness(getNodeAt(x - 2, y + 0), temperature, .25);
    // adjustNodeFitness(getNodeAt(x + 0, y + 2), temperature, .25);
    // adjustNodeFitness(getNodeAt(x + 0, y - 2), temperature, .25);
    // adjustNodeFitness(getNodeAt(x + 1, y + 1), temperature, .25);
    // adjustNodeFitness(getNodeAt(x - 1, y - 1), temperature, .25);
    // adjustNodeFitness(getNodeAt(x + 1, y - 1), temperature, .25);
    // adjustNodeFitness(getNodeAt(x - 1, y + 1), temperature, .25);

    // adjustNodeFitness(getNodeAt(x - 3, y + 0), temperature, .125);
    // adjustNodeFitness(getNodeAt(x - 2, y - 1), temperature, .125);
    // adjustNodeFitness(getNodeAt(x - 2, y + 1), temperature, .125);
    // adjustNodeFitness(getNodeAt(x - 1, y - 2), temperature, .125);
    // adjustNodeFitness(getNodeAt(x - 1, y + 2), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 0, y - 3), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 0, y + 3), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 1, y - 2), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 1, y + 2), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 2, y - 1), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 2, y + 1), temperature, .125);
    // adjustNodeFitness(getNodeAt(x + 3, y + 0), temperature, .125);
  }

  public void adjustNodeFitness(Node node, double temperature, double learningScale) {
    if (node == null) {
      return;
    }
    double diff = temperature - node.getWeight();
    node.setWeight(node.getWeight() + diff * Constants.LEARNING_FACTOR);
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
}