/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */
package org.esa.beam.util.io;

import org.esa.beam.util.Guardian;
import org.esa.beam.util.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.bc.ceres.core.Assert;

/**
 * This class provides additional functionality in handling with files. All methods in this class dealing with
 * extensions, expect that an extension is the last part of a file name starting with the dot '.' character.
 *
 * @author Tom Block
 * @author Sabine Embacher
 * @author Norman Fomferra

 */
public class FileUtils {

    /**
     * Gets the extension (which always includes a leading dot) of a file.
     *
     * @param file the file whose extension is to be extracted.
     *
     * @return the extension string which always includes a leading dot. Returns <code>null</code> if the file has
     *         no extension.
     */
    public static String getExtension(File file) {
        Guardian.assertNotNull("file", file);
        return getExtension(file.getPath());
    }

    /**
     * Gets the extension of a file path.
     *
     * @param path the file path whose extension is to be extracted.
     *
     * @return the extension string which always includes a leading dot. Returns <code>null</code> if the file path has
     *         no extension.
     */
    public static String getExtension(String path) {
        Guardian.assertNotNull("path", path);
        int index = getExtensionDotPos(path);
        if (index <= 0) {
            return null;
        }
        return path.substring(index);
    }

    /**
     * Gets the filename without its extension from the given file path.
     *
     * @param file the file whose filename is to be extracted.
     *
     * @return the filename without its extension.
     */
    public static String getFilenameWithoutExtension(File file) {
        return getFilenameWithoutExtension(file.getName());
    }

