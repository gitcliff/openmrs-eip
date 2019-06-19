package org.openmrs.sync.core.camel.extract;

import org.apache.camel.Exchange;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Sql("classpath:org/openmrs/sync/core/camel/extract/init_person_extract.sql")
public class OpenMrsExtractPersonITest extends OpenMrsExtractEndpointITest {

    private LocalDateTime date = null;

    @Test
    public void extract() throws JSONException {
        // Given
        CamelInitObect camelInitObect = CamelInitObect.builder()
                .entityName("person")
                .lastSyncDate(date)
                .build();

        // When
        template.sendBody(camelInitObect);

        // Then
        List<Exchange> result = resultEndpoint.getExchanges();
        assertEquals(1, result.size());
        List<String> jsons = (List<String>) result.get(0).getIn().getBody();
        assertEquals(1, jsons.size());
        JSONAssert.assertEquals(getExpectedJson(), jsons.get(0), false);
    }

    private String getExpectedJson() {
        return "{" +
                "\"uuid\":\"dd279794-76e9-11e9-8cd9-0242ac1c000b\"," +
                "\"creatorUuid\":null," +
                "\"dateCreated\":[2005,1,1,0,0]," +
                "\"changedByUuid\":null," +
                "\"dateChanged\":null," +
                "\"voided\":false," +
                "\"voidedByUuid\":null," +
                "\"dateVoided\":null," +
                "\"voidReason\":null," +
                "\"gender\":\"M\"," +
                "\"birthdate\":null," +
                "\"birthdateEstimated\":false," +
                "\"dead\":false," +
                "\"deathDate\":null," +
                "\"causeOfDeathUuid\":null," +
                "\"causeOfDeathClassUuid\":null," +
                "\"causeOfDeathDatatypeUuid\":null," +
                "\"deathdateEstimated\":false," +
                "\"birthtime\":null" +
                "}";
    }
}