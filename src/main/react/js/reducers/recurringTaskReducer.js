import * as types from "../actions/actionTypes";
import {cloneDeep, findIndex} from "lodash";

export default function recurringTaskReducer(state = [] , action) {
    const copyOfState = cloneDeep(state);

    switch (action.type) {

        case types.FIND_ALL_RECURRING_TASKS_SUCCESS:
            return action.recurringTasks;

        case types.CREATE_RECURRING_TASK_SUCCESS:
            copyOfState.push(action.recurringTask);
            return copyOfState;

        case types.UPDATE_RECURRING_TASK_SUCCESS:
            let indexOfUpdatedRecurringTask = findIndex(copyOfState, {id: action.recurringTask.id});
            copyOfState[indexOfUpdatedRecurringTask] =  action.recurringTask;
            return copyOfState;

        case types.DELETE_RECURRING_TASK_SUCCESS:
            return remove(copyOfState, { id: action.recurringTaskId });

        default:
            return state;
    }
};