package com.duofuen.repair.rest;

import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.CharacterRepository;
import com.duofuen.repair.domain.Store;
import com.duofuen.repair.domain.StoreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.duofuen.repair.util.Const.ROLE_CODE_MANAGER;
import static com.duofuen.repair.util.Const.Rest.ROOT_PATH;

@RestController
@RequestMapping(path = ROOT_PATH)
public class StoreController {
    private static final Logger LOGGER = LogManager.getLogger();

    private final StoreRepository storeRepository;
    private final CharacterRepository characterRepository;

    @Autowired
    public StoreController(StoreRepository storeRepository, CharacterRepository characterRepository) {
        this.storeRepository = storeRepository;
        this.characterRepository = characterRepository;
    }

    @GetMapping("/getStoreList")
    public BaseResponse<RbStoreList> getStoreList(@RequestParam Integer userId) {
        LOGGER.info("====>restful method getStoreList called, userId {}", userId);

        BaseResponse<RbStoreList> baseResponse;
        if (StringUtils.isEmpty(userId)) {
            return BaseResponse.fail("userId must not be empty");
        }

        Optional<Character> character = characterRepository.findByIdAndEnabledTrue(userId);
        if (!character.isPresent() || !character.get().getRoleCode().equals(ROLE_CODE_MANAGER)) {
            baseResponse = BaseResponse.fail("you must be a MANAGER");
        } else {
            List<Store> stores = storeRepository.findAllByUserIdAndEnabledTrue(userId, ROLE_CODE_MANAGER);
            RbStoreList rbStoreList = new RbStoreList();
            List<RbStoreList.Store> storeList = new ArrayList<>();
            for (Store s : stores) {
                RbStoreList.Store store = rbStoreList.giveOneStore();
                store.setStoreId(s.getId());
                store.setStoreName(s.getName());
                store.setStoreAddr(s.getCompleteAddr());
                storeList.add(store);
            }
            rbStoreList.setStoreList(storeList);

            baseResponse = BaseResponse.success(rbStoreList);
        }
        return baseResponse;
    }

}
