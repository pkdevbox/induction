@rem - This scripts generates javadocs for the Induction project
@rem - Log
@rem - Jun 23, 2008 APR

@set path=%path%;c:\dev\jdk1.6.0_04\bin

@rem - setup the classpath
@set classpath=
@set classpath=%classpath%;../../../../lib/apache-bcel/5.2/bcel-5.2.jar
@set classpath=%classpath%;../../../../lib/apache-commons-collections/3.2.1/commons-collections-3.2.1.jar
@set classpath=%classpath%;../../../../lib/apache-commons-digester/1.8/commons-digester-1.8.jar
@set classpath=%classpath%;../../../../lib/apache-commons-fileupload/1.2.1/commons-fileupload-1.2.1.jar
@set classpath=%classpath%;../../../../lib/apache-commons-io/1.4/commons-io-1.4.jar
@set classpath=%classpath%;../../../../lib/apache-commons-logging/1.1.1/commons-logging-1.1.1.jar
@set classpath=%classpath%;../../../../lib/apache-commons-logging/1.1.1/commons-logging-adapters-1.1.1.jar
@set classpath=%classpath%;../../../../lib/apache-commons-logging/1.1.1/commons-logging-api-1.1.1.jar
@set classpath=%classpath%;../../../../lib/freemarker/2.3.12/freemarker-2.3.12.jar
@set classpath=%classpath%;../../../../lib/j2ee/1.3.1/j2ee-1.3.1.jar
@set classpath=%classpath%;../../class

@set custom_tags=-tag created:X -tag change-summary:X

@rem - remove old javadocs
@rd ..\..\javadoc\commons /s/q

@rem - generate javadocs for Acciente Commons
@javadoc  %custom_tags% -public -classpath %classpath% -d ../../javadoc/commons -sourcepath ../src @commons-package-list.txt

@rem - remove old javadocs
@rd ..\..\javadoc\induction /s/q

@rem - generate javadocs for Acciente Induction
@javadoc  %custom_tags% -public -classpath %classpath% -d ../../javadoc/induction -sourcepath ../src @induction-package-list.txt