package com.zs.crmserver.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.pagehelper.PageInfo;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TCustomer;
import com.zs.crmserver.query.BasePageQuery;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.result.CustomerExcel;
import com.zs.crmserver.result.R;
import com.zs.crmserver.service.CustomerService;
import com.zs.crmserver.util.PageResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @GetMapping("api/customer")
    public R getCustomers(
        BaseQuery query,
        BasePageQuery pageQuery,
        @RequestParam(value = "ownerIds", required = false) List<Long> ownerIds
    ) {
        PageInfo<TCustomer> list = customerService.getCustomersByPage(query, pageQuery, ownerIds);
        return PageResponseUtils.buildPageResponse(list);
    }

    @GetMapping(value = "/api/exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestParam(value = "ids", required = false) String ids) throws IOException {

        // 要想让浏览器弹出下载框，你后端要设置一下响应头信息
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(Constants.EXCEL_FILE_NAME+System.currentTimeMillis(), StandardCharsets.UTF_8) + ".xlsx");

        // 查询数据库的数据
        List<String> idList = StringUtils.hasText(ids) ? Arrays.asList(ids.split(",")) : new ArrayList<>();
        List<TCustomer> tCustomerList = customerService.getCustomerByExcel(idList);
        // 把数据写入Excel，然后把Excel以IO流的方式输出到前端浏览器
        List<CustomerExcel> customerExcelList = new ArrayList<>();
        tCustomerList.forEach(tCustomer -> {
            CustomerExcel customerExcel = new CustomerExcel();
            BeanUtils.copyProperties(tCustomer, customerExcel);
            customerExcelList.add(customerExcel);
        });

        // 字体设置
        WriteFont headFont = new WriteFont();
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBold(true);
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setWriteFont(headFont);

        EasyExcel.write(response.getOutputStream(), CustomerExcel.class)
            .registerWriteHandler(new HorizontalCellStyleStrategy(headStyle, new WriteCellStyle()))
            .sheet()
            .doWrite(customerExcelList);
    }
}
