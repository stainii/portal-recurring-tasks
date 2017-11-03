import React from "react";
import HousagotchiCreature from "./components/HousagotchiCreature/HousagotchiCreature";
import AddExecution from "./components/AddExecution/AddExecution";

class Housagotchi extends React.Component {
    render() {
        return (
            <div className="housagotchi-component">
                <HousagotchiCreature/>
                <AddExecution/>
            </div>
        );
    }
}

window.Housagotchi = Housagotchi;