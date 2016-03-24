package com.github.mtakaki.credentialstorage.client.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;

public class CredentialTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper().setPropertyNamingStrategy(
            PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    @Test
    public void deserializesFromJSON() throws Exception {
        final Credential credential = Credential.builder()
                .symmetricKey("sym")
                .primary("user")
                .secondary("password").build();
        assertThat(MAPPER.readValue(FixtureHelpers.fixture("fixtures/credential.json"),
                Credential.class)).isEqualTo(credential);
    }

    @Test
    public void serializesToJSON() throws Exception {
        final Credential credential = Credential.builder()
                .symmetricKey("sym")
                .primary("user")
                .secondary("password").build();
        assertThat(MAPPER.writeValueAsString(credential))
                .isEqualTo(FixtureHelpers.fixture("fixtures/credential.json"));
    }
}