<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {useModuleStore} from "../../../global/store/moduleStore.ts";
import {getModuleIcon} from "../../../api/moduleApi.ts";
import {ref} from "vue";
import Module from "../../../model/Module.ts";

const emit = defineEmits(['moduleClick'])

const modules = ref(useModuleStore().getInstallModule());
const activeModuleName = ref<string | null>(null);

const moduleItemClick = (module: Module) => {
  activeModuleName.value = module.name;
  emit('moduleClick', module);
}

const isActive = (module: Module) => {
  return activeModuleName.value === module.name;
}
</script>

<template>
  <div class="module-menu-container">
    <el-scrollbar height="100%" class="module-scrollbar" :horizontal="false">
      <div class="module-menu-content">
        <el-tooltip
            v-for="(module, index) in modules"
            :key="index"
            :content="module.label"
            placement="right"
            :show-after="800"
            :disabled="false"
        >
          <div 
              class="module-item"
              :class="{ 'module-item--active': isActive(module) }"
              @click="moduleItemClick(module)"
          >
            <div class="module-icon">
              <el-image 
                  style="width: 24px; height: 24px;"
                  :src="getModuleIcon(module.name, module.icon)"
                  fit="cover"
                  :lazy="true"
                  :preview-disabled="true"
              />
            </div>
            <div class="module-label">{{ module.label }}</div>
          </div>
        </el-tooltip>
      </div>
    </el-scrollbar>
  </div>
</template>

<style scoped>
.module-menu-container {
  height: 100%;
  padding: 12px 0;
  overflow-x: hidden;
}

.module-scrollbar {
  height: 100%;
  overflow-x: hidden;
}

.module-menu-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 8px;
  gap: 8px;
  overflow-x: hidden;
}

.module-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.2s ease;
  min-width: 60px;
  width: 100%;
  max-width: 80px;
  overflow: hidden;
}

.module-item:hover {
  background-color: var(--el-color-primary-light-9);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.module-item--active {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.module-item--active .module-label {
  color: var(--el-color-primary);
  font-weight: 600;
}

.module-item--active:hover {
  background-color: var(--el-color-primary-light-8);
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  border-color: var(--el-color-primary);
}

.module-item:not(.module-item--active):hover .module-label {
  color: var(--el-color-primary);
  font-weight: 500;
}

.module-icon {
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.module-label {
  font-size: 12px;
  text-align: center;
  color: var(--el-text-color-regular);
  line-height: 1.2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

/* 自定义滚动条样式 */
.module-scrollbar :deep(.el-scrollbar__bar.is-vertical) {
  right: 2px;
  width: 6px;
}

.module-scrollbar :deep(.el-scrollbar__thumb) {
  background-color: var(--el-color-info-light-5);
  border-radius: 3px;
}

.module-scrollbar :deep(.el-scrollbar__thumb:hover) {
  background-color: var(--el-color-info);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .module-menu-content {
    padding: 0 4px;
    gap: 4px;
  }
  
  .module-item {
    padding: 6px 8px;
    min-width: 50px;
    max-width: 70px;
  }
  
  .module-label {
    font-size: 11px;
  }
}
</style>