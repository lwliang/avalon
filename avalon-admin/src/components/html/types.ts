/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

/**
 * HTML编辑器组件 Props 类型定义
 */
export interface HtmlProps {
  /** 是否禁用编辑器 */
  disabled?: boolean

  /** 是否只读 */
  readonly?: boolean

  /** 占位符文本 */
  placeholder?: string

  /** 是否必填 */
  required?: boolean

  /** 编辑器高度 */
  height?: string | number

  /** 编辑器宽度 */
  width?: string | number

  /** 工具栏配置 */
  toolbar?: string | string[]

  /** 主题 */
  theme?: 'snow' | 'bubble'

  /** 是否显示字数统计 */
  showWordCount?: boolean

  /** 最大字数限制 */
  maxLength?: number

  /** 是否自动聚焦 */
  autoFocus?: boolean

  /** 编辑器配置选项 */
  options?: {
    modules?: {
      toolbar?: string | string[]
      history?: {
        delay?: number
        maxStack?: number
        userOnly?: boolean
      }
      clipboard?: {
        matchVisual?: boolean
      }
      keyboard?: {
        bindings?: any
      }
    }
    theme?: string
    placeholder?: string
    readOnly?: boolean
    bounds?: string | HTMLElement
    scrollingContainer?: string | HTMLElement
    tabIndex?: number
  }
} 