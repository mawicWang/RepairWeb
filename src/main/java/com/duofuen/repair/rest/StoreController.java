package com.duofuen.repair.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.duofuen.repair.util.Const.Rest.ROOT_PATH;

@RestController
@RequestMapping(path = ROOT_PATH)
public class StoreController {
    private static final Logger LOGGER = LogManager.getLogger();

    @GetMapping("/getStoreList")
    public BaseResponse<RbStoreList> getStoreList(@RequestParam Integer userId){
        LOGGER.info("====>restful method getStoreList called, userId {}", userId);

        BaseResponse<RbStoreList> baseResponse;
        //TODO
        RbStoreList rbStoreList = new RbStoreList();
        RbStoreList.Store store1 = rbStoreList.giveOneStore();
        store1.setStoreId(1);
        store1.setStoreName("南京东路店");
        store1.setStoreAddr("上海市黄浦区南京东路123号");
        RbStoreList.Store store2 = rbStoreList.giveOneStore();
        store2.setStoreId(2);
        store2.setStoreName("人名广场店");
        store2.setStoreName("上海市黄浦区人名广场10086号");
        RbStoreList.Store store3 = rbStoreList.giveOneStore();
        store3.setStoreId(3);
        store3.setStoreName("新天地店");
        store3.setStoreName("上海市黄浦区淮海东路888号");
        List<RbStoreList.Store> storeList = new ArrayList<>();
        storeList.add(store1);
        storeList.add(store2);
        storeList.add(store3);
        rbStoreList.setStoreList(storeList);


        return BaseResponse.success(rbStoreList);
    }

}
