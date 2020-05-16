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

    System.out.println("done with training data.");

    while (true) {
      System.out.println("\nenter temperature:");
      double temperatureInput = getUserInput();
      Node bestfit = net.findBestFit(temperatureInput);
      System.out.format("bestfit: %d,%d\nweight: %f\nfitness: %f\nlabel: %s\n", bestfit.getX(), bestfit.getY(),
          bestfit.getWeight(), bestfit.getFitness(temperatureInput), bestfit.getLabel());
    }
  }

  public void train() {
    double randomTemperature = Util.getRandomTemperature();
    Node bestfit = findBestFit(randomTemperature);
    System.out.println("train " + randomTemperature + " bestfit " + bestfit.getX() + "," + bestfit.getY()
        + " fitness " + bestfit.getFitness(randomTemperature));
    bestfit.setLabel(Util.getWaterState(randomTemperature));
    adjustMapFitness(bestfit, randomTemperature);
  }

  public void print() {
    for (int i = 0; i < Constants.MAP_SIZE; i++) {
      for (int j = 0; j < Constants.MAP_SIZE; j++)
        System.out.format("%7s", map[i][j].getLabel());
      System.out.println();
      for (int j = 0; j < Constants.MAP_SIZE; j++)
        System.out.format("%7.2f", map[i][j].getWeight());
      System.out.println();
      System.out.println();
    }
    System.out.println();
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

  public void adjustMapFitness(Node bestFit, double temperature) {
    int x = bestFit.getX();
    int y = bestFit.getY();

    Node node;

    if (x - 2 >= 0) {
      node = map[x - 2][y];
      adjustNodeFitness(node, temperature);
    }

    for (int j = y - 1; j < y + 1; j++)
      if (j >= 0 && j < Constants.MAP_SIZE && x - 1 >= 0) {
        node = map[x - 1][j];
        adjustNodeFitness(node, temperature);
      }

    for (int j = y - 2; j < y + 2; j++)
      if (j >= 0 && j < Constants.MAP_SIZE) {
        node = map[x][j];
        adjustNodeFitness(node, temperature);
      }

    for (int j = y - 1; j < y + 1; j++)
      if (j >= 0 && j < Constants.MAP_SIZE && x + 1 < Constants.MAP_SIZE) {
        node = map[x + 1][j];
        adjustNodeFitness(node, temperature);
      }

    if (x + 2 < Constants.MAP_SIZE) {
      node = map[x + 2][y];
      adjustNodeFitness(node, temperature);
    }
  }

  public void adjustNodeFitness(Node node, double temperature) {
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

  // public Node fitUserPattern() {
  // Node bestfit = findBestFit(temperatureInput);

  // if (bestfit.getLabel() == null) {
  // int[] labels = new int[10];
  // int i = bestfit.getX();
  // int j = bestfit.getY();

  // System.out.println("no label: " + i + "," + j);

  // int offset = 1;
  // i -= offset;
  // j -= offset;
  // while (true) {
  // if (i < 10 && i >= 0 && j < 10 && j >= 0 && map[i][j].getLabel() != -1)
  // labels[map[i][j].getLabel()]++;

  // if (i == bestfit.getX() - 1 - offset && j == bestfit.getY() - offset) {
  // int maxlabel = 0;
  // int label = -1;
  // for (int k = 0; k < labels.length; k++)
  // if (labels[k] > maxlabel) {
  // maxlabel = labels[k];
  // label = k;
  // }
  // if (label == -1) {
  // offset++;
  // if (offset == 10)
  // break;
  // j++;
  // } else {
  // bestfit.setLabel(label);
  // break;
  // }
  // } else if (i == bestfit.getX() - offset && j < bestfit.getY() + offset)
  // j++;
  // else if (i == bestfit.getX() + offset && j > bestfit.getY() - offset)
  // j--;
  // else if (j == bestfit.getY() - offset && i > bestfit.getX() - offset)
  // i--;
  // else if (j == bestfit.getY() + offset && i < bestfit.getX() + offset)
  // i++;

  // System.out.println("next check: " + i + "," + j);
  // }
  // }

  // return bestfit;
  // }
}