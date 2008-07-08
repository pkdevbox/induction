@rem - This scripts generates javadocs for the Induction project
@rem - Log
@rem - Jun 23, 2008 APR created
@rem - Jul 03, 2008 APR updated

@rem -- check if the user specified a release-name, otherwise complain
@if %1_==_ goto :error_usage
@set release_version=%1

@rem -- check if the release root already exists, if it does complain and exit!
@if exist ..\..\javadoc\commons\%release_version% goto :error_version_exists
@if exist ..\..\javadoc\induction\%release_version% goto :error_version_exists

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
@set custom_header_footer=-header "<a href="http://www.inductionframework.org" target="_top">Return to www.inductionframework.org</a>" -bottom "<a href="http://www.acciente.com" target="_top">Copyright (c) 2008 Acciente, LLC. All rights reserved.</a>"

@rem - generate javadocs for Acciente Commons
@set custom_title=-doctitle "<h1>Acciente Commons v%release_version% API Documentation</h1>" -windowtitle "Commons v%release_version% API Documentation"
@javadoc %custom_title% %custom_tags% %custom_header_footer% -public -classpath %classpath% -d ../../javadoc/commons/%release_version%/api -sourcepath ../src @commons-package-list.txt

@rem - generate javadocs for Acciente Induction
@set custom_title=-doctitle "<h1>Acciente Induction v%release_version% API Documentation</h1>" -windowtitle "Induction v%release_version% API Documentation"
@javadoc %custom_title% %custom_tags% %custom_header_footer% -public -classpath %classpath% -d ../../javadoc/induction/%release_version%/api -sourcepath ../src @induction-package-list.txt

@echo INFO: javadocs packaged for version: %release_version%
@goto :end_script

@:error_usage
@echo INFO: Usage:
@echo INFO: gen-javadoc version-number-or-name
@echo INFO: e.g: gen-javadoc 1.1.0b
@echo .
@goto end_script

@:error_version_exists
@echo ERROR: there is already javadocs for a version named %release_version%
@goto end_script

@:end_script

@pause