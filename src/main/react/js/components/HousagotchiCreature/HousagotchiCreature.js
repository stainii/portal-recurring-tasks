import React from "react";
import HousagotchiBalloon from "../HousagotchiBalloon/HousagotchiBalloon";
import {connect} from "react-redux";
import mapStateToPropsForHousagotchiCreature from "../../mappers/StateToPropsForHousagotchiCreatureMapper";
import "./HousagotchiCreature.scss";

const HousagotchiCreature = ({urgentMessages, normalMessages, mood}) => {

    return (
        <div className="housagotchi-creature">
            <img src={"./imgs/creature/" + mood + ".png"} />
            <HousagotchiBalloon>
                {
                    urgentMessages.map(message => {
                        return <p key={message} className="urgent">{message}</p>
                    })
                }
                {
                    normalMessages.map(message => {
                        return <p key={message}>{message}</p>
                    })
                }
            </HousagotchiBalloon>
        </div>
    );
};

export default connect(mapStateToPropsForHousagotchiCreature)(HousagotchiCreature);