import expect from "expect";
import recurringTaskReducer from "../../js/reducers/recurringTaskReducer";
import * as actionTypes from "../../js/actions/actionTypes";

describe("Recurring task reducer", () => {

    it(`should update the last execution of a task 
        if passing ADD_EXECUTION_SUCCESS 
        and the next execution is later than the latest execution 
        case 1`, () => {
        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-07"
            },
        ];

        const action = {
            type: actionTypes.ADD_EXECUTION_SUCCESS,
            execution: {
                recurringTaskId: 1,
                date: "2017-11-08"
            }
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState[0].lastExecution).toBe("2017-11-08");
        expect(newState[1].lastExecution).toBe("2017-11-07");
    });

    it(`should update the last execution of a task 
        if passing ADD_EXECUTION_SUCCESS 
        and the next execution is later than the latest execution 
        case 2`, () => {
        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-07"
            },
        ];

        const action = {
            type: actionTypes.ADD_EXECUTION_SUCCESS,
            execution: {
                recurringTaskId: 2,
                date: "2017-11-08"
            }
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState[0].lastExecution).toBe("2017-11-01");
        expect(newState[1].lastExecution).toBe("2017-11-08");
    });

    it(`should update the last execution of a task 
        if passing ADD_EXECUTION_SUCCESS
        and the latest execution is undefined`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: undefined
            },
        ];

        const action = {
            type: actionTypes.ADD_EXECUTION_SUCCESS,
            execution: {
                recurringTaskId: 2,
                date: "2017-11-08"
            }
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState[0].lastExecution).toBe("2017-11-01");
        expect(newState[1].lastExecution).toBe("2017-11-08");

    });

    it(`should not update the last execution of a task 
        if passing ADD_EXECUTION_SUCCESS 
        and the next execution is earlier than the latest execution`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            },
        ];

        const action = {
            type: actionTypes.ADD_EXECUTION_SUCCESS,
            execution: {
                recurringTaskId: 2,
                date: "2017-11-08"
            }
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState[0].lastExecution).toBe("2017-11-01");
        expect(newState[1].lastExecution).toBe("2017-11-29");
    });

    it(`should not update the last execution of a task 
        if passing something else than ADD_EXECUTION_SUCCESS`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            },
        ];

        const action = {
            type: "SOMETHING_ELSE",
            execution: {
                recurringTaskId: 2,
                date: "2017-11-08"
            }
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState[0].lastExecution).toBe("2017-11-01");
        expect(newState[1].lastExecution).toBe("2017-11-29");
    });

    it(`should return all tasks 
        when passing FIND_ALL_RECURRING_TASKS_SUCCESS`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }
        ];

        const allRecurringTasks = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-28"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }, {
                id: 3,
                name: "Laundry",
                lastExecution: "2017-11-29"
            }
        ];

        const action = {
            type: actionTypes.FIND_ALL_RECURRING_TASKS_SUCCESS,
            recurringTasks: allRecurringTasks
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState).toBe(allRecurringTasks);
    });

    it(`should not update the last execution of a task 
        if passing something else than ADD_EXECUTION_SUCCESS`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            },
        ];

        const action = {
            type: "SOMETHING_ELSE",
            execution: {
                recurringTaskId: 2,
                date: "2017-11-08"
            }
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState[0].lastExecution).toBe("2017-11-01");
        expect(newState[1].lastExecution).toBe("2017-11-29");
    });

    it(`should add the new task to the state 
        when passing CREATE_RECURRING_TASK_SUCCESS`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }
        ];

        const newTask = {
                id: 3,
                name: "Laundry",
                lastExecution: "2017-11-29"
            };

        const action = {
            type: actionTypes.CREATE_RECURRING_TASK_SUCCESS,
            recurringTask: newTask
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState.length).toBe(3);
        expect(newState[0]).toEqual(initialState[0]);
        expect(newState[1]).toEqual(initialState[1]);
        expect(newState[2]).toBe(newTask);
    });

    it(`should copy the update of the task to the state 
        when passing UPDATE_RECURRING_TASK_SUCCESS,
        case 1`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }
        ];

        const updatedTask = {
            id: 1,
            name: "Ironing (while watching Pluralsight videos)",
            lastExecution: "2017-11-29"
        };

        const action = {
            type: actionTypes.UPDATE_RECURRING_TASK_SUCCESS,
            recurringTask: updatedTask
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState.length).toBe(2);
        expect(newState[0]).toBe(updatedTask);
        expect(newState[1]).toEqual(initialState[1]);
    });

    it(`should copy the update of the task to the state 
        when passing UPDATE_RECURRING_TASK_SUCCESS,
        case 2`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }
        ];

        const updatedTask = {
            id: 2,
            name: "Doing the dishes",
            lastExecution: "2017-11-29"
        };

        const action = {
            type: actionTypes.UPDATE_RECURRING_TASK_SUCCESS,
            recurringTask: updatedTask
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState.length).toBe(2);
        expect(newState[0]).toEqual(initialState[0]);
        expect(newState[1]).toBe(updatedTask);
    });

    it(`should delete the right task 
        when passing DELETE_RECURRING_TASK_SUCCESS,
        case 1`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }
        ];

        const action = {
            type: actionTypes.DELETE_RECURRING_TASK_SUCCESS,
            recurringTaskId: 1
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState.length).toBe(1);
        expect(newState[0]).toEqual(initialState[1]);
    });

    it(`should delete the right task 
        when passing DELETE_RECURRING_TASK_SUCCESS,
        case 2`, () => {

        const initialState = [
            {
                id: 1,
                name: "Ironing",
                lastExecution: "2017-11-01"
            }, {
                id: 2,
                name: "Dishes",
                lastExecution: "2017-11-29"
            }
        ];

        const action = {
            type: actionTypes.DELETE_RECURRING_TASK_SUCCESS,
            recurringTaskId: 2
        };

        const newState = recurringTaskReducer(initialState, action);

        expect(newState.length).toBe(1);
        expect(newState[0]).toEqual(initialState[0]);
    });
});