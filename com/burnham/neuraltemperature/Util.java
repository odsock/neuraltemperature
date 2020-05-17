package com.burnham.neuraltemperature;

import java.util.Random;

public class Util {
  public static double getRandomTemperature() {
    return new Random().nextDouble() * Constants.TEMP_RANGE + Constants.MIN_TEMP;
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