## Sriman Cherukuru

## Summary

This a project to implement a map reduced framework for a log generator to help in paralelly processing the given data.
The log generator randomly generates the logs and with the help of Map Reduced functionality, we process the logs and aggregate into different ways as required by the tasks.
These Map reduced classes are then locally tested using Apache hadoop which is deployed in a VMware machine and
then finally deployed into AWS as EMR function to execute the program at low cost.

## Prerequisites

* Install SBT to build the jar
* Terminal to SSH and SCP into VM to execute Hadoop commands
* Install VM Workstation Pro and run Horton Sandbox which comes with Apache Hadoop installation.
* AWS account to execute the jar file in Amazon EMR .

## Installation

* Clone the GIT repository by using git clone https://github.com/sriman98/LogFileGenerator.git
* Run the following commands in the console

```
sbt clean compile test
```
```
sbt clean compile run
```
* It builds and compiles the project
* If you are using IntellIj clone the repository by using "Check out from Version Control and then Git."

* The scala version should be set in the Global Libraries under Project Structure in Files .
* The SBT configuration should be added by using Edit Configurations and then simulations can be ran in the IDE .
* Now Build the project and then run the  mapReduce in hadoop to get the output.

## Execution

```
sbt clean compile assembly
```

The above commands generate the jar file in the root target folder under the scala path and this jar file has to be
moved into the hdfs file system to execute the map reduce in the hadoop environment .

Run the main method in generate log data class to generate the random log values .
For this project , I have generated 500 records to be used as a input value .

### Steps

* Start the hortonworks in the VM workstation Pro . The URL to be used to SSH will be displayed on the screen once it starts .
* Use the URL to SSH and login into the hortonworks

```
 ssh -p 2222 root@192.168.184.128
```

The default password in hadoop . Set the password you require after that . Make sure to use port 2222 to login to execute hadoop commands .

```
 su - hdfs
```

Change the filesystem to hdfs to execute the mapreduce in hadoop environment .

```
scp -P 2222 Path\LogFileGenerator-assembly-0.1.jar root@192.168.184.128:/home/hdfs
scp -P 2222 Path\input.txt root@192.168.184.128:/home/hdfs
```

Copy the jar and the input file to the hdfs system . SCP is used to copy the file from local system to VM system .

``` 
cd /home/hdfs
hdfs dfs -mkdir /LogFileGenerator
 ```

Create a hdfs directory for copying the local file to hdfs format

``` 
hdfs dfs -copyFromLocal LogFileGenerator-assembly-0.1.jar /LogFileGenerator
 ```

The above command is used to copy the file from normal file system to HDFS file system

``` 
hdfs dfs -rm -R /LogFileGenerator/LogFileGenerator-assembly-0.1.jar
``` 

Incase we need to delete the file we have to use the above command .

``` 
hadoop jar LogFileGenerator-assembly-0.1.jar ClassName inputfile outputfile
``` 

In order to execute the Map Reduce job, you must use the above command to execute the particular class file from the given jar . The input file will be taken and parsed in the code level .

``` 
hdfs dfs -rm -R outputfile
``` 

The above command is used to delete the output file incase we want to rerun the jar again and take a new output .

``` 
hdfs dfs -text outputFile/part-r-00000
``` 

To view the output file use the above command

### Output

