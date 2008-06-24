@rem Script to make an Acciente Induction release based on the latest compiled classes
@rem Log
@rem May 27, 2008 APR	created

@set path=%path%;c:\dev\jdk1.6.0_04\bin

@if exist c:\acciente\acciente-projects\software  goto :home_root
@if exist t:\acciente-projects\software           goto :work_root
@goto :error_root

@:home_root
@set common_root=c:\acciente\acciente-projects\software
@goto :end_root

@:work_root
@set common_root=t:\acciente-projects\software
@goto :end_root

@:error_root
@echo none of exected root path(s) found
@goto :end_script

@:end_root

@echo using root %common_root%
@set release_root=%common_root%\release\induction\latest
@set compiled_class_root=%common_root%\project\induction\class
@set sample_conf_file=%common_root%\project\induction\subversion\conf\induction-complete-sample-config.xml
@set demoapp_root=%common_root%\project\demoapp

@rd %release_root% /s/q
@md %release_root%

@jar -cfM %release_root%\acciente-induction-beta-latest.jar	-C %compiled_class_root% /com/acciente/induction
@jar -cfM %release_root%\acciente-commons-beta-latest.jar	-C %compiled_class_root% /com/acciente/commons
@jar -cfM %release_root%\demoapp-src.jar			-C %demoapp_root%        /
@copy %sample_conf_file%                                           %release_root%	

@echo Released package to: %release_root%

:end_script

@pause