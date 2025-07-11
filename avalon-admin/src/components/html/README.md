# MyHtml 组件

基于 vue-quill 的富文本编辑器组件。

## 基本用法

```vue
<template>
  <my-html v-model="content" placeholder="请输入内容..." />
</template>

<script setup>
import { ref } from 'vue'

const content = ref('<p>Hello World!</p>')
</script>
```

## 属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| disabled | boolean | false | 是否禁用编辑器（禁用时工具栏和编辑区域都不可用） |
| readonly | boolean | false | 是否只读（只读时工具栏可用，但编辑区域不可编辑） |
| placeholder | string | '' | 占位符文本 |
| required | boolean | false | 是否必填 |
| height | string \| number | '200px' | 编辑器高度 |
| width | string \| number | '100%' | 编辑器宽度 |
| toolbar | string \| string[] | 默认工具栏 | 工具栏配置 |
| theme | 'snow' \| 'bubble' | 'snow' | 编辑器主题 |
| showWordCount | boolean | false | 是否显示字数统计 |
| maxLength | number | - | 最大字数限制 |
| autoFocus | boolean | false | 是否自动聚焦 |
| options | object | - | 编辑器配置选项 |

## 事件

| 事件名 | 参数 | 说明 |
|--------|------|------|
| change | value: string | 内容变化时触发 |
| blur | event: FocusEvent | 失焦时触发 |
| focus | event: FocusEvent | 聚焦时触发 |
| ready | quill: any | 编辑器准备就绪时触发 |
| textChange | delta, oldContents, source | 文本变化时触发 |
| selectionChange | range, oldRange, source | 选择变化时触发 |
| editorChange | eventName, ...args | 编辑器变化时触发 |

## 方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| validate | - | boolean | 验证内容 |
| getEditor | - | Quill实例 | 获取编辑器实例 |
| insertText | index, text, source | - | 插入文本 |
| insertHTML | index, html, source | - | 插入HTML |
| setContents | delta, source | - | 设置内容 |
| getContents | - | Delta | 获取内容 |
| getText | - | string | 获取文本内容 |
| getHTML | - | string | 获取HTML内容 |

## 示例

### 基础用法

```vue
<template>
  <my-html 
    v-model="content" 
    placeholder="请输入内容..."
    :height="300"
    :show-word-count="true"
    :max-length="1000"
  />
</template>
```

### 自定义工具栏

```vue
<template>
  <my-html 
    v-model="content" 
    :toolbar="customToolbar"
  />
</template>

<script setup>
import { ref } from 'vue'

const content = ref('')
const customToolbar = [
  ['bold', 'italic', 'underline'],
  [{ 'list': 'ordered'}, { 'list': 'bullet' }],
  ['link', 'image']
]
</script>
```

### 只读模式

```vue
<template>
  <my-html 
    v-model="content" 
    :readonly="true"
  />
</template>
```

### 禁用模式

```vue
<template>
  <my-html 
    v-model="content" 
    :disabled="true"
  />
</template>
```

**注意：**
- `readonly="true"`：工具栏可用，但编辑区域不可编辑
- `disabled="true"`：工具栏和编辑区域都不可用

### 验证

```vue
<template>
  <my-html 
    ref="htmlEditor"
    v-model="content" 
    :required="true"
    @blur="validateContent"
  />
</template>

<script setup>
import { ref } from 'vue'

const content = ref('')
const htmlEditor = ref()

const validateContent = () => {
  const isValid = htmlEditor.value.validate()
  console.log('验证结果:', isValid)
}
</script>
``` 