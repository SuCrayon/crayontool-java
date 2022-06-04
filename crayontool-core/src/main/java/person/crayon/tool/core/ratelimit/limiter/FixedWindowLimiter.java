package person.crayon.tool.core.ratelimit.limiter;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import person.crayon.tool.core.common.domain.ratelimit.LimitFreq;
import person.crayon.tool.core.common.domain.ratelimit.LimitRecord;
import person.crayon.tool.core.common.domain.ratelimit.LimitResult;

import java.util.concurrent.TimeUnit;

/**
 * @author Crayon
 * @date 2022/6/3 21:16
 * 固定窗口限流器
 */
@Slf4j
public class FixedWindowLimiter implements Limiter {

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

    public FixedWindowLimiter() {
        // 开启定时任务清理缓存内容，如果不开启的话是懒惰式检查，只有获取key的时候才去检查键是否过期
        cache.schedulePrune(DELAY);
    }

    @Override
    public LimitResult check(String throttleId, LimitFreq limitFreq) {
        long currentTime = DateUtil.current();
        LimitRecord limitRecord;
        int count;
        log.debug("{}: throttleId: {}, currentTime: {}", this.getClass().getSimpleName(), throttleId, currentTime);
        if (cache.containsKey(throttleId)) {
            // 缓存中有，则需要取出来进行判断
            limitRecord = cache.get(throttleId);
            if (currentTime - limitRecord.getLastRequestTime() > limitFreq.getMilliSpan()) {
                // 当前时间 - 上次请求时间 大于 限定的时间跨度了
                // 计数清空
                limitRecord.clearCount();
                // 设置最新的请求时间
                limitRecord.setLastRequestTime(currentTime);
            }
        } else {
            // 直接放行
            // 保存这次的请求时间戳，过期时间就是限流频率的时间跨度
            limitRecord = new LimitRecord();
            cache.put(throttleId, limitRecord, limitFreq.getMilliSpan());
            // return limitResult.setAllow(true).setRemainCount(limitFreq.getAmount() - limitRecord.getCount()).setRemainResetTime(limitRecord.getLastRequestTime() + limitFreq.getMilliSpan() - currentTime);
        }
        // 自增
        count = limitRecord.incrCount();
        LimitResult limitResult = new LimitResult();
        boolean allow = count <= limitFreq.getAmount();
        if (allow) {
            // 如果限流通过的话，就要设置最新的请求时间
            limitRecord.setLastRequestTime(currentTime);
        }
        // 判断是否小于等于阈值
        limitResult.setAllow(allow).setRemainCount(Math.max(0, limitFreq.getAmount() - limitRecord.getCount())).setRemainResetTime(limitRecord.getLastRequestTime() + limitFreq.getMilliSpan() - currentTime);
        return limitResult;
    }
}
