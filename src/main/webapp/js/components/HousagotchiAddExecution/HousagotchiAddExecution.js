import React from "react";

import moment from "moment";

class HousagotchiAddExecution extends React.Component {

    constructor(props) {
        super(props);
        this.state = {task: '', date: moment().format("YYYY-MM-DD")};

        this.handleChangeOfTask = this.handleChangeOfTask.bind(this);
        this.handleChangeOfDate = this.handleChangeOfDate.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChangeOfTask(event) {
        this.setState({task: event.target.value});
    }

    handleChangeOfDate(event) {
        this.setState({date: event.target.value});
    }

    handleSubmit(event) {
        alert(this.state.task + " was executed at " + this.state.date);
        event.preventDefault();
    }

    render() {
        return (
            <div className="add-execution">
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="task">What did you do?</label>
                        <select className="form-control"
                                id="task"
                                value={this.state.task}
                                onChange={this.handleChangeOfTask}>
                            <option value="grapefruit">Grapefruit</option>
                            <option value="lime">Lime</option>
                            <option value="coconut">Coconut</option>
                            <option value="mango">Mango</option>
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

export default HousagotchiAddExecution;