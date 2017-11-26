import React from "react";
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as recurringTaskActions from "../../actions/recurringTaskActions";

class HousagotchiManageRecurringTasks extends React.Component {

    constructor(props) {
        super(props);

        this.getTasks = this.getTasks.bind(this);
        this.onAfterInsertRow = this.onAfterInsertRow.bind(this);
        this.onAfterDeleteRow = this.onAfterDeleteRow.bind(this);
        this.onBeforeSaveCell = this.onBeforeSaveCell.bind(this);
        this.onAfterSaveCell = this.onAfterSaveCell.bind(this);


        this.options = {
            afterInsertRow: this.onAfterInsertRow,
            afterDeleteRow: this.onAfterDeleteRow,
            beforeSaveCell: this.onBeforeSaveCell,
            afterSaveCell: this.onAfterSaveCell
        };

        this.cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.onBeforeSaveCell,
            afterSaveCell: this.onAfterSaveCell
        };
    }

    getTasks() {
        if (this.props.recurringTasks) {
            return this.props.recurringTasks;
        } else {
            return [];
        }
    }

    onAfterInsertRow(row) {
        this.props.actions.createRecurringTask(row);
    }

    onAfterDeleteRow(rowKeys) {
        const self = this;
        rowKeys.forEach(rowKey => self.props.actions.deleteRecurringTask(rowKey));
    }

    onAfterSaveCell(row, cellName, cellValue) {
        this.props.actions.updateRecurringTask(row);
    }

    onBeforeSaveCell(row, cellName, cellValue) {
        // You can do any validation on here for editing value,
        // return false for reject the editing
        return true;
    }

    render() {
        return (
            <div className="manage-recurring-tasks">
                <BootstrapTable data={this.getTasks()}
                                version='4'
                                striped
                                hover
                                cellEdit={ this.cellEditProp }
                                options={ this.options }
                                insertRow={ true }
                                deleteRow={ true }
                                selectRow={ {mode: "checkbox"} } >
                    <TableHeaderColumn isKey dataField='id'>ID</TableHeaderColumn>
                    <TableHeaderColumn dataField='name'>Task</TableHeaderColumn>
                    <TableHeaderColumn dataField='minNumberOfDaysBetweenExecutions'>Min # days</TableHeaderColumn>
                    <TableHeaderColumn dataField='maxNumberOfDaysBetweenExecutions'>Max # days</TableHeaderColumn>
                </BootstrapTable>
            </div>
        );
    };

}


//configure redux
const mapStateToProps = (state, ownProps) => {
    console.log("update state to ", state);
    return {
        recurringTasks: state.recurringTasks
    };
};

const mapDispatchToProps = (dispatch) => {
    return {
        actions: bindActionCreators(recurringTaskActions, dispatch)
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(HousagotchiManageRecurringTasks);