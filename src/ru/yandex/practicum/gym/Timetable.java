package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public Timetable() {
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<TimeOfDay, List<TrainingSession>> teeMapByDayOfWeek = new TreeMap<>();
            timetable.put(day, teeMapByDayOfWeek);
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        Map<TimeOfDay, List<TrainingSession>> treeMap = timetable.get(trainingSession.getDayOfWeek());
        List<TrainingSession> list = treeMap.getOrDefault(trainingSession.getTimeOfDay(), new ArrayList<>());
        list.add(trainingSession);
        treeMap.put(trainingSession.getTimeOfDay(), list);
        timetable.put(trainingSession.getDayOfWeek(), treeMap);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public int getCountTrainingSessions() {
        int count = 0;
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (timetable.get(dayOfWeek).isEmpty()) {
                continue;
            }
            for (TimeOfDay timeOfDay : timetable.get(dayOfWeek).keySet()) {
                count += timetable.get(dayOfWeek).get(timeOfDay).size();
            }
        }

        return count;
    }

    public int getCountTrainingSessions(DayOfWeek dayOfWeek) {
        if (timetable.get(dayOfWeek).isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Map.Entry<TimeOfDay, List<TrainingSession>> treeMap : timetable.get(dayOfWeek).entrySet()) {
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

        return timetable.get(dayOfWeek).get(timeOfDay).size();
    }
}
