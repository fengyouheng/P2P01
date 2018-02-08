package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2018/1/20.
 */
@Setter@Getter
public class QueryObject {
    private Integer currentPage = 1;
    private Integer pageSize = 10;
}
