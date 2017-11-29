import * as types from "../actions/actionTypes";
import {cloneDeep, findIndex, remove} from "lodash";
import {initialState} from "../store/initialState";
import moment from "moment";

export default function recurringTaskReducer(state =  initialState.recurringTasks , action) {
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
            remove(copyOfState, { id: action.recurringTaskId });
            return copyOfState;

        case types.ADD_EXECUTION_SUCCESS:
            for (let task of copyOfState) {

                let newExecutionIsLaterThanLatestKnownExecution =
                    task.lastExecution == undefined
                    || moment(task.lastExecution).isBefore(moment(action.execution.date));

                if (task.id == action.execution.recurringTaskId
                    && newExecutionIsLaterThanLatestKnownExecution) {
                    task.lastExecution = action.execution.date;
                }

            }
            return copyOfState;

        default:
            return state;
    }
};