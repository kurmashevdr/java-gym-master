package ru.yandex.practicum.gym;

import java.util.Objects;

public class CounterCoachSessions implements Comparable<CounterCoachSessions> {
    private final Coach coach;
    private int count;

    public CounterCoachSessions(Coach coach) {
        this.coach = coach;
        this.count = 0;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    public void addSession() {
        count++;
    }

    @Override
    public int compareTo(CounterCoachSessions o) {
        return Integer.compare(count, o.count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterCoachSessions that = (CounterCoachSessions) o;
        return coach.equals(that.getCoach()) && count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, count);
    }

    @Override
    public String toString() {
        return "CounterCoachSessions{" + "coach=" + coach + ", count=" + count + '}';
    }
}
