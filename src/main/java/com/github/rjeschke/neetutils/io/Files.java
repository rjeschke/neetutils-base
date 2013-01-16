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
package com.github.rjeschke.neetutils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.github.rjeschke.neetutils.Strings;
import com.github.rjeschke.neetutils.SysUtils;
import com.github.rjeschke.neetutils.collections.Colls;
import com.github.rjeschke.neetutils.rng.RNG;
import com.github.rjeschke.neetutils.rng.RNGFactory;
import com.github.rjeschke.neetutils.rng.RNGType;

public final class Files implements Runnable
{
    private final static ConcurrentLinkedQueue<File> TEMP_FOLDERS = new ConcurrentLinkedQueue<File>();
    
    private Files()
    {
        //
    }

    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Files()));
    }
    
    /**
     * Recursively lists all files at the given path.
     * 
     * @param parent Path.
     * @return List of files.
     */
    public final static List<File> listFiles(File parent)
    {
        List<File> files = Colls.list();
        listFiles(parent, files);
        return files;
    }
    
    private final static void listFiles(File parent, List<File> files)
    {
        if(parent.isFile())
            files.add(parent);
        else
        {
            final File[] fs = parent.listFiles();
            for(final File f : fs)
            {
                if(f.isFile())
                    files.add(f);
                else
                    listFiles(f, files);
            }
        }
    }
    
    public final static File createUniqueTempFolder()
    {
        return createUniqueTempFolder("neetutils", true);
    }

    public final static File createUniqueTempFolder(String prefix, boolean autoCleanup)
    {
        final String tmp = System.getProperty("java.io.tmpdir");
        final RNG rnd = RNGFactory.create(RNGType.LCG);
        for(int i = 0; i < 0x40000000; i++)
        {
            final File t = new File(tmp, String.format("%s-tmp-%08x", prefix, rnd.nextInt()));
            if(!t.exists())
            {
                if(t.mkdir())
                {
                    if(autoCleanup)
                        TEMP_FOLDERS.offer(t);
                    return t;
                }
            }
        }
        
        throw new RuntimeException("Could not create unique temp folder, please clean up your /tmp.");
    }
    
    public final static void recurseDeleteFolder(File path)
    {
        if(path.isDirectory())
        {
            final File[] files = path.listFiles();
            for(final File f : files)
            {
                if(f.isDirectory())
                    recurseDeleteFolder(f);
                else
                    f.delete();
            }
            path.delete();
        }
        else
            path.delete();
    }
    
    public final static void copy(File input, File output) throws IOException
    {
        final InputStream in = new FileInputStream(input);
        try
        {
            final OutputStream out = new FileOutputStream(output);
            try
            {
                copy(in, out);
            }
            finally
            {
                out.close();
            }
        }
        finally
        {
            in.close();
        }
    }

    public final static void copy(InputStream in, File output) throws IOException
    {
        final OutputStream out = new FileOutputStream(output);
        try
        {
            copy(in, out);
        }
        finally
        {
            out.close();
        }
    }

    public final static void copy(File input, OutputStream out) throws IOException
    {
        final InputStream in = new FileInputStream(input);
        try
        {
            copy(in, out);
        }
        finally
        {
            in.close();
        }
    }

    public final static void copy(InputStream input, OutputStream output) throws IOException
    {
        final byte[] buffer = new byte[65536];

        for(;;)
        {
            final int read = input.read(buffer);
            if(read == -1)
                break;
            output.write(buffer, 0, read);
        }
    }

    public final static byte[] asBytes(String filename) throws IOException
    {
        return asBytes(new File(filename));
    }
    
    public final static byte[] asBytes(File file) throws IOException
    {
        final FileInputStream fis = new FileInputStream(file);
        final byte[] buffer = new byte[(int)file.length()];
        try
        {
            int p = 0;
            while(p < buffer.length)
            {
                final int r = fis.read(buffer, p, buffer.length - p);
                if(r == -1)
                    break;
                p += r;
            }
            if(p != buffer.length)
                throw new IOException("Unexpected end of stream, expected " + buffer.length + ", got " + p + " bytes");
            return buffer;
        }
        finally
        {
            fis.close();
        }
    }
    
    public final static byte[] asBytes(InputStream in) throws IOException
    {
        byte[] buffer = new byte[65536];
        try
        {
            int p = 0;
            for(;;)
            {
                final int r = in.read(buffer, p, buffer.length - p);
                if(r < 0)
                    break;
                p += r;
                if(p >= buffer.length)
                    buffer = Arrays.copyOf(buffer, buffer.length + 65536); 
            }
            return p != buffer.length ? Arrays.copyOf(buffer, p) : buffer;
        }
        finally
        {
            in.close();
        }
    }

    public final static String asString(String filename, String charsetName) throws IOException
    {
        return new String(asBytes(filename), charsetName);
    }
    
    public final static String asString(File file, String charsetName) throws IOException
    {
        return new String(asBytes(file), charsetName);
    }
    
    public final static String asString(InputStream in, String charsetName) throws IOException
    {
        return new String(asBytes(in), charsetName);
    }
    
    public final static void saveBytes(String filename, byte[] bytes) throws IOException
    {
        saveBytes(filename, bytes, 0, bytes.length);
    }
    
    public final static void saveBytes(String filename, byte[] bytes, int offs, int len) throws IOException
    {
        saveBytes(new File(filename), bytes, offs, len);
    }
    
    public final static void saveBytes(File file, byte[] bytes) throws IOException
    {
        saveBytes(file, bytes, 0, bytes.length);
    }
    
    public final static void saveBytes(File file, byte[] bytes, int offs, int len) throws IOException
    {
        final FileOutputStream fos = new FileOutputStream(file);
        try
        {
            fos.write(bytes, offs, len);
        }
        finally
        {
            fos.close();
        }
    }
    
    /**
     * Lists all resources in the given package.
     * 
     * @param pkgName
     *            The package name
     * @return List of all resources inside the package (recursively)
     */
    public final static List<String> list(final String pkgName)
    {
        final URL url = SysUtils.class.getResource("/" + pkgName.replace('.', '/'));
        if(url == null)
            return null;

        if(url.getProtocol().equals("jar"))
            return getJars(url, pkgName.replace('.', '/'));

        return getFiles(url.getPath(), pkgName);
    }

    /**
     * Gets a list of all resources on the classpath.
     * 
     * @return A list of Strings.
     */
    public final static List<String> getFilesOnClasspath()
    {
        final ArrayList<String> ret = new ArrayList<String>();
        final char sep = System.getProperty("path.separator").charAt(0);
        final List<String> paths = Strings.split(System.getProperty("java.class.path"), sep);

        for(final String path : paths)
        {
            final File file = new File(path);
            if(file.isDirectory())
            {
                final List<File> files = listFiles(file);
                final int cut = file.toString().length();
                for(final File f : files)
                {
                    ret.add(f.toString().substring(cut).replace('\\', '/'));
                }
            }
            else if(file.isFile() && file.getName().toLowerCase().endsWith(".jar"))
            {
                ret.addAll(getJarFiles(file));
            }
        }
        return ret;
    }
    
    private final static List<String> getFiles(final String path, String basePackage)
    {
        final ArrayList<String> classes = new ArrayList<String>();
        final String basePath = "/" + basePackage.replace('.', '/') + "/";
        final File dir = new File(path).getAbsoluteFile();
        final File[] files = dir.listFiles();
        for(File f : files)
        {
            final String fn = f.getName();
            if(f.isDirectory())
            {
                classes.addAll(getFiles(new File(dir, fn).getAbsolutePath(), basePackage + "." + fn));
            }
            else if(f.isFile())
            {
                classes.add(basePath + fn);
            }
        }
        return classes;
    }

    private final static List<String> getJarFiles(File file)
    {
        final ArrayList<String> classes = new ArrayList<String>();
        try
        {
            final JarFile jar = new JarFile(file);
            final Enumeration<JarEntry> j = jar.entries();
            while(j.hasMoreElements())
            {
                final JarEntry je = j.nextElement();
                if(!je.isDirectory())
                {
                    classes.add("/" + je.getName());
                }
            }
            jar.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return classes;
    }

    private final static List<String> getJars(URL furl, String pkgname)
    {
        final ArrayList<String> classes = new ArrayList<String>();
        try
        {
            final URL url = new URL(furl.getPath());
            final String f = URLDecoder.decode(url.getFile(), System.getProperty("file.encoding"));
            final File dir = new File(f.substring(0, f.lastIndexOf('!')));
            final JarFile jar = new JarFile(dir);
            final Enumeration<JarEntry> j = jar.entries();
            while(j.hasMoreElements())
            {
                final JarEntry je = j.nextElement();
                if(!je.isDirectory())
                {
                    if(je.getName().startsWith(pkgname))
                    {
                        classes.add("/" + je.getName());
                    }
                }
            }
            jar.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return classes;
    }

    public final static String home(String path)
    {
        return new File(System.getProperty("user.home"), path).getAbsolutePath();
    }
    
    public final static File home(File path)
    {
        return new File(new File(System.getProperty("user.home")), path.toString());
    }
    
    @Override
    public void run()
    {
        File file;
        while((file = TEMP_FOLDERS.poll()) != null)
            recurseDeleteFolder(file);
    }

}
