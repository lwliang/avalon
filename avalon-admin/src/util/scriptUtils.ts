export function loadScript(url: any, callback: any) {
    // 创建一个 <script> 标签
    const script = document.createElement('script');

    // 设置 script 的 src 属性为传入的 URL
    script.src = url;

    // 设置 script 的加载完成事件
    script.onload = () => {
        console.log(`Script loaded successfully from ${url}`);
        if (callback) {
            callback(); // 加载完成后执行回调函数
        }
    };

    // 设置加载错误的处理逻辑
    script.onerror = () => {
        console.error(`Failed to load script from ${url}`);
    };

    // 将 script 标签插入到 <head> 或 <body> 中
    document.head.appendChild(script);
}