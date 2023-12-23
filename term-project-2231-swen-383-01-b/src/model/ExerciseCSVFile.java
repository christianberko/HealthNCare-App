package model;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class ExerciseCSVFile implements IExerciseStorage {
  // Exercise
  // e,Gardening,180
  // e,name,caloriesPerHour

  private final String exerciseFilePath = "exercise.csv";

  public HashMap<String, Exercise> readExercises() {
    HashMap<String, Exercise> exercises = new HashMap<String, Exercise>();
    try {
      CSVParser csvParser =
          CSVParser.parse(Files.newBufferedReader(Path.of(exerciseFilePath)), CSVFormat.DEFAULT);
      for (CSVRecord line : csvParser) {
        if (line.get(0).equals("e")) { // verifies record is properly formatted
          exercises.put(
              line.get(1).toLowerCase(), new Exercise(line.get(1), Float.parseFloat(line.get(2))));
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return exercises;
  }

  public void writeExercises(HashMap<String, Exercise> exercises) {
    try {
      FileWriter fw = new FileWriter(exerciseFilePath);
      CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.DEFAULT);
      for (Exercise e : exercises.values()) {
        String[] record = {"e", e.getName(), String.valueOf(e.getCaloriePerHour())};

        csvPrinter.print((Object[]) record);
      }

      csvPrinter.flush();
      csvPrinter.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
