package com.example.controller.file;

import com.example.model.file.Resp;
import com.example.model.file.TagAddUpdateForm;
import com.example.model.file.TagVo;
import com.example.service.file.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查询所有标签
     *
     * @return
     */
    @GetMapping("/getAll")
    public Resp<List<List<TagVo>>> getAll() {
        return Resp.success(tagService.getAllTags());
    }

    /**
     * 按动画Id获得标签
     */
    @GetMapping("/getByAnimationId")
    public Resp<List<TagVo>> getByAnimationId(@RequestParam("animationId") Integer animationId) {
        return Resp.success(tagService.getByAnimationId(animationId));
    }

    /**
     * 增加标签
     */
    @PostMapping("/add")
    public Resp add(@RequestBody TagAddUpdateForm form) {
        tagService.add(form.getAnimationId(), form.getTagId());
        return Resp.success();
    }

    /**
     * 减少标签
     */
    @PostMapping("/delete")
    public Resp delete(@RequestBody TagAddUpdateForm form) {
        tagService.delete(form.getAnimationId(), form.getTagId());
        return Resp.success();
    }
    //查询
}
