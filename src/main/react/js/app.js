import React from "react";
import ReactDOM from "react-dom";
import HousagotchiCreature from "./components/HousagotchiCreature/HousagotchiCreature";
import HousagotchiAddExecution from "./components/HousagotchiAddExecution/HousagotchiAddExecution";
import HousagotchiManageRecurringTasks from "./components/HousagotchiManageRecurringTasks/HousagotchiManageRecurringTasks";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';

class Housagotchi extends React.Component {
    render() {
        return (
            <div className="housagotchi-component">
                <HousagotchiCreature/>
                <Tabs className="tabs">
                    <TabList>
                        <Tab>Add execution</Tab>
                        <Tab>Manage tasks</Tab>
                    </TabList>
                    <TabPanel>
                        <HousagotchiAddExecution/>
                    </TabPanel>
                    <TabPanel>
                        <HousagotchiManageRecurringTasks/>
                    </TabPanel>
                </Tabs>
            </div>
        );
    }
}


(function() {
    ReactDOM.render(
        <Housagotchi />,
        document.getElementById("main")
    );
})();