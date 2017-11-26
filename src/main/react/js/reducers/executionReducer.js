import * as types from "../actions/actionTypes";
import cloneDeep from "lodash.clonedeep";

export default function executionReducer(state = [] , action) {
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