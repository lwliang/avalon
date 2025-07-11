<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Coin, Plus, Lock, Download, CopyDocument, RefreshLeft, Delete } from '@element-plus/icons-vue';
import { createDB, dropDB, getDB } from "../api/dbAPI.ts";
import { useStorage } from "@vueuse/core";
import { goLogin } from "../util/routerUtils.ts";
import type { FormInstance, FormRules } from 'element-plus';

const databases = ref<any[]>([]);
const selectDB = useStorage('db', '');
const loading = ref(false);

// 创建数据库对话框
const showCreateDialog = ref(false);
const createFormRef = ref<FormInstance>();
const createForm = ref({
  name: ''
});

// 表单验证规则
const createRules = ref<FormRules>({
  name: [
    { required: true, message: '请输入数据库名称', trigger: 'blur' },
    { min: 1, max: 50, message: '数据库名称长度在 1 到 50 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '数据库名称只能包含字母、数字和下划线', trigger: 'blur' }
  ]
});

const loadDB = async () => {
  try {
    loading.value = true;
    const data = await getDB();
    databases.value.splice(0, databases.value.length);
    databases.value.push(...data);
  } catch (error) {
    ElMessage.error('获取数据库列表失败');
  } finally {
    loading.value = false;
  }
};

// 初始化加载数据库列表
loadDB();

const developerClick = () => {
  ElMessage.info('开发中敬请期待');
};

const createDBClick = () => {
  showCreateDialog.value = true;
  createForm.value.name = '';
};

const hideCreateDialog = () => {
  showCreateDialog.value = false;
  createFormRef.value?.resetFields();
};

const handleCreateDB = async () => {
  if (!createFormRef.value) return;
  
  try {
    await createFormRef.value.validate();
    loading.value = true;
    
    await createDB(createForm.value.name);
    ElMessage.success('创建数据库完成');
    showCreateDialog.value = false;
    await loadDB();
    
  } catch (error: any) {
    if (error.message) {
      ElMessage.error(error.message);
    } else {
      ElMessage.error('创建数据库失败');
    }
  } finally {
    loading.value = false;
  }
};

const dropDBClick = async (dbName: string) => {
  try {
    await ElMessageBox.confirm(
      `确认删除数据库 "${dbName}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button--danger'
      }
    );
    
    loading.value = true;
    await dropDB(dbName);
    ElMessage.success(`删除 ${dbName} 数据库完成`);
    await loadDB();
    
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除数据库失败');
    }
  } finally {
    loading.value = false;
  }
};

const selectDBClick = (dbName: string) => {
  selectDB.value = dbName;
  goLogin({ db: dbName });
};
</script>

<template>
  <div class="database-manager-container">
    <div class="content-wrapper">
      <div class="main-content">
        <!-- Logo 区域 -->
        <div class="logo-section">
          <img src="/avalon_logo.png" alt="Avalon Logo" class="logo" />
          <h1 class="title">数据库管理</h1>
        </div>

        <!-- 数据库列表 -->
        <el-card v-loading="loading" class="database-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Coin /></el-icon>
              <span>数据库列表</span>
            </div>
          </template>

          <div v-if="databases.length === 0" class="empty-state">
            <el-empty description="暂无数据库">
              <el-button type="primary" :icon="Plus" @click="createDBClick">
                创建第一个数据库
              </el-button>
            </el-empty>
          </div>

          <div v-else class="database-list">
            <div 
              v-for="(db, index) in databases" 
              :key="index" 
              class="database-item"
            >
              <div class="database-info">
                <el-button 
                  type="primary" 
                  link 
                  size="large"
                  class="database-name"
                  @click="selectDBClick(db.dataName)"
                >
                  <el-icon><Coin /></el-icon>
                  {{ db.dataName }}
                </el-button>
              </div>
              
              <div class="database-actions">
                <el-button-group>
                  <el-button 
                    :icon="Download" 
                    @click="developerClick"
                    title="备份数据库"
                  >
                    备份
                  </el-button>
                  <el-button 
                    type="info" 
                    :icon="CopyDocument" 
                    @click="developerClick"
                    title="复制数据库"
                  >
                    复制
                  </el-button>
                  <el-button 
                    type="warning" 
                    :icon="RefreshLeft" 
                    @click="developerClick"
                    title="恢复数据库"
                  >
                    恢复
                  </el-button>
                  <el-button 
                    type="danger" 
                    :icon="Delete" 
                    @click="dropDBClick(db.dataName)"
                    title="删除数据库"
                  >
                    删除
                  </el-button>
                </el-button-group>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button 
            type="success" 
            size="large"
            :icon="Plus" 
            @click="createDBClick"
          >
            创建数据库
          </el-button>
          <el-button 
            type="danger" 
            size="large"
            :icon="Lock" 
            @click="developerClick"
          >
            设置主密码
          </el-button>
        </div>
      </div>
    </div>

    <!-- 创建数据库对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      title="创建数据库"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="createFormRef"
        :model="createForm"
        :rules="createRules"
        label-width="100px"
      >
        <el-form-item label="数据库名称" prop="name">
          <el-input
            v-model="createForm.name"
            placeholder="请输入数据库名称"
            clearable
          >
            <template #prefix>
              <el-icon><Coin /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="hideCreateDialog">取消</el-button>
          <el-button 
            type="primary" 
            :loading="loading"
            @click="handleCreateDB"
          >
            {{ loading ? '创建中...' : '确认创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 动态背景装饰 -->
    <div class="background-decoration">
      <div class="floating-element" v-for="i in 8" :key="i" :style="{ animationDelay: i * 0.8 + 's' }"></div>
      <div class="particle" v-for="i in 12" :key="'particle-' + i" :style="{ animationDelay: i * 0.5 + 's' }"></div>
    </div>

    <!-- 背景几何形状 -->
    <div class="geometric-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>
  </div>
</template>

<style scoped>
.database-manager-container {
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-attachment: fixed;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  box-sizing: border-box;
  position: relative;
  overflow: hidden;
}

.content-wrapper {
  width: 100%;
  max-width: 800px;
  position: relative;
  z-index: 10;
}

.main-content {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 1px 0 rgba(255, 255, 255, 0.4) inset,
    0 -1px 0 rgba(0, 0, 0, 0.05) inset;
  animation: fadeInUp 0.6s ease-out;
}

/* Logo 区域 */
.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid var(--el-color-primary-light-8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  margin-bottom: 16px;
  display: block;
}

.logo:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.title {
  color: var(--el-color-primary);
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

/* 数据库列表卡片 */
.database-list-card {
  margin-bottom: 24px;
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: var(--el-color-primary);
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
}

.database-list {
  max-height: 400px;
  overflow-y: auto;
}

.database-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.database-item:last-child {
  border-bottom: none;
}

.database-info {
  flex: 1;
}

.database-name {
  font-size: 16px;
  font-weight: 500;
}

.database-name .el-icon {
  margin-right: 8px;
}

.database-actions {
  flex-shrink: 0;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
}

/* 对话框样式 */
.dialog-footer {
  display: flex;
  gap: 12px;
}

/* 动态背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
}

.floating-element {
  position: absolute;
  width: 20px;
  height: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 15s infinite linear;
}

.floating-element:nth-child(1) { left: 10%; animation-duration: 12s; }
.floating-element:nth-child(2) { left: 20%; animation-duration: 18s; }
.floating-element:nth-child(3) { left: 30%; animation-duration: 14s; }
.floating-element:nth-child(4) { left: 40%; animation-duration: 16s; }
.floating-element:nth-child(5) { left: 50%; animation-duration: 20s; }
.floating-element:nth-child(6) { left: 60%; animation-duration: 13s; }
.floating-element:nth-child(7) { left: 70%; animation-duration: 17s; }
.floating-element:nth-child(8) { left: 80%; animation-duration: 19s; }

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  animation: particle-float 10s infinite linear;
}

.particle:nth-child(odd) { left: 15%; animation-duration: 8s; }
.particle:nth-child(even) { left: 85%; animation-duration: 12s; }

/* 背景几何形状 */
.geometric-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.shape {
  position: absolute;
  opacity: 0.1;
  animation: rotate 20s infinite linear;
}

.shape-1 {
  top: 10%;
  left: 10%;
  width: 80px;
  height: 80px;
  background: linear-gradient(45deg, #f093fb 0%, #f5576c 100%);
  border-radius: 20px;
  animation-duration: 25s;
}

.shape-2 {
  top: 20%;
  right: 10%;
  width: 60px;
  height: 60px;
  background: linear-gradient(45deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 50%;
  animation-duration: 30s;
  animation-direction: reverse;
}

.shape-3 {
  bottom: 20%;
  left: 15%;
  width: 100px;
  height: 100px;
  background: linear-gradient(45deg, #43e97b 0%, #38f9d7 100%);
  border-radius: 30px;
  animation-duration: 22s;
}

.shape-4 {
  bottom: 10%;
  right: 20%;
  width: 70px;
  height: 70px;
  background: linear-gradient(45deg, #fa709a 0%, #fee140 100%);
  border-radius: 50%;
  animation-duration: 28s;
  animation-direction: reverse;
}

/* 动画定义 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
    opacity: 0;
  }
}

@keyframes particle-float {
  0% {
    transform: translateY(100vh) translateX(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-50px) translateX(50px);
    opacity: 0;
  }
}

@keyframes rotate {
  0% {
    transform: rotate(0deg) scale(1);
  }
  50% {
    transform: rotate(180deg) scale(1.1);
  }
  100% {
    transform: rotate(360deg) scale(1);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .database-manager-container {
    padding: 10px;
  }
  
  .main-content {
    padding: 24px 20px;
  }
  
  .database-item {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }
  
  .database-actions {
    width: 100%;
  }
  
  .database-actions .el-button-group {
    width: 100%;
    display: flex;
  }
  
  .database-actions .el-button {
    flex: 1;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: stretch;
  }
}

@media (max-width: 480px) {
  .logo {
    width: 80px;
    height: 80px;
  }
  
  .title {
    font-size: 20px;
  }
  
  .database-actions .el-button {
    font-size: 12px;
    padding: 8px 12px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .main-content {
    background: rgba(20, 20, 20, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 
      0 8px 32px rgba(0, 0, 0, 0.3),
      0 1px 0 rgba(255, 255, 255, 0.1) inset,
      0 -1px 0 rgba(0, 0, 0, 0.2) inset;
  }
}

/* 滚动条样式 */
.database-list::-webkit-scrollbar {
  width: 8px;
}

.database-list::-webkit-scrollbar-track {
  background: var(--el-fill-color-lighter);
  border-radius: 4px;
}

.database-list::-webkit-scrollbar-thumb {
  background: var(--el-color-primary-light-5);
  border-radius: 4px;
}

.database-list::-webkit-scrollbar-thumb:hover {
  background: var(--el-color-primary);
}
</style>