#### 1. 获取token
```
http://localhost:8080/oauth/token?username=admin&password=123456&client_id=dev&client_secret=password&grant_type=password
```
#### 2. 刷新token
```
http://localhost:8080/oauth/token?refresh_token=869dd3de-caf4-4e0b-a0e2-4ce13e91c4bd&client_id=dev&client_secret=dev&grant_type=refresh_token
```

#### 3. 获取资源
```
http://localhost:8080/hi?name=Oven&access_token=57794a40-86ac-4be1-9076-0da769ebae11
```