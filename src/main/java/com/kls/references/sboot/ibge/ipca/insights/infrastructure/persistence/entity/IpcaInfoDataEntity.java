package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity;

import com.kls.references.sboot.ibge.ipca.insights.infrastructure.constants.DocumentCollections;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = DocumentCollections.IPCA_INFO_DATA_COLLECTION)
@TypeAlias("IpcaInfoData")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IpcaInfoDataEntity extends IpcaDataEntity {

    @Id
    private String id;
    private String inflationRate;

    public IpcaInfoDataEntity() {
        super();
    }
}
