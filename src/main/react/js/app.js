import React from "react";
import HousagotchiCreature from "./components/HousagotchiCreature/HousagotchiCreature";
import HousagotchiAddExecution from "./components/HousagotchiAddExecution/HousagotchiAddExecution";
import HousagotchiManageRecurringTasks
    from "./components/HousagotchiManageRecurringTasks/HousagotchiManageRecurringTasks";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import configureStore from "./store/configureStore";
import {Provider} from "react-redux";
import {findAllRecurringTasks} from "./actions/recurringTaskActions";
import LoadingBarComponent from "./components/LoadingBarComponent/LoadingBarComponent";
import {register} from 'web-react-components';

import "./app.scss";


class Housagotchi extends React.Component {
    render() {
        return (
            <div>
                <link rel="stylesheet" href="./main.css" />

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
            </div>
        );
    }
}


const h = (function() {

    const store = configureStore();
    store.dispatch(findAllRecurringTasks());

    return <Provider store={store}>
            <Housagotchi />
        </Provider>;
});
register(h, 'app-housagotchi');