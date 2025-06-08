/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export default class EditingCell {
    rowIndex: number;
    colIndex: number;

    constructor(rowIndex: number, colIndex: number) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }
}