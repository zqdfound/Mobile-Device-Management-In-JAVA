package com.easyback.manager.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.base.PageVO;
import com.easyback.infra.service.RechargeRecordService;
import com.easyback.model.dto.RechargeAddDTO;
import com.easyback.model.dto.RechargeQryDTO;
import com.easyback.model.vo.RechargeRecordExportVO;
import com.easyback.model.vo.RechargeRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/4
 */
@RestController
@Api(tags = "服务管理")
@Slf4j
@RequestMapping("/recharge")
@SaCheckLogin
public class RechargeController {

    @Resource
    private RechargeRecordService rechargeRecordService;

    @ApiOperation(value = "分页列表")
    @PostMapping("/page")
    public Result<PageVO<RechargeRecordVO>> page(@RequestBody RechargeQryDTO dto) {
        return ResultGenerator.genSuccessResult(rechargeRecordService.getPage(dto));
    }

    @ApiOperation(value = "充值")
    @PostMapping("/add")
    public Result page(@RequestBody RechargeAddDTO dto) {
        rechargeRecordService.rechargeAdd(dto);
        return ResultGenerator.genSuccessResult();
    }


    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(@RequestBody RechargeQryDTO dto, HttpServletResponse response)  throws IOException {

        List<RechargeRecordExportVO> exportList = rechargeRecordService.getExportList(dto);

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "公司名称");
        writer.addHeaderAlias("rechargeNum", "充值台数");
        writer.addHeaderAlias("rechargeMoney", "支付金额");
        writer.addHeaderAlias("payType", "支付方式");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("createTime", "创建时间");

        writer.write(exportList, true);

        writer.autoSizeColumnAll();

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        String fileName = URLEncoder.encode("流水", "UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xls");

//        String fileName = "流水.xls";
//        fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);

    }



}
