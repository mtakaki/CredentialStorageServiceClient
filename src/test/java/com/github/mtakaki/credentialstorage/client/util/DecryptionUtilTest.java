package com.github.mtakaki.credentialstorage.client.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.Test;

public class DecryptionUtilTest {
    private DecryptionUtil encryptionUtil;

    @Before
    public void setup() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
            NoSuchProviderException, InvalidKeySpecException {
        this.encryptionUtil = new DecryptionUtil(new File("./src/test/resources/private_key.der"));
    }

    @Test
    public void testDecryptSecretKey() throws Exception {
        final SecretKey symmetricKey = this.encryptionUtil.decryptSecretKey(
                "rKPE9tDeyL4auxAE6/OBDUbbSu1BzRgjpU3aaCXIXZJmIKL1G9RnJkXNqjP86gOOl7XVQygzhWpzvS95003AQXYR9KN0NuYH2h7EU3xAYCI7tzdXL4qPzgPKkFOcoPZVeDibZTkhB3sDYWGnZcPag3kp34ba9XhDBEB1LNp9SNJLqp7clt39hubk8BElhj4Sq5qsCOgL/Vkvq5tAlAHN6TwsDwjsGWywNspEUSb0AO/PmzzoUMEwt5ObUJOaKRnMAmKhtFJQ7HGC77q6qlZ1dzEBid+nsxvzENC8m986YdvU1knxW+iAJ9btj2ZCbWVgoM33Fm6W5A0yjKrdTmbjt2bX45w1ZZeb/dSVWsDNKGFBCC+3Dgi1GpFJPPK91W/Gwqgy6qxgQjMQDA9cqQUp/1o+rLGXdSXdbJMHz1EkoPvVqVirtUQMT1HpaOIb0fNTa7qMQwrUT6RhpAeLJggxS1Unr2M1Q/w9L2SJAtcUeZdh7lIoNLjgYMhKQNBIf2YnUrQ0wsvZWHamWov6AO4tkbkIy29TrSXCpK73wW1AsaMOeXI4jRbhd6tQGoJnsVEy4nx7W/fcaswmMY0mZxwMuFBqBdU0wa2zDCgvBXXkp7/ZrKxoLFYvtD4elh8i7d/aN0CLN/GpBpDJKFUtkLm9XXkFGxb1TCQKXzE8pSwVIsE=");
        assertThat(symmetricKey).isNotNull();
    }

    @Test
    public void testDecryptSecretKeyWithEmptyKey() throws Exception {
        final SecretKey symmetricKey = this.encryptionUtil.decryptSecretKey(
                "");
        assertThat(symmetricKey).isNull();
    }

    @Test
    public void testDecryptText() throws Exception {
        final SecretKey symmetricKey = this.encryptionUtil.decryptSecretKey(
                "rKPE9tDeyL4auxAE6/OBDUbbSu1BzRgjpU3aaCXIXZJmIKL1G9RnJkXNqjP86gOOl7XVQygzhWpzvS95003AQXYR9KN0NuYH2h7EU3xAYCI7tzdXL4qPzgPKkFOcoPZVeDibZTkhB3sDYWGnZcPag3kp34ba9XhDBEB1LNp9SNJLqp7clt39hubk8BElhj4Sq5qsCOgL/Vkvq5tAlAHN6TwsDwjsGWywNspEUSb0AO/PmzzoUMEwt5ObUJOaKRnMAmKhtFJQ7HGC77q6qlZ1dzEBid+nsxvzENC8m986YdvU1knxW+iAJ9btj2ZCbWVgoM33Fm6W5A0yjKrdTmbjt2bX45w1ZZeb/dSVWsDNKGFBCC+3Dgi1GpFJPPK91W/Gwqgy6qxgQjMQDA9cqQUp/1o+rLGXdSXdbJMHz1EkoPvVqVirtUQMT1HpaOIb0fNTa7qMQwrUT6RhpAeLJggxS1Unr2M1Q/w9L2SJAtcUeZdh7lIoNLjgYMhKQNBIf2YnUrQ0wsvZWHamWov6AO4tkbkIy29TrSXCpK73wW1AsaMOeXI4jRbhd6tQGoJnsVEy4nx7W/fcaswmMY0mZxwMuFBqBdU0wa2zDCgvBXXkp7/ZrKxoLFYvtD4elh8i7d/aN0CLN/GpBpDJKFUtkLm9XXkFGxb1TCQKXzE8pSwVIsE=");
        assertThat(symmetricKey).isNotNull();
        assertThat(this.encryptionUtil.decryptText(symmetricKey, "s+KpNGC/0McSdf4W2YxBuw=="))
                .isEqualTo("123");
    }

    @Test
    public void testDecryptTextWithEmptyText() throws Exception {
        final SecretKey symmetricKey = this.encryptionUtil.decryptSecretKey(
                "rKPE9tDeyL4auxAE6/OBDUbbSu1BzRgjpU3aaCXIXZJmIKL1G9RnJkXNqjP86gOOl7XVQygzhWpzvS95003AQXYR9KN0NuYH2h7EU3xAYCI7tzdXL4qPzgPKkFOcoPZVeDibZTkhB3sDYWGnZcPag3kp34ba9XhDBEB1LNp9SNJLqp7clt39hubk8BElhj4Sq5qsCOgL/Vkvq5tAlAHN6TwsDwjsGWywNspEUSb0AO/PmzzoUMEwt5ObUJOaKRnMAmKhtFJQ7HGC77q6qlZ1dzEBid+nsxvzENC8m986YdvU1knxW+iAJ9btj2ZCbWVgoM33Fm6W5A0yjKrdTmbjt2bX45w1ZZeb/dSVWsDNKGFBCC+3Dgi1GpFJPPK91W/Gwqgy6qxgQjMQDA9cqQUp/1o+rLGXdSXdbJMHz1EkoPvVqVirtUQMT1HpaOIb0fNTa7qMQwrUT6RhpAeLJggxS1Unr2M1Q/w9L2SJAtcUeZdh7lIoNLjgYMhKQNBIf2YnUrQ0wsvZWHamWov6AO4tkbkIy29TrSXCpK73wW1AsaMOeXI4jRbhd6tQGoJnsVEy4nx7W/fcaswmMY0mZxwMuFBqBdU0wa2zDCgvBXXkp7/ZrKxoLFYvtD4elh8i7d/aN0CLN/GpBpDJKFUtkLm9XXkFGxb1TCQKXzE8pSwVIsE=");
        assertThat(symmetricKey).isNotNull();
        assertThat(this.encryptionUtil.decryptText(symmetricKey, "")).isNull();
    }
}