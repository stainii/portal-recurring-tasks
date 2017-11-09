import React from "react";

class HousagotchiBalloon extends React.Component {

    constructor(props) {
        super(props);
        this.state = { text: props.text }
    }

    render() {
        return (
            <div className="housagotchi-balloon">
                <p className="triangle-border top">{this.state.text}</p>
            </div>
        );
    }
}

export default HousagotchiBalloon;