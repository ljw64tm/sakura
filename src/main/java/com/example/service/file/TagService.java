package com.example.service.file;

import com.example.dao.entity.TagEntity;
import com.example.dao.mapper.AnimationTagMapper;
import com.example.dao.mapper.TagMapper;
import com.example.model.file.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 标签管理相关
 */
@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private AnimationTagMapper animationTagMapper;

    /**
     * TagCache
     */
    //key:AnimationId  value:Tags
    private final Map<Integer, List<TagVo>> tagCacheA2T = new ConcurrentHashMap<>();
    //key:TagId  value:AnimationIds
    private final Map<Integer, List<Integer>> tagCacheT2A = new ConcurrentHashMap<>();

    /**
     * 获取全部并分组
     *
     * @return
     */
    public List<List<TagVo>> getAllTags() {
        List<List<TagVo>> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new ArrayList<>());
        }
        List<TagEntity> tagEntityList = tagMapper.getAll();
        for (TagEntity entity : tagEntityList) {
            List<TagVo> temp = result.get(entity.getType());
            temp.add(new TagVo(entity.getId(), entity.getName(), entity.getType()));
            result.set(entity.getType(), temp);
        }
        for (int i = 0, j = result.size(); i < j; i++) {
            if (result.get(i).isEmpty()) {
                result.remove(i);
                j--;
                i--;
            }
        }
        return result;
    }

    /**
     * 新增一个动画的标签
     *
     * @param animationId
     * @param tagId
     */
    public void add(Integer animationId, Integer tagId) {
        List<TagEntity> tagEntityList = animationTagMapper.getByAnimationId(animationId);
        for (TagEntity entity : tagEntityList) {
            if (entity.getId().equals(tagId)) {
                return;
            }
        }
        animationTagMapper.add(animationId, tagId);
        this.clearCache(tagId, animationId);
    }

    /**
     * 删除一个动画的标签
     *
     * @param animationId
     * @param tagId
     */
    public void delete(Integer animationId, Integer tagId) {
        animationTagMapper.delete(animationId, tagId);
        this.clearCache(tagId,animationId);
    }

    /**
     * 按动画id获取标签
     *
     * @param animationId
     * @return
     */
    public List<TagVo> getByAnimationId(Integer animationId) {
        List<TagVo> cache = tagCacheA2T.get(animationId);
        if (cache == null) {
            List<TagEntity> tagEntityList = animationTagMapper.getByAnimationId(animationId);
            if (!tagEntityList.isEmpty()) {
                cache = new ArrayList<>();
                for (TagEntity entity : tagEntityList) {
                    cache.add(new TagVo(entity.getId(), entity.getName(), entity.getType()));
                }
                tagCacheA2T.put(animationId, cache);
            }
        }
        return cache == null ? new ArrayList<>() : cache;
    }

    /**
     * 按标签Id获取动画ID
     *
     * @param tagId
     * @return
     */
    public List<Integer> getByTagId(Integer tagId) {
        List<Integer> cache = tagCacheT2A.get(tagId);
        if (CollectionUtils.isEmpty(cache)) {
            List<Integer> tagIdList = animationTagMapper.getByTagId(tagId);
            if (!tagIdList.isEmpty()) {
                cache = tagIdList;
                tagCacheT2A.put(tagId, cache);
            }
        }
        return cache == null ? new ArrayList<>() : cache;
    }

    /**
     * 按标签Id获取动画ID
     * 批量，聚合，
     *
     * @param tagIds
     * @return null不受限。空的list受限
     */
    public List<Integer> getByTagIds(List<Integer> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return null;
        }
        List<Integer> result = this.getByTagId(tagIds.get(0));
        for (int i = 1; i < tagIds.size(); i++) {
            result.retainAll(this.getByTagId(tagIds.get(i)));
        }
        return result;
    }

    /**
     * 清空缓存
     *
     * @param tagId
     * @param animationId
     */
    private void clearCache(Integer tagId, Integer animationId) {
        if (tagId != null) {
            tagCacheT2A.remove(tagId);
            for (Map.Entry<Integer, List<TagVo>> entry : tagCacheA2T.entrySet()) {
                if (entry.getValue() != null) {
                    for (TagVo tagVo : entry.getValue()) {
                        if (Objects.equals(tagVo.getId(), tagId)) {
                            tagCacheA2T.remove(entry.getKey(), null);
                            break;
                        }
                    }
                }
            }
        }
        if (animationId != null) {
            tagCacheA2T.remove(animationId);
            for (Map.Entry<Integer, List<Integer>> entry : tagCacheT2A.entrySet()) {
                if (entry.getValue() != null) {
                    for (Integer tempId : entry.getValue()) {
                        if (Objects.equals(tempId, tagId)) {
                            tagCacheT2A.remove(entry.getKey());
                            break;
                        }
                    }
                }
            }
        }

    }
}
