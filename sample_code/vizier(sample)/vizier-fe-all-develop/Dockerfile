# build stage
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json .
ARG ENV=production
ENV ENV=$ENV
# add nginx configuration files
COPY nginx/nginx.conf nginx/nginx_dev.conf nginx/nginx_test.conf nginx/nginx_prod.conf . 
RUN rm -rf node_modules
RUN rm package-lock.json
RUN npm install
RUN pwd
RUN ls
COPY . .
RUN pwd
RUN ls
RUN npm run build:$ENV

# production stage
FROM nginx:stable-alpine as production-stage
# 새로운 DocumentRoot 디렉토리 생성
RUN mkdir -p /var/www/vizier/html && chown -R nginx:nginx /var/www/vizier
# build 산출물 복사
COPY --from=build-stage /app/dist /var/www/vizier/html
# COPY --from=build-stage /app/dist /usr/share/nginx/html
# copy all nginx configurations
COPY --from=build-stage /app/nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage /app/nginx_dev.conf /etc/nginx/nginx_dev.conf
COPY --from=build-stage /app/nginx_test.conf /etc/nginx/nginx_test.conf
COPY --from=build-stage /app/nginx_prod.conf /etc/nginx/nginx_prod.conf

# Add entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 80

# Use entrypoint script to set up NGINX
ENTRYPOINT ["/entrypoint.sh"]

#CMD ["nginx", "-g", "daemon off;"]