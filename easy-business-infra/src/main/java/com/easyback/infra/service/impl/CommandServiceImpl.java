package com.easyback.infra.service.impl;

import com.easyback.infra.entity.Command;
import com.easyback.infra.mapper.CommandMapper;
import com.easyback.infra.service.CommandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 命令执行信息 服务实现类
 * </p>
 *
 * @author zhuangqingdian
 * @since 2023-07-03
 */
@Service
public class CommandServiceImpl extends ServiceImpl<CommandMapper, Command> implements CommandService {

}
