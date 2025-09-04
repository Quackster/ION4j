package net.nillus.ion.Scripting;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Provides a host for Ion plugins, exposing access to types and methods in the Ion plugin host and it's parents.
 */
public class IonPluginHost implements IIonPluginHost {
    /**
     * Tries to find a System.Type by doing a case-sensitive search for a full type name including namespaces. If the type is found it is returned. Null is returned if an exception is thrown.
     *
     * @param sTypeName The full, case-sensitive type name string of the type to get.
     * @return Type
     */
    public Class<?> getIonEnvironmentType(String sTypeName) {
        try {
            return Class.forName(sTypeName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Tries to find System.Reflection.MethodInfo of a given method in a given type, by doing a search in the type. If the method is found, the MethodInfo is returned. Null is returned if an exception is thrown.
     *
     * @param pType       The System.Type instance of the type to search in.
     * @param sMethodName The method name of the wanted method as a string.
     * @return Method
     */
    public Method getIonEnvironmentMethod(Class<?> pType, String sMethodName) {
        try {
            return pType.getMethod(sMethodName);
        } catch (Exception e) {
            return null;
        }
    }
}

interface IIonPluginHost {
    /**
     * Methods
     */
    Class<?> getIonEnvironmentType(String sType);

    Method getIonEnvironmentMethod(Class<?> pType, String sMethodName);
}