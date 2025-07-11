<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { WarningFilled, HomeFilled, ArrowLeft, StarFilled, House, Menu, Refresh } from '@element-plus/icons-vue';
import { goModelParent } from "../util/routerUtils.ts";

const router = useRouter();
const countdown = ref(10);

// 倒计时自动跳转
const countdownTimer = () => {
  const timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer);
      goModelParent();
    }
  }, 1000);
};

// 手动返回首页
const goHome = () => {
  goModelParent();
};

// 返回上一页
const goBack = () => {
  router.go(-1);
};

onMounted(() => {
  countdownTimer();
});
</script>

<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <!-- 404 图标 -->
      <div class="error-icon">
        <el-icon size="120" color="#409eff">
          <WarningFilled />
        </el-icon>
      </div>

      <!-- 错误信息 -->
      <div class="error-info">
        <h1 class="error-title">404</h1>
        <h2 class="error-subtitle">页面不存在</h2>
        <p class="error-description">
          抱歉，您访问的页面不存在或已被移除。
          <br />
          请检查 URL 是否正确，或使用下方按钮返回。
        </p>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" size="large" @click="goHome">
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
        <el-button size="large" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回上页
        </el-button>
      </div>

      <!-- 倒计时提示 -->
      <div class="countdown-info">
        <el-alert
          :title="`${countdown} 秒后自动跳转到首页`"
          type="info"
          center
          :closable="false"
        />
      </div>

      <!-- 建议链接 -->
      <div class="suggestions">
        <el-divider>
          <el-icon><StarFilled /></el-icon>
          常用功能
        </el-divider>
        <div class="suggestion-links">
          <el-button link type="primary" @click="goHome">
            <el-icon><House /></el-icon>
            系统首页
          </el-button>
          <el-button link type="primary" @click="goModelParent">
            <el-icon><Menu /></el-icon>
            模块管理
          </el-button>
          <el-button link type="primary" @click="goBack">
            <el-icon><Refresh /></el-icon>
            刷新页面
          </el-button>
        </div>
      </div>
    </div>

    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="floating-element" v-for="i in 6" :key="i" :style="{ animationDelay: i * 0.5 + 's' }"></div>
    </div>
  </div>
</template>

<style scoped>
.not-found-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  box-sizing: border-box;
  overflow: hidden;
}

.not-found-content {
  text-align: center;
  background: var(--el-bg-color);
  border-radius: 16px;
  padding: 60px 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
  position: relative;
  z-index: 10;
  animation: fadeInUp 0.8s ease-out;
}

.error-icon {
  margin-bottom: 30px;
  animation: bounce 2s infinite;
}

.error-title {
  font-size: 6rem;
  font-weight: 700;
  color: var(--el-color-primary);
  margin: 0 0 16px 0;
  line-height: 1;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.error-subtitle {
  font-size: 2rem;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 20px 0;
}

.error-description {
  font-size: 1rem;
  color: var(--el-text-color-regular);
  line-height: 1.6;
  margin: 0 0 40px 0;
}

.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.countdown-info {
  margin-bottom: 30px;
}

.suggestions {
  margin-top: 20px;
}

.suggestion-links {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 16px;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.floating-element {
  position: absolute;
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.floating-element:nth-child(1) {
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.floating-element:nth-child(2) {
  top: 20%;
  right: 10%;
  animation-delay: 1s;
}

.floating-element:nth-child(3) {
  top: 60%;
  left: 5%;
  animation-delay: 2s;
}

.floating-element:nth-child(4) {
  bottom: 20%;
  right: 15%;
  animation-delay: 3s;
}

.floating-element:nth-child(5) {
  bottom: 10%;
  left: 20%;
  animation-delay: 4s;
}

.floating-element:nth-child(6) {
  top: 50%;
  right: 5%;
  animation-delay: 5s;
}

/* 动画 */
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

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-content {
    padding: 40px 20px;
  }
  
  .error-title {
    font-size: 4rem;
  }
  
  .error-subtitle {
    font-size: 1.5rem;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .action-buttons .el-button {
    width: 200px;
  }
  
  .suggestion-links {
    flex-direction: column;
    align-items: center;
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .not-found-container {
    padding: 10px;
  }
  
  .error-title {
    font-size: 3rem;
  }
  
  .error-subtitle {
    font-size: 1.2rem;
  }
  
  .error-description {
    font-size: 0.9rem;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .not-found-content {
    background: var(--el-bg-color-page);
    border: 1px solid var(--el-border-color);
  }
  
  .floating-element {
    background: rgba(255, 255, 255, 0.05);
  }
}

/* 打印样式 */
@media print {
  .background-decoration,
  .floating-element {
    display: none;
  }
  
  .not-found-container {
    background: white;
  }
}
</style>