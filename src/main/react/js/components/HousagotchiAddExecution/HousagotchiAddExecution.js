import React from "react";

import moment from "moment";
import {connect} from "react-redux";
import * as executionActions from "../../actions/executionActions";
import {bindActionCreators} from "redux";
import toastr from "toastr";

class HousagotchiAddExecution extends React.Component {

    constructor(props) {
        super(props);
        if (!props.recurringTasks) {
            props.recurringTasks = [];
        }
        console.log("HousagotchiAddExecution.constructor: " , props);
        this.state = {
            date: moment().format("YYYY-MM-DD"),
            recurringTaskId: props.recurringTaskId
        };

        this.handleChangeOfTask = this.handleChangeOfTask.bind(this);
        this.handleChangeOfDate = this.handleChangeOfDate.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentWillReceiveProps(newProps) {
        console.log("HousagotchiAddExecution.componentWillReceiveProps: " , newProps);
        if (this.props.recurringTasks.length === 0 && newProps.recurringTasks.length > 0) {
            this.setState({
                date: this.state.date,
                recurringTaskId: newProps.recurringTaskId
            });
        }
    }

    handleChangeOfTask(event) {
        this.setState({recurringTaskId: event.target.value});
    }

    handleChangeOfDate(event) {
        this.setState({date: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.actions.addExecution({
            recurringTaskId: this.state.recurringTaskId,
            date: this.state.date
        }).catch(error => toastr.error(error));
    }

    render() {
        return (
            <div className="add-execution">
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="task">What did you do?</label>
                        <select className="form-control"
                                id="task"
                                value={this.state.recurringTaskId}
                                onChange={this.handleChangeOfTask}>

                            {
                                this.props.recurringTasks.map( recurringTask => {
                                    return <option key={recurringTask.id}
                                                   value={recurringTask.id}>
                                            {recurringTask.name}
                                        </option>
                                })
                            }

                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="date">When did you do it?</label>
                        <input type="date"
                               id="date"
                               className="form-control"
                               value={this.state.date} onChange={this.handleChangeOfDate} />
                    </div>
                    <input className="btn btn-primary" type="submit" value="Submit" />
                </form>
            </div>
        );
    };
}


//configure redux
const mapStateToProps = (state, ownProps) => {
    console.log("HousagotchiAddExecution.new state: ", state);
    return {
        recurringTasks: state.recurringTasks,
        recurringTaskId: state.recurringTasks.length > 0 ? state.recurringTasks[0].id : undefined
    };
};

const mapDispatchToProps = (dispatch) => {
    return {
        actions: bindActionCreators(executionActions, dispatch)
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(HousagotchiAddExecution);