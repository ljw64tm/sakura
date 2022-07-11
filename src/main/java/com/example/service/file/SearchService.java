package com.example.service.file;

import com.example.dao.entity.AnimationEntity;
import com.example.dao.mapper.AnimationMapper;
import com.example.model.file.FileResVo;
import com.example.model.file.SearchDto;
import com.example.model.file.SearchForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private AnimationMapper animationMapper;
    @Autowired
    private TagService tagService;

    /**
     * 强力搜索
     *
     * @param searchForm
     * @return
     */
    public List<FileResVo> search(SearchForm searchForm) {
        searchForm.setKeyword("%" + searchForm.getKeyword() + "%");
        //标签过滤
        List<Integer> animationIds = tagService.getByTagIds(searchForm.getTagIds());
        if (animationIds != null && animationIds.isEmpty()) {
            return new ArrayList<>();
        }
        SearchDto searchDto = new SearchDto();
        BeanUtils.copyProperties(searchForm, searchDto);
        searchDto.setAnimationIds(animationIds);
        List<FileResVo> result = this.entity2Vo(animationMapper.searchByName(searchDto));
        for (FileResVo fileResVo : result) {
            fileResVo.setTags(tagService.getByAnimationId(fileResVo.getId()));
        }
        return result;
    }

    /**
     * 实体转Vo
     *
     * @param entityList
     * @return
     */
    private List<FileResVo> entity2Vo(List<AnimationEntity> entityList) {
        List<FileResVo> result = new ArrayList<>();
        for (AnimationEntity entity : entityList) {
            result.add(new FileResVo(entity.getId(), entity.getName(), entity.getFullName(), false));
        }
        return result;
    }
}
