package org.ranjith.util.xml.merge.exception;

/**
 * Invalid or missing xml merger configurations.
 * 
 * @author rmekala
 *
 */
public class XMLMergeException extends RuntimeException {

	private static final long serialVersionUID = -3117830476728416510L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 */
	public XMLMergeException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 */
	public XMLMergeException(Exception ex) {
		super("Internal processor error:" + ex.getMessage());
	}

}
