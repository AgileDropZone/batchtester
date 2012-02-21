package com.agiledropzone.batchtester.tools;

/**
 * Fourni les informations pour le manifest.
 */
public class Version {
    /** */
    public static String getSpecification() {
        Package pkg = Version.class.getPackage();
        return (pkg == null) ? null : pkg.getSpecificationVersion();
    }

    /** */
    public static String getImplementation() {
        Package pkg = Version.class.getPackage();
        return (pkg == null) ? null : pkg.getImplementationVersion();
    }

    /** */
    public static void main(String[] args) {
        System.out.println("Version : " + getSpecification());
        System.out.println("Implementation : " + getImplementation());
    }
}
