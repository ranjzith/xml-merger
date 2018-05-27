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
This section will be updated soon...

This is my first contribution towards freelancing utilities, feel free to download, try, modify, play around to suit your needs.

For any suggestions or questions, please reach me at **ranjzith@gmail.com**.