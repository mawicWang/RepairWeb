package com.duofuen.repair.rest.order;

import com.alibaba.fastjson.JSON;
import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.*;
import com.duofuen.repair.rest.BaseResponse;
import com.duofuen.repair.rest.RbNull;
import com.duofuen.repair.service.UserService;
import com.duofuen.repair.util.ChuangLanSmsUtil;
import com.duofuen.repair.util.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

import static com.duofuen.repair.util.Const.Rest.*;

@RestController
@RequestMapping(path = ROOT_PATH)
@Validated
public class OrderController {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StoreRepository storeRepository;
    private final CharacterRepository characterRepository;
    private final OrderRepository orderRepository;
    private final OrderImageRepository orderImageRepository;
    private final UserService userService;
    private final OrderRecordRepository orderRecordRepository;

    @Autowired
    public OrderController(StoreRepository storeRepository, CharacterRepository characterRepository,
                           OrderRepository orderRepository, OrderImageRepository orderImageRepository,
                           UserService userService, OrderRecordRepository orderRecordRepository) {
        this.storeRepository = storeRepository;
        this.characterRepository = characterRepository;
        this.orderRepository = orderRepository;
        this.orderImageRepository = orderImageRepository;
        this.userService = userService;
        this.orderRecordRepository = orderRecordRepository;
    }

    @Transactional
    @PostMapping("/submitOrder")
    public BaseResponse<RbOrderId> submitOrder(@RequestBody @Valid SubmitOrderRequest soRequest) {
        LOGGER.info("==>restful method submitOrder called, parameter: {}", JSON.toJSONString(soRequest));

        BaseResponse<RbOrderId> baseResponse;

        Optional<Character> character = characterRepository.findByIdAndEnabledTrue(soRequest.getManagerId());
        if (!character.isPresent() || !character.get().getRoleCode().equals(Const.ROLE_CODE_MANAGER)) {
            return BaseResponse.fail("you must be a MANAGER");
        }
        Optional<Store> storeOptional = storeRepository.findByIdAndEnabledTrue(soRequest.getStoreId());
        if (!storeOptional.isPresent()) {
            baseResponse = BaseResponse.fail("invalid store id :" + soRequest.getStoreId());
        } else {
            Character repairman = characterRepository.findByStoreIdAndRoleCodeAndEnabledTrue(soRequest.getStoreId(), Const.ROLE_CODE_REPAIRMAN);

            // save order
            Order order = new Order();
            order.setManagerId(soRequest.getManagerId());
            order.setStoreId(soRequest.getStoreId());
            order.setTitle(soRequest.getTitle());
            order.setDesc(soRequest.getDesc());
            order.setOrderState(Const.ORDER_STATE_OPEN);
            order.setCreateTime(Date.from(Instant.now()));
            if (repairman != null) {
                order.setRepairmanId(repairman.getId());
            }
            orderRepository.save(order);

            //save image
            for (Integer imgId : soRequest.getImgs()) {
                OrderImage orderImage = new OrderImage();
                orderImage.setId(new OrderImagePK(order.getId(), imgId));
                orderImageRepository.save(orderImage);
            }
            RbOrderId rbOrderId = new RbOrderId(order.getId());
            baseResponse = BaseResponse.success(rbOrderId);


            // save order operation record
            orderRecordRepository.save(
                    new OrderRecord(order.getId(),
                            "create",
                            MessageFormat.format("用户（区域经理）#{0} 提交新订单，id#{1} ，门店#{2}，师傅#{3}",
                                    order.getManagerId(), order.getId(), order.getStoreId(), order.getStoreId()),
                            new Date()));

            if (repairman == null) {
                // 发送短信给客服配师傅
                LOGGER.warn("下单成功，无匹配师傅，发送短信给客服！id:{}, storeId:{}", order.getId(), soRequest.getStoreId());
                Users admin = userService.findAdministrator();
                String msg = MessageFormat.format("订单#{0} 辖区内无配送师傅，请分配师傅！", order.getId());
                ChuangLanSmsUtil.sendMsg(admin.getUserInfo().getPhoneNum(), msg);
            } else {
                // 发送短信给师傅
                String message = MessageFormat.format(Const.MSG_NEW_ORDER, storeOptional.get().getName(),
                        storeOptional.get().getCompleteAddrDisplay(), storeOptional.get().getTelephone());
                boolean msgSuccess = ChuangLanSmsUtil.sendMsg(repairman.getPhoneNum(), message);
                if (!msgSuccess) {
                    LOGGER.warn("发送短信给师傅失败！");
                }
            }

            // 发送短信给客服
            String message = MessageFormat.format(Const.MSG_NEW_ORDER, storeOptional.get().getName(),
                    storeOptional.get().getCompleteAddrDisplay(), storeOptional.get().getTelephone());
            boolean msgCusSerSuccess = ChuangLanSmsUtil.sendMsg(Const.CUSTOMER_SERVIER_PHONENUM, message);
            if (!msgCusSerSuccess) {
                LOGGER.warn("发送短信给客服失败！");
            }
        }

        return baseResponse;
    }

