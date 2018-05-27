# xml-merger

XML merger is a merge utility provided as an executable fat/uber java archive with the dependencies included.

This README file explains how to generate the executable and usage of the xml-merger services.

## Prerequisites
* Java 1.8 or higher

## Generating the fat/uber jar

xml-merger is configured with gradle wrapper, this helps in generating executables without forcing the manual installation of gradle. 
When you first run any of the below gradle commands, it will download the specific version of gradle and then continues with the appropriate task.

If you already have gradle installed on your machine, you could use **gradle** instead of **gradlew** for the following commands.

Run the fatJar task from the xml-merger root directory.

    gradlew fatJar

To clear the old builds before generating the executable run the above with gradle clean task

    gradlew clean fatJar

Above commands shall generate **xml-merger-all-X.X.X.jar** in the <xml-merger-root>/build/libs directory

## Usage
You have two ways in using the xml-merger services, by specifying below parameters.

* **source xml file path** - File 1, Absolute path to the source xml file
* **source xml file path** - File 2, Absolute path to the source xml file
* **destination xml file path** - Absolute path to the destination xml file, a new file would be created after successful merge operation.
* **comma seperated leaf nodes** - Comma seperated leaf nodes, where no children comparison would be applied if a leaf node is reached.

### 1. Executing the jar through CLI

Run the executable from the command prompt from windows or **nux shell.

    java -jar path/to/executable/xml-merger-all-X.X.X.jar /path/to/src1 /path/to/src2 /path/to/dest leafNode1,leafNode2

>java -jar xml-merger-all-1.0.0.jar file1.xml file2.xml dest.xml product

Above shall output the loggers on to the console

### 2. Invoking the API in your java application

Include xml-merger-all-X.X.X.jar in your java application's classpath and use **XMLMergeProcessor**'s **process** method to get the job done, this is the same processor invoked from a driver class in the CLI approach.

Example: 

    XMLMergeProcessor processor = new XMLMergeProcessor();
    String[] args = new String[4];
    args[0] = "/path/to/file1.xml";
    args[1] = "/path/to/file2.xml";
    args[2] = "/path/to/dest.xml";
    args[3] = "product";
    System.out.println(processor.process(args));


This work is my contribution towards freelancing utilities, feel free to download, try, modify, play around to suit your needs.

For any suggestions or questions, please reach me at **ranjzith@gmail.com**.
