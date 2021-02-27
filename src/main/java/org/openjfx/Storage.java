package org.openjfx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface Storage {
    <T> HashMap<Date, ArrayList<Event>> getEventsForMonth(int month, int year);
    <T> T[] getNearestToNotify();
    void write(String dt, String description, int notifyBefore);
    int update(int id, String dt, String description, Integer notifyBefore);
    int delete(int id);
    int delete();
}
