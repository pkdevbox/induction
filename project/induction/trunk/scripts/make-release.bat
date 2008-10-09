@rem Script to make an Acciente Induction release based on the latest compiled classes
@rem Log
@rem May 27, 2008 APR	created
@rem Jun 23, 2008 APR	updated

@set path=%path%;c:\dev\jdk1.6.0_04\bin

@rem -- check if the user specified a release-name, otherwise complain
@if %1_==_ goto :error_usage
@set release_version=%1

@rem -- determine the root we should use for the source files
@if exist c:\acciente\acciente-projects\software  goto :home_root
@if exist t:\acciente-projects\software           goto :work_root
@goto :error_root

@:home_root
@set common_root=c:\acciente\acciente-projects\software
@goto :end_root

@:work_root
@set common_root=t:\acciente-projects\software
@goto :end_root

@:end_root

@echo INFO: Using root %common_root%

@rem -- target folder containing this release
@set release_root=%common_root%\release\induction\%release_version%

@rem -- source folder for (induction+commons) compiled class files 
@set classes_root=%common_root%\project\induction\class_release

@rem -- source folder for (induction+commons) source files
@set src_root=%common_root%\project\induction\subversion\src

@rem -- temp folder for (induction+commons) javadoc generation
@set javadoc_root=%release_root%\tmp-javadoc

@rem -- source folder for (demoapp) files 
@set demoapp_root=%common_root%\project\demoapp

@rem -- target folder for (induction+commons) temp copy of source 
@set tmp_src_root=%release_root%\tmp-src

@rem -- full file names for (induction+commons) LICENSE.txt, NOTICE.txt and induction-complete-sample-config.xml
@set license_root=%common_root%\project\induction\subversion
@set sample_conf_file=%common_root%\project\induction\subversion\conf\induction-complete-sample-config.xml

@rem -- check if the release root already exists, if it does complain and exit!
@if exist %release_root% goto :error_version_exists

@rem -- otherwise create the release folders
@md %release_root%
@md %release_root%\jdk1_4-compile
@md %release_root%\jdk1_6-compile

@rem -- generate the javadocs

@rem setup the classpath for javadoc generation
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

@rem generate javadocs for Acciente Commons
@set custom_title=-doctitle "<h1>Acciente Commons v%release_version% API Documentation</h1>" -windowtitle "Commons v%release_version% API Documentation"
@javadoc %custom_title% %custom_tags% %custom_header_footer% -public -classpath %classpath% -d %javadoc_root%/commons   -sourcepath %src_root%  @commons-package-list.txt

@rem generate javadocs for Acciente Induction
@set custom_title=-doctitle "<h1>Acciente Induction v%release_version% API Documentation</h1>" -windowtitle "Induction v%release_version% API Documentation"
@javadoc %custom_title% %custom_tags% %custom_header_footer% -public -classpath %classpath% -d %javadoc_root%/induction -sourcepath %src_root%  @induction-package-list.txt

@rem -- make a temp copy of the sources to exclude .svn files in .jar
@xcopy %src_root%	%tmp_src_root%	/s /i /exclude:src-excludes.txt /q

@rem -- copy the LICENSE.txt, NOTICE.txt to the distribution root
@copy %license_root%\LICENSE.txt						%release_root%
@copy %license_root%\NOTICE.txt							%release_root%

@rem -- copy a sample Induction config file to the distribution root
@copy %sample_conf_file%							%release_root%

@rem -- create jars for Acciente Commons
@jar -cfM %release_root%\jdk1_4-compile\acciente-commons-%release_version%-jdk1_4.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %classes_root%\jdk1_4	/com/acciente/commons
@jar -cfM %release_root%\jdk1_6-compile\acciente-commons-%release_version%-jdk1_6.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %classes_root%\jdk1_6	/com/acciente/commons
@jar -cfM %release_root%\acciente-commons-%release_version%-sources.jar			-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %tmp_src_root%		/com/acciente/commons
@jar -cfM %release_root%\acciente-commons-%release_version%-javadoc.jar			-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %javadoc_root%/commons	/

@rem -- create jars for Acciente Induction
@jar -cfM %release_root%\jdk1_4-compile\acciente-induction-%release_version%-jdk1_4.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %classes_root%\jdk1_4	/com/acciente/induction
@jar -cfM %release_root%\jdk1_6-compile\acciente-induction-%release_version%-jdk1_6.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %classes_root%\jdk1_6	/com/acciente/induction
@jar -cfM %release_root%\acciente-induction-%release_version%-sources.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %tmp_src_root%		/com/acciente/induction
@jar -cfM %release_root%\acciente-induction-%release_version%-javadoc.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %javadoc_root%/induction	/


@rem -- make a release of Acciente DemoApp
@xcopy %demoapp_root%\subversion\src	%release_root%\demoapp\src		/s /i /exclude:src-excludes.txt /q
@xcopy %demoapp_root%\subversion\conf	%release_root%\demoapp\conf		/s /i /exclude:src-excludes.txt /q

@rem -- remove temp copy of sources, and java docs
@rd %tmp_src_root% /s/q
@rd %javadoc_root% /s/q

@echo INFO: released package to: %release_root%
@goto :end_script

@:error_usage
@echo INFO: Usage:
@echo INFO: make-release-latest version-number-or-name
@echo INFO: e.g: make-release-latest 1.1.0b
@echo .
@goto end_script

@:error_version_exists
@echo ERROR: there is already a version named %release_version%
@goto end_script

@:error_root
@echo ERROR: none of the standard Acciente root path(s) found
@goto :end_script

@:end_script

@pause