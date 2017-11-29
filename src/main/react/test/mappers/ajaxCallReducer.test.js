import expect from "expect";
import * as actionTypes from "../../js/actions/actionTypes";
import ajaxCallReducer from "../../js/reducers/ajaxCallReducer";

describe("Ajax call reducer", () => {

    it(`should increment update the number of current ajax calls by 1
        if passing AJAX_CALL_STARTED`, () => {
        const initialState = 2;
        const action = {
            type: actionTypes.AJAX_CALL_STARTED
        };
        const newState = ajaxCallReducer(initialState, action);

        expect(newState).toBe(3);
    });

    it(`should decrease update the number of current ajax calls by 1
        if passing AJAX_CALL_STARTED`, () => {
        const initialState = 5;
        const action = {
            type: actionTypes.AJAX_CALL_ENDED
        };
        const newState = ajaxCallReducer(initialState, action);

        expect(newState).toBe(4);
    });

    it(`should not update the number of current ajax calls 
        if passing something else than AJAX_CALL_STARTED or AJAX_CALL_ENDED`, () => {

        const initialState = 7;
        const action = {
            type: "SOMETHING_ELSE"
        };
        const newState = ajaxCallReducer(initialState, action);

        expect(newState).toBe(initialState);
    });

});