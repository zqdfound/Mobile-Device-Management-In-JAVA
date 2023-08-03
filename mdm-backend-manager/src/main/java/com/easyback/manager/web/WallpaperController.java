package com.easyback.manager.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.infra.context.AdminContext;
import com.easyback.infra.entity.Wallpaper;
import com.easyback.infra.service.WallpaperService;
import com.easyback.model.dto.WallpaperDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/4
 */
@RestController
@Api(tags = "壁纸管理")
@Slf4j
@RequestMapping("/wallpaper")
@SaCheckLogin
public class WallpaperController {

    @Resource
    private WallpaperService wallpaperService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public Result<List<Wallpaper>> list() {
        Integer adminId = AdminContext.getUserId();
        List<Wallpaper> list = wallpaperService.list(new LambdaQueryWrapper<Wallpaper>()
                .eq(Wallpaper::getAdminId, adminId)
                .orderByDesc(Wallpaper::getCreateTime));
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result add( @RequestBody WallpaperDTO dto) {
        Integer adminId = AdminContext.getUserId();
        Wallpaper wallpaper = BeanUtil.copyProperties(dto,Wallpaper.class);
        wallpaper.setAdminId(adminId);
        wallpaperService.save(wallpaper);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "编辑")
    @PostMapping("/update/{id}")
    public Result update(@PathVariable Integer id, @RequestBody WallpaperDTO dto) {
        Wallpaper wallpaper = wallpaperService.getById(id);
        if (wallpaper == null) {
            return ResultGenerator.genFailResult("壁纸不存在");
        }
        wallpaper.setName(dto.getName());
        wallpaper.setBase64(dto.getBase64());
        wallpaperService.updateById(wallpaper);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        wallpaperService.removeById(id);
        return ResultGenerator.genSuccessResult();
    }

}
