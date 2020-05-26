# training

```
curl 'localhost:8080/todo/add' -H 'Content-Type: application/json;charset=UTF-8' -H 'Accept: application/json, text/plain, */*' -d '{"content": "ccc", "gmtCreate" : "2020-05-21 10:01:02"}'
```

## 异常
@ControllerAdvise + @ExceptionHandler注解在@RestController上，会导致原本应当返回500等http状态码，却返回了200，并且吞掉了原有的状态码