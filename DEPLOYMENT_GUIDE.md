# 🐳 Docker Deployment Guide - EasyFin API

## 📦 What's Included

- **Dockerfile**: Multi-stage build for optimized image (~200MB final size)
- **.dockerignore**: Excludes unnecessary files from Docker build
- **docker-compose.yml**: Easy local testing and deployment

---

## 🚀 Quick Start

### 1. Build the Docker Image
```bash
docker build -t easyfin-api:latest .
```

### 2. Run the Container
```bash
docker run -d -p 8080:8080 --name easyfin-api easyfin-api:latest
```

### 3. Check if Running
```bash
docker ps
curl http://localhost:8080/api/v1/dashboard/summary
```

---

## 🎯 Using Docker Compose (Recommended)

### Start the service:
```bash
docker-compose up -d
```

### View logs:
```bash
docker-compose logs -f
```

### Stop the service:
```bash
docker-compose down
```

### Rebuild and restart:
```bash
docker-compose up -d --build
```

---

## ☁️ Deploy to Cloud Platforms

### **Render.com (Easiest)**
1. Push your code to GitHub (including Dockerfile)
2. Go to https://render.com
3. Create "New Web Service"
4. Select "Docker" as environment
5. Render auto-detects Dockerfile and deploys!

### **Fly.io**
```bash
# Install flyctl
powershell -Command "iwr https://fly.io/install.ps1 -useb | iex"

# Login and deploy
flyctl auth login
flyctl launch
flyctl deploy
```

### **Railway.app**
1. Go to https://railway.app
2. "New Project" → "Deploy from GitHub"
3. Railway auto-detects Dockerfile
4. Deploy! 🚀

### **Google Cloud Run**
```bash
# Build and push to Google Container Registry
gcloud builds submit --tag gcr.io/PROJECT-ID/easyfin-api
gcloud run deploy easyfin-api --image gcr.io/PROJECT-ID/easyfin-api --platform managed
```

### **Azure Container Instances**
```bash
az container create \
  --resource-group myResourceGroup \
  --name easyfin-api \
  --image easyfin-api:latest \
  --dns-name-label easyfin-api \
  --ports 8080
```

### **AWS ECS/Fargate**
```bash
# Push to ECR
aws ecr create-repository --repository-name easyfin-api
docker tag easyfin-api:latest AWS_ACCOUNT_ID.dkr.ecr.REGION.amazonaws.com/easyfin-api:latest
docker push AWS_ACCOUNT_ID.dkr.ecr.REGION.amazonaws.com/easyfin-api:latest
# Then deploy via ECS console or CLI
```

---

## 🔧 Environment Variables

You can pass environment variables to customize the deployment:

```bash
docker run -d \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx1024m" \
  -e SPRING_PROFILES_ACTIVE=prod \
  --name easyfin-api \
  easyfin-api:latest
```

---

## 📊 Monitoring

### Check health:
```bash
curl http://localhost:8080/actuator/health
```

### View logs:
```bash
docker logs easyfin-api -f
```

### Container stats:
```bash
docker stats easyfin-api
```

---

## 🐛 Troubleshooting

### Container won't start:
```bash
docker logs easyfin-api
```

### Rebuild from scratch:
```bash
docker build --no-cache -t easyfin-api:latest .
```

### Enter the container:
```bash
docker exec -it easyfin-api sh
```

### Check port binding:
```bash
docker port easyfin-api
```

---

## 📝 Docker Image Details

- **Base Image**: Eclipse Temurin 17 JRE Alpine (lightweight)
- **Final Size**: ~200MB (optimized with multi-stage build)
- **Security**: Runs as non-root user
- **Health Check**: Built-in health monitoring
- **Port**: 8080

---

## 🌐 Update iOS Documentation

Once deployed, update your iOS developer with the new URL:

**For Render.com:**
```
https://easyfin-api.onrender.com/api/v1
```

**For Fly.io:**
```
https://easyfin-api.fly.dev/api/v1
```

**For Railway:**
```
https://easyfin-api.up.railway.app/api/v1
```

**No ngrok headers needed! ✅**

---

## 🎉 Next Steps

1. ✅ Build the Docker image locally and test
2. ✅ Push to GitHub (if not already)
3. ✅ Choose a cloud platform (Render.com recommended)
4. ✅ Deploy and get your permanent URL
5. ✅ Update `API_FOR_IOS.md` with new production URL
6. ✅ Share with iOS developer

**Happy Deploying! 🚀**

