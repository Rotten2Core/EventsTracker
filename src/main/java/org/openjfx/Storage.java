package org.openjfx;

public interface Storage {
    <T> T[] getAll();
    <T> T[] getNearest();
    <T> T[] getNearestToNotify();
    void write(String dt, String description, int notifyBefore);
    int update(int id, String dt, String description, Integer notifyBefore);
    int delete(int id);
    int delete();
}
