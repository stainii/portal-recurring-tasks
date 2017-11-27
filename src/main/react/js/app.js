import React from "react";
import ReactDOM from "react-dom";
import HousagotchiCreature from "./components/HousagotchiCreature/HousagotchiCreature";
import HousagotchiAddExecution from "./components/HousagotchiAddExecution/HousagotchiAddExecution";
import HousagotchiManageRecurringTasks from "./components/HousagotchiManageRecurringTasks/HousagotchiManageRecurringTasks";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import configureStore from "./store/configureStore";
import {Provider} from "react-redux";
import {findAllRecurringTasks} from "./actions/recurringTaskActions";
import LoadingBarComponent from "./components/LoadingBarComponent/LoadingBarComponent";

class Housagotchi extends React.Component {
    render() {
        return (
            <div className="housagotchi-component">

                <LoadingBarComponent/>

                <HousagotchiCreature/>

                <Tabs className="tabs">
                    <TabList>
                        <Tab>Add execution</Tab>
                        <Tab>Manage tasks</Tab>
                    </TabList>
                    <TabPanel>
                        <HousagotchiAddExecution />
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

    const store = configureStore();
    store.dispatch(findAllRecurringTasks());

    ReactDOM.render(

        <Provider store={store}>
            <Housagotchi />
        </Provider>,

        document.getElementById("main")
    );
})();