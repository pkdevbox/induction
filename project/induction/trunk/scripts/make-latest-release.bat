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

@call gen-javadoc.bat

@echo INFO: Using root %common_root%

@rem -- target folder containing this release
@set release_root=%common_root%\release\induction\%release_version%

@rem -- source folder for (induction+commons) compiled class files 
@set classes_root=%common_root%\project\induction\class

@rem -- source folder for (induction+commons) source files
@set src_root=%common_root%\project\induction\subversion\src

@rem -- source folder for (induction+commons) javadoc files
@set javadoc_root=%common_root%\project\induction\javadoc

@rem -- source folder for (demoapp) files 
@set demoapp_root=%common_root%\project\demoapp

@rem -- target folder for (induction+commons) temp copy of source 
@set tmp_src_root=%release_root%\tmp-src

@rem -- full file names for (induction+commons) LICENSE.txt, NOTICE.txt and induction-complete-sample-config.xml
@set license_root=%common_root%\project\induction\subversion
@set sample_conf_file=%common_root%\project\induction\subversion\conf\induction-complete-sample-config.xml

@rem -- check if the release root already exists, if it does complain and exit!
@if exist %release_root% goto :error_version_exists

@rem -- otherwise create the release root
@md %release_root%

@rem -- make a temp copy of the sources to exclude .svn files in .jar
@xcopy %src_root%	%tmp_src_root%	/s /i /exclude:src-excludes.txt /q

@rem -- copy the LICENSE.txt, NOTICE.txt to the distribution root
@copy %license_file%								%release_root%
@copy %notice_file%								%release_root%

@rem -- copy a sample Induction config file to the distribution root
@copy %sample_conf_file%							%release_root%

@rem -- create jars for Acciente Commons
@jar -cfM %release_root%\acciente-commons-%release_version%.jar			-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %classes_root%		/com/acciente/commons
@jar -cfM %release_root%\acciente-commons-%release_version%-sources.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %tmp_src_root%		/com/acciente/commons
@jar -cfM %release_root%\acciente-commons-%release_version%-javadoc.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %javadoc_root%/commons	/

@rem -- create jars for Acciente Induction
@jar -cfM %release_root%\acciente-induction-%release_version%.jar		-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %classes_root%		/com/acciente/induction
@jar -cfM %release_root%\acciente-induction-%release_version%-sources.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %tmp_src_root%		/com/acciente/induction
@jar -cfM %release_root%\acciente-induction-%release_version%-javadoc.jar	-C %license_root% LICENSE.txt -C %license_root% NOTICE.txt -C %javadoc_root%/induction	/


@rem -- make a release of Acciente DemoApp
@xcopy %demoapp_root%\subversion\src	%release_root%\demoapp\src		/s /i /exclude:src-excludes.txt /q
@xcopy %demoapp_root%\subversion\conf	%release_root%\demoapp\conf		/s /i /exclude:src-excludes.txt /q

@rem -- remove temp copy sources
@rd %tmp_src_root% /s/q

@echo INFO: released package to: %release_root%
@goto :end_script

@:error_usage
@echo INFO: Usage:
@echo INFO: make-release-latest version-number-or-name
@echo INFO: e.g: make-release-latest beta-1.1.0
@echo .
@goto end_script

@:error_version_exists
@echo ERROR: there is already a version named %release_version%
@goto end_script

@:error_root
@echo ERROR: none of exected root path(s) found
@goto :end_script

@:end_script

@pause