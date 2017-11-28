import * as types from "../actions/actionTypes";
import cloneDeep from "lodash.clonedeep";
import {initialState} from "../store/initialState";

export default function executionReducer(state = initialState.recurringTasks , action) {
    const copyOfState = cloneDeep(state);

    switch (action.type) {

        case types.ADD_EXECUTION_SUCCESS:
            copyOfState.forEach(task => {

                if (task.id === action.execution.recurringTaskId
                    && task.lastExecution.isBefore(action.execution.date)) {
                    task.lastExecution = action.execution.date;
                }

            });
            return copyOfState;

        default:
            return state;
    }
};