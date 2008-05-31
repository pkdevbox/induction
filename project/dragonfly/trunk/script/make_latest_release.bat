@rem Script to make an Acciente Induction release based on the latest compiled classes
@rem Log
@rem May 27, 2008 APR	created

@startlocal
@set path=%path%;c:\dev\j2sdk1.4.2_16\bin
@set release_root=\acciente-projects\software\release\induction\latest
@set compiled_class_root=\acciente-projects\software\project\induction\class\prod
@set demoapp_root=\acciente-projects\software\project\demoapp

@rd %release_root% /s/q
@md %release_root%

@jar -cfM %release_root%\acciente-induction-latest-beta.jar	-C %compiled_class_root% /com/acciente/induction
@jar -cfM %release_root%\acciente-commons-latest-beta.jar	-C %compiled_class_root% /com/acciente/commons
@jar -cfM %release_root%\demoapp-src.jar			-C %demoapp_root%        /

@echo Release package to: %release_root%

@endlocal