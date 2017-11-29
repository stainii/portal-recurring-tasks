import expect from "expect";
import mapStateToPropsForHousagotchiCreature, {
    Mood,
    NO_TASKS_MESSAGE
} from "../../js/mappers/StateToPropsForHousagotchiCreatureMapper";
import moment from "moment";

describe("State to props for Housagotchi creature mapper", () => {

    it(`should have very late tasks and a mad mood`, () => {
        const state = {
            recurringTasks: [{
                id: 1,
                name: "Cleaning the kitchen",
                minNumberOfDaysBetweenExecutions: 5,
                maxNumberOfDaysBetweenExecutions: 7,
                lastExecution: moment().subtract(10, "days")
            }, {
                id: 2,
                name: "Cleaning the toilet",
                minNumberOfDaysBetweenExecutions: 2,
                maxNumberOfDaysBetweenExecutions: 4,
                lastExecution: moment().subtract(5, "days")
            }]
        };

        let mappedState = mapStateToPropsForHousagotchiCreature(state);

        expect(mappedState.normalMessages).toEqual([]);
        expect(mappedState.urgentMessages.length).toBe(2);
        expect(mappedState.urgentMessages[0]).toBe("Cleaning the kitchen");
        expect(mappedState.urgentMessages[1]).toBe("Cleaning the toilet");
        expect(mappedState.mood).toBe(Mood.MAD);
    });

    it(`should have late and very late tasks and a mad mood`, () => {
        const state = {
            recurringTasks: [{
                id: 1,
                name: "Cleaning the kitchen",
                minNumberOfDaysBetweenExecutions: 5,
                maxNumberOfDaysBetweenExecutions: 7,
                lastExecution: moment().subtract(10, "days")
            }, {
                id: 2,
                name: "Cleaning the toilet",
                minNumberOfDaysBetweenExecutions: 3,
                maxNumberOfDaysBetweenExecutions: 5,
                lastExecution: moment().subtract(4, "days")
            }]
        };

        let mappedState = mapStateToPropsForHousagotchiCreature(state);

        expect(mappedState.normalMessages.length).toBe(1);
        expect(mappedState.normalMessages[0]).toBe("Cleaning the toilet");
        expect(mappedState.urgentMessages.length).toBe(1);
        expect(mappedState.urgentMessages[0]).toEqual("Cleaning the kitchen");
        expect(mappedState.mood).toBe(Mood.MAD);
    });

    it(`should have late tasks and a attention mood`, () => {
        const state = {
            recurringTasks: [{
                id: 1,
                name: "Cleaning the kitchen",
                minNumberOfDaysBetweenExecutions: 5,
                maxNumberOfDaysBetweenExecutions: 7,
                lastExecution: moment().subtract(6, "days")
            }]
        };

        let mappedState = mapStateToPropsForHousagotchiCreature(state);

        expect(mappedState.normalMessages.length).toBe(1);
        expect(mappedState.normalMessages[0]).toBe("Cleaning the kitchen");
        expect(mappedState.urgentMessages).toEqual([]);
        expect(mappedState.mood).toBe(Mood.ATTENTION);
    });

    it(`should have no late nor very tasks and a happy mood`, () => {
        const state = {
            recurringTasks: [{
                id: 1,
                name: "Cleaning the attic",
                minNumberOfDaysBetweenExecutions: 10,
                maxNumberOfDaysBetweenExecutions: 30,
                lastExecution: moment().subtract(2, "days")
            }]
        };

        let mappedState = mapStateToPropsForHousagotchiCreature(state);

        expect(mappedState.normalMessages).toEqual([NO_TASKS_MESSAGE]);
        expect(mappedState.urgentMessages).toEqual([]);
        expect(mappedState.mood).toBe("happy");
    });

    it(`should have no late nor very tasks and a happy mood
        when no tasks are defined`, () => {
        const state = {
            recurringTasks: []
        };

        let mappedState = mapStateToPropsForHousagotchiCreature(state);

        expect(mappedState.normalMessages).toEqual([NO_TASKS_MESSAGE]);
        expect(mappedState.urgentMessages).toEqual([]);
        expect(mappedState.mood).toBe(Mood.HAPPY);
    });

    it(`should ignore a task that has never been executed`, () => {
        const state = {
            recurringTasks: [{
                id: 1,
                name: "Cleaning the attic",
                minNumberOfDaysBetweenExecutions: 10,
                maxNumberOfDaysBetweenExecutions: 30,
                lastExecution: undefined
            }]
        };

        let mappedState = mapStateToPropsForHousagotchiCreature(state);

        expect(mappedState.normalMessages).toEqual([NO_TASKS_MESSAGE]);
        expect(mappedState.urgentMessages).toEqual([]);
        expect(mappedState.mood).toBe(Mood.HAPPY);
    });

});