@rem Script to make an Acciente Induction distirbution based on a specific release
@rem Log
@rem Jun 27, 2008 APR	created

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

@rem -- target folder containing this distrib
@set induction_distrib_core_file=%common_root%\distrib\induction\%release_version%\acciente-induction-%release_version%-core.zip
@set induction_distrib_req_libs_file=%common_root%\distrib\induction\%release_version%\acciente-induction-%release_version%-required-libs.zip
@set induction_distrib_samples_file=%common_root%\distrib\induction\%release_version%\acciente-induction-%release_version%-samples.zip

@rem -- source folder containing this release
@set release_root=%common_root%\release\induction\%release_version%

@rem -- check if the distrib root already exists, if it does complain and exit!
@if exist %induction_distrib_core_file%		goto :error_distrib_exists
@if exist %induction_distrib_req_libs_file%	goto :error_distrib_exists
@if exist %induction_distrib_samples_file%	goto :error_distrib_exists

@rem -- check if the release root exists, if it does not complain and exit!
@if not exist %release_root% goto :error_release_not_found

@rem -- create the distribution for the core product
@set induction_distrib_core_content=
@set induction_distrib_core_content=%induction_distrib_core_content% %release_root%\*.txt 
@set induction_distrib_core_content=%induction_distrib_core_content% %release_root%\*.xml 
@7z a -tzip    %induction_distrib_core_file% %induction_distrib_core_content%
@7z a -tzip -r %induction_distrib_core_file% %release_root%\*.jar

@rem -- create the distribution for the supporting libs
@set induction_distrib_req_libs_content=
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\apache-bcel\5.2\bcel-5.2.jar
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\apache-commons-collections\3.2.1\commons-collections-3.2.1.jar
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\apache-commons-digester\1.8\commons-digester-1.8.jar
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\apache-commons-fileupload\1.2.1\commons-fileupload-1.2.1.jar
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\apache-commons-io\1.4\commons-io-1.4.jar
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\apache-commons-logging\1.1.1\commons-logging-1.1.1.jar
@set induction_distrib_req_libs_content=%induction_distrib_req_libs_content% %common_root%\lib\freemarker\2.3.12\freemarker-2.3.12.jar
@7z a -tzip %induction_distrib_req_libs_file% %induction_distrib_req_libs_content%

@rem -- create the distribution for the samples
@set induction_distrib_samples_content=
@set induction_distrib_samples_content=%induction_distrib_samples_content% %release_root%\demoapp
@7z a -tzip %induction_distrib_samples_file% %induction_distrib_samples_content%

@echo INFO: created the 3 distribution files below: 
@echo %induction_distrib_core_file% 
@echo %induction_distrib_req_libs_file% 
@echo %induction_distrib_samples_file%
@goto :end_script

@:error_usage
@echo INFO: Usage:
@echo INFO: make-distrib.bat induction-version-number-or-name
@echo INFO: e.g: make-distrib.bat 1.1.0b
@echo .
@goto end_script

@:error_distrib_exists
@echo ERROR: there is already a distribution for Induction release: %release_version%
@goto end_script

@:error_release_not_found
@echo ERROR: release: %release_version% for Induction not found in /release directory!
@goto end_script

@:error_root
@echo ERROR: none of the standard Acciente root path(s) found
@goto :end_script

@:end_script

@pause