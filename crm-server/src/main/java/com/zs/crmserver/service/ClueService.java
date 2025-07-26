package com.zs.crmserver.service;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.model.TClue;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.query.ClueQuery;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ClueService {
    PageInfo<TClue> getClueByPage(BaseQuery query, BasePageQuery pageQuery, List<Long> ownerIds);

    Boolean checkPhone(String phone);

    int saveClue(ClueQuery clueQuery);

    int updateClue(ClueQuery clueQuery);

    TClue getClueById(String id);

    Integer delClueById(Integer id);

    Integer batchDelClues(Integer[] ids);

    void importClue(InputStream inputStream, String token);
}
