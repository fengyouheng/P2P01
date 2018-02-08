package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by seemygo on 2018/1/21.
 */
@Setter@Getter
public class MailVerify extends BaseDomain {
    private String email;
    private Long userId;
    private Date sendTime;
    private String uuid;
}
