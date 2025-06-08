/**
 * @author lwlianghehe@gmail.com
 * @date 2025/5/21
 */

// 简单通用：把颜色加透明度，返回 rgba
export function toHoverRgba(color: string, alpha = 0.13): string {
    if (!color) return `rgba(0,0,0,${alpha})`;
    // 如果是 rgba
    if (color.startsWith('rgba')) {
        return color.replace(/[\d\.]+\)$/g, `${alpha})`);
    }
    // 如果是 rgb
    if (color.startsWith('rgb')) {
        return color.replace('rgb', 'rgba').replace(/\)$/, `,${alpha})`);
    }
    // 如果是 hex
    let hex = color.replace('#', '');
    if (hex.length === 3) hex = hex.split('').map(x => x + x).join('');
    if (hex.length === 6) {
        const r = parseInt(hex.slice(0, 2), 16);
        const g = parseInt(hex.slice(2, 4), 16);
        const b = parseInt(hex.slice(4, 6), 16);
        return `rgba(${r},${g},${b},${alpha})`;
    }
    return `rgba(0,0,0,${alpha})`;
}
