package com.avalon.core.model;

import lombok.Data;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/07 10:07
 */
@Data
public class ChangeRecordRow {
    private RecordRow value; // 变更的记录
    private ChangeWarningList warnings; //警告

    public ChangeRecordRow() {
        value = RecordRow.build();
        warnings = new ChangeWarningList();
    }

    public void addField(String fieldName, Object value) {
        this.value.put(fieldName, value);
    }

    public void combineValue(RecordRow value) {
        this.value.putAll(value);
    }

    public void addWarning(ChangeWarning warning) {
        this.warnings.add(warning);
    }

    public void addWarnings(ChangeWarningList warnings) {
        this.warnings.addAll(warnings);
    }

    public void addWarning(String title, String message) {
        this.warnings.add(new ChangeWarning(title, message));
    }
}
