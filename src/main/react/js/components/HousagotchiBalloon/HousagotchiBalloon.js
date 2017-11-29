import React from "react";

const HousagotchiBalloon = ({children}) => {
    return (
        <div className="housagotchi-balloon">
            <p className="triangle-border top">{children}</p>
        </div>
    )
};

export default HousagotchiBalloon;