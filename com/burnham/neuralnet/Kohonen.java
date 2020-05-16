package com.burnham.neuralnet;

/*	Mike Burnham
	CS405 - AI
	hw5 exersize 2b
	12/14/2006

	RESULTS:
			This program seems to do fairly well.  When the number of loops through
		the training data is increased (10000) it seems to do much better.
		The learning factor may be why.  I was using .5 to start with, but .2 seemed
		to get it learning faster.
			It still often mistakes 0 for 8, and sometimes doesn't end up labeling
		a node at all for some classes.  I think that is due to them getting replaced
    repeatedly durning training.

    Updated 05/15/2020
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Kohonen {
  private double temperature;
  private Node[][] map = new Node[10][10];

  int currenttrain = -1;

  public static void main(String args[]) {
    Kohonen net = new Kohonen();

    for (int i = 0; i < 1000; i++) {
      double randomTemperature = Util.getRandomTemperature();
      Node bestfit = net.findBestFit(randomTemperature);
      System.out.println("train " + net.currenttrain + " bestfit " + bestfit.getX() + "," + bestfit.getY() + " fitness "
          + bestfit.getFitness(randomTemperature));
      bestfit.setLabel(Util.getWaterState(randomTemperature));
      net.improveFitness(bestfit);
    }

    System.out.println();
    System.out.println("current labels");
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++)
        System.out.print(String.format("%3d", net.map[i][j].getLabel()));
      System.out.println();
    }
    System.out.println();

    System.out.println("done with training data.");

    while (true) {
      System.out.println("\nenter temperature.");
      double temperatureInput = getUserInput();
      Node bestfit = net.findBestFit(temperatureInput);
      System.out.println("bestfit " + bestfit.getX() + "," + bestfit.getY() + " label: " + bestfit.getLabel());
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
    for (int i = 0; i < 10; i++)
      for (int j = 0; j < 10; j++)
        map[i][j] = new Node(i, j);
  }

  public void improveFitness(Node bestfit) {
    int x = bestfit.getX();
    int y = bestfit.getY();

    Node n;

    if (x - 2 >= 0) {
      n = map[x - 2][y];
      n.setWeight((temperature - n.getWeight()) * Constants.LEARNING_FACTOR);
    }
    for (int j = y - 1; j < y + 1; j++)
      if (j >= 0 && j < 10 && x - 1 >= 0) {
        n = map[x - 1][j];
        n.setWeight((temperature - n.getWeight()) * Constants.LEARNING_FACTOR);
      }
    for (int j = y - 2; j < y + 2; j++)
      if (j >= 0 && j < 10) {
        n = map[x][j];
        n.setWeight((temperature - n.getWeight()) * Constants.LEARNING_FACTOR);
      }
    for (int j = y - 1; j < y + 1; j++)
      if (j >= 0 && j < 10 && x + 1 < 10) {
        n = map[x + 1][j];
        for (int i = 0; i < 20; i++)
          n.setWeight((temperature - n.getWeight()) * Constants.LEARNING_FACTOR);
      }
    if (x + 2 < 10) {
      n = map[x + 2][y];
      n.setWeight((temperature - n.getWeight()) * Constants.LEARNING_FACTOR);
    }
  }

  public Node findBestFit(double temperatureInput) {
    Node bestfit = null;
    double bestsofar = 100000;

    for (int i = 0; i < 10; i++)
      for (int j = 0; j < 10; j++) {
        double fitness = map[i][j].getFitness(temperatureInput);
        if (fitness < bestsofar) {
          bestsofar = fitness;
          bestfit = map[i][j];
        }
      }

    return bestfit;
  }

  // public Node fitUserPattern() {
  //   Node bestfit = findBestFit(temperatureInput);

  //   if (bestfit.getLabel() == null) {
  //     int[] labels = new int[10];
  //     int i = bestfit.getX();
  //     int j = bestfit.getY();

  //     System.out.println("no label: " + i + "," + j);

  //     int offset = 1;
  //     i -= offset;
  //     j -= offset;
  //     while (true) {
  //       if (i < 10 && i >= 0 && j < 10 && j >= 0 && map[i][j].getLabel() != -1)
  //         labels[map[i][j].getLabel()]++;

  //       if (i == bestfit.getX() - 1 - offset && j == bestfit.getY() - offset) {
  //         int maxlabel = 0;
  //         int label = -1;
  //         for (int k = 0; k < labels.length; k++)
  //           if (labels[k] > maxlabel) {
  //             maxlabel = labels[k];
  //             label = k;
  //           }
  //         if (label == -1) {
  //           offset++;
  //           if (offset == 10)
  //             break;
  //           j++;
  //         } else {
  //           bestfit.setLabel(label);
  //           break;
  //         }
  //       } else if (i == bestfit.getX() - offset && j < bestfit.getY() + offset)
  //         j++;
  //       else if (i == bestfit.getX() + offset && j > bestfit.getY() - offset)
  //         j--;
  //       else if (j == bestfit.getY() - offset && i > bestfit.getX() - offset)
  //         i--;
  //       else if (j == bestfit.getY() + offset && i < bestfit.getX() + offset)
  //         i++;

  //       System.out.println("next check: " + i + "," + j);
  //     }
  //   }

  //   return bestfit;
  // }
}