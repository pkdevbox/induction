@rem Script to make an Acciente Induction release based on the latest compiled classes
@rem Log
@rem May 27, 2008 APR	created

@set path=%path%;c:\dev\jdk1.6.0_04\bin

@if %1_==_ goto :error_usage
@set release_version=%1

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

@echo INFO: using root %common_root%
@set release_root=%common_root%\release\induction\%release_version%
@set compiled_class_root=%common_root%\project\induction\class
@set source_class_root=%common_root%\project\induction\subversion\src
@set javadoc_root=%common_root%\project\induction\javadoc
@set sample_conf_file=%common_root%\project\induction\subversion\conf\induction-complete-sample-config.xml
@set demoapp_root=%common_root%\project\demoapp

@if exist %release_root% goto :error_version_exists

@rd %release_root% /s/q
@md %release_root%

@rem -- make a temp copy of the source to remove .svn files
@xcopy %source_class_root% %release_root%\tmp-src /s /i /exclude:src-excludes.txt

@rem -- make a release of Acciente Commons
@jar -cfM %release_root%\acciente-commons-%release_version%.jar			-C %compiled_class_root%	/com/acciente/commons
@jar -cfM %release_root%\acciente-commons-%release_version%-sources.jar		-C %release_root%\tmp-src	/com/acciente/commons
@jar -cfM %release_root%\acciente-commons-%release_version%-javadoc.jar		-C %javadoc_root%/commons	/

@rem -- make a release of Acciente Induction
@jar -cfM %release_root%\acciente-induction-%release_version%.jar		-C %compiled_class_root%	/com/acciente/induction
@jar -cfM %release_root%\acciente-induction-%release_version%-sources.jar	-C %release_root%\tmp-src	/com/acciente/induction
@jar -cfM %release_root%\acciente-induction-%release_version%-javadoc.jar	-C %javadoc_root%/induction	/
@copy %sample_conf_file%							%release_root%

@rem -- remove temp source directory
@rd %release_root%\tmp-src /s/q

@rem -- make a release of Acciente DemoApp
@rem jar -cfM %release_root%\demoapp-src.jar					-C %demoapp_root%        	/ 

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