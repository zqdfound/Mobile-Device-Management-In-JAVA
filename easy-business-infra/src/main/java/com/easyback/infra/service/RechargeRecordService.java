package com.easyback.infra.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.entity.RechargeRecord;
import com.easyback.model.dto.RechargeAddDTO;
import com.easyback.model.dto.RechargeQryDTO;
import com.easyback.model.vo.RechargeRecordExportVO;
import com.easyback.model.vo.RechargeRecordVO;

import java.util.List;

/**
 * <p>
 * 充值记录 服务类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
public interface RechargeRecordService extends IService<RechargeRecord> {

    PageVO<RechargeRecordVO> getPage(RechargeQryDTO dto);

    List<RechargeRecordExportVO> getExportList(RechargeQryDTO dto);

    void rechargeAdd(RechargeAddDTO dto);
}
