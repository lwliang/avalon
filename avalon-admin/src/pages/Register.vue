<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { ref, reactive } from "vue";
import { ElMessage } from 'element-plus';
import { User, Lock, UserFilled } from '@element-plus/icons-vue';
import { setUserId } from "../cache/userStorage.ts";
import { setToken } from "../cache/tokenStorage.ts";
import { goDatabaseManager, goLogin } from "../util/routerUtils.ts";
import { register } from "../api/loginApi.ts";
import type { FormInstance, FormRules } from 'element-plus';

// 表单引用
const registerFormRef = ref<FormInstance>();

// 表单数据
const registerForm = reactive({
  account: '',
  name: '',
  password: '',
  confirmPassword: ''
});

// 自定义验证规则
const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
};

// 表单验证规则
const rules = reactive<FormRules>({
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
});

// 状态管理
const loading = ref(false);

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return;
  
  try {
    await registerFormRef.value.validate();
    loading.value = true;
    
    const res = await register(
      registerForm.name,
      registerForm.account,
      registerForm.password
    );
    
    setUserId(res.id);
    setToken(res.token);
    
    ElMessage.success('注册成功！即将跳转到登录页面');
    
    // 延迟跳转，让用户看到成功消息
    setTimeout(() => {
      goLoginClick();
    }, 1500);
    
  } catch (error: any) {
    console.error('注册失败:', error);
    
    if (error.message) {
      ElMessage.error(error.message);
    } else {
      ElMessage.error('注册失败，请重试');
    }
  } finally {
    loading.value = false;
  }
};

// 重置表单
const resetForm = () => {
  if (!registerFormRef.value) return;
  registerFormRef.value.resetFields();
};

// 导航方法
const goLoginClick = () => {
  goLogin();
};

const goDatabaseManagerClick = () => {
  goDatabaseManager();
};
</script>

<template>
  <div class="register-container">
    <div class="register-wrapper">
      <div class="register-card">
        <!-- Logo -->
        <div class="logo-container">
          <img src="/avalon_logo.png" alt="Logo" class="logo" />
        </div>
        
        <el-divider />
        
        <!-- 注册表单 -->
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="rules"
          label-width="0"
          class="register-form"
          @keyup.enter="handleRegister"
        >
          <!-- 账号输入 -->
          <el-form-item prop="account">
            <el-input
              v-model="registerForm.account"
              placeholder="请输入账号"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 姓名输入 -->
          <el-form-item prop="name">
            <el-input
              v-model="registerForm.name"
              placeholder="请输入姓名"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 密码输入 -->
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              show-password
              clearable
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 确认密码输入 -->
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认密码"
              size="large"
              show-password
              clearable
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 注册按钮 -->
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="register-button"
              :loading="loading"
              @click="handleRegister"
            >
              {{ loading ? '注册中...' : '注册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 操作链接 -->
        <div class="action-links">
          <el-button type="primary" link @click="goLoginClick">
            已经拥有账户？立即登录
          </el-button>
        </div>

        <el-divider />

        <!-- 底部链接 -->
        <div class="footer-links">
          <el-button type="primary" link @click="goDatabaseManagerClick">
            管理数据库
          </el-button>
          <el-divider direction="vertical" />
          <el-button type="primary" link>
            由Avalon提供支持
          </el-button>
        </div>
      </div>
    </div>

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
.register-container {
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

.register-wrapper {
  width: 100%;
  max-width: 400px;
}

.register-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 40px 32px;
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 1px 0 rgba(255, 255, 255, 0.4) inset,
    0 -1px 0 rgba(0, 0, 0, 0.05) inset;
  position: relative;
  z-index: 10;
}

.logo-container {
  text-align: center;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid var(--el-color-primary-light-8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.register-form {
  margin-top: 20px;
}

.register-form .el-form-item {
  margin-bottom: 24px;
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 6px;
}

.action-links {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.footer-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-container {
    padding: 10px;
  }
  
  .register-card {
    padding: 24px 20px;
  }
  
  .footer-links {
    flex-direction: column;
    gap: 8px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .register-card {
    background: rgba(20, 20, 20, 0.9);
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 
      0 8px 32px rgba(0, 0, 0, 0.3),
      0 1px 0 rgba(255, 255, 255, 0.1) inset,
      0 -1px 0 rgba(0, 0, 0, 0.2) inset;
  }
}

/* 动画效果 */
.register-card {
  animation: fadeInUp 0.6s ease-out;
}

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

/* 表单项样式优化 */
.register-form :deep(.el-input__wrapper) {
  border-radius: 6px;
  box-shadow: 0 0 0 1px var(--el-border-color) inset;
  transition: all 0.3s;
}

.register-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--el-color-primary) inset;
}

.register-form :deep(.el-input.is-focus .el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--el-color-primary) inset;
}

/* 成功状态样式 */
.register-form :deep(.el-form-item.is-success .el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--el-color-success) inset;
}

/* 错误状态样式 */
.register-form :deep(.el-form-item.is-error .el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--el-color-danger) inset;
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
</style>