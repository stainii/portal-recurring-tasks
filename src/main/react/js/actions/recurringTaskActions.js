import * as types from "./actionTypes";
import "whatwg-fetch";

export function findAllRecurringTasksSuccess(recurringTasks) {
    return {
        type: types.FIND_ALL_RECURRING_TASKS_SUCCESS,
        recurringTasks: recurringTasks
    };
}

export function createRecurringTaskSuccess(recurringTask) {
    return {
        type: types.CREATE_RECURRING_TASK_SUCCESS,
        recurringTask: recurringTask
    };
}

export function updateRecurringTaskSuccess(recurringTask) {
    return {
        type: types.UPDATE_RECURRING_TASK_SUCCESS,
        recurringTask: recurringTask
    };
}

export function deleteRecurringTaskSuccess(recurringTaskId) {
    return {
        type: types.DELETE_RECURRING_TASK_SUCCESS,
        recurringTaskId: recurringTaskId
    };
}

export function findAllRecurringTasks() {
    return (dispatch) => {
        return fetch("/api/recurring-task/")
            .then(response => response.json())
            .then(recurringTasks => dispatch(findAllRecurringTasksSuccess(recurringTasks)));
    };
}

export function createRecurringTask(recurringTask) {
    return (dispatch) => {
        return fetch("/api/recurring-task/", {
            method: "POST",
            body: JSON.stringify(recurringTask),
            headers: new Headers({'content-type': 'application/json'})
        }).then(() => dispatch(createRecurringTaskSuccess(recurringTask)));
    };
}

export function updateRecurringTask(recurringTask) {
    return (dispatch) => {
        return fetch("/api/recurring-task/" + recurringTask.id + "/", {
            method: "PUT",
            body: JSON.stringify(recurringTask),
            headers: new Headers({'content-type': 'application/json'})
        }).then(() => dispatch(updateRecurringTaskSuccess(recurringTask)));
    };
}

export function deleteRecurringTask(id) {
    return (dispatch) => {
        return fetch("/api/recurring-task/" + id + "/", {
            method: "DELETE"
        }).then(() => dispatch(deleteRecurringTaskSuccess(id)));
    };
}