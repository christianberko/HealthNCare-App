package model;

import java.util.ArrayList;

/**
 * Common interface for any log storage medium. It should be able to read all the log entries and
 * write them back out
 */
public interface ILog {
  public ArrayList<LogEntry> readLog();

  /**
   * Write the log entries to the storage medium
   *
   * @param log The log entries to write
   */
  public void writeLog(ArrayList<LogEntry> log);
}
