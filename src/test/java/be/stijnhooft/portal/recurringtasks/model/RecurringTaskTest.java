package be.stijnhooft.portal.recurringtasks.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecurringTaskTest {

    @Test(expected = NullPointerException.class)
    public void constructorWhenNameIsNull() {
        new RecurringTask(null, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWhenMinNumberOfDaysBetweenExecutionsIsNegative() {
        new RecurringTask("test", -1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWhenMinNumberOfDaysBetweenExecutionsIsZero() {
        new RecurringTask("test", 0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWhenMaxNumberOfDaysBetweenExecutionsIsNegative() {
        new RecurringTask("test", 1, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWhenMaxNumberOfDaysBetweenExecutionsIsZero() {
        new RecurringTask("test", 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWhenMaxNumberOfDaysBetweenExecutionsIsSmallerThanMinNumberOfDaysBetweenExecutions() {
        new RecurringTask("test", 3, 2);
    }

    @Test
    public void constructorWhenMaxNumberOfDaysBetweenExecutionsIsEqualsToMinNumberOfDaysBetweenExecutions() {
        RecurringTask recurringTask = new RecurringTask("test", 3, 3);

        assertEquals("test", recurringTask.getName());
        assertEquals(3, recurringTask.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTask.getMaxNumberOfDaysBetweenExecutions());    }

    @Test
    public void constructorWhenSuccess() {
        RecurringTask recurringTask = new RecurringTask("test", 3, 5);

        assertEquals("test", recurringTask.getName());
        assertEquals(3, recurringTask.getMinNumberOfDaysBetweenExecutions());
        assertEquals(5, recurringTask.getMaxNumberOfDaysBetweenExecutions());
    }


    @Test(expected = NullPointerException.class)
    public void updateWhenNameIsNull() {
        new RecurringTask().update(null, 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWhenMinNumberOfDaysBetweenExecutionsIsNegative() {
        new RecurringTask().update("test", -1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWhenMinNumberOfDaysBetweenExecutionsIsZero() {
        new RecurringTask().update("test", 0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWhenMaxNumberOfDaysBetweenExecutionsIsNegative() {
        new RecurringTask().update("test", 1, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWhenMaxNumberOfDaysBetweenExecutionsIsZero() {
        new RecurringTask().update("test", 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWhenMaxNumberOfDaysBetweenExecutionsIsSmallerThanMinNumberOfDaysBetweenExecutions() {
        new RecurringTask().update("test", 3, 2);
    }

    @Test
    public void updateWhenMaxNumberOfDaysBetweenExecutionsIsEqualsToMinNumberOfDaysBetweenExecutions() {
        RecurringTask recurringTask = new RecurringTask("testietest", 1, 2);
        recurringTask.update("test", 3, 3);

        assertEquals("test", recurringTask.getName());
        assertEquals(3, recurringTask.getMinNumberOfDaysBetweenExecutions());
        assertEquals(3, recurringTask.getMaxNumberOfDaysBetweenExecutions());
    }

    @Test
    public void updateWhenSuccess() {
        RecurringTask recurringTask = new RecurringTask("testietest", 1, 2);
        recurringTask.update("test", 3, 5);

        assertEquals("test", recurringTask.getName());
        assertEquals(3, recurringTask.getMinNumberOfDaysBetweenExecutions());
        assertEquals(5, recurringTask.getMaxNumberOfDaysBetweenExecutions());
    }


}