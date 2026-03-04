package ru.yandex.practicum.gym;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY));
        //Проверить, что за вторник не вернулось занятий
        assertEquals(0, timetable.getCountTrainingSessions(DayOfWeek.TUESDAY));
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
        assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY));
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        assertEquals(new TimeOfDay(13, 0), timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(20, 0), timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).get(1).getTimeOfDay());
        assertEquals(2,timetable.getCountTrainingSessions(DayOfWeek.THURSDAY));
        // Проверить, что за вторник не вернулось занятий
        assertEquals(0,timetable.getCountTrainingSessions(DayOfWeek.TUESDAY));

    }

@Test
void testGetTrainingSessionsForDayAndTime() {
    TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
            DayOfWeek.MONDAY, new TimeOfDay(13, 0));
    timetable.addNewTrainingSession(singleTrainingSession);
    //Проверить, что за понедельник в 13:00 вернулось одно занятие
    assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY,
            new TimeOfDay(13, 0)));
    //Проверить, что за понедельник в 14:00 не вернулось занятий
    assertEquals(0, timetable.getCountTrainingSessions(DayOfWeek.MONDAY,
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
    assertEquals(2, timetable.getCountTrainingSessions(DayOfWeek.MONDAY, time));
}

@Test
void testAdd2NotUniqueTrainingSessionsAtDay() {
    TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
            DayOfWeek.MONDAY, new TimeOfDay(13, 0));
    TrainingSession mondayChildTrainingSession2 = new TrainingSession(groupChild, coach,
            DayOfWeek.MONDAY, new TimeOfDay(13, 30));
    timetable.addNewTrainingSession(mondayChildTrainingSession);
    timetable.addNewTrainingSession(mondayChildTrainingSession2);
    assertEquals(1, timetable.getCountTrainingSessions(DayOfWeek.MONDAY));
}

@Test
void testAddTrainingSessionWithDifferentCoach() {
    TimeOfDay time = new TimeOfDay(13, 0);
    TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
            DayOfWeek.MONDAY, time);
    coach = new Coach("Купцов", "Николай", "Сергеевич");
    TrainingSession tuesdayChildTrainingSession = new TrainingSession(groupChild, coach,
            DayOfWeek.TUESDAY, time);
    timetable.addNewTrainingSession(mondayChildTrainingSession);
    timetable.addNewTrainingSession(tuesdayChildTrainingSession);
    assertEquals(2, timetable.getCountTrainingSessions());
}

@Test
void testGetCountTrainingSessions() {
    TimeOfDay time = new TimeOfDay(13, 0);
    Coach coach2 = new Coach("Купцов", "Николай", "Сергеевич");
    TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, time);
    TrainingSession tuesdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.TUESDAY, time);
    TrainingSession wednsdayChildTrainingSession = new TrainingSession(groupChild, coach2, DayOfWeek.WEDNESDAY,
            time);
    timetable.addNewTrainingSession(mondayChildTrainingSession);
    timetable.addNewTrainingSession(tuesdayChildTrainingSession);
    timetable.addNewTrainingSession(wednsdayChildTrainingSession);
    CounterCoachSessions counterSessionsForCoach = new CounterCoachSessions(coach);
    CounterCoachSessions counterSessionsForCoach2 = new CounterCoachSessions(coach2);
    Set<CounterCoachSessions> counterCoachSessions = getCounterCoachSessions(counterSessionsForCoach, counterSessionsForCoach2);
    assertEquals(counterCoachSessions, timetable.getCountByCoaches());
}

