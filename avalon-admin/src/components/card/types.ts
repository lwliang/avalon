/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/21
 */
export type CardShadow = 'always' | 'hover' | 'never'

export interface CardProps {
  shadow?: CardShadow
  width?: string | number
  round?: boolean    // 是否有圆角
  border?: boolean   // 是否带边框
}


