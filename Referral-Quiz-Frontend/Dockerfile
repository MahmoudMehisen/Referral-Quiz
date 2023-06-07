FROM node:18-alpine AS build
WORKDIR /app

COPY . .
RUN npm install --legacy-peer-deps
RUN npm run build --prod

# Serve Application using Nginx Server
FROM nginx:alpine
COPY --from=build /app/dist/referral-quiz-frontend/ /usr/share/nginx/html
COPY /nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 4200
