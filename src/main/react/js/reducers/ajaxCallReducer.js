import * as types from "../actions/actionTypes";
import {initialState} from "../store/initialState";

export default function ajaxCallReducer(state = initialState.numberOfAjaxCallsInProgress , action) {

    switch (action.type) {
        case types.AJAX_CALL_STARTED:
            return state + 1;
        case types.AJAX_CALL_ENDED:
            return state - 1;
        default:
            return state;
    }

};