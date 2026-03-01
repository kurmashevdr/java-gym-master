package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();

    public Timetable() {
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<TimeOfDay, Set<TrainingSession>> teeMapByDayOfWeek = new TreeMap<>();
            timetable.put(day, teeMapByDayOfWeek);
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        Group trainingGroup = trainingSession.getGroup();
        DayOfWeek trainingDay = trainingSession.getDayOfWeek();
        if (!trainingGroup.checkDay(trainingDay)) {
            Map<TimeOfDay, Set<TrainingSession>> treeMap = timetable.get(trainingSession.getDayOfWeek());
            Set<TrainingSession> hashSet = treeMap.getOrDefault(trainingSession.getTimeOfDay(), new HashSet<>());
            trainingGroup.addDay(trainingDay);
            hashSet.add(trainingSession);
            treeMap.put(trainingSession.getTimeOfDay(), hashSet);
            timetable.put(trainingSession.getDayOfWeek(), treeMap);
        } else {
            System.out.print("Для этой группы уже есть занятие в этот день.");
        }
    }

    public Map<TimeOfDay, Set<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public Set<CounterCoachSessions> getCountByCoaches() {
        Map<Coach, CounterCoachSessions> map = new HashMap<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (timetable.get(dayOfWeek).isEmpty()) {
                continue;
            }
            for (TimeOfDay timeOfDay : timetable.get(dayOfWeek).keySet()) {
                for (TrainingSession trainingSession : timetable.get(dayOfWeek).get(timeOfDay)) {
                    CounterCoachSessions counterCoachSessions = map.getOrDefault(trainingSession.getCoach(),
                            new CounterCoachSessions(trainingSession.getCoach()));
                    counterCoachSessions.addSession();
                    map.put(trainingSession.getCoach(), counterCoachSessions);
                }
            }
        }
        NavigableSet<CounterCoachSessions> navigableSet = new TreeSet<>(map.values());

        return navigableSet.descendingSet();
    }

    public int getCountTrainingSessions() {
        int count = 0;
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (timetable.get(dayOfWeek).isEmpty()) {
                continue;
            }
            for (TimeOfDay timeOfDay : timetable.get(dayOfWeek).keySet()) {
                count += getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay).size();
            }
        }
        return count;
    }

    public int getCountTrainingSessions(DayOfWeek dayOfWeek) {
        if (timetable.get(dayOfWeek).isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Map.Entry<TimeOfDay, Set<TrainingSession>> treeMap : timetable.get(dayOfWeek).entrySet()) {
            count += treeMap.getValue().size();
        }
        return count;
    }

    public int getCountTrainingSessions(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.get(dayOfWeek).isEmpty()) {
            return 0;
        }
        if (!timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            return 0;
        }
        return getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay).size();
    }
}
