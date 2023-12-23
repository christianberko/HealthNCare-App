package model;

import view.Observer;

public interface Observable {
    void addSubscriber(Observer observer);
    void removeSubscriber(Observer observer);
    void notifySubscribers(String messageType, String message);
}
