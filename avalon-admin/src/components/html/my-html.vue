<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import { ref, computed, watch, useAttrs } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import type { HtmlProps } from './types'

const props = withDefaults(defineProps<HtmlProps>(), {
  disabled: false,
  readonly: false,
  placeholder: '',
  required: false,
  height: '200px',
  width: '100%',
  theme: 'snow',
  showWordCount: false,
  autoFocus: false
})

const htmlValue = defineModel<string>({
  default: ''
})

const attrs = useAttrs()

const emit = defineEmits<{
  (e: 'change', value: string): void
  (e: 'blur', event: FocusEvent): void
  (e: 'focus', event: FocusEvent): void
  (e: 'ready', quill: any): void
  (e: 'textChange', delta: any, oldContents: any, source: any): void
  (e: 'selectionChange', range: any, oldRange: any, source: any): void
  (e: 'editorChange', eventName: string, ...args: any[]): void
}>()

// 验证状态
const isValidate = ref(true)

// 编辑器引用
const editorRef = ref()

// 计算编辑器样式
const editorStyle = computed(() => ({
  height: typeof props.height === 'number' ? `${props.height}px` : props.height,
  width: typeof props.width === 'number' ? `${props.width}px` : props.width
}))

// 计算编辑器配置
const editorOptions = computed(() => {
  // 默认工具栏配置
  const defaultToolbar = [
    ['bold', 'italic', 'underline', 'strike'],
    ['blockquote', 'code-block'],
    [{ 'header': 1 }, { 'header': 2 }],
    [{ 'list': 'ordered'}, { 'list': 'bullet' }],
    [{ 'script': 'sub'}, { 'script': 'super' }],
    [{ 'indent': '-1'}, { 'indent': '+1' }],
    [{ 'direction': 'rtl' }],
    [{ 'size': ['small', false, 'large', 'huge'] }],
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
    [{ 'color': [] }, { 'background': [] }],
    [{ 'font': [] }],
    [{ 'align': [] }],
    ['clean'],
    ['link', 'image', 'video']
  ]

  const baseOptions = {
    theme: props.theme,
    placeholder: props.placeholder,
    readOnly: props.disabled, // 只在完全禁用时设置为只读
    autoFocus: props.autoFocus,
    modules: {
      toolbar: props.toolbar || defaultToolbar,
      history: {
        delay: 2000,
        maxStack: 500,
        userOnly: true
      },
      clipboard: {
        matchVisual: false
      }
    }
  }

  // 合并用户自定义选项
  if (props.options) {
    return {
      ...baseOptions,
      ...props.options,
      modules: {
        ...baseOptions.modules,
        ...props.options.modules
      }
    }
  }

  return baseOptions
})

// 字数统计
const wordCount = computed(() => {
  if (!htmlValue.value) return 0
  // 移除HTML标签计算纯文本长度
  const textContent = htmlValue.value.replace(/<[^>]*>/g, '')
  return textContent.length
})

// 是否超出字数限制
const isOverLimit = computed(() => {
  if (!props.maxLength) return false
  return wordCount.value > props.maxLength
})

/** 内容变化事件 */
function onChange(value: string) {
  htmlValue.value = value
  emit('change', value)
}

/** 失焦事件 */
function onBlur(e: FocusEvent) {
  validate()
  emit('blur', e)
}

/** 聚焦事件 */
function onFocus(e: FocusEvent) {
  emit('focus', e)
}

/** 编辑器准备就绪事件 */
function onReady(quill: any) {
  // 设置编辑器状态
  if (props.disabled) {
    quill.disable()
  } else {
    quill.enable()
  }
  emit('ready', quill)
}

/** 文本变化事件 */
function onTextChange(delta: any, oldContents: any, source: any) {
  emit('textChange', delta, oldContents, source)
}

/** 选择变化事件 */
function onSelectionChange(range: any, oldRange: any, source: any) {
  emit('selectionChange', range, oldRange, source)
}

/** 编辑器变化事件 */
function onEditorChange(eventName: string, ...args: any[]) {
  emit('editorChange', eventName, ...args)
}

