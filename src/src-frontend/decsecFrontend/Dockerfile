FROM node:20 as build

WORKDIR /app
COPY src /app/src
COPY angular.json /app/angular.json
COPY package.json /app/package.json
COPY package-lock.json /app/package-lock.json
COPY tsconfig.json /app/tsconfig.json
COPY tsconfig.app.json /app/tsconfig.app.json
COPY tsconfig.spec.json /app/tsconfig.spec.json

RUN npm install -g @angular/cli
RUN npm install --legacy-peer-deps

CMD ["ng", "serve", "--host", "0.0.0.0"]
EXPOSE 4200