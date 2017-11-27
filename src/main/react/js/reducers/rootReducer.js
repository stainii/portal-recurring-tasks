import {combineReducers} from "redux";
import executionReducer from "./executionReducer";
import recurringTaskReducer from "./recurringTaskReducer";
import ajaxCallReducer from "./ajaxCallReducer";

const rootReducer = combineReducers({
    executions: executionReducer,
    recurringTasks: recurringTaskReducer,
    numberOfAjaxCallsInProgress: ajaxCallReducer
});

export default rootReducer;