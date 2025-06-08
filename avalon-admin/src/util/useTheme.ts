/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/22 11:19
 */
// useTheme.ts
import {ref, onMounted, watch} from 'vue'

export const theme = ref<'light' | 'dark'>('light')

export function useTheme() {
    // 初始化
    onMounted(() => {
        const saved = localStorage.getItem('theme')
        if (saved === 'dark' || (!saved && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
            theme.value = 'dark'
        } else {
            theme.value = 'light'
        }
    })

    // 响应式切换
    watch(theme, (val) => {
        const html = document.documentElement
        if (val === 'dark') {
            html.classList.add('dark')
            localStorage.setItem('theme', 'dark')
        } else {
            html.classList.remove('dark')
            localStorage.setItem('theme', 'light')
        }
    })

    // 切换方法
    function toggleTheme() {
        theme.value = theme.value === 'dark' ? 'light' : 'dark'
    }

    return {theme, toggleTheme}
}
