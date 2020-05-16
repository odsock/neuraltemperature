package com.burnham.neuralnet;

import java.util.Random;

public class Util {
  public static double getRandomTemperature() {
    return new Random().nextDouble() * Constants.TEMPERATURE_RANGE + Constants.MINIMUM_TEMPERATURE;
  }

  public static String getWaterState(double randomTemperature) {
    if (randomTemperature > 100) {
      return "gas";
    } else if (randomTemperature > 0) {
      return "liquid";
    } else {
      return "frozen";
    }
  }
}