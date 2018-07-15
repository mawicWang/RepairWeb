package com.duofuen.repair.web;


import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.*;
import com.duofuen.repair.util.ChuangLanSmsUtil;
import com.duofuen.repair.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.*;

@Controller
public class OrderWebController {

    private final OrderRepository orderRepository;
    private final CharacterRepository characterRepository;
    private final OrderRecordRepository orderRecordRepository;


    @Autowired
    public OrderWebController(OrderRepository orderRepository, CharacterRepository characterRepository,
                              OrderRecordRepository orderRecordRepository) {
        this.orderRepository = orderRepository;
        this.characterRepository = characterRepository;
        this.orderRecordRepository = orderRecordRepository;
    }

    @RequestMapping("/listOrder")
    public String listCharacter(Model model) {
        Iterable<Order> orders = orderRepository.findAll();
        model.addAttribute("listOrder", orders);
        model.addAttribute("orderStateMap", orderStateMap());
//        model.addAttribute("dateFormatter", orderStateMap());
        return "listOrder";
    }

    @Transactional
    @RequestMapping("/changeRepairman")
    public String changeRepairman(Integer orderId, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Assert.isTrue(orderOptional.isPresent(), "orderId not correct!");
        Assert.isTrue(orderOptional.get().getOrderState().equals(Const.ORDER_STATE_OPEN), "order is already finished!");

        List<Character> listRepairman = characterRepository.findByAddress1IdAndEnabledTrue(orderOptional.get().getStore().getAddr1Id(), Const.ROLE_CODE_REPAIRMAN);
        model.addAttribute("listRepairman", listRepairman);
        model.addAttribute("curRepairmanId", orderOptional.get().getRepairmanId());
        model.addAttribute("orderId", orderId);
        return "changeRepairman";
    }

    @Transactional
    @RequestMapping("/saveChangeRepairman")
    @ResponseBody
    public String saveChangeRepairman(@RequestBody Map<String, Integer> param) {
        Optional<Order> orderOptional = orderRepository.findById(param.get("orderId"));
        Assert.isTrue(orderOptional.isPresent(), "orderId not correct!");
        Assert.isTrue(orderOptional.get().getOrderState().equals(Const.ORDER_STATE_OPEN), "order is already finished!");

        Optional<Character> characterOptional = characterRepository.findById(param.get("repairmanId"));
        Assert.isTrue(characterOptional.isPresent() && characterOptional.get().getRoleCode().equals(Const.ROLE_CODE_REPAIRMAN)
                , "you must be a repairman!");

        // check repairman is available for order
        List<Character> listRepairman = characterRepository.findByAddress1IdAndEnabledTrue(orderOptional.get().getStore().getAddr1Id(), Const.ROLE_CODE_REPAIRMAN);
        Assert.isTrue(listRepairman.contains(characterOptional.get()), "repair man is not available for order");

        Order order = orderOptional.get();
        Integer curRepairmanId = order.getRepairmanId();
        order.setRepairmanId(param.get("repairmanId"));
        orderRepository.save(order);

        // save order operation record
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        orderRecordRepository.save(
                new OrderRecord(order.getId(),
                        "changeRepairman",
                        MessageFormat.format("后台用户 <{0}> 为订单id#{1} 重新分配维修师傅。从 #{2} 修改为 #{3}",
                                username, order.getId(), curRepairmanId, order.getRepairmanId()),
                        new Date()));

        // 发送短信给师傅
        String message = MessageFormat.format(Const.MSG_NEW_ORDER, order.getStore().getName(),
                order.getStore().getCompleteAddrDisplay(), order.getStore().getTelephone());
        boolean msgSuccess = ChuangLanSmsUtil.sendMsg(characterOptional.get().getPhoneNum(), message);
        if (!msgSuccess) {
            return "发送短信给师傅失败！请人工处理。";
        }
        return "重新分配师傅成功，系统会以短信通知师傅。";
    }

    @RequestMapping("/showOrderHistory")
    public String showOrderHistory(Integer orderId, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Assert.isTrue(orderOptional.isPresent(), "invalid orderId");

        Iterable<OrderRecord> listOrderRecord = orderRecordRepository.findAllByOrderId(orderId);
        model.addAttribute("listOrderRecord", listOrderRecord);

        return "listOrderRecord";
    }


    private Map<String, String> orderStateMap() {
        Map<String, String> map = new HashMap<>();
        map.put(Const.ORDER_STATE_OPEN, Const.ORDER_STATE_OPEN_NAME);
        map.put(Const.ORDER_STATE_FINISH, Const.ORDER_STATE_FINISH_NAME);
        return map;
    }


}
