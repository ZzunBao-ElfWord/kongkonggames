package com.pwyl.modules.orders.controller;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.gift.dao.GiftUserDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.utils.excel.ExcelData;
import com.pwyl.modules.utils.excel.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Api(value = "订单信息", tags = {"订单信息"})
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private GiftUserDao giftUserDao;


    @PostMapping("/insertOrders")
    @ApiOperation("添加订单")
    public Result insertOrders(@RequestBody Orders orders){
        return ordersService.insertOrders(orders);
    }



    /**
     * 查看所有订单
     *
     * @param page
     * @param limit
     * @param name
     * @param status
     * @return
     */
    @RequestMapping("/queryOrders")
    public Result queryOrdersAll(Long page, Long limit, Long type,String orderName, String name, Long status,Long userId,Integer orderType,Integer ordersType,String startTime,String endTime,String ordersNo) {

        return ordersService.queryOrdersAll(page, limit, type, orderName,name, status,userId,orderType,ordersType,startTime,endTime,ordersNo);
    }

    @GetMapping("/ordersListExcel")
    @ApiOperation("订单导出")
    public void ordersListExcel(Long type,String orderName,  String name, Long status, Long userId, Integer orderType,Integer ordersType,String startTime,String endTime,String ordersNo, HttpServletResponse response) throws Exception {
        ExcelData data = ordersService.ordersListExcel(type, name, orderName, status,userId,orderType,ordersType,startTime,endTime,ordersNo);
        ExportExcelUtils.exportExcel(response,"订单列表.xlsx",data);
    }

    @ApiOperation("修改订单状态")
    @GetMapping("/cancelOrder")
    public Result cancelOrder(Long id, String status) {
        return ordersService.cancelOrder(id, status);
    }

    /**
     * 删除订单
     */
    @RequestMapping("/deleteOrders")
    public Result deleteOrders(Long id) {
        return ordersService.deleteOrder(id);
    }

    @GetMapping("/selectMyTakeOrders")
    @ApiOperation("查询我的接单")
    public Result selectMyTakeOrders(Integer page, Integer limit,Long userId, Integer status){
        return ordersService.selectMyTakeOrders(page,limit,userId,status);
    }

    @GetMapping("/selectTeamOrdersList")
    @ApiOperation("获取团队订单")
    public Result selectTeamOrdersList(Long userId, Integer page, Integer limit, Integer type, Integer status){
        return ordersService.selectTeamOrdersList(page,limit,userId,type,status);
    }


    @GetMapping("/selectTeamUserList")
    @ApiOperation("获取团队列表")
    public Result selectTeamUserList(Long userId, Integer page, Integer limit, Integer type){
        UserEntity user = userService.selectUserById(userId);
        return ordersService.selectTeamUserList(page,limit,user.getInvitationCode(),type,userId);
    }

    @GetMapping("/selectTeamStatistics")
    @ApiOperation("团队统计")
    public Result selectTeamStatistics(Long userId,Integer type){
        UserEntity user = userService.selectUserById(userId);
        Double teamMoney = ordersService.selectOrdersMoneyCountByUserId(user.getUserId(), type);
        Integer teamCount = ordersService.selectUserCountByInvitationCode(user.getInvitationCode(), type);
        Map<String,Object> result=new HashMap<>();
        result.put("teamMoney",teamMoney);
        result.put("teamCount",teamCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectOrdersCount")
    @ApiOperation("订单统计")
    public Result selectOrdersCount(String time,Integer flag){
        //0待支付1待服务2已完成3已退款 4进行中
        Integer ordersSumCount = ordersService.selectOrdersCountByState(null, 1,flag, time);
        Integer ordersDZFCount = ordersService.selectOrdersCountByState(0, 1, flag, time);
        Integer ordersDFWCount = ordersService.selectOrdersCountByState(1, 1, flag, time);
        Integer ordersYWCCount = ordersService.selectOrdersCountByState(2, 1,flag, time);
        Integer ordersYTKCount = ordersService.selectOrdersCountByState(3, 1, flag, time);
        Integer ordersJXZCount = ordersService.selectOrdersCountByState(4, 1,flag, time);

        Integer yueSumCount = ordersService.selectOrdersCountByState(null, 2,flag, time);
        Integer yueDZFCount = ordersService.selectOrdersCountByState(0, 2, flag, time);
        Integer yueDFWCount = ordersService.selectOrdersCountByState(1, 2, flag, time);
        Integer yueYWCCount = ordersService.selectOrdersCountByState(2, 2,flag, time);
        Integer yueYTKCount = ordersService.selectOrdersCountByState(3, 2, flag, time);
        Integer yueJXZCount = ordersService.selectOrdersCountByState(4, 2,flag, time);

        Map<String,Object> result=new HashMap<>();
        result.put("ordersSumCount",ordersSumCount);result.put("yueSumCount",yueSumCount);
        result.put("ordersDZFCount",ordersDZFCount);result.put("yueDZFCount",yueDZFCount);
        result.put("ordersDFWCount",ordersDFWCount);result.put("yueDFWCount",yueDFWCount);
        result.put("ordersYWCCount",ordersYWCCount);result.put("yueYWCCount",yueYWCCount);
        result.put("ordersYTKCount",ordersYTKCount);result.put("yueYTKCount",yueYTKCount);
        result.put("ordersJXZCount",ordersJXZCount);result.put("yueJXZCount",yueJXZCount);
        return Result.success().put("data",result);
    }


    @GetMapping("/selectMoneyCount")
    @ApiOperation("统计收益")
    public Result selectMoneyCount(String time,Integer flag){
        BigDecimal sumOrdersMoney = ordersService.selectOrdersMoneyByTime(1,flag, time);
        BigDecimal pingOrdersMoney = ordersService.selectOrdersMoneyByTime(2,flag, time);
        BigDecimal sumGiftMoney = giftUserDao.selectOrdersMoneyByTime(1, flag, time);
        BigDecimal pingGiftMoney = giftUserDao.selectOrdersMoneyByTime(2, flag, time);
        Map<String,Object> result=new HashMap<>();
        result.put("sumOrdersMoney",sumOrdersMoney);result.put("sumGiftMoney",sumGiftMoney);
        result.put("pingOrdersMoney",pingOrdersMoney);result.put("pingGiftMoney",pingGiftMoney);
        result.put("sumMoney",sumOrdersMoney.add(sumGiftMoney));result.put("pingMoney",pingOrdersMoney.add(pingGiftMoney));
        return Result.success().put("data",result);
    }

    @GetMapping("/selectTakingUserOrdersList")
    @ApiOperation("陪玩收益排行榜")
    public Result selectTakingUserList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",ordersService.selectTakingUserList(page, limit, userName, phone));
    }

    @GetMapping("/selectUserMoneyOrdersList")
    @ApiOperation("用户支付排行榜")
    public Result selectUserMoneyOrdersList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",ordersService.selectUserMoneyOrdersList(page, limit, userName, phone));
    }

    @GetMapping("/selectGiveRewardList")
    @ApiOperation("被打赏人排行榜")
    public Result selectGiveRewardList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",ordersService.selectGiveRewardList(page, limit, userName, phone));
    }

}
