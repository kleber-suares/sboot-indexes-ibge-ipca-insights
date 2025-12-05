package com.kls.references.sboot.ibge.ipca.insights.infrastructure.mapper;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaInfoDataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class IpcaDataEntityMapper {

    private IpcaDataEntityMapper() {}

    public static List<IpcaHistoryDataEntity> mapHistoryData(List<IpcaData> ipcaDataList) {
        List<IpcaHistoryDataEntity> list = new ArrayList<>();

        ipcaDataList.forEach(ipcaData ->
            list.add(
                mapCommonFields(
                    ipcaData,
                    IpcaHistoryDataEntity::new,
                    (target, src) ->
                        target.setInflationRate(new BigDecimal(src.getInflationRate().trim())) //TODO: ver se necessario validar notacao cientifica
                )
            )
        );

        return list;
    }

    public static List<IpcaInfoDataEntity> mapInfoData(List<IpcaData> ipcaDataList) {
        List<IpcaInfoDataEntity> list = new ArrayList<>();

        ipcaDataList.forEach(ipcaData ->
            list.add(
                mapCommonFields(
                    ipcaData,
                    IpcaInfoDataEntity::new,
                    (target, src) ->
                    target.setInflationRate(src.getInflationRate())
                )
            )
        );

        return list;
    }

    private static <T extends IpcaDataEntity> T mapCommonFields(
        IpcaData src,
        Supplier<T> factory,
        BiConsumer<T, IpcaData> inflationSetter
    ) {
        T target = factory.get();

        target.setTerritorialLevelCode(src.getTerritorialLevelCode());
        target.setTerritorialLevelName(src.getTerritorialLevelName());
        target.setUnitOfMeasureCode(src.getUnitOfMeasureCode());
        target.setUnitOfMeasureLabel(src.getUnitOfMeasureLabel());
        target.setRegionCode(src.getRegionCode());
        target.setRegionName(src.getRegionName());
        target.setIndicatorCode(src.getIndicatorCode());
        target.setIndicatorName(src.getIndicatorName());
        target.setReferencePeriodCode(src.getReferencePeriodCode());
        target.setReferencePeriodLabel(src.getReferencePeriodLabel());

        inflationSetter.accept(target, src);

        return target;
    }

}
