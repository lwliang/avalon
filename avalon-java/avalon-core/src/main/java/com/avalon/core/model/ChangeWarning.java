package com.avalon.core.model;

import lombok.Data;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/07 10:09
 */
@Data
public class ChangeWarning {
    private String message;
    private String title;

    public ChangeWarning(String message, String title) {
        this.message = message;
        this.title = title;
    }
}
