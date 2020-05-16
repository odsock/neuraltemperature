package com.burnham.neuralnet;

import java.util.Date;
import java.util.Random;

class Node {
  private int x = -1;
  private int y = -1;
  private double weight;
  private String label;

  Node(final int x, final int y) {
    this.x = x;
    this.y = y;
    final Random r = new Random(new Date().getTime());
    this.setWeight(r.nextDouble() * Double.MAX_VALUE * Constants.MINIMUM_TEMPERATURE);
  }

  public double getFitness(double temperature) {
    return Math.pow(weight - temperature, 2);
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}