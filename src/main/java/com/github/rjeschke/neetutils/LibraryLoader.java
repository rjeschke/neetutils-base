/*
 * Copyright (C) 2012 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rjeschke.neetutils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.github.rjeschke.neetutils.io.Files;

/**
 * Utility class to load JNI and other native libraries.
 * 
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public final class LibraryLoader
{
    private final static File TEMP_DIR = Files.createUniqueTempFolder();

    private LibraryLoader()
    {
        /* no instances */
    }

    private final static Set<String> loadedLibs = new HashSet<String>();

    public final static void load(final String basePackage, final String lib)
    {
        final Class<?> caller = getCallingClass();
        load(caller, mapLibrary(caller, basePackage, lib), false);
    }

    public final static void loadNative(final String os, final String basePackage, final String lib)
    {
        final Class<?> caller = getCallingClass();
        loadNative(caller, os, basePackage, lib);
    }

    private final static String copyLibrary(final Class<?> caller, final String resource)
    {
        final String libname = resource.substring(resource.lastIndexOf('/') + 1);
        final File f = new File(TEMP_DIR, libname);
        final InputStream is = caller.getResourceAsStream(resource);
        if(is == null)
        {
            throw new UnsatisfiedLinkError("Can't locate '" + libname + "'");
        }

        try
        {
            if(!loadedLibs.contains(f.getAbsolutePath()))
                Files.copy(is, f);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load library '" + libname + "'", e);
        }

        return f.getAbsolutePath();
    }

    private final static void loadLibrary(final Class<?> caller, final String resource)
    {
        final String filename = copyLibrary(caller, resource);
        if(!loadedLibs.contains(filename))
        {
            loadedLibs.add(filename);
            System.load(filename);
        }
    }

    private final static void loadNative(final Class<?> caller, final String os, final String basePackage,
            final String lib)
    {
        final String osId = getOsIdentifier();
        final String osStr = osId + System.getProperty("sun.arch.data.model");

        if(osId.equals(os))
            load(caller, mapLibrary(caller, basePackage, lib), true);
        else if(osStr.equals(os))
            load(caller, basePackage + "." + osStr + "." + lib, true);
    }

    private final static void load(final Class<?> caller, final String lib, final boolean isNative)
    {
        String libname, pkg;
        if(lib.indexOf('.') == -1)
        {
            pkg = "/";
            libname = lib;
        }
        else
        {
            if(isNative)
            {
                int i = lib.lastIndexOf('.');
                i = lib.substring(0, i).lastIndexOf('.');
                pkg = "/" + lib.substring(0, i + 1).replace('.', '/');
                libname = lib.substring(i + 1);
            }
            else
            {
                final int i = lib.lastIndexOf('.');
                pkg = "/" + lib.substring(0, i + 1).replace('.', '/');
                libname = lib.substring(i + 1);
            }
        }

        try
        {
            System.loadLibrary(libname);
            return;
        }
        catch (UnsatisfiedLinkError e)
        {
            // Ignore
        }

        final String osLib = isNative ? libname : SysUtils.mapLibraryName(libname);

        loadLibrary(caller, pkg + osLib);
    }

    private final static Class<?> getCallingClass()
    {
        try
        {
            final StackTraceElement myCaller = Thread.currentThread().getStackTrace()[3];
            return Class.forName(myCaller.getClassName());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private final static String getOsIdentifier()
    {
        final String os = System.getProperty("os.name").toLowerCase();

        if(os.startsWith("windows"))
            return "win";
        if(os.startsWith("linux"))
            return "linux";
        if(os.startsWith("mac"))
            return "macos";

        throw new RuntimeException("Unsupported OS version:" + System.getProperty("os.name"));
    }

    private final static String mapLibrary(final Class<?> caller, final String basePackage, final String libName)
    {
        final String osid = getOsIdentifier();
        final String arch = System.getProperty("sun.arch.data.model");
        final String check = basePackage + "." + osid;
        if(caller.getResource("/" + check.replace('.', '/') + arch + "/") != null)
        {
            return check + arch + "." + libName;
        }
        if(caller.getResource("/" + check.replace('.', '/') + "32/") != null)
        {
            return check + "32." + libName;
        }
        if(caller.getResource("/" + check.replace('.', '/') + "/") != null)
        {
            return check + "." + libName;
        }
        throw new RuntimeException("Can't map library: " + basePackage + "/" + libName);
    }
}
