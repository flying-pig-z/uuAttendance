-- 切换到单命令复制模式
redis.replicate_commands()

-- 定义键名和限流参数
local limit_key = KEYS[1]
local limit_num = tonumber(ARGV[1])       -- 限制的请求数
local window_size = tonumber(ARGV[2])  -- 窗口大小，单位为秒


-- 获取当前时间戳
local current_time = tonumber(redis.call('TIME')[1])

-- 计算窗口的起始时间戳和结束时间戳
local window_start = current_time - window_size

-- 删除窗口外的旧数据，保留窗口内的数据
redis.call('ZREMRANGEBYSCORE', limit_key, '-inf', window_start)

-- 统计当前窗口内的请求数量
local current_count = redis.call('ZCARD', limit_key)

-- 如果当前请求数量未超过限制，则增加当前请求的时间戳到有序集合中
if current_count < limit_num then
    redis.call('ZADD', limit_key, current_time, current_time)
    redis.call('EXPIRE', limit_key, window_size + 1)  -- 设置过期时间略大于窗口大小，确保过期时删除整个键
    return true
else
    return false  -- 修正拼写错误
end
