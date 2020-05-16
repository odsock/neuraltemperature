package com.burnham.neuralnet;

import java.util.Random;

public class Util {
  public static double getRandomTemperature() {
    return new Random().nextDouble() * Double.MAX_VALUE - Constants.MINIMUM_TEMPERATURE;
  }

  public static String getWaterState(double randomTemperature) {
    if (randomTemperature > 212) {
      return "gas";
    } else if (randomTemperature > 32) {
      return "liquid";
    } else {
      return "frozen";
    }
  }
}