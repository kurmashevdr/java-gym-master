package ru.yandex.practicum.gym;

import java.util.HashSet;
import java.util.Set;

public class Group {
    //название группы
    private final String title;
    //тип (взрослая или детская)
    private final Age age;
    //длительность (в минутах)
    private final int duration;
    //проверка дней
    private final Set<DayOfWeek> timetable;

    public Group(String title, Age age, int duration) {
        this.title = title;
        this.age = age;
        this.duration = duration;
        timetable = new HashSet<>();
    }

    public void addDay(DayOfWeek dayOfWeek) {
        timetable.add(dayOfWeek);
    }

    public boolean checkDay(DayOfWeek dayOfWeek) {
        return timetable.contains(dayOfWeek);
    }

    public String getTitle() {
        return title;
    }

    public Age getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }
}
