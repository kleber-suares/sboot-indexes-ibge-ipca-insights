package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.constants.DocumentCollections;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = DocumentCollections.IPCA_HISTORY_DATA_COLLECTION)
@TypeAlias("IpcaHistoryData")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IpcaHistoryDataEntity extends IpcaDataEntity {

    @Id
    private String id;
    private BigDecimal inflationRate;

    public IpcaHistoryDataEntity() {
        super();
    }

}
