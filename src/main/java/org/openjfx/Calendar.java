package org.openjfx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Calendar extends Thread {
    private boolean shouldRun = true;
    private final Storage storage;
    Calendar(Storage storage) {
        this.storage = storage;
    }

    public HashMap<Date, ArrayList<Event>> getEvents(int month, int year) {
        return storage.getEventsForMonth(month, year);
    }

    public void writeEvent(String dt, String description, int notifyBefore) {
        this.storage.write(dt, description, notifyBefore);
    }
    public int deleteEvent(int id){
        return storage.delete(id);
    }
    public int deleteEvents(){
        return storage.delete();
    }
    public int updateEvent(int id, String dt, String description, Integer notifyBefore){
        return storage.update(id, dt, description, notifyBefore);
    }

    public void stopThread() {
        shouldRun = false;
    }

    @Override
    public void run() {
        while (shouldRun) {
            Event[] events = storage.getNearestToNotify();
            for (Event event: events) {
                System.out.println(event.toString());
            }
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
