# AGENTS.md

## 固定开发规则

以下规则适用于本仓库的所有代码修改、构建、测试与协作任务，优先于一般开发习惯：

1. 不要自行停止当前正在运行的 Spring Boot `SkyApplication`。
2. 不要自行启动、重启或替换用户当前在 IntelliJ IDEA 中运行的 Spring Boot 服务。
3. 修改代码后可以执行 Maven 编译和自动化测试，但不要为了测试而重启用户当前运行中的 Spring Boot 服务。
4. 代码修改、编译和自动化测试完成后，暂停并向用户报告结果，等待用户手动重启 `SkyApplication`。
5. 只有在用户确认已经重新启动 `SkyApplication` 后，才能继续运行时或接口测试。
6. 优先遵循现有项目结构、命名方式和编码风格。
7. 除非需求明确要求，否则不修改数据库结构、不删除现有功能，也不做无关重构。
8. 每次修改完成后，必须汇总：
   - 修改了哪些文件；
   - 每个文件修改了什么；
   - 执行了什么测试；
   - 测试是否通过；
   - 是否需要用户重启 `SkyApplication`；
   - 还需要用户手动验证什么。

## 项目概览

一米阳光母婴商品销售平台后端。它是一个 Maven 多模块 Spring Boot 2.7.3 项目，根坐标为 `com.sky:mask:1.0-SNAPSHOT`。

- `sky-server`：可运行的 Web 服务、控制器、业务服务、MyBatis Mapper、配置与资源；启动类为 `com.sky.SkyApplication`。
- `sky-pojo`：实体、DTO、VO 及请求/响应对象。
- `sky-common`：通用结果对象、配置属性、JSON 与基础工具类。

服务默认监听 `8099` 端口。运行时依赖 MySQL、Redis、RabbitMQ，并可能使用 OSS、微信支付等外部配置；这些依赖未就绪时，不要假定应用能够完整启动。

## 常用命令

从仓库根目录执行：

```powershell
# 编译全部模块（不执行测试）
mvn clean package -DskipTests

# 执行全部测试
mvn test

# 仅构建服务模块及其依赖
mvn -pl sky-server -am package -DskipTests

# 本地启动服务（需先准备 application.yml 中的外部依赖）
mvn -pl sky-server -am spring-boot:run
```

项目未提交 Maven Wrapper；优先使用已安装的 Maven，并确保使用与 Spring Boot 2.7 兼容的 JDK（通常为 JDK 8 或 17）。

## 代码约定

- Java 包名维持在 `com.sky` 下；新增代码放入职责对应的模块与现有分层中。
- 典型服务层分为 `controller`、`service`、`service.impl`、`mapper`，持久化 SQL XML 位于 `sky-server/src/main/resources/mapper`。
- 新增或变更 Mapper 方法时，同步检查 Java Mapper 接口与对应 XML 的 `namespace`、语句 ID、参数名和结果映射。
- 业务接口、实现、DTO/VO 与实体应保持既有命名和 Lombok 风格；避免顺便进行大规模重构或格式化。
- 将密钥、令牌、数据库密码、OSS/支付凭据留在本地配置或环境变量中，不要写入源码、示例或提交记录。

## 修改与验证

- 先检查工作区状态；不要覆盖或撤销用户已有改动。
- 修改 `application.yml`、数据库访问、Redis、消息队列或支付逻辑后，说明所需的外部服务与配置，且避免以生产凭据验证。
- 任何业务变更至少执行受影响模块的 Maven 编译或测试；若因缺少外部依赖无法运行，明确记录原因与未执行项。
- 提交前检查 `git diff --check`，并避免提交 `target/`、IDE 文件、日志或本地配置。

## 当前仓库注意事项

- `README.md` 当前含有未解决的 Git 冲突标记。除非任务明确要求处理文档冲突，否则不要在无关改动中修改它。
