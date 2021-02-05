package andro.jf.androclassloader;

import java.io.File;
import java.io.IOException;

import android.util.Log;
import dalvik.system.DexFile;

public class CustomClassLoader extends ClassLoader {

	private static final String DEX_SUFFIX = ".dex";
	private DexFile dex;

	public CustomClassLoader(File file, 
			File optimizedDirectory,
			String libraryPath, 
			ClassLoader parent) {
		super(parent);
		String optimizedPath = optimizedPathFor(file, optimizedDirectory);
		try {
			dex = DexFile.loadDex(file.getPath(), optimizedPath, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException 
	{
		Log.i("JFL", "CustomClassLoader: Loading class " + name);
		Class<?> clazz=null;

		clazz = dex.loadClass(name, this);

		if (clazz != null && clazz.getName().equals(name))
		{
			return clazz;
		}
		else
		{
			return null;
		}
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException 
	{
		Class<?> c = super.loadClass(className);

		if (c != null)
		{
			Log.i("JFL", "returning "  + c);
			return c;
		}
		else
		{
			Log.i("JFL", "Cannot load: " + className);
			return findClass(className);
		}
	}

	/**
	 * Converts a dex/jar file path and an output directory to an
	 * output file path for an associated optimized dex file.
	 */
	private static String optimizedPathFor(File path,
			File optimizedDirectory) {
		/*
		 * Get the filename component of the path, and replace the
		 * suffix with ".dex" if that's not already the suffix.
		 *
		 * We don't want to use ".odex", because the build system uses
		 * that for files that are paired with resource-only jar
		 * files. If the VM can assume that there's no classes.dex in
		 * the matching jar, it doesn't need to open the jar to check
		 * for updated dependencies, providing a slight performance
		 * boost at startup. The use of ".dex" here matches the use on
		 * files in /data/dalvik-cache.
		 */
		String fileName = path.getName();
		if (!fileName.endsWith(DEX_SUFFIX)) {
			int lastDot = fileName.lastIndexOf(".");
			if (lastDot < 0) {
				fileName += DEX_SUFFIX;
			} else {
				StringBuilder sb = new StringBuilder(lastDot + 4);
				sb.append(fileName, 0, lastDot);
				sb.append(DEX_SUFFIX);
				fileName = sb.toString();
			}
		}

		File result = new File(optimizedDirectory, fileName);
		return result.getPath();
	}
}