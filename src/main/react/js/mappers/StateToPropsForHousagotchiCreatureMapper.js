import moment from "moment";
import React from "react";

const now = moment();

export const Mood = {
    HAPPY: "happy",
    ATTENTION: "attention",
    MAD: "mad"
};

export const NO_TASKS_MESSAGE = "Hey! Nothing to do!! I'm happy!!!";

const calculateMood = (lateTasks, veryLateTasks) => {

    if (veryLateTasks.length > 0) {
        return Mood.MAD;
    } else if (lateTasks.length > 0) {
        return Mood.ATTENTION;
    } else {
        return Mood.HAPPY;
    }

};

export default function mapStateToPropsForHousagotchiCreature(state) {
    console.log("Creature has received new props!");
    const recurringTasks = state.recurringTasks;

    let lateTasks = [];
    let veryLateTasks = [];

    recurringTasks.forEach(recurringTask => {
        if (!recurringTask.lastExecution) {
            return;
        }

        if (moment(recurringTask.lastExecution)
                .add(recurringTask.maxNumberOfDaysBetweenExecutions, "days")
                .isBefore(now)) {

            veryLateTasks.push(recurringTask.name);

        } else if (moment(recurringTask.lastExecution)
                .add(recurringTask.minNumberOfDaysBetweenExecutions, "days")
                .isBefore(now)) {

            lateTasks.push(recurringTask.name);
        }

    });

    return {
        normalMessages: lateTasks.length > 0 || veryLateTasks.length > 0 ? lateTasks : [NO_TASKS_MESSAGE],
        urgentMessages: veryLateTasks,
        mood: calculateMood(lateTasks, veryLateTasks)
    };
}