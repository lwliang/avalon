/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/21
 */
import {Ref, ref, watchEffect} from 'vue'

import {useDark} from '@vueuse/core'

export function useComputedStyleValue(
    targetRef: Ref<HTMLElement | null> | HTMLElement | null,
    styleProp: keyof CSSStyleDeclaration = 'backgroundColor',
) {
    const value = ref('')

    // 拿 vueuse 自带的 light/dark 响应式变量
    const isDark = useDark()

    watchEffect(() => {
        // 只要 theme 切换 或 dom ref 变动，自动repaint
        void isDark.value
        const el = (targetRef && 'value' in targetRef)
            ? targetRef.value
            : targetRef
        if (el && el instanceof Element) {
            value.value = getComputedStyle(el)[styleProp] as string || ''
        } else {
            value.value = ''
        }
    })

    return value
}

