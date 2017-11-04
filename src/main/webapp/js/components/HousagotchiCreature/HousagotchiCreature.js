import React from "react";

class HousagotchiCreature extends React.Component {

    constructor(props) {
        super(props);
        this.state = { mood: "happy"}
    }
    render() {
        return (
            <div className="housagotchi-creature">
                <img src={"/portal/module/housagotchi/static/imgs/creature/" + this.state.mood + ".png"} />
            </div>
        );
    }
}

export default HousagotchiCreature;