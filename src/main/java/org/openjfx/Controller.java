package org.openjfx;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Calendar;
import java.util.*;

public class Controller implements Initializable {

    public Label mon, tue, wed, thu, fri, sat, sun;
    public Label day0, day1, day2, day3, day4, day5, day6;
    public Label day7, day8, day9, day10, day11, day12, day13;
    public Label day14, day15, day16, day17, day18, day19, day20;
    public Label day21, day22, day23, day24, day25, day26, day27;
    public Label day28, day29, day30, day31, day32, day33, day34;
    public Label day35, day36, day37, day38, day39, day40, day41;
    public Label yearMonthLabel;
    public Pane eventsPane;
    public StackPane root;
    public Label eventsPaneTitle;
    public ListView eventsPaneEvents;
    public Button updateEvent, deleteEvent;
    public TextField eventsDescription;
    public ChoiceBox eventsNotifyBefore;

    private int currentMonth = 0, currentYear = 1900;
    private final int[] openedOn = new int[]{currentYear, currentMonth};
    private Label clickedDay = null;
    private static org.openjfx.Calendar calendar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mon.setText("Monday");
        tue.setText("Tuesday");
        wed.setText("Wednesday");
        thu.setText("Thursday");
        fri.setText("Friday");
        sat.setText("Saturday");
        sun.setText("Sunday");

        eventsPane.visibleProperty().set(false);
        eventsPane.managedProperty().set(false);
        calendar = new org.openjfx.Calendar(new SQLiteStorage());
        setCurrentMonth(null);
    }

    private void renderCurrentMonth() {
        HashMap<Date, ArrayList<Event>> events = calendar.getEvents(currentMonth + 1, currentYear);
        Label[] days = new Label[]{
                day0, day1, day2, day3, day4, day5, day6,
                day7, day8, day9, day10, day11, day12, day13,
                day14, day15, day16, day17, day18, day19, day20,
                day21, day22, day23, day24, day25, day26, day27,
                day28, day29, day30, day31, day32, day33, day34,
                day35, day36, day37, day38, day39, day40, day41
        };

        for (Label day : days) {
            day.setText("");
            day.getStyleClass().remove("clickable");
            day.getStyleClass().remove("current-day");
            day.getStyleClass().remove("has-events");
            day.onMouseClickedProperty().unbind();
        }

        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int realCurrentYear = cal.get(Calendar.YEAR);
        int realCurrentMonth = cal.get(Calendar.MONTH);

        cal.set(Calendar.MONTH, currentMonth);
        cal.set(Calendar.YEAR, currentYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        yearMonthLabel.setText(currentYear + ", " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        int offset = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (offset == 0) {
            offset = 7;
        }

        for (int i = 0; i < cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            days[i + offset - 1].setText(String.valueOf(i + 1));
            days[i + offset - 1].getStyleClass().add("clickable");
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
            if (events.containsKey(cal.getTime())) {
                days[i + offset - 1].getStyleClass().add("has-events");
            }
            days[i + offset - 1].onMouseClickedProperty().set((MouseEvent t) -> {
                eventsDescription.setText("");
                updateEvent.onMouseClickedProperty().unbind();
                if (t.getTarget() instanceof Label) {
                    Label l = (Label) t.getTarget();
                    // toggle events pane
                    if (clickedDay != l) {
                        eventsPane.managedProperty().set(true);
                        eventsPane.visibleProperty().set(true);
                        clickedDay = l;
                    } else if (currentYear == openedOn[0] && currentMonth == openedOn[1]) {
                        eventsPane.managedProperty().set(false);
                        eventsPane.visibleProperty().set(false);
                        clickedDay = null;
                    }
                    openedOn[0] = currentYear;
                    openedOn[1] = currentMonth;

                    eventsPaneTitle.setText("Events for " + l.getText() + " of " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));

                    cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(l.getText()));
                    eventsPaneEvents.setItems(null);
                    eventsPaneEvents.onMouseClickedProperty().unbind();
                    if (events.containsKey(cal.getTime())) {
                        eventsPaneEvents.setItems(FXCollections.observableArrayList(events.get(cal.getTime())));
                        eventsPaneEvents.setOnMouseClicked(e -> {
                            Event event = (Event) eventsPaneEvents.getSelectionModel().getSelectedItem();
                            eventsNotifyBefore.setValue(event.notifyBefore);
                            if (event != null) {
                                eventsDescription.setText(event.description);
                                updateEvent.onMouseClickedProperty().set((MouseEvent me) -> {
                                    calendar.updateEvent(event.id, null, eventsDescription.getText(), Integer.valueOf((String) eventsNotifyBefore.getValue()));
                                    event.description = eventsDescription.getText();
                                    renderCurrentMonth();
                                });
                                deleteEvent.onMouseClickedProperty().set((MouseEvent me) -> {
                                    calendar.deleteEvent(event.id);
                                    renderCurrentMonth();
                                });
                            }
                        });
                    }
                }
            });
        }
        if (currentYear == realCurrentYear && currentMonth == realCurrentMonth) {
            days[currentDay + offset - 2].getStyleClass().add("current-day");
        }
    }

    public void nextMonth(MouseEvent mouseEvent) {
        currentMonth += 1;
        if (currentMonth == 12) {
            currentYear += 1;
            currentMonth = 0;
        }

        renderCurrentMonth();
    }

    public void previousMonth(MouseEvent mouseEvent) {
        currentMonth -= 1;
        if (currentMonth == -1) {
            currentYear -= 1;
            currentMonth = 11;
        }

        renderCurrentMonth();
    }

    public void setCurrentMonth(MouseEvent mouseEvent) {
        Calendar cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH);
        currentYear = cal.get(Calendar.YEAR);

        renderCurrentMonth();
    }
}