package com.example.service.file;

import com.example.dao.entity.TagEntity;
import com.example.dao.mapper.AnimationTagMapper;
import com.example.dao.mapper.TagMapper;
import com.example.model.file.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签管理相关
 */
@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private AnimationTagMapper animationTagMapper;

    public List<TagVo> getAllTags() {
        List<TagVo> result = new ArrayList<>();
        List<TagEntity> tagEntityList = tagMapper.getAll();
        for (TagEntity entity : tagEntityList) {
            result.add(new TagVo(entity.getId(), entity.getName()));
        }
        return result;
    }

    public void add(Integer animationId, Integer tagId) {
        List<TagEntity> tagEntityList = animationTagMapper.getByAnimationId(animationId);
        for (TagEntity entity : tagEntityList) {
            if (entity.getId().equals(tagId)) {
                return;
            }
        }
        animationTagMapper.add(animationId, tagId);
    }

    public void delete(Integer animationId, Integer tagId) {
        animationTagMapper.delete(animationId, tagId);
    }

    public List<TagVo> getByAnimationId(Integer animationId) {
        List<TagVo> result = new ArrayList<>();
        List<TagEntity> tagEntityList = animationTagMapper.getByAnimationId(animationId);
        for (TagEntity entity : tagEntityList) {
            result.add(new TagVo(entity.getId(), entity.getName()));
        }
        return result;
    }
}
