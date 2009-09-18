/*
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * OscarSpringContextLoader.java
 *
 * Created on May 4, 2007, 10:42 AM
 */

package org.drugref.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtils {

    private static Logger logger = Logger.getLogger(ConfigUtils.class);
    private static Properties properties = null;

    static {
        try {
            String overrideProperties = System.getProperty("drugref");
            logger.info("loading " + overrideProperties);
            properties = getProperties(overrideProperties, "/drugref.properties");
        } catch (IOException e) {
            logger.error("unexpected error", e);
        }
    }

    public static String getProperty(String key) {
        return (properties.getProperty(key));
    }

    public static String getProperty(Class<?> c, String key) {
        return (getProperty(properties, c, key));
    }

    protected static String getProperty(Properties p, Class<?> c, String key) {
        return (p.getProperty(c.getName() + '.' + key));
    }

    /**
     * This will automatically read in the values in the file to this object.
     */
    protected static Properties getProperties(String propertiesUrl, String defaultPropertiesUrl) throws IOException {
        Properties p = new Properties();
        readFromFile(defaultPropertiesUrl, p);

        if (propertiesUrl != null) {
            p = new Properties(p);
            readFromFile(propertiesUrl, p);
        }

        return (p);
    }

    protected static Properties getProperties() {
        Enumeration em=properties.propertyNames();
        while(em.hasMoreElements()){
            System.out.println("property="+em.nextElement());
            
        }
        return (properties);
    }

    /**
     * This method reads the properties from the url into the object passed in.
     */
    private static void readFromFile(String url, Properties p) throws IOException {
        logger.info("Reading properties : " + url);

        InputStream is = ConfigUtils.class.getResourceAsStream(url);
        if (is == null) {
            is = new FileInputStream(url);
        }

        try {
            p.load(is);
        } finally {
            is.close();
        }
    }
}