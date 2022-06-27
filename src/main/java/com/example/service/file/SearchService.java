package com.example.service.file;

import com.example.dao.entity.AnimationEntity;
import com.example.dao.mapper.AnimationMapper;
import com.example.model.file.FileResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private AnimationMapper animationMapper;

    /**
     * 强力搜索
     *
     * @param keyword
     * @return
     */
    public List<FileResVo> search(String keyword) {
        keyword = "%" + keyword + "%";
        List<FileResVo> result = new ArrayList<>();
        result.addAll(this.entity2Vo(animationMapper.searchByName(keyword)));
        result.addAll(this.entity2Vo(animationMapper.searchByNamePinYin(keyword)));
        Map<Integer, FileResVo> resultMap = result.stream().collect(Collectors.toMap(FileResVo::getId, p -> p, (p1, p2) -> p2));
        return new ArrayList<>(resultMap.values());
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
