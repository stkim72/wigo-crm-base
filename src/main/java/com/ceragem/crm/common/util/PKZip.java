
package com.ceragem.crm.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <pre>
 * com.ceragem.crm.common.util - PKZip.java
 * </pre>
 *
 * @ClassName : PKZip
 * @Description : 파일Zip 유틸
 * @author : 김성태
 * @date : 2021. 1. 5.
 * @Version : 1.0
 * @Company : Copyright ⓒ wigo.ai. All Right Reserved
 */
@Slf4j
public final class PKZip {

	private static final int BUFFER_SIZE = 2048;
	private static final String ENCODE_LANG = "EUC-KR";

	private PKZip() {
	}

	static public boolean unZip(String strZipfilenm, String targetDir) {
		String strZipfilename = strZipfilenm;
		String strTargetDir = targetDir;
		ZipFile zipFile = null;
		try {

			char sep = File.separatorChar;
			strZipfilename = strZipfilename.replace('\\', '/');
			if (strTargetDir.length() == 0) {
				int nIndex = strZipfilename.lastIndexOf(sep);
				strTargetDir = strZipfilename.substring(0, nIndex);
			}
			String str = strTargetDir.substring(strTargetDir.length() - 1);
			if (!str.equals("/") && !str.equals("\\"))
				strTargetDir += File.separator;
			String strTargetFile = "";
			zipFile = new ZipFile(strZipfilename, ENCODE_LANG);
			Enumeration<? extends ZipArchiveEntry> enume = zipFile.getEntries();
			ZipArchiveEntry entry = null;
			while (enume.hasMoreElements()) {
				entry = enume.nextElement();
				if (entry.isDirectory())
					continue;
				if (entry.getSize() == 0)
					continue;

				// if(Config.getUseEncode())
				// {
				// strTargetFile=strTargetDir+new
				// String(entry.getName().getBytes(Config.getEncByte()),Config.getEncChar());
				// }
				// else
				strTargetFile = strTargetDir + entry.getName();
				String strDir = BaseUtilities.getFilePath(strTargetFile);
				BaseUtilities.createDirectory(strDir);
				if (!UnZip(zipFile.getInputStream(entry), strTargetFile))
					return false;

			}
			return true;
		}

		catch (FileNotFoundException e) {
			BaseUtilities.trace(e);
		} catch (IOException ex) {
			BaseUtilities.trace(ex);
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e) {
				log.debug(e.getMessage());
			}

		}
		return false;

	}

	static private boolean UnZip(InputStream inputStream, String strOutfilename) {
		if (inputStream == null)
			return true;
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(strOutfilename);

			byte[] b = new byte[512];
			int len = 0;
			while ((len = inputStream.read(b)) != -1) {
				fileOut.write(b, 0, len);
			}

			return true;

		} catch (FileNotFoundException e) {
			BaseUtilities.trace(e);
		} catch (IOException ex) {
			BaseUtilities.trace(ex);
		} catch (NullPointerException nex) {
			BaseUtilities.trace(nex);
		} finally {
			if (fileOut != null)
				try {
					fileOut.close();
				} catch (IOException e) {
					log.debug(e.toString());
				}
		}
		return false;

	}

	static public boolean zip(String zipName, String target) {
		if (BaseUtilities.isEmpty(zipName) || BaseUtilities.isEmpty(target))
			return false;
//		BufferedInputStream origin = null;
		ZipArchiveOutputStream zout = null;
		File fZip = new File(zipName);
		if (fZip.isFile())
			fZip.delete();
		try {
			zout = new ZipArchiveOutputStream(new File(zipName));
			File topFile = new File(target);
			File[] subFiles = topFile.listFiles();
			for (int i = 0; i < subFiles.length; i++) {
				if (!subFiles[i].getAbsolutePath().equals(zipName)) {
					zip(null, zout, subFiles[i], "", zipName);
				}
			}
			zout.closeArchiveEntry();
			return true;
		} catch (Exception e) {
			BaseUtilities.trace(e);
			return false;
		} finally {
			try {
				if (zout != null)
					zout.close();
			} catch (IOException e) {
				BaseUtilities.trace(e);
			}
		}
	}

	static public boolean zip(String zipName, List<File> targets, List<String> dispNames) {
		if (BaseUtilities.isEmpty(zipName) || BaseUtilities.isEmpty(targets))
			return false;
		ZipArchiveOutputStream zout = null;
		File fZip = new File(zipName);
		if (fZip.isFile())
			fZip.delete();
		try {
			zout = new ZipArchiveOutputStream(new File(zipName));
			// zout = new ZipArchiveOutputStream( new BufferedOutputStream(new
			// CheckedOutputStream( new FileOutputStream( zipName ), new Adler32() ) ) );

			for (int idx = 0; idx < targets.size(); idx++) {
				File file = targets.get(idx);
				String dispName = file.getName();
				if (!file.isFile())
					continue;
				if (BaseUtilities.isNotEmpty(dispNames) && dispNames.size() > idx) {
					dispName = dispNames.get(idx);
				}
				addEntry(zout, dispName, file);

			}
			zout.closeArchiveEntry();
			return true;
		} catch (Exception e) {
			BaseUtilities.trace(e);
			return false;
		} finally {
			try {
				if (zout != null)
					zout.close();
			} catch (IOException e) {
				BaseUtilities.trace(e);
			}
		}
	}

	static public boolean zip(String zipName, List<String> targets) {
		if (BaseUtilities.isEmpty(zipName) || BaseUtilities.isEmpty(targets))
			return false;
//         BufferedInputStream origin = null;
		ZipArchiveOutputStream zout = null;
		File fZip = new File(zipName);
		if (fZip.isFile())
			fZip.delete();
		try {
			zout = new ZipArchiveOutputStream(new File(zipName));

			for (int idx = 0; idx < targets.size(); idx++) {
				String target = targets.get(idx);
				File topFile = new File(target);
				if (topFile.isDirectory()) {
					File[] subFiles = topFile.listFiles();
					for (int i = 0; i < subFiles.length; i++) {
						if (!subFiles[i].getAbsolutePath().equals(zipName)) {
							zip(null, zout, subFiles[i], topFile.getName() + "/", zipName);
						}
					}
				} else {
					zip(null, zout, topFile, "", zipName);
				}

			}
			zout.closeArchiveEntry();
			return true;
		} catch (Exception e) {
			BaseUtilities.trace(e);
			return false;
		} finally {
			try {
				if (zout != null)
					zout.close();
			} catch (IOException e) {
				BaseUtilities.trace(e);
			}
		}
	}

	static private void zip(BufferedInputStream origin, ZipArchiveOutputStream zout, File file, String parentPath,
			String zipName) {

		String slash = File.separator;

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files.length != 0) {
				for (int i = 0; i < files.length; i++) {
					if (!files[i].getAbsolutePath().equals(zipName))
						zip(origin, zout, files[i], parentPath + file.getName() + slash, zipName);
				}
			} else {
				try {
					ZipArchiveEntry entry = new ZipArchiveEntry(parentPath + file.getName() + slash);
					zout.putArchiveEntry(entry);
				} catch (IOException e) {
					BaseUtilities.trace(e);
				} finally {
					try {
						if (origin != null)
							origin.close();
					} catch (IOException e) {
						BaseUtilities.trace(e);
					}
				}
			}
		} else {
			addEntry(zout, parentPath + file.getName(), file);

		}

	}

	private static void addEntry(ZipArchiveOutputStream zout, String entryName, File file) {
		BufferedInputStream origin = null;
		try {
			byte data[] = new byte[BUFFER_SIZE];
			origin = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
			ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
			zout.putArchiveEntry(entry);

			int count;
			while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
				zout.write(data, 0, count);
			}

		} catch (IOException e) {
			BaseUtilities.trace(e);
		} finally {
			try {
				if (origin != null)
					origin.close();
			} catch (IOException e) {
				BaseUtilities.trace(e);
			}
		}
	}

}
