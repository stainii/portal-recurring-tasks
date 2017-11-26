import * as types from "./actionTypes";

export function addExecutionSuccess(execution) {
    return {
        type: types.ADD_EXECUTION_SUCCESS,
        execution: execution
    };
}

export function addExecution(execution) {
    let dateAsISOString = execution.date + "T00:00:00";
    let endpoint = "/api/recurring-task/" + execution.recurringTaskId + "/execution/";

    return (dispatch) => {
        return fetch(endpoint, {
            method: "POST",
            body: JSON.stringify({date: dateAsISOString}),
            headers: new Headers({'content-type': 'application/json'})
        }).then(() => dispatch(addExecutionSuccess(execution)));
    };
}