    /**
     * Gets the filename without its extension from the given filename.
     *
     * @param fileName the name of the file whose filename is to be extracted.
     *
     * @return the filename without its extension.
     */
    public static String getFilenameWithoutExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            return fileName.substring(0, i);
        }
        return fileName;
    }

    /**
     * Returns the file string with the given new extension. If the given file string have no extension, the given
     * extension will be added.
     * <p/>
     * Example1:
     * <pre> "tie.point.grids\tpg1.hdr" </pre>
     * results to
     * <pre> "tie.point.grids\tpg1.raw" </pre>
     * <p/>
     * Example2:
     * <pre> "tie.point.grids\tpg1" </pre>
     * results to
     * <pre> "tie.point.grids\tpg1.raw" </pre>
     *
     * @param path      the string to change the extension
     * @param extension the new file extension including a leading dot (e.g. <code>".raw"</code>).
     *
     * @throws java.lang.IllegalArgumentException
     *          if one of the given strings are null or empty.
     */
    public static String exchangeExtension(String path, String extension) {
        Guardian.assertNotNullOrEmpty("path", path);
        Guardian.assertNotNull("extension", extension);
        if (extension.length() > 0 && path.endsWith(extension)) {
            return path;
        }
        int extensionDotPos = getExtensionDotPos(path);
        if (extensionDotPos > 0) {
            // replace existing extension
            return path.substring(0, extensionDotPos) + extension;
        } else {
            // append extension
            return path + extension;
        }
    }

    /**
     * Returns a file with the given new extension. If the given file have no extension, the given extension will be
     * added.
     * <p/>
     * Example1:
     * <pre> "tie.point.grids\tpg1.hdr" </pre>
     * results to
     * <pre> "tie.point.grids\tpg1.raw" </pre>
     * <p/>
     * Example2:
     * <pre> "tie.point.grids\tpg1" </pre>
     * results to
     * <pre> "tie.point.grids\tpg1.raw" </pre>
     *
     * @param file      the file to change the extension
     * @param extension the new file extension including a leading dot (e.g. <code>".raw"</code>).
     *
     * @throws java.lang.IllegalArgumentException
     *          if one of the parameter strings are null or empty.
     */
    public static File exchangeExtension(File file, String extension) {
        Guardian.assertNotNull("file", file);
        String path = file.getPath();
        if (path.endsWith(extension)) {
            return file;
        }
        return new File(exchangeExtension(path, extension));
    }

    /**
     * Returns a file with the given extension. If the given path have no extension or the extension are not equal to
     * the given extension, the given extension will be added.
     * <p/>
     * Example1: param path = example.dim param extension = ".dim"
     * <pre> "example.dim" </pre>
     * results to
     * <pre> "example.dim" </pre>
     * <p/>
     * Example2: param path = example param extension = ".dim"
     * <pre> "example" </pre>
     * results to
     * <pre> "example.dim" </pre>
     * <p/>
     * Example3: param path = example.lem param extension = ".dim"
     * <pre> "example.lem" </pre>
     * results to
     * <pre> "example.lem.dim" </pre>
     *
     * @param path      the string to ensure the extension
     * @param extension the new file extension including a leading dot (e.g. <code>".raw"</code>).
     *
     * @throws java.lang.IllegalArgumentException
     *          if one of the given strings are null or empty.
     */
    public static String ensureExtension(String path, String extension) {
        Guardian.assertNotNullOrEmpty("path", path);
        Guardian.assertNotNullOrEmpty("extension", extension);
        if (path.endsWith(extension)) {
            return path;
        } else {
            // append extension
            if (path.length() > 1 && path.endsWith(".")) {
                path = path.substring(0, path.length() - 1);
            }
            return path + extension;
        }
    }

    /**
     * Returns a file with the given extension. If the given file has no extension or the extension is not equal to
     * the given extension, the given extension will be added.
     * <p/>
     * Example1: param file = example.dim param extension = ".dim"
     * <pre> "example.dim" </pre>
     * results to
     * <pre> "example.dim" </pre>
     * <p/>
     * Example2: param file = example param extension = ".dim"
     * <pre> "example" </pre>
     * results to
     * <pre> "example.dim" </pre>
     * <p/>
     * Example3: param file = example.lem param extension = ".dim"
     * <pre> "example.lem" </pre>
     * results to
     * <pre> "example.lem.dim" </pre>
     *
     * @param file      the file to ensure the extension
     * @param extension the new file extension including a leading dot (e.g. <code>".raw"</code>).
     *
     * @throws java.lang.IllegalArgumentException
     *          if one of the parameter strings are null or empty.
     */
    public static File ensureExtension(File file, String extension) {
        Guardian.assertNotNull("file", file);
        String path = file.getPath();
        if (path.endsWith(extension)) {
            return file;
        }
        return new File(ensureExtension(path, extension));
    }

    public static int getExtensionDotPos(String path) {
        Guardian.assertNotNullOrEmpty("path", path);
        int extensionDotPos = path.lastIndexOf('.');
        if (extensionDotPos > 0) {
            int lastSeparatorPos = path.lastIndexOf('/');
            lastSeparatorPos = Math.max(lastSeparatorPos, path.lastIndexOf('\\'));
            lastSeparatorPos = Math.max(lastSeparatorPos, path.lastIndexOf(':'));
            if (lastSeparatorPos < extensionDotPos - 1) {
                return extensionDotPos;
            }
        }
        return -1;
    }

    /**
     * Retrieves the file name from a complete path. example: "c:/testData/MERIS/meris_test.N1" will be converted to
     * "meris_test.N1"
     */
    public static String getFileNameFromPath(String path) {
        Guardian.assertNotNullOrEmpty("path", path);
        String fileName;
        int lastChar = path.lastIndexOf(File.separator);
        if (lastChar >= 0) {
            fileName = path.substring(lastChar + 1, path.length());
        } else {
            fileName = path;
        }
        return fileName;
    }

    /**
     * Lists the files with the specified extension contained in the given directory.
     *
     * @see java.io.File#listFiles()
     * @see java.io.File#listFiles(java.io.FilenameFilter)
     */
    public static File[] listFilesWithExtension(File dir, String extension) {
        if (dir == null) {
            return null;
        }
        if (extension != null && extension.length() > 0) {
            return dir.listFiles(createExtensionFilenameFilter(extension));
        } else {
            return dir.listFiles();
        }
    }

    /**
     * Lists the filenames with the specified extension contained in the given directory.
     *
     * @see java.io.File#list()
     * @see java.io.File#list(java.io.FilenameFilter)
     */
    public static String[] listFilePathsWithExtension(File dir, String extension) {
        if (dir == null) {
            return null;
        }
        if (extension != null && extension.length() > 0) {
            return dir.list(createExtensionFilenameFilter(extension));
        } else {
            return dir.list();
        }
    }

    /**
     * Creates a file filter which only lets files through which have the given extension.
     *
     * @see java.io.FilenameFilter
     */
    public static FilenameFilter createExtensionFilenameFilter(String extension) {
        final String extensionLC = extension.toLowerCase();
        return new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.length() > extensionLC.length()
                       && name.toLowerCase().endsWith(extensionLC);
            }
        };
    }

    /**
     * Creates a valid filename for the given source name. The method returns a string which is the given name where
     * each occurence of a character which is not a letter, a digit or one of '_', '-', '.' is replaced by an
     * underscore. The returned string always has the same length as the source name.
     *
     * @param name the source name, must not be  <code>null</code>
     */
    public static String createValidFilename(String name) {
        Guardian.assertNotNull("name", name);
        return StringUtils.createValidName(name, new char[]{'_', '-', '.'}, '_');
    }

