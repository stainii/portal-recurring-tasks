import React from "react";
import HousagotchiCreature from "./components/HousagotchiCreature/HousagotchiCreature";
import AddExecution from "./components/AddExecution/AddExecution";
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
                        <AddExecution/>
                    </TabPanel>
                    <TabPanel>
                        <h2>Manage tasks: todo</h2>
                    </TabPanel>
                </Tabs>
            </div>
        );
    }
}

window.Housagotchi = Housagotchi;

export default Housagotchi;