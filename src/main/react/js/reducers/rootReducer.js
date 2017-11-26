import {combineReducers} from "redux";
import executionReducer from "./executionReducer";
import recurringTaskReducer from "./recurringTaskReducer";

const rootReducer = combineReducers({
    executions: executionReducer,
    recurringTasks: recurringTaskReducer
});

export default rootReducer;