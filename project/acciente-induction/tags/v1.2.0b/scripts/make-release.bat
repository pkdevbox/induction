@rem Script to make an Acciente Induction release based on the latest compiled classes
@rem Log
@rem May 27, 2008 APR	created
@rem Jun 23, 2008 APR	updated
@rem May 04, 2009 APR	updated

@set path=%path%;c:\dev\jdk1.6.0_04\bin

@rem -- check if the user specified a release-name, otherwise complain
@if %1_==_ goto :error_usage
@set release_version=%1

@rem -- determine the root we should use for the source files
@if exist c:\acciente\acciente-projects\software  goto :home_root
@goto :error_root

@:home_root
@set common_root=c:\acciente\acciente-projects\software
@goto :end_root

@:end_root

@echo INFO: Using root %common_root%

@rem -- target folder containing this release
@set release_root=%common_root%\release\acciente-induction\%release_version%

@rem -- full file names for (induction+commons) LICENSE.txt, NOTICE.txt and induction-complete-sample-config.xml
@set license_root=%common_root%\project\acciente-induction
@set sample_conf_file1=%common_root%\project\acciente-induction\src\main\config\induction-complete-sample-config.xml
@set sample_conf_file2=%common_root%\project\acciente-induction\src\main\config\induction-complete-sample-config-include.xml

@rem -- check if the release root already exists, if it does complain and exit!
@if exist %release_root% goto :error_version_exists

@rem -- otherwise create the release folder
@md %release_root%

@rem -- copy the LICENSE.txt, NOTICE.txt to the distribution root
@copy %license_root%\LICENSE.txt						%release_root%
@copy %license_root%\NOTICE.txt							%release_root%

@rem -- copy a sample Induction config file to the distribution root
@copy %sample_conf_file1%							%release_root%
@copy %sample_conf_file2%							%release_root%

@rem -- package Acciente POM
@cd ..\acciente-induction-pom
@call mvn clean
@call mvn install

@rem -- package Acciente Commons
@cd ..\acciente-commons
@call mvn clean
@call mvn install -P jdk1_4 -D induction-version=%release_version%
@call mvn install -P jdk1_6 -D induction-version=%release_version%
@call mvn source:jar        -D induction-version=%release_version%
@call mvn javadoc:jar       -D induction-version=%release_version%
@move target\*.jar						%release_root%

@rem -- package Acciente Induction
@cd ..\acciente-induction
@call mvn clean
@call mvn install -P jdk1_4 -D induction-version=%release_version%
@call mvn install -P jdk1_6 -D induction-version=%release_version%
@call mvn source:jar        -D induction-version=%release_version%
@call mvn javadoc:jar       -D induction-version=%release_version%
@move target\*.jar					%release_root%

@jar -ufM %release_root%\acciente-commons-%release_version%-jdk1_4.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt
@jar -ufM %release_root%\acciente-commons-%release_version%-jdk1_6.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt
@jar -ufM %release_root%\acciente-commons-%release_version%-sources.jar			-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt
@jar -ufM %release_root%\acciente-commons-%release_version%-javadoc.jar			-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt

@rem -- create jars for Acciente Induction
@jar -ufM %release_root%\acciente-induction-%release_version%-jdk1_4.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt
@jar -ufM %release_root%\acciente-induction-%release_version%-jdk1_6.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt
@jar -ufM %release_root%\acciente-induction-%release_version%-sources.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt
@jar -ufM %release_root%\acciente-induction-%release_version%-javadoc.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt

@rem -- add the demoapp to the release
@xcopy %common_root%\project\demoapp\subversion\src		%release_root%\demoapp\src		/s /i /exclude:scripts\src-excludes.txt /q
@xcopy %common_root%\project\demoapp\subversion\conf		%release_root%\demoapp\conf		/s /i /exclude:scripts\src-excludes.txt /q

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