/** 校验逻辑 */
function validate(): boolean {
  if (props.required && (!htmlValue.value || htmlValue.value.trim() === '')) {
    isValidate.value = false
    return false
  }
  
  if (props.maxLength && wordCount.value > props.maxLength) {
    isValidate.value = false
    return false
  }
  
  isValidate.value = true
  return true
}

/** 自动恢复校验 */
watch(htmlValue, () => {
  if (!isValidate.value) {
    isValidate.value = true
  }
})

/** 监听只读状态变化 */
watch(() => props.readonly, (newReadonly) => {
  const quill = getEditor()
  if (quill) {
    quill.enable(!props.disabled) // 只在完全禁用时禁用编辑器
  }
}, { immediate: true })

/** 获取编辑器实例 */
function getEditor() {
  return editorRef.value?.getQuill()
}

/** 插入内容 */
function insertText(index: number, text: string, source?: string) {
  const quill = getEditor()
  if (quill) {
    quill.insertText(index, text, source)
  }
}

/** 插入HTML */
function insertHTML(index: number, html: string, source?: string) {
  const quill = getEditor()
  if (quill) {
    quill.clipboard.dangerouslyPasteHTML(index, html, source)
  }
}

/** 设置内容 */
function setContents(delta: any, source?: string) {
  const quill = getEditor()
  if (quill) {
    quill.setContents(delta, source)
  }
}

/** 获取内容 */
function getContents() {
  const quill = getEditor()
  return quill ? quill.getContents() : null
}

/** 获取文本内容 */
function getText() {
  const quill = getEditor()
  return quill ? quill.getText() : ''
}

/** 获取HTML内容 */
function getHTML() {
  const quill = getEditor()
  return quill ? quill.root.innerHTML : ''
}

defineExpose({ 
  validate, 
  isValidate, 
  getEditor, 
  insertText, 
  insertHTML, 
  setContents, 
  getContents, 
  getText, 
  getHTML,
  wordCount
})
</script>

<template>
  <div class="html-editor-wrapper" :class="{ 
    'is-error': !isValidate,
    'disabled': disabled,
    'readonly': readonly && !disabled
  }">
    <QuillEditor
      ref="editorRef"
      v-model:content="htmlValue"
      :options="editorOptions"
      :style="editorStyle"
      content-type="html"
      @update:content="onChange"
      @blur="onBlur"
      @focus="onFocus"
      @ready="onReady"
      @textChange="onTextChange"
      @selectionChange="onSelectionChange"
      @editorChange="onEditorChange"
      v-bind="attrs"
    />
    
    <!-- 字数统计 -->
    <div v-if="showWordCount || maxLength" class="word-count">
      <span :class="{ 'text-red-500': isOverLimit }">
        {{ wordCount }}
      </span>
      <span v-if="maxLength" class="text-gray-400">
        / {{ maxLength }}
      </span>
    </div>
  </div>
</template>

<style scoped>
.html-editor-wrapper {
  width: 100%;
}

.html-editor-wrapper.is-error :deep(.ql-editor) {
  border-color: var(--el-color-danger);
}

.html-editor-wrapper.is-error :deep(.ql-toolbar) {
  border-color: var(--el-color-danger);
}

.word-count {
  font-size: 12px;
  color: #666;
  text-align: right;
  margin-top: 4px;
  padding-right: 8px;
}

/* 自定义工具栏样式 */
:deep(.ql-toolbar) {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  border-bottom: 1px solid #ccc;
}

:deep(.ql-container) {
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
}

:deep(.ql-editor) {
  min-height: 150px;
  font-size: 14px;
  line-height: 1.6;
}

/* 只读状态样式 */
:deep(.ql-editor[contenteditable="false"]) {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

/* 禁用状态样式 - 只在完全禁用时禁用工具栏 */
.html-editor-wrapper.disabled :deep(.ql-toolbar button),
.html-editor-wrapper.disabled :deep(.ql-toolbar .ql-picker) {
  opacity: 0.6;
  pointer-events: none;
}

/* 只读状态下工具栏保持可用 */
.html-editor-wrapper.readonly :deep(.ql-toolbar button),
.html-editor-wrapper.readonly :deep(.ql-toolbar .ql-picker) {
  opacity: 1;
  pointer-events: auto;
}
</style>
