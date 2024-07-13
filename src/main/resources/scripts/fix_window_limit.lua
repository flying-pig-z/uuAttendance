-- 根据传入的KEYS和ARGV获取相应的键和参数
local tokens_key = KEYS[1] .. '_token'        -- 存储令牌数量的键名
local timestamp_key = KEYS[1] .. '_timestamp' -- 存储时间戳的键名

-- 从ARGV中获取参数并转换为数值
local bucketCapacity = tonumber(ARGV[1])       -- 令牌桶容量
local consumeTokenRate = tonumber(ARGV[2])    -- 令牌消耗速率
local generateTokenPerReq = tonumber(ARGV[3])   -- 每次请求增加的令牌数

-- 获取当前时间戳（秒级）
local now = redis.call('TIME')[1]

-- 计算填充时间和TTL
local fill_time = bucketCapacity / consumeTokenRate
local ttl = math.floor(fill_time * 2)

-- 获取上存储的令牌数量，如果不存在则默次认为0
local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
    last_tokens = 0
end

-- 获取上次刷新的时间戳，如果不存在则默认为0
local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
    last_refreshed = 0
end

-- 计算时间间隔，上次还存在的令牌数-与上次时间间隔*生成速率=目前的令牌数（注意目前的小为0）
local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.max(0, last_tokens - (delta * consumeTokenRate))

-- 判断当前请求是否允许（目前的令牌数是否大于等于请求需要消耗的令牌数量）
local allowed = 0
if filled_tokens <= bucketCapacity then
    allowed = 1
end

-- 如果TTL大于0，则更新令牌数量和时间戳的键值对
if ttl > 0 then
    redis.call("setex", tokens_key, ttl, filled_tokens - generateTokenPerReq)
    redis.call("setex", timestamp_key, ttl, now)
end

-- 返回允许的请求数和更新后的令牌数量
return allowed
