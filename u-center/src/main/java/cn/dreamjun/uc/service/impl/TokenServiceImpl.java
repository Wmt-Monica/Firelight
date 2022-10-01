package cn.dreamjun.uc.service.impl;

import cn.dreamjun.uc.pojo.UcToken;
import cn.dreamjun.uc.mapper.UcTokenMapper;
import cn.dreamjun.uc.service.ITokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-09
 */
@Service
public class TokenServiceImpl extends ServiceImpl<UcTokenMapper, UcToken> implements ITokenService {

}
