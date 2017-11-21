import React from "react";
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';

class HousagotchiManageRecurringTasks extends React.Component {

    constructor(props) {
        super(props);
        this.state = {name: '', minNumberOfDaysBetweenExecutions: 7, maxNumberOfDaysBetweenExecutions: 14};


        this.options = {
            afterInsertRow: this.onAfterInsertRow,
            afterDeleteRow: this.onAfterDeleteRow,
            afterDeleteRow: this.onAfterDeleteRow,
            beforeSaveCell: this.onBeforeSaveCell, // a hook for before saving cell
            afterSaveCell: this.onAfterSaveCell  // a hook for after saving cell
        };

        this.cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.onBeforeSaveCell, // a hook for before saving cell
            afterSaveCell: this.onAfterSaveCell  // a hook for after saving cell
        };

        this.getTasks = this.getTasks.bind(this);
        this.onAfterInsertRow = this.onAfterInsertRow.bind(this);
    }

    getTasks() {
        return [{
            id: 1,
            name: "Task 1",
            minNumberOfDaysBetweenExecutions: 7,
            maxNumberOfDaysBetweenExecutions: 14,
        }, {
            id: 2,
            name: "Task 2",
            minNumberOfDaysBetweenExecutions: 5,
            maxNumberOfDaysBetweenExecutions: 9,
        }];
    }

    onAfterInsertRow(row) {
        let newRowStr = '';

        for (const prop in row) {
            newRowStr += prop + ': ' + row[prop] + ' \n';
        }
        alert('The new row is:\n ' + newRowStr);
    }

    onAfterDeleteRow(rowKeys) {
        alert('The rowkey you drop: ' + rowKeys);
    }

    onAfterSaveCell(row, cellName, cellValue) {
        alert(`Save cell ${cellName} with value ${cellValue}`);

        let rowStr = '';
        for (const prop in row) {
            rowStr += prop + ': ' + row[prop] + '\n';
        }

        alert('The whole row :\n' + rowStr);
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

export default HousagotchiManageRecurringTasks;