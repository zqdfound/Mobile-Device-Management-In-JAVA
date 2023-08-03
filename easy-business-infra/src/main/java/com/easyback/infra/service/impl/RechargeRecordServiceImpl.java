package com.easyback.infra.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyback.framework.exceptions.BizException;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.entity.Admin;
import com.easyback.infra.entity.RechargeRecord;
import com.easyback.infra.mapper.AdminMapper;
import com.easyback.infra.mapper.RechargeRecordMapper;
import com.easyback.infra.service.RechargeRecordService;
import com.easyback.model.dto.RechargeAddDTO;
import com.easyback.model.dto.RechargeQryDTO;
import com.easyback.model.vo.RechargeRecordExportVO;
import com.easyback.model.vo.RechargeRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 充值记录 服务实现类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-04
 */
@Service
@Slf4j
public class RechargeRecordServiceImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecord> implements RechargeRecordService {
    @Resource
    private AdminMapper adminMapper;

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageVO<RechargeRecordVO> getPage(RechargeQryDTO dto) {
        LambdaQueryWrapper<RechargeRecord> qw = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(dto.getName())){
            List<Integer> adminIdList = adminMapper.selectList(new LambdaQueryWrapper<Admin>().like(Admin::getName,dto.getName()))
                    .stream().map(Admin::getId).collect(Collectors.toList());
            if(adminIdList.size() > 0){
                qw.in(RechargeRecord::getAdminId,adminIdList);
            }else{
                qw.in(RechargeRecord::getAdminId, Arrays.asList(-1));
            }
        }
        qw.ge(dto.getRechargeNumMin() != null,RechargeRecord::getRechargeNum,dto.getRechargeNumMin());
        qw.le(dto.getRechargeNumMax() != null,RechargeRecord::getRechargeNum,dto.getRechargeNumMax());
        qw.eq(dto.getPayType() != null,RechargeRecord::getPayType,dto.getPayType());
        qw.ge(dto.getCreateTimeBegin() != null,RechargeRecord::getCreateTime,dto.getCreateTimeBegin());
        qw.le(dto.getCreateTimeEnd() != null,RechargeRecord::getCreateTime,dto.getCreateTimeEnd());
        qw.orderByDesc(RechargeRecord::getCreateTime);
        Page<RechargeRecord> page = page(Page.of(dto.getPageNum(), dto.getPageSize()), qw);
        PageVO<RechargeRecordVO> pageVO = PageVO.getPageData(page,l->{
            RechargeRecordVO vo = BeanUtil.copyProperties(l,RechargeRecordVO.class);
            vo.setName(adminMapper.selectById(l.getAdminId()).getName());
            return vo;
        });
        return pageVO;
    }


    @Override
    public List<RechargeRecordExportVO> getExportList(RechargeQryDTO dto) {
        LambdaQueryWrapper<RechargeRecord> qw = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(dto.getName())){
            List<Integer> adminIdList = adminMapper.selectList(new LambdaQueryWrapper<Admin>().like(Admin::getName,dto.getName()))
                    .stream().map(Admin::getId).collect(Collectors.toList());
            if(adminIdList.size() > 0){
                qw.in(RechargeRecord::getAdminId,adminIdList);
            }else{
                qw.in(RechargeRecord::getAdminId, Arrays.asList(-1));
            }
        }
        qw.ge(dto.getRechargeNumMin() != null,RechargeRecord::getRechargeNum,dto.getRechargeNumMin());
        qw.le(dto.getRechargeNumMax() != null,RechargeRecord::getRechargeNum,dto.getRechargeNumMax());
        qw.eq(dto.getPayType() != null,RechargeRecord::getPayType,dto.getPayType());
        qw.ge(dto.getCreateTimeBegin() != null,RechargeRecord::getCreateTime,dto.getCreateTimeBegin());
        qw.le(dto.getCreateTimeEnd() != null,RechargeRecord::getCreateTime,dto.getCreateTimeEnd());
        qw.orderByDesc(RechargeRecord::getCreateTime);
        List<RechargeRecord> list =list(qw);
        List<RechargeRecordExportVO> voList = new ArrayList<>();
       list.forEach(l->{
           RechargeRecordExportVO vo = RechargeRecordExportVO.builder()
                   .name(adminMapper.selectById(l.getAdminId()).getName())
                   .rechargeNum(l.getRechargeNum())
                   .rechargeMoney(new BigDecimal(String.valueOf(l.getRechargeMoney())).divide(new BigDecimal(100)).toString())
                   .payType(l.getPayType() == 0 ? "现金":"")
                   .remark(l.getRemark())
                   .createTime(df.format(l.getCreateTime()))
                   .build();

           voList.add(vo);
        });
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeAdd(RechargeAddDTO dto) {
        Integer operatorId = AdminContext.getUserId();
        // 新增充值记录
        RechargeRecord rechargeRecord = BeanUtil.copyProperties(dto,RechargeRecord.class);
        rechargeRecord.setOperatorId(operatorId);
        if(save(rechargeRecord)){
            Integer rechargeNum = dto.getRechargeNum();
            log.info("用户{}充值{}",rechargeRecord.getAdminId(),rechargeNum);
            // 更改用户信息
            Admin admin = adminMapper.selectById(rechargeRecord.getAdminId());
            admin.setUsableNum(admin.getUsableNum() + rechargeNum);
            admin.setTotalNum(admin.getTotalNum() + rechargeNum);
            if(adminMapper.updateById(admin) > 0){
                return;
            }else {
                throw new BizException("更新充值记录失败");
            }
        }else{
            throw new BizException("增加充值记录失败");
        }
    }
}