#### Task1 -> Distribution of Logs at Time Intervals
This task is executed by mapping the combination of second as a bin and Type of log mapped together and later reduced in the reducer function. The Output is a CSV as required.
``` 
DEBUG 01:34:33,2                                                                                                                                                        
DEBUG 01:34:34,4                                                                                                                                                        
DEBUG 01:34:35,1                                                                                                                                                        
DEBUG 09:13:22,1                                                                                                                                                        
DEBUG 09:13:23,5                                                                                                                                                        
DEBUG 09:13:24,1                                                                                                                                                        
DEBUG 11:04:12,1                                                                                                                                                        
DEBUG 11:04:13,5                                                                                                                                                        
DEBUG 11:04:14,1                                                                                                                                                        
DEBUG 16:31:33,5                                                                                                                                                        
DEBUG 16:31:34,6                                                                                                                                                        
DEBUG 16:31:35,5                                                                                                                                                        
DEBUG 16:31:36,8                                                                                                                                                        
DEBUG 16:31:37,8                                                                                                                                                        
DEBUG 16:31:38,8                                                                                                                                                        
DEBUG 16:31:39,9                                                                                                                                                        
ERROR 01:34:33,1                                                                                                                                                        
ERROR 01:34:34,1                                                                                                                                                        
ERROR 09:13:22,1                                                                                                                                                        
ERROR 09:13:23,1                                                                                                                                                        
ERROR 11:04:13,2                                                                                                                                                        
ERROR 16:31:33,2                                                                                                                                                        
ERROR 16:31:35,1                                                                                                                                                        
INFO 01:34:33,11                                                                                                                                                        
INFO 01:34:34,34                                                                                                                                                        
INFO 01:34:35,21                                                                                                                                                        
INFO 09:13:22,9                                                                                                                                                         
INFO 09:13:23,37                                                                                                                                                        
INFO 09:13:24,20                                                                                                                                                        
INFO 11:04:12,6                                                                                                                                                         
INFO 11:04:13,32                                                                                                                                                        
INFO 11:04:14,28                                                                                                                                                        
INFO 16:31:32,1                                                                                                                                                         
INFO 16:31:33,34                                                                                                                                                        
INFO 16:31:34,52                                                                                                                                                        
INFO 16:31:35,57                                                                                                                                                        
INFO 16:31:36,57                                                                                                                                                        
INFO 16:31:37,60                                                                                                                                                        
INFO 16:31:38,58                                                                                                                                                        
INFO 16:31:39,33                                                                                                                                                        
WARN 01:34:33,8                                                                                                                                                         
WARN 01:34:34,13                                                                                                                                                        
WARN 01:34:35,7                                                                                                                                                         
WARN 09:13:22,5                                                                                                                                                         
WARN 09:13:23,17                                                                                                                                                        
WARN 09:13:24,6                                                                                                                                                         
WARN 11:04:12,3                                                                                                                                                         
WARN 11:04:13,17                                                                                                                                                        
WARN 11:04:14,8                                                                                                                                                         
WARN 16:31:32,1                                                                                                                                                         
WARN 16:31:33,19                                                                                                                                                        
WARN 16:31:34,16                                                                                                                                                        
WARN 16:31:35,16                                                                                                                                                        
WARN 16:31:36,13                                                                                                                                                        
WARN 16:31:37,10                                                                                                                                                        
WARN 16:31:38,14                                                                                                                                                        
WARN 16:31:39,10                                                                                                                                                        

``` 

#### Task2 -> Distribution of ERROR logs descendingly ordered according to frequancy

This is acheived by using two mappers and two reducers, first one used to bin logs by frequancy and the second ones to order them in descending order.
``` 
16:31:33,2                                                                                                                                                              
11:04:13,2                                                                                                                                                              
16:31:35,1                                                                                                                                                              
09:13:23,1                                                                                                                                                              
09:13:22,1                                                                                                                                                              
01:34:34,1                                                                                                                                                              
01:34:33,1                                                                                                                                                              

``` 

#### Task3 ->Count of each Log aggregated by Level

This is done by groupong messages with Log level in the map function and getting the count in the reducer function.
``` 
DEBUG,70                                                                                                                                                                
ERROR,9                                                                                                                                                                 
INFO,550                                                                                                                                                                
WARN,183   
``` 

#### Task4 -> Longest log messages of each Log level

This is acheived by grouping the messages with Log level in the map function and getting the minimum of them all in the reducer function
``` 
DEBUG,84                                                                                                                                                                
ERROR,52                                                                                                                                                                
INFO,94                                                                                                                                                                 
WARN,79      
``` 

### To run on AWS EMR

* Create a account in aws.amazon.com and create a IAM user .
*  Create a S3 bucket and deploy the jar and input file in the s3 bucket in AWS .
*  Go to Amazon EMR and create a cluster which is used to rapid processing and analyzing big data in AWS .
* Configure the steps displayed as required and run the job .
* On completion, run the jar file and check the output folder which will have the output of the mapReduce class which has been executed in the hadoop environment in EMR .


