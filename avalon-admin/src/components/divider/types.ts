/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 10:52
 */

export type DividerTheme =
    | 'primary'
    | 'success'
    | 'warning'
    | 'danger'
    | 'info'
    | 'border';

export type DividerTextSize = 'lg' | 'default' | 'sm';

export interface DividerProps {
    direction?: 'horizontal' | 'vertical';
    borderStyle?:
        | 'none'
        | 'hidden'
        | 'dotted'
        | 'dashed'
        | 'solid'
        | 'double'
        | 'groove'
        | 'ridge'
        | 'inset'
        | 'outset';
    contentPosition?: 'left' | 'center' | 'right';
    theme?: DividerTheme;
    textSize?: DividerTextSize;
}
