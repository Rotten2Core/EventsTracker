package org.openjfx;

public class Event {
    int id;
    String dt;
    String description;
    int notifyBefore;

    Event(int id, String dt, String description, int notifyBefore) {
        this.id = id;
        this.dt = dt;
        this.description = description;
        this.notifyBefore = notifyBefore;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", dt=" + dt +
                ", description='" + description +
                "', notify_before=" + notifyBefore + "}";
    }
}