    @GetMapping("/getOrderList")
    public BaseResponse<RbOrderList> getOrderList(@NotNull(message = "userId must not be null") @RequestParam(name = Const.Rest.ORDER_USER_ID) Integer userId,
                                                  @NotNull(message = "step must not be null") @RequestParam(name = Const.Rest.ORDER_STEP) Integer step) {
        LOGGER.info("==>restful method getOrderList called, userId: {}, step: {}", userId, step);

        BaseResponse<RbOrderList> baseResponse;

        Optional<Character> character = characterRepository.findByIdAndEnabledTrue(userId);
        if (!character.isPresent()) {
            return BaseResponse.fail("not invalid userid " + userId);
        }

        Page<Order> orders;
        if (character.get().getRoleCode().equals(Const.ROLE_CODE_MANAGER)) {
            orders = orderRepository.findAllByManagerIdOrderByCreateTimeDesc(userId, PageRequest.of(step, Const.ORDER_PER_PAGE));
        } else {
            orders = orderRepository.findAllByRepairmanIdOrderByCreateTimeDesc(userId, PageRequest.of(step, Const.ORDER_PER_PAGE));
        }

        RbOrderList rbOrderList = new RbOrderList();
        List<RbOrderList.Order> orderList = new ArrayList<>();
        for (Order o : orders) {
            RbOrderList.Order order = rbOrderList.giveNewOrder();
            order.setOrderId(o.getId());

            Store store = storeRepository.findById(o.getStoreId()).get();
            order.setStoreId(store.getId());
            order.setStoreName(store.getName());
            order.setStoreAddr(store.getCompleteAddrDisplay());

            order.setManagerId(o.getManagerId());
            order.setRepairmanId(o.getRepairmanId());
            order.setTitle(o.getTitle());
            order.setOrderState(o.getOrderState());
            order.setCreateTime(String.valueOf(o.getCreateTime().getTime()));

            orderList.add(order);
        }

        rbOrderList.setOrderList(orderList);
        baseResponse = BaseResponse.success(rbOrderList);

        return baseResponse;
    }

    @GetMapping("/getDetailOrder")
    public BaseResponse<RbDetailOrder> getDetailOrder(@NotNull(message = "userId must not be null") @RequestParam(name = Const.Rest.ORDER_USER_ID) Integer userId,
                                                      @NotNull(message = "orderId must not be null") @RequestParam(name = Const.Rest.ORDER_ORDER_ID) Integer orderId) {
        LOGGER.info("==>restful method getDetailOrder called, userId: {}, orderId: {}", userId, orderId);

        BaseResponse<RbDetailOrder> baseResponse;

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            return BaseResponse.fail("not invalid order id : " + orderId);
        }
        Order order = orderOptional.get();

