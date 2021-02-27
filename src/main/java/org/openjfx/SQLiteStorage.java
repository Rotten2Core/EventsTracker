package org.openjfx;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.Date;

public class SQLiteStorage implements Storage {
    SQLiteStorage() {
        try {
            if (checkIfTableExists("events")) {
                System.getLogger("dbLogger").log(
                        System.Logger.Level.DEBUG,
                        "Events table exists"
                );
            } else {
                createEventsTable();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:calendar.db";
            conn = DriverManager.getConnection(url);
            System.getLogger("dbLogger").log(
                System.Logger.Level.DEBUG,
                "Connection to SQLite has been established."
            );
        } catch (SQLException e) {
            System.getLogger("dbLogger").log(
                System.Logger.Level.ERROR,
                e.getMessage()
            );
        }

        return conn;
    }

    private boolean checkIfTableExists(String tableName) throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name = ?";
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, tableName);
        ResultSet rs = statement.executeQuery();
        conn.close();

        return rs.next();
    }

    private void createEventsTable() throws SQLException {
        String query = "CREATE TABLE events (id INTEGER NOT NULL PRIMARY KEY, dt TEXT NOT NULL, description TEXT NOT NULL, notify_before INTEGER NOT NULL)";
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.execute();
        conn.close();
    }

    public void write(String dt, String description, int notifyBefore) {
        String query = "INSERT INTO events(dt, description, notify_before) VALUES(?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, dt);
            statement.setString(2, description);
            statement.setInt(3, notifyBefore);
            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }
    private Event[] get(String query){
        ArrayList<Event> events = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("dt"),
                        rs.getString("description"),
                        rs.getInt("notify_before")
                ));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        return events.toArray(new Event[0]);
    }
    public HashMap<Date, ArrayList<Event>> getEventsForMonth(int month, int year) {
        // TODO: rewrite with prepare statement
        String query = "SELECT * FROM events WHERE cast(strftime('%m', dt) as int) = " + month + " AND cast(strftime('%Y', dt) as int) = " + year;
        Event[] events = get(query);
        HashMap<Date, ArrayList<Event>> monthEvents = new HashMap<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar calendar = Calendar.getInstance();
        for (Event event : events) {
            try {
                Date date = formatter.parse(event.dt);
                calendar.setTime(date);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                date = calendar.getTime();
                if (!monthEvents.containsKey(date)) {
                    monthEvents.put(date, new ArrayList<>());
                }
                monthEvents.get(date).add(event);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return monthEvents;
    }

    public Event[] getNearestToNotify(){
        String query = "SELECT * FROM events WHERE datetime(dt, '-' || notify_before || ' minutes') BETWEEN datetime('now') AND datetime('now', '+1 minutes')";
        return get(query);
    }

    public int delete(int id) {
        String query = "DELETE FROM events WHERE id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            return statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        return 0;
    }

    public int delete() {
        String query = "DELETE FROM events";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            return statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        return 0;
    }

    public int update(int id, String dt, String description, Integer notifyBefore) {
        HashMap<String, String> values = new HashMap<>();
        if (dt != null){
            values.put("dt = ?", dt);
        }
        if (description != null) {
            values.put("description = ?", description);
        }
        if (notifyBefore != null) {
            values.put("notify_before = ?", notifyBefore.toString());
        }
        String query = "UPDATE events SET " + String.join(", ", values.keySet()) + " WHERE id = ?";

        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            int index = 1;
            for (String value: values.values()) {
                statement.setString(index++, value);
            }
            statement.setInt(index, id);

            return statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return 0;
    }
}
