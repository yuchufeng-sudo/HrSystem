package com.ys.common.swagger.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

/**
 * Swagger Configuration Properties
 * Configuration class for SpringDoc/Swagger API documentation settings
 *
 * @author ruoyi
 */
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties
{
    /**
     * Gateway URL
     */
    private String gatewayUrl;

    /**
     * Basic information for the API documentation
     */
    @NestedConfigurationProperty
    private InfoProperties info = new InfoProperties();

    /**
     * <p>
     * Basic properties for API documentation information
     * </p>
     *
     * @see io.swagger.v3.oas.models.info.Info
     *
     * This class is duplicated here to enable Spring Boot auto-configuration
     * to generate configuration hints and suggestions
     */
    public static class InfoProperties
    {
        /**
         * API documentation title
         */
        private String title = null;

        /**
         * API documentation description
         */
        private String description = null;

        /**
         * Contact information
         */
        @NestedConfigurationProperty
        private Contact contact = null;

        /**
         * License information
         */
        @NestedConfigurationProperty
        private License license = null;

        /**
         * API version
         */
        private String version = null;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public Contact getContact()
        {
            return contact;
        }

        public void setContact(Contact contact)
        {
            this.contact = contact;
        }

        public License getLicense()
        {
            return license;
        }

        public void setLicense(License license)
        {
            this.license = license;
        }

        public String getVersion()
        {
            return version;
        }

        public void setVersion(String version)
        {
            this.version = version;
        }
    }

    public String getGatewayUrl()
    {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl)
    {
        this.gatewayUrl = gatewayUrl;
    }

    public InfoProperties getInfo()
    {
        return info;
    }

    public void setInfo(InfoProperties info)
    {
        this.info = info;
    }
}
