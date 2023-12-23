package controller;

import model.HealthNCare;

/*
 * This class is responsible for saving the data in the program.
 */
public class SaveDataListener {
  private HealthNCare healthNCare;

  /**
   * @param  healthNCare The main model class of the application.
   */
  public SaveDataListener(HealthNCare healthNCare) {
    this.healthNCare = healthNCare;
  }

  /**
   * Saves the data in the program.
   */
  public void onAction() {
    healthNCare.saveData();
  }
}
