package model;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/** Concrete implementation of {@link ILog} interface that handles the log storage as a CSV file */
public class LogCSVFile implements ILog {
  // Weight log
  // 2023,10,05,w,185.5
  // yyyy,mm,dd,w,weight

  // Calorie limit
  // 2023,10,05,c,1800.0
  // yyyy,mm,dd,c,calories

  // Food consumption
  // 2023,10,0 5,f,Hot Dog-Bun-Mustard,2.5
  // yyyy,mm,dd,f,foodName           ,count

  private final String LOGFILE = "log.csv";

  /**
   * Reads and parses the CSV file and returns it. It will create different {@link LogEntry} items
   * depending on what type the record was.
   *
   * @return {@code ArrayList<LogEntry>} The list of log entries in no particular order
   */
  public ArrayList<LogEntry> readLog() {
    ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();
    try {
      CSVParser csvParser =
          CSVParser.parse(Files.newBufferedReader(Path.of(LOGFILE)), CSVFormat.DEFAULT);
      for (CSVRecord line : csvParser) {
        LogEntry entry;
        LocalDate date =
            LocalDate.of(
                Integer.parseInt(line.get(0)),
                Integer.parseInt(line.get(1)),
                Integer.parseInt(line.get(2)));
        switch (line.get(3)) {
          case "w": // Weight entry
            entry = new WeightEntry(date, Float.parseFloat(line.get(4)));
            break;
          case "c": // Calorie limit
            entry = new CalorieLimitEntry(date, Float.parseFloat(line.get(4)));
            break;
          case "f": // food consumed
            entry = new ConsumptionEntry(date, line.get(4), Float.parseFloat(line.get(5)));
            break;
          case "e": // exercise entry
            entry = new ExerciseEntry(date, line.get(4), Float.parseFloat(line.get(5)));
            break;
          default:
            continue; // log this error
        }
        logEntries.add(entry);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return logEntries;
  }

  /**
   * Writes the current log back to the CSV file. It will completely overwrite the existing file and
   * replaces it with what the given object
   *
   * @param {@code ArrayList<LogEntry>} log containing all log entries
   */
  public void writeLog(ArrayList<LogEntry> log) {
    try {
      FileWriter fw = new FileWriter(LOGFILE);
      CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.DEFAULT);

      for (LogEntry logEntry : log) {
        csvPrinter.printRecord((Object[]) logEntry.toRecord());
      }

      csvPrinter.flush();
      csvPrinter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
