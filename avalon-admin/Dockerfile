FROM ubuntu:latest
FROM node:latest AS builder
LABEL authors="avalon"

WORKDIR /app

# 拷贝项目文件到工作目录
COPY package*.json ./
COPY index.html ./
COPY postcss.config.js ./
COPY tailwind.config.js ./
COPY tsconfig.app.json ./
COPY tsconfig.json ./
COPY tsconfig.node.json ./
COPY yarn.lock ./
COPY vite.config.ts ./
COPY src/ ./src
COPY public/ ./public

# 安装依赖
RUN npm install

# 构建项目
RUN npm run build

# 使用 Nginx 镜像作为运行阶段
FROM nginx:latest

# 复制构建的静态文件到 Nginx 默认目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 暴露 Nginx 的默认端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]

ENTRYPOINT ["top", "-b"]