# MyWebSite
>项目涉及邮箱、数据库隐私禁止泄露！
## 云数据库连接
```shell
# 主机：sh-cynosdbmysql-grp-nlp7sgk2.sql.tencentcdb.com
# 端口：23579
# 用户：root
# 密码：Codezijun2004
mysql -h sh-cynosdbmysql-grp-nlp7sgk2.sql.tencentcdb.com -P 23579 -u root -p
```

## 项目运行
* 正确设置`Mybatis-Plus`的数据库配置
* `RabbitMQ`使用默认配置
* `Redis`使用默认配置并已开启
* 确认`JDK version >= 17`
* 邮箱已正确配置无需更改