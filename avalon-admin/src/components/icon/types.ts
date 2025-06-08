/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/18
 */
import {definePropType} from "../../util/propUtils.ts";

export type IconType = 'fas' | 'fab' | 'far'
export const borderStyleType = definePropType<'round' | 'bottom'>(String)
export type IconSizeType =
    '2xs'
    | 'xs'
    | 'sm'
    | 'lg'
    | 'xl'
    | '2xl'
    | '1x'
    | '2x'
    | '3x'
    | '4x'
    | '5x'
    | '6x'
    | '7x'
    | '8x'
    | '9x'
    | '10x'

export type IconFlipType = 'horizontal' | 'vertical' | 'both'