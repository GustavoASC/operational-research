@echo off
rem Percorre os casos de teste do diret�rio
    for /f %%i in ('dir /ad /b') do (
       call :ExecutaTeste %%i
    )
    goto :Fim
rem
rem Executa o teste atual
:ExecutaTeste
    echo Executando teste %1...
    call java -jar ..\..\..\target\operational-research-1.0-SNAPSHOT.jar .\%1\input.txt > .\%1\output.txt
rem Se o gabarito ainda n�o existia
    if not exist ".\%1\expected.txt" (
rem Cria o gabarito a partir da �ltima execu��o do teste
       echo Criando expected.txt a partir da �ltima execu��o...
       copy .\%1\output.txt .\%1\expected.txt
    ) else (
       call fc .\%1\expected.txt .\%1\output.txt >nul
rem Se encontrou alguma diferen�a entre a sa�da e o esperado
       if not "%errorlevel%"=="0" (
          echo Diferen�as encontradas
          call fc .\%1\expected.txt .\%1\output.txt
          echo.
       )
    )
    goto :eof
:Fim
@echo on