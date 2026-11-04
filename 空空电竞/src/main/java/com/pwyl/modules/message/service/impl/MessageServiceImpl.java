package com.pwyl.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Query;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.message.dao.MessageInfoDao;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.taking.dao.GameDao;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.service.GameService;
import com.pwyl.modules.talk.dao.TalkRepository;
import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.service.TalkService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends
        ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderTakingDao orderTakingDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TalkService talkService;
    @Autowired
    private TalkRepository talkRepository;
    @Autowired
    private GameDao gameDao;

    @Override
    public Result auditMessage(Long messageId,Integer status,String auditContent){
        MessageInfo messageInfo = baseMapper.selectById(messageId);
        messageInfo.setStatus(status);
        UserEntity byId = userService.getById(messageInfo.getByUserId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(status==2){
            if(byId.getStatus().equals(1)){

                //2封号
                byId.setStatus(2);
                userService.updateById(byId);
                //任务下架
                orderTakingDao.updateTakingStatusByUserId(byId.getUserId());
                //动态下架
                talkRepository.updateTalkStatusByUserId(byId.getUserId());
                MessageInfo messageInfos = new MessageInfo();
                messageInfos.setContent("当前账号存在违规行为，已被系统封禁，如有疑问，请联系客服！");
                messageInfos.setTitle("账号封禁通知");
                messageInfos.setState(String.valueOf(5));
                messageInfos.setUserName(byId.getUserName());
                messageInfos.setUserId(String.valueOf(byId.getUserId()));
                messageInfos.setCreateAt(sdf.format(new Date()));
                messageInfos.setIsSee("0");
                baseMapper.insert(messageInfos);
            }

        }else if(status==3){
            //3下架
            if("3".equals(messageInfo.getPlatform())){
                Talk talk = talkRepository.findById(messageInfo.getPlatformId()).orElse(null);
                if(talk!=null && (talk.getIsDelete()==null || talk.getIsDelete()!=1)){
                    byId = userService.getById(talk.getUserId());
                    talkService.deleteTalk(messageInfo.getPlatformId());
                    MessageInfo messageInfos = new MessageInfo();
                    String content=talk.getContent();
                    if(content.length()>5){
                        content=content.substring(0,5)+"...";
                    }
                    messageInfos.setContent("您发布的动态："+content+"，涉及违规，已做删除处理,如有疑问请联系客服！");
                    messageInfos.setTitle("动态封禁通知");
                    messageInfos.setState(String.valueOf(4));
                    messageInfos.setUserName(byId.getUserName());
                    messageInfos.setUserId(String.valueOf(byId.getUserId()));
                    messageInfos.setCreateAt(sdf.format(new Date()));
                    messageInfos.setIsSee("0");
                    baseMapper.insert(messageInfos);
                }
            }else if(messageInfo.getPlatform().equals("4")){
                OrderTaking orderTaking = orderTakingDao.selectById(messageInfo.getPlatformId());
                if(orderTaking!=null){
                    byId = userService.getById(orderTaking.getUserId());
                    orderTakingDao.deleteById(messageInfo.getPlatformId());
                    MessageInfo messageInfos = new MessageInfo();
                    Game game = gameDao.selectById(orderTaking.getGameId());
                    String content=game.getGameName();
                    if(content.length()>5){
                        content=content.substring(0,5)+"...";
                    }
                    messageInfos.setContent("您发布的服务："+content+"，涉及违规，已做删除处理,如有疑问请联系客服！");
                    messageInfos.setTitle("接单封禁通知");
                    messageInfos.setState(String.valueOf(4));
                    messageInfos.setUserName(byId.getUserName());
                    messageInfos.setUserId(String.valueOf(byId.getUserId()));
                    messageInfos.setCreateAt(sdf.format(new Date()));
                    messageInfos.setIsSee("0");
                    baseMapper.insert(messageInfos);
                }
            }
        }
        messageInfo.setAuditContent(auditContent);
        baseMapper.updateById(messageInfo);
        return Result.success();
    }


    @Override
    public PageUtils selectMessageList(Map<String,Object> params){
        Long userId = (Long)params.get("userId");
        Integer state = (Integer)params.get("state");
        Integer type = (Integer)params.get("type");
        Integer platform = (Integer)params.get("platform");
        Integer status = (Integer)params.get("status");
        IPage<MessageInfo> page = this.page(
                new Query<MessageInfo>().getPage(params),
                new QueryWrapper<MessageInfo>()
                        .eq(userId!=null,"user_id", userId)
                        .eq(state!=null,"state", state)
                        .and(state==null,Wrapper -> Wrapper.eq("state", "4").or().eq("state ","5"))
                        .eq(type!=null,"type", type)
                        .eq(status!=null && status!=0,"status", status)
                        .eq(platform!=null,"platform", platform).orderByDesc("create_at")
        );
        List<MessageInfo> records = page.getRecords();
        if(records.size()>0){
            for(MessageInfo messageInfo:records){
                if(state!=null &&  state==9){
                    if(StringUtils.isNotEmpty(messageInfo.getType())){
                        Orders orders = ordersService.getById(Long.parseLong(messageInfo.getType()));
                        messageInfo.setOrders(orders);
                        if(orders!=null){
                            OrderTaking orderTaking = orderTakingDao.selectById(orders.getOrderTakingId());
                            messageInfo.setOrderTaking(orderTaking);
                        }
                    }
                }
                if(messageInfo.getUserId()!=null){
                    UserEntity user = userService.getById(messageInfo.getUserId());
                    if(user!=null){
                        messageInfo.setUserName(user.getUserName());
                    }
                }
                if(messageInfo.getByUserId()!=null){
                    UserEntity user = userService.getById(messageInfo.getByUserId());
                    if(user!=null){
                        messageInfo.setByUserName(user.getUserName());
                    }
                }

            }
        }

        return new PageUtils(page);
    }

    @Override
    public int saveBody(MessageInfo messageInfo){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setStatus(0);
        messageInfo.setCreateAt(sdf.format(now));
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public int update(MessageInfo messageInfo){
        return messageInfoDao.updateById(messageInfo);
    }

    @Override
    public int delete(Long id){
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id){
        return messageInfoDao.selectById(id);
    }

    @Override
    public int updateSendState(Long userId, Integer state) {
        return messageInfoDao.updateSendState(userId, state);
    }


}
