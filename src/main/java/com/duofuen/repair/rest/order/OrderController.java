package com.duofuen.repair.rest.order;

import com.alibaba.fastjson.JSON;
import com.duofuen.repair.rest.BaseResponse;
import com.duofuen.repair.rest.RbNull;
import com.duofuen.repair.util.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.duofuen.repair.util.Const.Rest.*;

@RestController
@RequestMapping(path = ROOT_PATH)
@Validated
public class OrderController {
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("/submitOrder")
    public BaseResponse<RbOrderId> submitOrder(@RequestBody @Valid SubmitOrderRequest soRequest) {
        LOGGER.info("==>restful method submitOrder called, parameter: {}", JSON.toJSONString(soRequest));
//TODO
        RbOrderId rbOrderId = new RbOrderId();
        rbOrderId.setOrderId(soRequest.getManagerId() + soRequest.getStoreId());
        return BaseResponse.success(rbOrderId);
    }

    @GetMapping("/getOrderList")
    public BaseResponse<RbOrderList> getOrderList(@NotEmpty(message = "userId must not be empty") @RequestParam(name = Const.Rest.ORDER_USER_ID) String userId,
                                                  @NotNull @RequestParam(name = Const.Rest.ORDER_STEP) Integer step) {
        LOGGER.info("==>restful method getOrderList called, userId: {}, step: {}", userId, step);

        BaseResponse<RbOrderList> baseResponse;

        //TODO
        RbOrderList rbOrderList = new RbOrderList();
        List<RbOrderList.Order> orderList = new ArrayList<>();
        RbOrderList.Order order1 = rbOrderList.giveNewOrder();
        order1.setOrderId(1);
        order1.setStoreId(2);
        order1.setStoreName("测试1");
        order1.setStoreAddr("南京西路");
        order1.setManagerId(1);
        order1.setRepairmanId(4);
        order1.setTitle("坏掉了");
        order1.setOrderState("00");
        order1.setCreateTime(String.valueOf(System.currentTimeMillis()));

        RbOrderList.Order order2 = rbOrderList.giveNewOrder();
        order2.setOrderId(2);
        order2.setStoreId(3);
        order2.setStoreName("测试2");
        order2.setStoreAddr("人民广场");
        order2.setManagerId(1);
        order2.setRepairmanId(4);
        order2.setTitle("又坏掉了");
        order2.setOrderState("01");
        order2.setCreateTime(String.valueOf(System.currentTimeMillis()));

        orderList.add(order1);
        orderList.add(order2);
        rbOrderList.setOrderList(orderList);

        baseResponse = BaseResponse.success(rbOrderList);

        return baseResponse;
    }

    @GetMapping("/getDetailOrder")
    public BaseResponse<RbDetailOrder> getDetailOrder(@RequestParam(name = Const.Rest.ORDER_USER_ID) Integer userId,
                                                      @RequestParam(name = Const.Rest.ORDER_ORDER_ID) Integer orderId) {
        LOGGER.info("==>restful method getDetailOrder called, userId: {}, orderId: {}", userId, orderId);
        //TODO
        BaseResponse<RbDetailOrder> baseResponse;

        RbDetailOrder detailOrder = new RbDetailOrder();
        detailOrder.setOrderId(Integer.valueOf(orderId));
        detailOrder.setStoreId(1);
        detailOrder.setStoreName("南京西路店");
        detailOrder.setStoreAddr("南京西路");
        detailOrder.setManagerId(Integer.valueOf(userId));
        detailOrder.setRepairmanId(4);
        detailOrder.setRepairmanPhoneNum("13817999999");
        detailOrder.setTitle("坏掉了");
        detailOrder.setDesc("描述坏掉了");
        detailOrder.setOrderState("00");
        detailOrder.setCreateTime("1529939375013");
        detailOrder.setFinishTime("1529939575013");
        List<RbDetailOrder.Img> imgs = new ArrayList<>();
        RbDetailOrder.Img img = detailOrder.giveOneImg();
        img.setImageId(1);
        img.setImgUrl("//undefined");
        imgs.add(img);
        detailOrder.setImgs(imgs);

        baseResponse = BaseResponse.success(detailOrder);

        return baseResponse;
    }

    @PostMapping("/completeOrder")
    public BaseResponse<RbNull> completeOrder(@RequestBody Map<String, String> map) {
        LOGGER.info("==>restful method completeOrder called, param: {}", map);
        String userId = map.get(ORDER_USER_ID);
        String orderId = map.get(ORDER_ORDER_ID);

// TODO
        return BaseResponse.success(new RbNull());
    }
}