@Test
void testGetCountTrainingSessionsWithAddSessionForCoach() {
    TimeOfDay time = new TimeOfDay(13, 0);
    Coach coach2 = new Coach("Купцов", "Николай", "Сергеевич");
    TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, time);
    TrainingSession tuesdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.TUESDAY, time);
    TrainingSession wednsdayChildTrainingSession = new TrainingSession(groupChild, coach2, DayOfWeek.WEDNESDAY,
            time);
    timetable.addNewTrainingSession(mondayChildTrainingSession);
    timetable.addNewTrainingSession(tuesdayChildTrainingSession);
    timetable.addNewTrainingSession(wednsdayChildTrainingSession);
    CounterCoachSessions counterSessionsForCoach = new CounterCoachSessions(coach);
    counterSessionsForCoach.addSession();
    CounterCoachSessions counterSessionsForCoach2 = new CounterCoachSessions(coach2);
    Set<CounterCoachSessions> counterCoachSessions = getCounterCoachSessions(counterSessionsForCoach, counterSessionsForCoach2);
    assertNotEquals(counterCoachSessions, timetable.getCountByCoaches());
    TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.THURSDAY,
            time);
    timetable.addNewTrainingSession(thursdayChildTrainingSession);
    assertEquals(counterCoachSessions, timetable.getCountByCoaches());
}
    @Test
    void testGetCountTrainingSessionWithDifferentCoach() {
        TimeOfDay time = new TimeOfDay(13, 0);
        Coach coach2 = new Coach("Купцов", "Николай", "Сергеевич");
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, time);
        TrainingSession tuesdayAdultTrainingSession = new TrainingSession(groupAdult, coach2, DayOfWeek.MONDAY, time);
        TrainingSession wednsdayChildTrainingSession = new TrainingSession(groupChild, coach2, DayOfWeek.WEDNESDAY,
                time);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(tuesdayAdultTrainingSession);
        timetable.addNewTrainingSession(wednsdayChildTrainingSession);
        CounterCoachSessions counterSessionsForCoach = new CounterCoachSessions(coach);
        CounterCoachSessions counterSessionsForCoach2 = new CounterCoachSessions(coach2);
        counterSessionsForCoach.addSession();
        counterSessionsForCoach2.addSession();
        counterSessionsForCoach2.addSession();
        Set<CounterCoachSessions> counterCoachSessions = new LinkedHashSet<>();
        counterCoachSessions.add(counterSessionsForCoach2);
        counterCoachSessions.add(counterSessionsForCoach);
        assertEquals(counterCoachSessions, timetable.getCountByCoaches());
    }

@Test
void testGetCountTrainingSessionsWithChangeTopCoach() {
    TimeOfDay time = new TimeOfDay(13, 0);
    Coach coach2 = new Coach("Купцов", "Николай", "Сергеевич");
    TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, time);
    TrainingSession tuesdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.TUESDAY, time);
    TrainingSession wednsdayChildTrainingSession = new TrainingSession(groupChild, coach2, DayOfWeek.WEDNESDAY,
            time);
    TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach2, DayOfWeek.THURSDAY,
            time);
    TrainingSession fridayChildTrainingSession = new TrainingSession(groupChild, coach2, DayOfWeek.FRIDAY, time);
    timetable.addNewTrainingSession(mondayChildTrainingSession);
    timetable.addNewTrainingSession(tuesdayChildTrainingSession);
    timetable.addNewTrainingSession(wednsdayChildTrainingSession);
    CounterCoachSessions counterSessionsForCoach = new CounterCoachSessions(coach);
    CounterCoachSessions counterSessionsForCoach2 = new CounterCoachSessions(coach2);
    counterSessionsForCoach.addSession();
    counterSessionsForCoach.addSession();
    counterSessionsForCoach2.addSession();
    counterSessionsForCoach2.addSession();
    counterSessionsForCoach2.addSession();
    Set<CounterCoachSessions> counterCoachSessions = new LinkedHashSet<>();
    counterCoachSessions.add(counterSessionsForCoach2);
    counterCoachSessions.add(counterSessionsForCoach);
    assertNotEquals(counterCoachSessions, timetable.getCountByCoaches());
    timetable.addNewTrainingSession(thursdayChildTrainingSession);
    timetable.addNewTrainingSession(fridayChildTrainingSession);
    assertEquals(counterCoachSessions, timetable.getCountByCoaches());
}

private Set<CounterCoachSessions> getCounterCoachSessions(CounterCoachSessions counterSessionsForCoach, CounterCoachSessions counterSessionsForCoach2) {
    Set<CounterCoachSessions> counterCoachSessions = new LinkedHashSet<>();
    counterSessionsForCoach.addSession();
    counterSessionsForCoach.addSession();
    counterSessionsForCoach2.addSession();
    counterCoachSessions.add(counterSessionsForCoach);
    counterCoachSessions.add(counterSessionsForCoach2);
    return counterCoachSessions;
}
}
