package view;

public interface Observer {
    public void update(String messageType, String message);
    //messageType allows the observer to filter for what applies to it; (BarChart, CalorieCalculator, _____List)
}
