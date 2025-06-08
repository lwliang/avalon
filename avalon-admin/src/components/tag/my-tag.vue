<script setup lang="ts">
import {computed} from 'vue'
import type {TagProps} from './types'
import myIcon from '../icon/my-icon.vue'
import {useDark} from '@vueuse/core'

const props = withDefaults(defineProps<TagProps>(), {
  type: 'primary',
  effect: 'light',
  size: 'small',
  round: false,
  closable: false,
  color: ''
})
const isDark = useDark()

const emit = defineEmits<{
  (e: 'close', value: any): void
  (e: 'click', value: any): void
}>()

const typeMap: Record<string, string> = {
  primary: 'bg-primary text-white ',
  success: 'bg-success text-white ',
  info: 'bg-info text-white ',
  warning: 'bg-warning text-white ',
  danger: 'bg-danger text-white '
}
const typeMapLight: Record<string, string> = {
  primary: 'bg-primary-light-7 text-primary',
  success: 'bg-success-light-7 text-success ',
  info: 'bg-info-light-7 text-info ',
  warning: 'bg-warning-light-7 text-warning ',
  danger: 'bg-danger-light-7 text-danger '
}
const typeMapPlain: Record<string, string> = {
  primary: 'text-primary border-primary border-solid',
  success: ' text-success border-success border-solid',
  info: 'text-info border-info border-solid',
  warning: 'text-warning border-warning border-solid',
  danger: 'text-danger border-danger border-solid'
}

const closeBtnBgMap: Record<'dark' | 'light' | 'plain', Record<string, string>> = {
  dark: {
    primary: 'bg-primary text-white',
    success: 'bg-success text-white',
    info: 'bg-info text-white',
    warning: 'bg-warning text-white',
    danger: 'bg-danger text-white'
  },
  light: {
    primary: 'bg-primary-light-7 text-primary',
    success: 'bg-success-light-7 text-success',
    info: 'bg-info-light-7 text-info',
    warning: 'bg-warning-light-7 text-warning',
    danger: 'bg-danger-light-7 text-danger'
  },
  plain: {
    primary: 'text-primary border border-solid border-primary',
    success: 'text-success border border-solid border-success',
    info: 'text-info',
    warning: 'text-warning',
    danger: 'text-danger'
  }
}

const sizeMap: Record<string, string> = {
  large: 'text-lg py-1 px-4 h-8',
  default: 'text-default px-3 py-[2px] h-7',
  small: 'text-sm px-2 py-[1px] h-6'
}

const roundClass = computed(() => props.round ? 'rounded-full' : 'rounded')

const tagClass = computed(() => {
  // plain + color 情况
  if (props.effect === 'plain' && props.color) {
    return [
      'border',            // 1px
      'border-solid',      // 实线
      'border-current',    // 用当前颜色
      'bg-transparent',
      'text-current',      // 文本色同 color（这句可选，根据你的需求）
      sizeMap[props.size],
      roundClass.value,
    ]
  }
  // 自定义色但非plain，保持原逻辑即可
  if (props.color) {
    const borderInPlain = props.effect === 'plain' ? 'border border-current border-solid' : 'border-0'
    return [
      'text-white',
      borderInPlain,
      sizeMap[props.size],
      roundClass.value
    ]
  }
  // 主题色情况
  let base = 'border'
  if (props.effect === 'dark') base += ' ' + (typeMap[props.type!] || '')
  else if (props.effect === 'plain') base += ' ' + (typeMapPlain[props.type!] || '')
  else base += ' ' + (typeMapLight[props.type!] || '')
  return [
    base,
    sizeMap[props.size],
    roundClass.value
  ]
})


const tagStyle = computed(() =>
    props.effect === 'plain' && props.color
        ? {color: props.color, backgroundColor: 'transparent'}
        : (props.color
                ? {backgroundColor: props.color}
                : undefined
        )
)

const closeBtnBaseClass = computed(() => {
  if (props.effect === 'plain' && props.color) {
    return 'bg-transparent text-current border-0'
  }
  if (props.color) {
    // 非plain时，还是白字透明底
    return 'text-white'
  }
  return closeBtnBgMap[props.effect][props.type!] || ''
})

const closeBtnStyle = computed(() => {
  if (props.effect === 'plain' && props.color) {
    return {color: props.color}
  }
  return undefined
})

function hexToRgba(hex: string, alpha = 0.08) {
  let h = hex.replace('#', '')
  if (h.length !== 6) return `rgba(0,0,0,${alpha})`
  const r = parseInt(h.substring(0, 2), 16)
  const g = parseInt(h.substring(2, 4), 16)
  const b = parseInt(h.substring(4, 6), 16)
  return `rgba(${r},${g},${b},${alpha})`
}

const closeBtnHoverBg = computed(() => {
  if (props.effect === 'plain' && props.color) {
    return hexToRgba(props.color, 0.15)
  }
  return isDark.value ? 'rgba(255,255,255,0.16)' : 'rgba(0,0,0,0.08)'
})

const closeBtnHoverStyle = computed(() => ({
  '--tag-close-hover-bg': closeBtnHoverBg.value
}))

const closeBtnHoverClass = computed(() =>
    'hover:bg-[var(--tag-close-hover-bg)]'
)

const closeBtnRound = computed(() => props.round ? 'rounded-full' : 'rounded')
const closeBtnSize = computed(() =>
    props.size === 'large'
        ? 'w-5 h-5 ml-2'
        : props.size === 'small'
            ? 'w-3.5 h-3.5 ml-1.5'
            : 'w-4 h-4 ml-2'
)
</script>

<template>
  <div
      :class="[
      'inline-flex items-center justify-center font-medium',
      tagClass
    ]"
      :style="tagStyle"
      @click="emit('click',props.value)"
  >
    <template v-if="label">
      <span>{{ label }}</span>
    </template>
    <template v-else>
       <slot/>
    </template>

    <button
        v-if="props.closable"
        type="button"
        :class="[
        'flex items-center justify-center cursor-pointer border-0 p-0 transition-all duration-200 outline-none',
        closeBtnBaseClass,
        closeBtnHoverClass,
        closeBtnRound,
        closeBtnSize
      ]"
        :style="[closeBtnStyle,closeBtnHoverStyle]"
        @click.stop="emit('close',props.value)"
        aria-label="close"
        tabindex="0"
    >
      <my-icon icon="xmark" type="fas" size="sm"/>
    </button>
  </div>
</template>
