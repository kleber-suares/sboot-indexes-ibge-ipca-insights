package com.kls.references.sboot.ibge.ipca.insights.infrastructure.mapper;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaHistoryValue;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaInfoValue;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaInfoDataEntity;

import java.util.ArrayList;
import java.util.List;

public class IpcaDataEntityMapper {

    private IpcaDataEntityMapper() {}

    public static List<IpcaHistoryDataEntity> toHistoryDataEntity(List<IpcaHistoryValue> domainList) {
        List<IpcaHistoryDataEntity> entityList = new ArrayList<>();

        for (IpcaHistoryValue domain : domainList) {
            IpcaHistoryDataEntity entity = new IpcaHistoryDataEntity();

            entity.setInflationRate(domain.getInflationRate());
            entity.setTerritorialLevelCode(domain.getTerritorialLevelCode());
            entity.setTerritorialLevelName(domain.getTerritorialLevelName());
            entity.setUnitOfMeasureCode(domain.getUnitOfMeasureCode());
            entity.setUnitOfMeasureLabel(domain.getUnitOfMeasureLabel());
            entity.setRegionCode(domain.getRegionCode());
            entity.setRegionName(domain.getRegionName());
            entity.setIndicatorCode(domain.getIndicatorCode());
            entity.setIndicatorName(domain.getIndicatorName());
            entity.setReferencePeriodCode(domain.getReferencePeriodCode());
            entity.setReferencePeriodLabel(domain.getReferencePeriodLabel());

            entityList.add(entity);
        }

        return entityList;
    }

    public static List<IpcaInfoDataEntity> toInfoDataEntity(List<IpcaInfoValue> domainList) {
        List<IpcaInfoDataEntity> entityList = new ArrayList<>();

        for (IpcaInfoValue domain : domainList) {
            IpcaInfoDataEntity entity = new IpcaInfoDataEntity();

            entity.setInflationRate(domain.getInflationRate());
            entity.setTerritorialLevelCode(domain.getTerritorialLevelCode());
            entity.setTerritorialLevelName(domain.getTerritorialLevelName());
            entity.setUnitOfMeasureCode(domain.getUnitOfMeasureCode());
            entity.setUnitOfMeasureLabel(domain.getUnitOfMeasureLabel());
            entity.setRegionCode(domain.getRegionCode());
            entity.setRegionName(domain.getRegionName());
            entity.setIndicatorCode(domain.getIndicatorCode());
            entity.setIndicatorName(domain.getIndicatorName());
            entity.setReferencePeriodCode(domain.getReferencePeriodCode());
            entity.setReferencePeriodLabel(domain.getReferencePeriodLabel());

            entityList.add(entity);
        }

        return entityList;
    }

}
