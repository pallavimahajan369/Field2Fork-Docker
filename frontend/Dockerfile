# Step 1: Build using Node.js
FROM node:18 AS build

WORKDIR /app
COPY . .

RUN npm install
RUN npm run build

# Step 2: Serve using Nginx
FROM nginx:alpine

# Copy build output to nginx public folder
COPY --from=build /app/dist /usr/share/nginx/html

# Optional: custom nginx config (not mandatory if default is okay)
 COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
