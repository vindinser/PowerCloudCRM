package com.zs.crmserver.web;

import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TClue;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.query.ClueQuery;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.ClueService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ClueController {

    @Resource
    public ClueService clueService;

    @GetMapping("/api/clues")
    public R cluePage(
        BaseQuery query,
        BasePageQuery pageQuery,
        @RequestParam(value = "ownerIds", required = false) List<Long> ownerIds
    ) {
        PageInfo<TClue> clueList = clueService.getClueByPage(query, pageQuery, ownerIds);
        return PageResponseUtils.buildPageResponse(clueList);
    }

    @GetMapping("/api/clues/{phone}")
    public R checkPhone(@PathVariable(value = "phone") String phone) {
        Boolean check = clueService.checkPhone(phone);
        return check ? R.OK() : R.FAIL();
    }

    @PostMapping("/api/clues")
    public R addClue(@RequestBody ClueQuery clueQuery, @RequestHeader(value = Constants.TOKEN_NAME)  String token) {
        Boolean b = clueService.checkPhone(clueQuery.getPhone());
        if(b) {
            clueQuery.setToken(token);
            int save = clueService.saveClue(clueQuery);
            return save > 0 ? R.OK() : R.FAIL();
        } else {
            return R.FAIL("该手机号已经录入过了不能再录入");
        }
    }

    @PutMapping("/api/clues")
    public R updateClue(@RequestBody ClueQuery clueQuery, @RequestHeader(value = Constants.TOKEN_NAME)  String token) {
        clueQuery.setToken(token);
        int update = clueService.updateClue(clueQuery);
        return update > 0 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/clues/detail/{id}")
    public R getClueDetail(@PathVariable String id) {
        TClue tClue = clueService.getClueById(id);
        return R.OK(tClue);
    }

    @DeleteMapping("/api/clues/{id}")
    public R delClue(@PathVariable Integer id) {
        Integer del = clueService.delClueById(id);
        return del > 0 ? R.OK() : R.FAIL();
    }

    @DeleteMapping("/api/clues")
    public R batchDelClues(@RequestParam(value = "ids") Integer[] ids) {
        Integer batchDel = clueService.batchDelClues(ids);
        return batchDel >= 1 ?  R.OK() : R.FAIL();
    }

    @PostMapping("api/clues/importExcel")
    public R importClue(MultipartFile file, @RequestHeader(value = Constants.TOKEN_NAME)  String token) throws IOException {
        clueService.importClue(file.getInputStream(), token);
        return R.OK();
    }
}
