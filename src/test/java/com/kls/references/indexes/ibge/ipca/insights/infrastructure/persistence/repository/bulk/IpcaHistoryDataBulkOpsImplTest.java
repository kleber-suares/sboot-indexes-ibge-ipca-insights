package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.bulk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

class IpcaHistoryDataBulkOpsImplTest {

    private MongoTemplate mongoTemplateMock;

    private IpcaDataBulkOpsImpl bulkOps;

    @BeforeEach
    void setUp() {
        mongoTemplateMock = Mockito.mock(MongoTemplate.class);
        bulkOps = new IpcaDataBulkOpsImpl(mongoTemplateMock);
    }

    @Test
    void shouldReplaceAllDocumentsSuccessfully() {

        BulkOperations bulkOperationsMock = mock(BulkOperations.class);

        when(mongoTemplateMock.bulkOps(BulkOperations.BulkMode.ORDERED, Person.class)).thenReturn(bulkOperationsMock);
        when(bulkOperationsMock.remove(any(Query.class))).thenReturn(bulkOperationsMock);
        when(bulkOperationsMock.insert(anyList())).thenReturn(bulkOperationsMock);

        ArgumentCaptor<List> insertCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Query> removeQueryCaptor = ArgumentCaptor.forClass(Query.class);

        var personsToInsertList = getPersonsList();

        bulkOps.replaceAll(personsToInsertList, Person.class);

        verify(mongoTemplateMock).bulkOps(BulkOperations.BulkMode.ORDERED, Person.class);

        verify(bulkOperationsMock, times(1)).remove(any(Query.class));
        verify(bulkOperationsMock).remove(removeQueryCaptor.capture());
        assertThat(removeQueryCaptor.getValue()).isNotNull();

        verify(bulkOperationsMock).insert(insertCaptor.capture());
        assertThat(personsToInsertList).hasSameSizeAs(insertCaptor.getValue());
        assertThat(insertCaptor.getValue()).containsAll(personsToInsertList);

        verify(bulkOperationsMock, times(1)).execute();

    }


    @Data
    private static class Person {
        private final String id;
        private final String name;
    }

    private List<Person> getPersonsList() {
        return List.of(
            new Person("1", "Person1"),
            new Person("2", "Person2")
        );
    }

}


