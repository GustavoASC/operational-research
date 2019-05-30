# Runs every available test
for directory in */
do
    # Removes slash from directory end for better output
    EXPECTED="./$directory/expected.txt"
    INPUT="./$directory/input.txt"
    OUTPUT="./$directory/output.txt"
    echo Running test $directory...
    java -jar ../../../target/operational-research-1.0-SNAPSHOT.jar $INPUT > $OUTPUT
    # If template does not exist yet
    if ! [ -f $EXPECTED ]; then
       echo "Criando gabarito para $directory..."
       cp $OUTPUT $EXPECTED
    else
       # If output file and expected file are different
       if ! diff --strip-trailing-cr -q $EXPECTED $OUTPUT  > /dev/null 2>&1; then
          echo "Diferenças encontradas"
          diff --strip-trailing-cr $EXPECTED $OUTPUT
          echo
       fi
    fi
done