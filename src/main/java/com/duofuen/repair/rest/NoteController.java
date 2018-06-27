package com.duofuen.repair.rest;

import com.duofuen.repair.rest.order.RbOrderList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.duofuen.repair.util.Const.Rest.*;

@RestController
@RequestMapping(path = ROOT_PATH)
public class NoteController {

    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("createNote")
    public BaseResponse<RbNull> createNote(@RequestBody Map<String, String> map) {
        LOGGER.info("==>restful method createNote called, param: {}", map);

        String userId = map.get(NOTE_USER_ID);
        String orderId = map.get(NOTE_ORDER_ID);
        String content = map.get(NOTE_CONTENT);

//TODO
        return BaseResponse.success(new RbNull());
    }


    @GetMapping("getNoteList")
    public BaseResponse<RbNoteList> getNoteList(@RequestParam(name = NOTE_USER_ID) String userId,
                                                @RequestParam(name = NOTE_ORDER_ID) String orderId) {
        LOGGER.info("==>restful method getNoteList called, userId: {}, orderId: {}", userId, orderId);
        BaseResponse<RbNoteList> baseResponse;

        //TODO
        RbNoteList rbNoteList = new RbNoteList();
        List<RbNoteList.Note> noteList = new ArrayList<>();
        RbNoteList.Note note1 = rbNoteList.giveOneNote();
        note1.setNoteId(1);
        note1.setContent("test test");
        note1.setCreateTime(String.valueOf(System.currentTimeMillis() - 20000));

        RbNoteList.Note note2 = rbNoteList.giveOneNote();
        note2.setNoteId(2);
        note2.setContent("测试测试");
        note2.setCreateTime(String.valueOf(System.currentTimeMillis() - 15000));


        noteList.add(note1);
        noteList.add(note2);
        rbNoteList.setNoteList(noteList);

        baseResponse = BaseResponse.success(rbNoteList);

        return baseResponse;

    }

}
