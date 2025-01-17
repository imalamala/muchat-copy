package io.xiaochangbai.muchat.connector.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import io.xiaochangbai.muchat.common.core.contant.AppConst;
import io.xiaochangbai.muchat.common.core.contant.RedisKey;
import io.xiaochangbai.muchat.common.core.enums.IMCmdType;
import io.xiaochangbai.muchat.common.core.model.HeartbeatInfo;
import io.xiaochangbai.muchat.common.core.model.IMSendInfo;
import io.xiaochangbai.muchat.connector.netty.ws.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HeartbeatProcessor extends   MessageProcessor<HeartbeatInfo> {


    @Autowired
    private WebSocketServer WSServer;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, HeartbeatInfo beatInfo) {
        // 响应ws
        IMSendInfo sendInfo = new IMSendInfo();
        sendInfo.setCmd(IMCmdType.HEART_BEAT.code());
        ctx.channel().writeAndFlush(sendInfo);

        // 设置属性
        AttributeKey<Long> attr = AttributeKey.valueOf("HEARTBEAt_TIMES");
        Long heartbeatTimes = ctx.channel().attr(attr).get();
        ctx.channel().attr(attr).set(++heartbeatTimes);
        if(heartbeatTimes%10 == 0){
            // 每心跳10次，用户在线状态续一次命
            attr = AttributeKey.valueOf("USER_ID");
            Long userId = ctx.channel().attr(attr).get();
            String key = RedisKey.IM_USER_SERVER_ID+userId;
            redisTemplate.expire(key, AppConst.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }
    }


    @Override
    public HeartbeatInfo transForm(Object o) {
        HashMap map = (HashMap)o;
        HeartbeatInfo heartbeatInfo = BeanUtil.fillBeanWithMap(map, new HeartbeatInfo(), false);
        return  heartbeatInfo;
    }
}
