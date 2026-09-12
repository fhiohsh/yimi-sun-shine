# Legacy Seckill Module

This directory archives the retired, standalone coupon-seckill implementation.
It is intentionally outside Maven's `sky-common`, `sky-pojo`, and `sky-server` modules, so it is not compiled or registered by Spring Boot.

## Archived scope

- Legacy `/admin/coupon/**` controller and request/response objects.
- Legacy `t_coupon_*` entities and MyBatis-Plus mappers.
- Coupon Redis cache, Redisson locking, RabbitMQ producer/consumer, and Redis-based message idempotency code.
- Coupon-specific constants, enums, and code-generation utility.

## Restoring it

Restore the archived files to their original module-relative paths, then reintroduce the RabbitMQ and Redisson dependencies, `MqConfig`, `RedissonConfig`, `@EnableRabbit`, and RabbitMQ configuration. Review and redesign its authentication, concurrency, idempotency, and Redis/MySQL consistency before enabling it in production.

The active coupon implementation is `NewCouponController` and `CouponRealController`, backed by the unprefixed `coupon_template`, `coupon_activity`, and `coupon_user` tables.
