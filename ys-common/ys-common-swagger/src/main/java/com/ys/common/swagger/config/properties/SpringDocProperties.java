package com.ys.common.swagger.config.properties;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Swagger
 *
 * @author ruoyi
 */
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties
{
    /**
     *
     */
    private String gatewayUrl;

    /**
     *
     */
    @NestedConfigurationProperty
    private InfoProperties info = new InfoProperties();

    /**
     * <p>
     *
     * </p>
     *
     * @see io.swagger.v3.oas.models.info.Info
     *
     *
     */
    public static class InfoProperties
    {
        /**
         * title
         */
        private String title = null;

        /**
         * Description
         */
        private String description = null;

        /**
         *
         */
        @NestedConfigurationProperty
        private Contact contact = null;

        /**
         *
         */
        @NestedConfigurationProperty
        private License license = null;

        /**
         *
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
