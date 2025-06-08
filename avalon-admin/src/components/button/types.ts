/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {IconType} from "../icon/types.ts";

// 按钮类型
export type ButtonType = 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'default';

// 按钮尺寸
export type ButtonSize = 'large' | 'default' | 'small';

// 原生按钮类型
export type ButtonNativeType = 'button' | 'submit' | 'reset';

// 按钮点击 执行方式 action 切换页面，object 执行方法
export type ButtonActionType = 'object' | 'action'


// Button 组件 Props 类型
export interface ButtonProps {
    type?: ButtonType;
    size?: ButtonSize;
    disabled?: boolean;
    loading?: boolean;
    round?: boolean;
    circle?: boolean,
    plain?: boolean;
    icon?: string | [string, string];
    iconType?: IconType;
    iconRight?: string | [string, string];
    iconRightType?: IconType;
    nativeType?: ButtonNativeType;

    action?: string;
    actionType?: ButtonActionType;
}