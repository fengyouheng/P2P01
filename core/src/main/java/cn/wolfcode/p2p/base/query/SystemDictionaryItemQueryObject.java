package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2018/1/23.
 */
@Setter@Getter
public class SystemDictionaryItemQueryObject extends QueryObject {
    private String keyword;
    private Long parentId;
}