//    /**
//     * Checks if the given file can be created with the given filesize.
//     *
//     * The given file must not be <code>null</code>. Also, the file must not exist and
//     * must contain an absolute path.
//     * @param file the file which will be checked
//     * @param size the size to check
//     * @return true if the given file can have the given size
//     * @throws IllegalArgumentException if the conditions above are not met.
//     */
//    public static boolean isFilesizeAvailable(File file, long size) {
//        final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
//        if (file == null || !file.isAbsolute() || file.exists()
//                || fileSystemView.isComputerNode(file)
//                || fileSystemView.isDrive(file)
//                || fileSystemView.isFileSystem(file)
//                || fileSystemView.isFileSystemRoot(file)
//                || fileSystemView.isFloppyDrive(file)
////        || fileSystemView.isParent(file,)
//                || fileSystemView.isRoot(file)
//                || fileSystemView.isTraversable(file).booleanValue()) {
//            throw new IllegalArgumentException("The given file is invalid");
//        } else {
//            RandomAccessFile randomAccessFile = null;
//            try {
//                   file.getParentFile().mkdirs();
//                file.createNewFile();
//                randomAccessFile = new RandomAccessFile(file, "rw");
//                randomAccessFile.setLength(size);
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (randomAccessFile != null) {
//                    try {
//                        randomAccessFile.close();
//                    } catch (IOException e) {
//                    }
//                }
//                file.delete();
//            }
//            return true;
//        }
//    }

//    public static String getAbsolutePath(File file) {
//        if (file == null) {
//            return null;
//        }
//        if (file.exists()) {
//            return file.getAbsolutePath();
//        }
//        final File workingDir = SystemUtils.getCurrentWorkingDir();
//        final String path = file.getPath();
//        final String s = File.separator;
//        if (path.startsWith(s)) {
//            return workingDir + path;
//        } else {
//            return workingDir + s + path;
//        }
//    }

    /**
     * Gets a normalized URL representation for the given file.
     * <p/>
     * <p>Unlike the  {@link java.io.File#toURL() File.toURL()} method,
     * this method automatically escapes characters that are illegal in URLs.
     * It converts the given abstract pathname into a URL by first converting it into a
     * URI, via the {@link java.io.File#toURI() File.toURI()} method, and then converting the URI
     * into a URL via the {@link java.net.URI#toURL() URI.toURL} method.
     * .
     * <p>See also the 'Usage Note' of the {@link java.io.File#toURL()} API documentation.
     *
     * @param file the file
     *
     * @return a normalized URL representation
     */
    public static URL getFileAsUrl(File file) throws MalformedURLException {
        final URI uri = file.toURI();
        return uri.toURL();
    }

    /**
     * Inverse of {@link #getFileAsUrl(java.io.File)}.
     *
     * @param url the URL
     *
     * @return the file
     */
    public static File getUrlAsFile(URL url) throws URISyntaxException {
        return new File(url.toURI());
    }

    public static String getDisplayText(File file, int maxLength) {
        Assert.notNull(file, "file");
        Assert.argument(maxLength >= 4, "maxLength >= 4");
        
        String text = file.getPath();
        if (text.length() <= maxLength) {
            return text;
        }

        while (text.length() + 3 > maxLength) {
            int pos = text.indexOf(File.separator, text.startsWith(File.separator) ? 1 : 0);
            if (pos == -1) {
                return "..." + text.substring(text.length() - maxLength + 3);
            }
            text = text.substring(pos);
        }
        return "..." + text;
    }

    public static String readText(File file) throws IOException {
        final FileReader reader1 = new FileReader(file);
        try {
            return readText(reader1);
        } finally {
            reader1.close();
        }
    }

    public static String readText(Reader reader) throws IOException {
        final BufferedReader br = new BufferedReader(reader);
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append("\n");
        }
        return text.toString();
    }
}