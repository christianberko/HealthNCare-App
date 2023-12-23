package model;

import java.time.LocalDate;

/** Generalization of log entries that allows a user to track data over time */
public abstract class LogEntry {
  protected LocalDate date;

  /**
   * Constructor for the LogEntry class
   *
   * @param date The date of the entry
   */
  public LogEntry(LocalDate date) {
    this.date = date;
  }

  /**
   * Returns the date of the entry
   *
   * @return {@link LocalDate} date of the entry
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Returns the entry as an array to be sent to the storage media
   *
   * @return {@code String[]} of values
   */
  public abstract String[] toRecord();
}
