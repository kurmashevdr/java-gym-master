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
        Group trainingGroup = trainingSession.getGroup();
        DayOfWeek trainingDay = trainingSession.getDayOfWeek();
        if (!trainingGroup.checkDay(trainingDay)) {
            Map<TimeOfDay, List<TrainingSession>> treeMap = timetable.get(trainingSession.getDayOfWeek());
            List<TrainingSession> linkedList = treeMap.getOrDefault(trainingSession.getTimeOfDay(), new LinkedList<>());
            trainingGroup.addDay(trainingDay);
            linkedList.add(trainingSession);
            treeMap.put(trainingSession.getTimeOfDay(), linkedList);
            timetable.put(trainingSession.getDayOfWeek(), treeMap);
        } else {
            System.out.print("Для этой группы уже есть занятие в этот день.");
        }
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> resultList = new LinkedList<>();
        if (!timetable.get(dayOfWeek).isEmpty()) {
            for (TimeOfDay timeOfDay : timetable.get(dayOfWeek).keySet()) {
                resultList.addAll(timetable.get(dayOfWeek).get(timeOfDay));
            }
        }
        return resultList;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, new LinkedList<>());
    }

    public Set<CounterCoachSessions> getCountByCoaches() {
        Map<Coach, CounterCoachSessions> map = new HashMap<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (timetable.get(dayOfWeek).isEmpty()) {
                continue;
            }
            for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : timetable.get(dayOfWeek).entrySet()) {
                for (TrainingSession trainingSession : entry.getValue()) {
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
        return getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay).size();
    }
}
