package com.duofuen.repair.web;

import com.duofuen.repair.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller
public class StoreWebController {

    private final StoreRepository storeRepository;
    private final Address1Repository address1Repository;
    private final Address2Repository address2Repository;

    @Autowired
    public StoreWebController(StoreRepository storeRepository, Address1Repository address1Repository, Address2Repository address2Repository) {
        this.storeRepository = storeRepository;
        this.address1Repository = address1Repository;
        this.address2Repository = address2Repository;
    }

    @RequestMapping("/listStore")
    public String listStore(Model model) {
        Iterable<Store> listStore = storeRepository.findAll();
        model.addAttribute("listStore", listStore);
        return "listStore";
    }

    @RequestMapping("/detailStore")
    public String detailStore(Integer storeId, Model model) {
        Store store;
        if (storeId != null) {
            Optional<Store> storeOptional = storeRepository.findById(storeId);
            Assert.isTrue(storeOptional.isPresent(), "store not exists");
            store = storeOptional.get();

            Iterable<Address2> listAddress2 = address2Repository.findAllByAddr1Id(store.getAddr1Id());
            model.addAttribute("listAddress2", listAddress2);
        } else {
            store = new Store();
            store.setEnabled(true);
        }

        Iterable<Address1> listAddress1 = address1Repository.findAll();

        model.addAttribute("store", store);
        model.addAttribute("listAddress1", listAddress1);
        return "detailStore";
    }

    @RequestMapping("/getAddr2List")
    @ResponseBody
    public Iterable<Address2> getAddr2List(Integer addr1Id) {
        if (addr1Id != null) {
            return address2Repository.findAllByAddr1Id(addr1Id);
        }
        return null;
    }

    @Transactional
    @RequestMapping("/saveStore")
    @ResponseBody
    public String saveStore(@RequestBody Store store) {
        storeRepository.save(store);
        return "success";
    }

    @Transactional
    @RequestMapping("/deleteStore")
    @ResponseBody
    public String deleteStore(Integer storeId) {
        storeRepository.deleteById(storeId);
        return "删除成功！";
    }

    @Transactional
    @RequestMapping("/toggleStoreEnable")
    @ResponseBody
    public String toggleStoreEnable(Integer storeId, Boolean enabled) {
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (!storeOptional.isPresent()) {
            return "门店不存在";
        }

        Store store = storeOptional.get();
        store.setEnabled(enabled);

        storeRepository.save(store);

        return "修改成功，门店 " + store.getName() + " 已经" + (enabled ? "启用" : "禁用");
    }
}
