local key = KEYS[1]
local limit = tonumber(ARGV[1])
local expire_time = tonumber(ARGV[2])

local count = tonumber(redis.call('INCR', key))
if count == 1 then
    redis.call('EXPIRE', key, expire_time)
end

if count > limit then
    return 0
else
    return 1
end