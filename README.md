Author: Victor Gustafsson - dv16vgn

RadioInfo is a program with the ability to display radio schedules.

It is compiled with the line:

    mvn package
    
...while sitting in a directory with the pom file and the source code. 
This will give you a JAR file in the target directory.

It is then run with:

    java -jar target/radio-info-1.0-SNAPSHOT-jar-with-dependencies.jar
    
    
All tests are run with:

    mvn test

![Screenshot](https://git.cs.umu.se/dv16vgn/RadioInfo/raw/db95081c6f9779fd859978c8c0951106c9a33adc/src/main/resources/radio_info.png)
