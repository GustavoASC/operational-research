@echo off
rem Percorre os casos de teste do diretório
    for /f %%i in ('dir /ad /b') do (
       call :ExecutaTeste %%i
    )
    goto :Fim
rem
rem Executa o teste atual
:ExecutaTeste
    echo Executando teste %1...
    call java -jar ..\..\..\target\operational-research-1.0-SNAPSHOT.jar .\%1\input.txt > .\%1\output.txt
rem Se o gabarito ainda não existia
    if not exist ".\%1\expected.txt" (
rem Cria o gabarito a partir da última execução do teste
       echo Criando expected.txt a partir da última execução...
       copy .\%1\output.txt .\%1\expected.txt
    ) else (
       call fc .\%1\expected.txt .\%1\output.txt >nul
rem Se encontrou alguma diferença entre a saída e o esperado
       if not "%errorlevel%"=="0" (
          echo Diferenças encontradas
          call fc .\%1\expected.txt .\%1\output.txt
          echo.
       )
    )
    goto :eof
:Fim
@echo on