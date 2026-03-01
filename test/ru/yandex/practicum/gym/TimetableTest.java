package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    private Timetable timetable;
    private Coach coach;
    private Group groupChild;

    @BeforeEach
    public void init() {
        timetable = new Timetable();
        coach = new Coach("Васильев", "Николай", "Сергеевич");
        groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);
        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY));
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getCountTrainingSessions(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY));
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        int count = 0;
        for (Map.Entry<TimeOfDay, Set<TrainingSession>> entry : timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)
                .entrySet()) {
            if (count == 0) {
                Assertions.assertEquals(new TimeOfDay(13, 0), entry.getKey());
            } else {
                Assertions.assertEquals(new TimeOfDay(20, 0), entry.getKey());
            }
            count++;
        }
        Assertions.assertEquals(2, timetable.getCountTrainingSessions(DayOfWeek.THURSDAY));
        // Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getCountTrainingSessions(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)));
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertEquals(0, timetable.getCountTrainingSessions(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0)));

    }

    @Test
    void testAdd2UniqueTrainingSessionsAtOneTime() {
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TimeOfDay time = new TimeOfDay(13, 0);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, time);
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, time);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        Assertions.assertEquals(2, timetable.getCountTrainingSessions(DayOfWeek.MONDAY, time));
    }

    @Test
    void testAdd2NotUniqueTrainingSessionsAtDay() {
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY,  new TimeOfDay(13, 30));
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        Assertions.assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY));
    }

    @Test
    void testAddTrainingSessionWithDifferentCoach() {
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        coach = new Coach("Купцов", "Николай", "Сергеевич");
        TrainingSession tuesdayAdultTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(tuesdayAdultTrainingSession);
        Assertions.assertEquals(2, timetable.getCountTrainingSessions());
    }
}
