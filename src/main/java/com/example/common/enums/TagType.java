package com.example.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TagType implements WebSupport {

    PLOT(1, "剧情分类"),
    //搞笑，运动，热血，战斗，竞技，校园，狗粮，后宫，治愈，致郁，魔法，魔术，悬疑，推理，科幻，穿越
    //游戏，恐怖，血腥，机战，战争，犯罪，历史，社会，职场，低幼向，科普，真人，偶像，肉番
    ATTR(2, "人物属性"),
    //魔王，勇者，吸血鬼，伪娘，IDOL，天使，恶魔，教师，娘化，AI，召唤
    ROLE(3, "角色特性"),
    //傲娇，萝莉，百合，御姐，青梅竹马，
    TIME(4, "放映类型"),
    //剧场版，OVA，泡面番，
    SOURCE(5, "来源"),
    //轻改，漫改，GAL改，原创，真人电影改，重制版
    ;

    private Integer value;
    private String description;

    @Override
    public Map<Integer, String> getALl() {
        Map<Integer, String> result = new HashMap<>();
        for (TagType tag : TagType.values()) {
            result.put(tag.getValue(), tag.getDescription());
        }
        return result;
    }
}
