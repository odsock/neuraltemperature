package com.burnham.neuralnet;

class Node {
  private int x = -1;
  private int y = -1;
  private double weight;
  private String label;
  private int adjustmentCount;

  Node(final int x, final int y) {
    this.x = x;
    this.y = y;
    this.weight = Util.getRandomTemperature();
  }

  public int getAdjustmentCount() {
    return adjustmentCount;
  }

  public double getFitness(double temperature) {
    double fitness = Math.pow(weight - temperature, 2);
    return fitness;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
    this.adjustmentCount++;
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