#!/bin/sh

# Default config file
CONFIG_FILE="/etc/nginx/nginx.conf"

# Select config file based on ENV variable
if [ "$ENV" = "dev" ]; then
    CONFIG_FILE="/etc/nginx/nginx_dev.conf"
elif [ "$ENV" = "test" ]; then
    CONFIG_FILE="/etc/nginx/nginx_test.conf"
elif [ "$ENV" = "prod" ]; then
    CONFIG_FILE="/etc/nginx/nginx_prod.conf"    
fi

echo "Using NGINX config: $CONFIG_FILE"
cp "$CONFIG_FILE" /etc/nginx/nginx.conf

# Start NGINX
exec nginx -g "daemon off;"
