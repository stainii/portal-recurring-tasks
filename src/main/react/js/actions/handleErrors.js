import {ajaxCallEnded} from "./ajaxCallActions";

export function handleError(dispatch) {
    return (response) => {
        dispatch(ajaxCallEnded());
        console.log(response);
        throw response;
    };
}

export function checkHttpStatusCode(response) {
    if (response.ok) {
        return response;
    } else {
        throw new Error("Something went wrong. Please check the logs.");
    }
}