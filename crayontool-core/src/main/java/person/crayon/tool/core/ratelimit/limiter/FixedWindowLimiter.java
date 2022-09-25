package person.crayon.tool.core.ratelimit.limiter;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.common.domain.ratelimit.LimitRecord;
import person.crayon.tool.core.common.domain.ratelimit.LimitResult;
import person.crayon.tool.core.common.domain.ratelimit.LimitRule;

import java.util.concurrent.TimeUnit;

/**
 * @author Crayon
 * @date 2022/6/3 21:16
 * 固定窗口限流器
 */
@Slf4j
public abstract class FixedWindowLimiter extends Limiter {

    /**
     * 缓存超时时间（毫秒）
     */
    private static final long TIMEOUT = TimeUnit.MINUTES.toMillis(30);

    /**
     * 缓存清理时间（毫秒）
     */
    private static final long DELAY = TIMEOUT >> 1;

    /**
     * 定时自清理缓存
     */
    private final TimedCache<String, LimitRecord> cache = CacheUtil.newTimedCache(TIMEOUT);

    /**
     * 缓存自动清理的钩子函数，子类重写以控制是否开启缓存自动清理
     * 
     * @return
     */
    protected boolean isCacheAutoClean() {
        return false;
    }

    public FixedWindowLimiter() {
        if (isCacheAutoClean()) {
            log.debug("turn on cache auto clean");
            // 开启定时任务清理缓存内容，如果不开启的话是懒惰式检查，只有获取key的时候才去检查键是否过期
            cache.schedulePrune(DELAY);
        }
    }

    @Override
    protected LimitResult checkRule(String throttleId, LimitRule limitRule) {
        long currentTime = DateUtil.current();
        LimitRecord limitRecord;
        int count;
        log.debug("{}: throttleId: {}, currentTime: {}", this.getClass().getSimpleName(), throttleId, currentTime);
        if (cache.containsKey(throttleId)) {
            // 缓存中有，则需要取出来进行判断
            limitRecord = cache.get(throttleId);
            if (currentTime - limitRecord.getLastRequestTime() > limitRule.getMilliSpan()) {
                // 当前时间 - 上次请求时间 大于 限定的时间跨度了
                // 计数清空
                limitRecord.clearCount();
                // 设置最新的请求时间
                limitRecord.setLastRequestTime(currentTime);
            }
        } else {
            // 直接放行
            // 保存这次的请求时间戳，过期时间就是限流频率的时间跨度
            limitRecord = new LimitRecord().setLastRequestTime(currentTime);
            cache.put(throttleId, limitRecord, limitRule.getMilliSpan());
        }
        // 自增
        count = limitRecord.incrCount();
        LimitResult limitResult = new LimitResult();
        boolean allow = count <= limitRule.getAmount();
        // 判断是否小于等于阈值
        limitResult.setAllow(allow).setRemainCount(Math.max(0, limitRule.getAmount() - limitRecord.getCount())).setRemainResetTime(limitRecord.getLastRequestTime() + limitRule.getMilliSpan() - currentTime);
        return limitResult;
    }
}
