import NProgress from "nprogress";
import React from "react";
import {connect} from "react-redux";

class LoadingBarComponent extends React.Component {


    componentDidMount() {
        if (this.props.loading) {
            NProgress.start();
        }
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.loading && !this.props.loading) {
            NProgress.start();
        } else if (!nextProps.loading && this.props.loading) {
            NProgress.done();
        }
    }

    render() {
        return false;
    }

}

//configure redux
const mapStateToProps = (state, ownProps) => {
    return {
        loading: state.numberOfAjaxCallsInProgress > 0
    };
};

export default connect(mapStateToProps)(LoadingBarComponent);