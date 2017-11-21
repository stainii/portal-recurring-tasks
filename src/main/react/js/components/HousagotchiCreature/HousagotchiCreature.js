import React from "react";
import HousagotchiBalloon from "../HousagotchiBalloon/HousagotchiBalloon";

class HousagotchiCreature extends React.Component {

    constructor(props) {
        super(props);
        this.state = { mood: "happy"}
    }
    render() {
        return (
            <div className="housagotchi-creature">
                <img src={"/static/imgs/creature/" + this.state.mood + ".png"} />
                <HousagotchiBalloon text="Hey! Nothing to do!! I'm happy!!!"/>
            </div>
        );
    }
}

export default HousagotchiCreature;