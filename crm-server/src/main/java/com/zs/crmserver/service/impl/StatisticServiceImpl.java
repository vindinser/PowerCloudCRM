package com.zs.crmserver.service.impl;

import com.zs.crmserver.manager.StatisticManager;
import com.zs.crmserver.result.NameValue;
import com.zs.crmserver.result.SummaryData;
import com.zs.crmserver.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Resource
    private StatisticManager statisticManager;

    @Override
    public SummaryData loadSummaryData() {
        return statisticManager.loadSummaryData();
    }

    @Override
    public List<NameValue> loadSaleFunnelData() {
        return statisticManager.loadSaleFunnelData();
    }

    @Override
    public List<NameValue> loadSourcePieData() {
        return statisticManager.loadSourcePieData();
    }
}
