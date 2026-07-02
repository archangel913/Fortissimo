package tokyo.archangel.fortissimo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * アプリケーションの設定を保持するクラス
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fortissimo")
public class ApplicationProperties {
    /**
     * クライアントID
     */
	private String clientId;

	/**
	 * クライアントシークレット
	 */
    private String clientSecret;

    /**
     * リダイレクトURL
     */
    private String redirectUri;
}
