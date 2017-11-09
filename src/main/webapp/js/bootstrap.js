/** used solely to run this module separately in its own lite server.
 * Will not be packaged in the js that is loaded in the front end core module.
 */

import React from "react";
import ReactDOM from "react-dom";
import Housagotchi from "./app";


(function() {
    ReactDOM.render(
        <Housagotchi />,
        document.getElementById("main")
    );
})();