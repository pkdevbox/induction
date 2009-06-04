@echo off
@if _%1==_ goto :show_usage
@if _%1==_clean goto :clean

@call mvn %1 -P jdk1_4
@call mvn %1 -P jdk1_6
@if _%1==_package goto :package
@goto :end

:package
@call mvn source:jar %1 
@call mvn javadoc:jar %1 
@goto :end

@:clean
@call mvn clean
@goto :end

@:show_usage
@echo Usage This batch file runs Maven with appropriate switches to generate a group of artifacts
@echo       run_mvn maven-phase-name
@echo       e.g. run_mvn package
@:end
