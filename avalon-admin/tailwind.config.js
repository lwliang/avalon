// tailwind.config.js
export default {
    darkMode: 'class',
    corePlugins: {
        preflight: false,
    },
    content: [
        './index.html',
        './src/**/*.{vue,js,ts,jsx,tsx}',
    ],
    theme: {
        extend: {
            colors: {
                /* 主色 */
                primary: 'var(--primary)',
                'primary-dark': 'var(--primary-dark)',
                'primary-hover': 'var(--primary-hover)',
                'primary-active': 'var(--primary-active)',
                'primary-disabled': 'var(--primary-disabled)',
                'primary-light-3': 'var(--primary-light-3)',
                'primary-light-5': 'var(--primary-light-5)',
                'primary-light-7': 'var(--primary-light-7)',
                'primary-light-8': 'var(--primary-light-8)',
                'primary-light-9': 'var(--primary-light-9)',

                /* 辅助色 - Success */
                success: 'var(--success)',
                'success-dark': 'var(--success-dark)',
                'success-hover': 'var(--success-hover)',
                'success-active': 'var(--success-active)',
                'success-disabled': 'var(--success-disabled)',
                'success-light-3': 'var(--success-light-3)',
                'success-light-5': 'var(--success-light-5)',
                'success-light-7': 'var(--success-light-7)',
                'success-light-8': 'var(--success-light-8)',
                'success-light-9': 'var(--success-light-9)',

                /* 辅助色 - Warning */
                warning: 'var(--warning)',
                'warning-dark': 'var(--warning-dark)',
                'warning-hover': 'var(--warning-hover)',
                'warning-active': 'var(--warning-active)',
                'warning-disabled': 'var(--warning-disabled)',
                'warning-light-3': 'var(--warning-light-3)',
                'warning-light-5': 'var(--warning-light-5)',
                'warning-light-7': 'var(--warning-light-7)',
                'warning-light-8': 'var(--warning-light-8)',
                'warning-light-9': 'var(--warning-light-9)',

                /* 辅助色 - Danger/Error */
                danger: 'var(--danger)',
                'danger-dark': 'var(--danger-dark)',
                'danger-hover': 'var(--danger-hover)',
                'danger-active': 'var(--danger-active)',
                'danger-disabled': 'var(--danger-disabled)',
                'danger-light-3': 'var(--danger-light-3)',
                'danger-light-5': 'var(--danger-light-5)',
                'danger-light-7': 'var(--danger-light-7)',
                'danger-light-8': 'var(--danger-light-8)',
                'danger-light-9': 'var(--danger-light-9)',

                /* 信息色 - Info */
                info: 'var(--info)',
                'info-dark': 'var(--info-dark)',
                'info-hover': 'var(--info-hover)',
                'info-active': 'var(--info-active)',
                'info-disabled': 'var(--info-disabled)',
                'info-light-3': 'var(--info-light-3)',
                'info-light-5': 'var(--info-light-5)',
                'info-light-7': 'var(--info-light-7)',
                'info-light-8': 'var(--info-light-8)',
                'info-light-9': 'var(--info-light-9)',

                /* 中性色 - Text */
                text: {
                    DEFAULT: 'var(--text-primary)',
                    primary: 'var(--text-primary)',
                    regular: 'var(--text-regular)',
                    secondary: 'var(--text-secondary)',
                    placeholder: 'var(--text-placeholder)',
                    disabled: 'var(--text-disabled)',
                },

                /* 中性色 - Border */
                border: {
                    DEFAULT: 'var(--border-base)',
                    base: 'var(--border-base)',
                    light: 'var(--border-light)',
                    lighter: 'var(--border-lighter)',
                    'extra-light': 'var(--border-extra-light)',
                    disabled: 'var(--border-disabled)',
                },

                /* 填充色 - Fill */
                fill: {
                    DEFAULT: 'var(--fill-base)',
                    base: 'var(--fill-base)',
                    light: 'var(--fill-light)',
                    lighter: 'var(--fill-lighter)',
                    'extra-light': 'var(--fill-extra-light)',
                    blank: 'var(--fill-blank)',
                    disabled: 'var(--fill-disabled)',
                },

                /* 背景色 - Background */
                background: {
                    DEFAULT: 'var(--background-page)',
                    page: 'var(--background-page)',
                    component: 'var(--background-component)',
                    disabled: 'var(--background-disabled)',
                    overlay: 'var(--background-overlay)',
                    base: 'var(--background-base)',
                },

                /* 遮罩 - Mask */
                mask: {
                    DEFAULT: 'var(--mask-primary)',
                    primary: 'var(--mask-primary)',
                    secondary: 'var(--mask-secondary)',
                },

                /* 遮罩 - Overlay */
                overlay: {
                    DEFAULT: 'var(--overlay-50)',
                    50: 'var(--overlay-50)',
                    70: 'var(--overlay-70)',
                    80: 'var(--overlay-80)',
                },
            },

            fontFamily: {
                sans: ['var(--font-family)'],
            },
            fontSize: {
                DEFAULT: 'var(--font-size-base)',
                base: 'var(--font-size-base)',
                sm: 'var(--font-size-small)',
                lg: 'var(--font-size-large)',
            },
            fontWeight: {
                primary: 'var(--font-weight-primary)',
            },
            lineHeight: {
                primary: 'var(--line-height-primary)',
            },
            borderRadius: {
                DEFAULT: 'var(--border-radius-base)',
                sm: 'var(--border-radius-small)',
                circle: 'var(--border-radius-circle)',
            },
            boxShadow: {
                DEFAULT: 'var(--box-shadow)',
                light: 'var(--box-shadow-light)',
                dark: 'var(--box-shadow-dark)',
            },
            opacity: {
                disabled: 'var(--opacity-disabled)',
            },
        },
    },
    plugins: [],
}