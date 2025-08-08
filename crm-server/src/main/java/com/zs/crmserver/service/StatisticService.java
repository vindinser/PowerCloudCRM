package com.zs.crmserver.service;

import com.zs.crmserver.result.NameValue;
import com.zs.crmserver.result.SummaryData;

import java.util.List;

public interface StatisticService {

    SummaryData loadSummaryData();

    List<NameValue> loadSaleFunnelData();

    List<NameValue> loadSourcePieData();
}
