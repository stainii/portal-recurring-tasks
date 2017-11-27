import * as types from "./actionTypes";

export function ajaxCallStarted() {
    return {
        type: types.AJAX_CALL_STARTED
    };
}

export function ajaxCallEnded() {
    return {
        type: types.AJAX_CALL_ENDED
    };
}