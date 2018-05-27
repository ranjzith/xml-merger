package org.ranjith.util.xml.merge.common;

import java.io.File;

import org.ranjith.util.xml.merge.exception.XMLMergeException;

public class CommonUtils {
	
	public static boolean validateFileExist(String filePath) {
		File file = new File(filePath);
		if(file.exists() && file.isFile()) {
			return true;
		}
		throw new XMLMergeException("Input file does not exist:" + filePath);
	}
	
	public static boolean validateInputArgs(String... args) {
		if(args.length >= Constnats.MANDATARY_CLI_ARGS) {
			return true;
		}
		throw new XMLMergeException("Insufficient arguments passed.");
	}
}