        RbDetailOrder detailOrder = new RbDetailOrder();
        detailOrder.setOrderId(order.getId());

        Store store = storeRepository.findById(order.getStoreId()).get();
        detailOrder.setStoreId(store.getId());
        detailOrder.setStoreName(store.getName());
        detailOrder.setStoreTel(store.getTelephone());
        detailOrder.setStoreAddr(store.getCompleteAddrDisplay());
        detailOrder.setManagerId(Integer.valueOf(userId));

        if (order.getRepairmanId() != null) {
            Optional<Character> optionalCharacter = characterRepository.findById(order.getRepairmanId());
            if (optionalCharacter.isPresent()) {
                Character repairman = optionalCharacter.get();
                detailOrder.setRepairmanId(repairman.getId());
                detailOrder.setRepairmanName(repairman.getUsername());
                detailOrder.setRepairmanPhoneNum(repairman.getPhoneNum());
            }
        }

        detailOrder.setTitle(order.getTitle());
        detailOrder.setDesc(order.getDesc());
        detailOrder.setOrderState(order.getOrderState());
        detailOrder.setCreateTime(dateToTimestamp(order.getCreateTime()));
        detailOrder.setFinishTime(dateToTimestamp(order.getFinishTime()));

        List<OrderImage> orderImages = orderImageRepository.findAllById_OrderId(orderId);
        List<RbDetailOrder.Img> imgs = new ArrayList<>();
        for (OrderImage orderImage : orderImages) {
            RbDetailOrder.Img img = detailOrder.giveOneImg();
            img.setImgId(orderImage.getId().getImageId());
            img.setImgUrl(ROOT_PATH + "/viewImage?id=" + orderImage.getId().getImageId());
            imgs.add(img);
        }
        detailOrder.setImgs(imgs);

        baseResponse = BaseResponse.success(detailOrder);

        return baseResponse;
    }

    @Transactional
    @PostMapping("/completeOrder")
    public BaseResponse<RbNull> completeOrder(@RequestBody Map<String, String> map) {
        LOGGER.info("==>restful method completeOrder called, param: {}", map);
        String userId = map.get(ORDER_USER_ID);
        String orderId = map.get(ORDER_ORDER_ID);

        BaseResponse<RbNull> response;

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(orderId)) {
            return BaseResponse.fail("empty param userId/orderId");
        }

        Optional<Character> character = characterRepository.findByIdAndEnabledTrue(Integer.valueOf(userId));
        if (!character.isPresent() || !character.get().getRoleCode().equals(Const.ROLE_CODE_REPAIRMAN)) {
            return BaseResponse.fail("you must be a repair man");
        }

        Optional<Order> orderOptional = orderRepository.findById(Integer.valueOf(orderId));
        if (!orderOptional.isPresent()) {
            response = BaseResponse.fail("not validate orderId : " + orderId);
        } else if (orderOptional.get().getRepairmanId() == null ||
                !orderOptional.get().getRepairmanId().toString().equals(userId)) {
            response = BaseResponse.fail("you are not responsible for this order");
        } else if (orderOptional.get().getOrderState().equals(Const.ORDER_STATE_FINISH)) {
            response = BaseResponse.fail("this order is already finished");
        } else {
            Order order = orderOptional.get();
            order.setOrderState(Const.ORDER_STATE_FINISH);
            order.setFinishTime(new Date());
            orderRepository.save(order);

            // save order operation record
            orderRecordRepository.save(
                    new OrderRecord(order.getId(),
                            "complete",
                            MessageFormat.format("用户（维修师傅）#{0} 完成订单 id #{1}",
                                    order.getRepairmanId(), order.getId()),
                            new Date()));

            response = BaseResponse.success(new RbNull());
        }

        return response;
    }

    private String dateToTimestamp(Date date) {
        return null == date ? null : String.valueOf(date.getTime());
    }
}
