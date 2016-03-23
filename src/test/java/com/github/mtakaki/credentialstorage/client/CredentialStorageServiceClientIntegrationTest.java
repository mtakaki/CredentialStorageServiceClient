package com.github.mtakaki.credentialstorage.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.mtakaki.credentialstorage.CredentialStorageApplication;
import com.github.mtakaki.credentialstorage.CredentialStorageConfiguration;
import com.github.mtakaki.credentialstorage.client.model.Credential;

import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;

public class CredentialStorageServiceClientIntegrationTest {
    private static final Credential CREDENTIAL = Credential.builder()
            .primary("my user")
            .secondary(
                    "a very long password that is actually not that long but we wanna test it anyway")
            .build();

    @Rule
    public final DropwizardAppRule<CredentialStorageConfiguration> RULE = new DropwizardAppRule<CredentialStorageConfiguration>(
            CredentialStorageApplication.class,
            ResourceHelpers.resourceFilePath("config.yml"));
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CredentialStorageServiceClient client;

    @Before
    public void setup() throws Exception {
        final JerseyClientConfiguration configuration = new JerseyClientConfiguration();
        configuration.setTimeout(Duration.minutes(1L));
        configuration.setConnectionTimeout(Duration.minutes(1L));

        this.client = new CredentialStorageServiceClient(
                new File("./src/test/resources/private_key.der"),
                new File("./src/test/resources/public_key.der"),
                String.format("http://localhost:%d/", this.RULE.getLocalPort()),
                configuration);
    }

    @Test
    public void testUploadNewCredential() throws Exception {
        final boolean credentialCreated = this.client.uploadNewCredential(CREDENTIAL);

        assertThat(credentialCreated).isTrue();
    }

    @Test
    public void testUploadNewCredentialOverwriteExistingCredential() throws Exception {
        this.testUploadNewCredential();

        final Credential credential = this.client.getCredential();
        assertThat(credential).isEqualToIgnoringGivenFields(CREDENTIAL, "symmetricKey");
        assertThat(credential.getSymmetricKey()).hasSize(684);

        final Credential newCredential = Credential.builder().primary("single primary").build();
        final boolean credentialCreated = this.client.uploadNewCredential(newCredential);
        assertThat(credentialCreated).isTrue();

        final Credential updatedCredential = this.client.getCredential();
        assertThat(updatedCredential).isEqualToIgnoringGivenFields(newCredential, "symmetricKey");
        assertThat(updatedCredential.getSymmetricKey()).isNotEqualTo(credential.getSymmetricKey());
        assertThat(updatedCredential.getSymmetricKey()).hasSize(684);
    }

    @Test
    public void testGetCredentialNotFound() throws Exception {
        this.expectedException.expect(NotFoundException.class);
        this.client.getCredential();
    }

    @Test
    public void testGetCredential() throws Exception {
        this.testUploadNewCredential();

        final Credential credential = this.client.getCredential();
        assertThat(credential).isEqualToIgnoringGivenFields(CREDENTIAL, "symmetricKey");
        assertThat(credential.getSymmetricKey()).hasSize(684);
    }

    @Test
    public void testUpdateCredential() throws Exception {
        this.testUploadNewCredential();

        final Credential newCredential = Credential.builder().primary("not the same").build();

        final boolean credentialCreated = this.client.updateCredential(newCredential);
        assertThat(credentialCreated).isTrue();

        final Credential credential = this.client.getCredential();
        assertThat(credential).isEqualToIgnoringGivenFields(newCredential, "symmetricKey");
        assertThat(credential.getSymmetricKey()).hasSize(684);
    }

    @Test
    public void testUpdateCredentialNotFound() throws Exception {
        final Credential newCredential = Credential.builder().primary("not the same").build();

        final boolean credentialCreated = this.client.updateCredential(newCredential);
        assertThat(credentialCreated).isFalse();
    }

    @Test
    public void testDeleteCredential() throws Exception {
        this.testUploadNewCredential();

        final boolean credentialCreated = this.client.deleteCredential();
        assertThat(credentialCreated).isTrue();

        this.expectedException.expect(NotFoundException.class);
        this.client.getCredential();
    }

    @Test
    public void testDeleteCredentialNotFound() throws Exception {
        final boolean credentialCreated = this.client.deleteCredential();
        assertThat(credentialCreated).isFalse();
    }
}