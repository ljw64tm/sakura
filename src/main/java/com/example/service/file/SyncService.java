package com.example.service.file;

import com.example.common.enums.YesNoEnum;
import com.example.dao.entity.AnimationEntity;
import com.example.dao.mapper.AnimationMapper;
import com.example.model.file.FileResVo;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SyncService {

    @Autowired
    private FileService fileService;
    @Autowired
    private AnimationMapper animationMapper;

    /**
     * 正向同步，硬盘中的动画目录将会新增至数据库中（如果存在跳过）
     *
     * @return
     */
    public String disk2db() {
        List<FileResVo> totalDiskList = fileService.index("");
        for (FileResVo fileResVo : totalDiskList) {
            if (this.nameFilter(fileResVo.getName())) {
                String finalName = this.nameHandler(fileResVo.getName());
                AnimationEntity animationEntity = animationMapper.getByName(finalName);
                if (animationEntity == null) {
                    //没有，新增
                    String py = this.namePYHandler(this.toPinyin(finalName));
                    animationMapper.insert(finalName, py, fileResVo.getName(), new Date());
                    log.info("磁盘到数据库同步，已添加：" + fileResVo.getName());
                    continue;
                }
                if (animationEntity.getDelete().intValue() == YesNoEnum.YES.getValue()) {
                    //已删除，恢复为正常状态
                    animationMapper.updateStatusById(animationEntity.getId(), YesNoEnum.NO.getValue(), null);
                    log.info("磁盘到数据库同步，已恢复：" + fileResVo.getName());
                }
            } else {
                log.info(fileResVo.getName() + "已过滤，跳过流程，请参见过滤器");
            }
        }
        return null;
    }

    /**
     * 自定义名称过滤器
     *
     * @param name
     * @return 是否新增至库 true 新增 false 跳过
     */
    private boolean nameFilter(String name) {
        if (name.indexOf("[CD]") > 0) {
            //CD类型暂不加入片库
            return false;
        }
        return true;
    }

    /**
     * 自定义名称处理器
     *
     * @param name
     * @return 入库名称
     */
    private String nameHandler(String name) {
        int index = name.indexOf("[");
        if (index > 0) {
            name = name.substring(0, index);
        }
        return name;
    }

    /**
     * 自定义拼音名称处理器
     *
     * @param name
     * @return
     */
    private String namePYHandler(String name) {
        name = name.replaceAll("u:", "v");
        return name.toLowerCase();
    }

    /**
     * 反向同步，数据库中存在，硬盘已不存在，将会删除数据库中信息
     *
     * @return
     */
    public String db2disk() {
        List<AnimationEntity> animationList = animationMapper.getAll();
        List<FileResVo> totalDiskList = fileService.index("");
        Map<String, String> totalDiskMap = totalDiskList.stream().collect(Collectors.toMap(p -> this.nameHandler(p.getName()), p -> this.nameHandler(p.getName()), (p1, p2) -> p2));
        for (AnimationEntity animationEntity : animationList) {
            if (animationEntity.getDelete().intValue() == YesNoEnum.YES.getValue() || totalDiskMap.containsKey(animationEntity.getName())) {
                //已删除 跳过
                //理想情况，硬盘与数据库中均存在 跳过
                continue;
            }
            //更新状态为删除
            animationMapper.updateStatusById(animationEntity.getId(), YesNoEnum.YES.getValue(), new Date());
            log.info("数据库到磁盘同步，已删除：" + animationEntity.getName());
        }
        return null;
    }

    private String toPinyin(String chinese) {
        StringBuilder pinyinBuilder = new StringBuilder();
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    String[] pinyinResult = PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat);
                    String temp = "";
                    if (pinyinResult != null && pinyinResult.length > 0) {
                        temp = pinyinResult[0];
                    }
                    pinyinBuilder.append(temp);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    log.error("拼音转化错误", badHanyuPinyinOutputFormatCombination);
                }
            } else {
                pinyinBuilder.append(newChar[i]);
            }
        }
        return pinyinBuilder.toString();
    }
}
