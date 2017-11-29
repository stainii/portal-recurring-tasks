import {combineReducers} from "redux";
import recurringTaskReducer from "./recurringTaskReducer";
import ajaxCallReducer from "./ajaxCallReducer";

const rootReducer = combineReducers({
    recurringTasks: recurringTaskReducer,
    numberOfAjaxCallsInProgress: ajaxCallReducer
});

export default rootReducer;