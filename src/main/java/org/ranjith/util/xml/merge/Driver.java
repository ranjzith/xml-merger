package org.ranjith.util.xml.merge;

import org.ranjith.util.xml.merge.processor.XMLMergeProcessor;

public class Driver {
	public static void main(String[] args) {
		System.out.println("Initiating XML merge processor...");
		XMLMergeProcessor processor = new XMLMergeProcessor();
		System.out.println(processor.process(args));
	}
}
