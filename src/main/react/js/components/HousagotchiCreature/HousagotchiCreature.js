import React from "react";
import HousagotchiBalloon from "../HousagotchiBalloon/HousagotchiBalloon";
import {connect} from "react-redux";
import mapStateToPropsForHousagotchiCreature from "../../mappers/StateToPropsForHousagotchiCreatureMapper";

const HousagotchiCreature = ({urgentMessages, normalMessages, mood}) => {

    return (
        <div className="housagotchi-creature">
            <img src={"/static/imgs/creature/" + mood + ".png"} />
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